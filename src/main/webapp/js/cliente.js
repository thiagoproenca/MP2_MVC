const app = new Vue({
    el: '#app',
    data: {
        // dados serão adicionados pelo membro responsável
        mesas: [], 
        mensagem: null, // { texto: '...', tipo: 'alert-success' }
        reservaEmAndamento: false, 
        modalReserva: { 
            mesaId: null,
            nomeCliente: '',
            telefoneCliente: ''
        }
    },
    mounted() {
        // Carrega os dados iniciais ao montar a aplicação
        this.loadMesas();
    },
    methods: {
        loadMesas() {
            this.mesas = []; // Limpa dados anteriores
            this.exibirMensagem('Carregando mesas...', 'alert-info');
            
            // *** PONTO DE INTEGRAÇÃO MVC: CHAMADA FETCH REAL (AJAX) ***
            fetch(API_URLS.MESAS_DISPONIVEIS)
                .then(res => {
                    if (!res.ok) {
                        // Lança um erro se a resposta HTTP não for 2xx
                        throw new Error(`Erro HTTP: ${res.status}`);
                    }
                    return res.json();
                })
                .then(data => { 
                    // Assume que 'data' é um array de objetos de mesas
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
                 // Pop-up de confirmação
                 const confirmacao = confirm(`Deseja realmente reservar a Mesa #${mesaId} (Capacidade: ${mesa.capacidade})?`);
                 
                 if (confirmacao) {
                    // Preenche os dados (simulando cliente logado)
                    this.modalReserva.mesaId = mesaId;
                    this.modalReserva.nomeCliente = 'Cliente Vue AJAX'; // Nome alterado para refletir o uso de AJAX
                    this.modalReserva.telefoneCliente = '555-5555';
                    
                    this.enviarReserva(mesaId);
                 } else {
                    this.exibirMensagem(`Reserva da Mesa #${mesaId} cancelada pelo usuário.`, 'alert-secondary');
                 }
                 
            } else if (mesa && mesa.status === 'Ocupada') {
                 // Pop-up informativo
                 alert(`A Mesa #${mesaId} está ocupada e não pode ser reservada.`);
            }
        },

        fecharModal() {
            this.modalAberto = false;
        },

        /**
         * Envia os dados da reserva (Mesa ID e dados do Cliente) para o Controller de Backend usando **Fetch API (AJAX)**.
         * @param {number} mesaId O ID da mesa a ser reservada.
         */
        enviarReserva(mesaId) {
            this.reservaEmAndamento = true;
            this.mensagem = null; // Limpa mensagens

            console.log(`Enviando reserva para a mesa ${mesaId} via AJAX...`);
            
            // Dados que serão enviados no corpo da requisição POST
            const dadosReserva = {
                mesa_id: mesaId,
                cliente_id: 10, // Exemplo de ID do cliente
                nome: this.modalReserva.nomeCliente, 
                telefone: this.modalReserva.telefoneCliente
            };

            // *** PONTO DE INTEGRAÇÃO MVC: CHAMADA FETCH POST REAL (AJAX) ***
            fetch(API_URLS.ENVIAR_RESERVA, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json' 
                    // Se necessário, adicione 'Authorization' aqui
                },
                body: JSON.stringify(dadosReserva)
            })
            .then(res => {
                if (!res.ok) {
                     // Lança um erro para ser pego pelo .catch
                    throw new Error(`Falha no servidor. Status: ${res.status}`);
                }
                return res.json();
            })
            .then(data => {
                // A resposta do Backend Controller deve conter o sucesso da operação
                if (data && data.sucesso === true) {
                    // Atualiza o estado da View (data)
                    const mesaIndex = this.mesas.findIndex(m => m.id === mesaId);
                    if (mesaIndex !== -1) {
                         this.$set(this.mesas, mesaIndex, {
                            ...this.mesas[mesaIndex],
                            status: 'Ocupada' // Atualiza a View
                        });
                    }
                    this.exibirMensagem(`✅ Mesa #${mesaId} reservada com sucesso!`, 'alert-success');
                } else {
                    // Se o backend retornar sucesso=false ou um erro específico
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