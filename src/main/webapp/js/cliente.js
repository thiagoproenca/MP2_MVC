const app = new Vue({
    el: '#app',
    data: {
        mesas: [], 
        mensagem: null,
        reservaEmAndamento: false,
        modalAberto: false, 
        modalReserva: { 
            mesaId: null,
            idCliente: '',
            nomeCliente: '',
            telefoneCliente: ''
        }
    },
    mounted() {
        this.loadMesas();
    },
    methods: {
        loadMesas() {
            this.mesas = [];
            this.exibirMensagem('Carregando mesas...', 'alert-info');
            
            // *** PONTO DE INTEGRAÇÃO MVC: CHAMADA FETCH REAL (AJAX) ***
            fetch('https://api.exemplo.com/dados')
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
                    // Dados de simulação (fallback) em caso de falha de conexão
                    this.mesas = [
                         { id: 1, capacidade: 4, status: 'Livre' },
                         { id: 2, capacidade: 6, status: 'Ocupada' }, 
                         { id: 3, capacidade: 2, status: 'Livre' },
                    ];
                    this.exibirMensagem(`❌ Erro de conexão. Mesas de demonstração carregadas.`, 'alert-danger');
                });
        },
        
        abrirModal(mesaId) {
            const mesa = this.mesas.find(m => m.id === mesaId);
            
            if (mesa && mesa.status === 'Livre') {
                 this.modalReserva.mesaId = mesaId;
                 this.modalReserva.capacidade = mesa.capacidade;
                 this.modalReserva.idCliente = '';
                 this.modalReserva.nomeCliente = ''; 
                 this.modalReserva.telefoneCliente = ''; 
                 
                 this.modalAberto = true;

                 this.$nextTick(() => {
                 console.log("Modal aberto no próximo ciclo de atualização do DOM.");
                });
            } else if (mesa && mesa.status === 'Ocupada') {
                 this.exibirMensagem(`A Mesa #${mesaId} está ocupada e não pode ser reservada.`, 'alert-warning');
            }
        },

        fecharModal() {
            this.modalAberto = false;
        },

        confirmarEnvio() {
            this.enviarReserva(this.modalReserva.mesaId);
            this.fecharModal();
        },

        enviarReserva(mesaId) {
            this.reservaEmAndamento = true;
            this.mensagem = null;

            console.log(`Enviando reserva para a mesa ${mesaId} via AJAX...`);
            
            // Dados que serão enviados no corpo da requisição POST
            const dadosReserva = {
                mesa_id: mesaId,
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
                    const mesaIndex = this.mesas.findIndex(m => m.id === mesaId);
                    if (mesaIndex !== -1) {
                         this.$set(this.mesas, mesaIndex, {
                            ...this.mesas[mesaIndex],
                            status: 'Ocupada' // Atualiza a View
                        });
                    }
                    this.exibirMensagem(`✅ Mesa #${mesaId} reservada com sucesso!`, 'alert-success');
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