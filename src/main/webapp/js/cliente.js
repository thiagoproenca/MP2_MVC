const app = new Vue({
    el: '#app',
    data: {
        mesas: [], 
        mensagem: null,
        reservaEmAndamento: false,
        modalAberto: false, 
        modalReserva: { 
            numero: null,
            idCliente: '',
            nomeCliente: '',
            telefoneCliente: ''
        }
    },
    mounted() {
        this.loadMesas();
    },
    methods: {
        async loadMesas() {
			this.mesas = [];
			this.exibirMensagem('Carregando mesas...', 'alert-info');

			await fetch('http://localhost:8080/reservas/api/mesas')
				.then(res => {
					if (!res.ok) {
						throw new Error(`Erro HTTP: ${res.status}`);
					}
					return res.json();
				})
				.then(data => {
					this.mesas = data;   
					this.exibirMensagem(`Mesas carregadas com sucesso! (${data.length} encontradas)`, 'alert-success');
				})
				.catch(err => {
					console.error('Erro ao carregar mesas:', err);
					this.mesas = [];
					this.exibirMensagem(`❌ Erro de conexão com o servidor.`, 'alert-danger');
				});
		},
        
        abrirModal(numero) {
            const mesa = this.mesas.find(m => m.numero === numero);
            
            if (mesa && mesa.status === 'LIVRE') {
                 this.modalReserva.numero = numero;
                 this.modalReserva.capacidade = mesa.capacidade;
                 this.modalReserva.idCliente = '';
                 this.modalReserva.nomeCliente = ''; 
                 this.modalReserva.telefoneCliente = ''; 
                 
                 this.modalAberto = true;

                 this.$nextTick(() => {
                 console.log("Modal aberto no próximo ciclo de atualização do DOM.");
                });
            } else if (mesa && mesa.status === 'RESERVADA') {
                 this.exibirMensagem(`A Mesa #${numero} está ocupada e não pode ser reservada.`, 'alert-warning');
            }
        },

        fecharModal() {
            this.modalAberto = false;
        },

        confirmarEnvio() {
            this.enviarReserva(this.modalReserva.numero);
            this.fecharModal();
        },

        enviarReserva(numero) {
            this.reservaEmAndamento = true;
            this.mensagem = null;

            console.log(`Enviando reserva para a mesa ${numero} via AJAX...`);
            
            // Dados que serão enviados no corpo da requisição POST
            const dadosReserva = {
                mesa_id: numero,
                cliente_id: this.modalReserva.idCliente,
                nome: this.modalReserva.nomeCliente, 
                telefone: this.modalReserva.telefoneCliente
            };

            fetch(API_URLS.ENVIAR_RESERVA, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json' 
                    // espaço pra auth caso necessário
                },
                body: JSON.stringify(dadosReserva)
            })
            .then(res => {
                if (!res.ok) {
                    throw new Error(`Falha no servidor. Status: ${res.status}`);
                }
                return res.json();
            })
            .then(data => {
                if (data && data.sucesso === true) {
                    const mesaIndex = this.mesas.findIndex(m => m.numero === numero);
                    if (mesaIndex !== -1) {
                         this.$set(this.mesas, mesaIndex, {
                            ...this.mesas[mesaIndex],
                            status: 'RESERVADA' // Atualiza a View
                        });
                    }
                    this.exibirMensagem(`✅ Mesa #${numero} reservada com sucesso!`, 'alert-success');
                } else {
                    this.exibirMensagem(`❌ Falha na reserva: ${data.mensagem || 'Mesa indisponível ou erro desconhecido.'}`, 'alert-danger');
                }
            })
            .catch(err => {
                console.error('Erro de rede ou no processo de reserva:', err);
                this.exibirMensagem('❌ Erro de comunicação ou falha no servidor. Tente novamente.', 'alert-danger');
            })
            .finally(() => { 
                this.reservaEmAndamento = false;
            });
        },

        /**
         * Função auxiliar para exibir e gerenciar mensagens de alerta na View.
         * @param {string} texto O conteúdo da mensagem.
         * @param {string} tipo A classe de alerta do Bootstrap (e.g., 'alert-success').
         */
        exibirMensagem(texto, tipo) {
            this.mensagem = { texto, tipo };
            // Auto-ocultar após 5 segundos
            setTimeout(() => {
                if (this.mensagem && this.mensagem.texto === texto) {
                    this.mensagem = null;
                }
            }, 5000);
        }
    }
});