const { createApp } = Vue
const options = {
    data() {
        return {
            accountSet: [],
            transactionSet: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            const queryString = location.search;
            const params = new URLSearchParams(queryString);
            const id = params.get("id");
            console.log(id);
            axios.get("http://localhost:8080/api/accounts/" + id)
                .then(response => {
                    this.accountSet = response.data
                    this.transactionSet = this.accountSet.transactionSet
                    console.log(this.transactionSet);
                   
                 
                })
                .catch(error => console.log(error))
        }
    }
}
const app = createApp(options)
app.mount('#app')
