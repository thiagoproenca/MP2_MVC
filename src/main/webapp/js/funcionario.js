const app = new Vue({
    el: '#app',

    data: {
        mesas: [],
        ultimaAtualizacao: '-',
        intervalo: null
    },

    methods: {
        async loadStatus() {
            const resp = await fetch("status-mesas");
            this.mesas = await resp.json();
            this.ultimaAtualizacao = new Date().toLocaleTimeString();
        },

        atualizarAgora() {
            this.loadStatus();
        }
    },

    created() {
        this.loadStatus();
        this.intervalo = setInterval(() => this.loadStatus(), 1500);
    }
});
