const app = new Vue({
    el: '#app',
    data: {
        mesas: [], 
        mensagem: null,
        reservaEmAndamento: false,
        modalAberto: false, 
        modalReserva: { 
            idMesa: null,
            numero: null,
            capacidade: null,
            nomeCliente: ''
        }
    },

    mounted() {
        this.loadMesas();
    },

    methods: {

        async loadMesas() {
            this.mesas = [];
            this.exibirMensagem('Carregando mesas...', 'alert-info');

            try {
                const res = await fetch('http://localhost:8080/reservas/api/mesas');
                const data = await res.json();
                this.mesas = data;
                this.exibirMensagem(`Mesas carregadas (${data.length})`, 'alert-success');
            } catch (err) {
                this.exibirMensagem('❌ Erro carregando mesas.', 'alert-danger');
            }
        },

        abrirModal(idMesa) {
            const mesa = this.mesas.find(m => m.id === idMesa);

            if (!mesa) return;

            if (mesa.status.toUpperCase() === "RESERVADA") {
                this.exibirMensagem(`Mesa #${mesa.numero} já reservada.`, 'alert-warning');
                return;
            }

            this.modalReserva.idMesa = mesa.id;
            this.modalReserva.numero = mesa.numero;
            this.modalReserva.capacidade = mesa.capacidade;
            this.modalReserva.nomeCliente = "";

            this.modalAberto = true;
        },

        fecharModal() {
            this.modalAberto = false;
        },

        confirmarEnvio() {
            this.enviarReserva(this.modalReserva.idMesa);
            this.fecharModal();
        },

        async enviarReserva(idMesa) {
            this.reservaEmAndamento = true;
            this.mensagem = null;

            const payload = {
                mesaId: idMesa,
                nome: this.modalReserva.nomeCliente
            };

            try {
                const resposta = await fetch("http://localhost:8080/reservas/api/reservar", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });

                const data = await resposta.json();

                if (data.sucesso) {
                    const mesa = this.mesas.find(m => m.id === idMesa);
                    if (mesa) {
                        mesa.status = "RESERVADA";
                    }
                    this.exibirMensagem(`Mesa #${mesa.numero} reservada com sucesso!`, "alert-success");
                } else {
                    this.exibirMensagem(data.mensagem, "alert-danger");
                }

            } catch (err) {
                this.exibirMensagem("Erro ao enviar reserva.", "alert-danger");
            }

            this.reservaEmAndamento = false;
        },

        exibirMensagem(texto, tipo) {
            this.mensagem = { texto, tipo };
            setTimeout(() => {
                if (this.mensagem && this.mensagem.texto === texto) {
                    this.mensagem = null;
                }
            }, 5000);
        }
    }
});
