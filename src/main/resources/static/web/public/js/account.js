const { createApp } = Vue
const options = {
    data() {
        return {
            accountSet: [],
            transactionSet: [],
            transactionCredit: [],
            account: null,
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
            axios.get("/api/clients/accounts/" + id)
                .then(response => {
                    this.accountSet = response.data
                    console.log(this.accountSet);
                    this.transactionSet = this.accountSet.transactionSet
                    console.log(this.transactionSet);
                    this.account = response.data 
                    console.log(this.account);
                    for(const transaction of this.transactionSet){
                        const aux = {
                             transaction : transaction.type
                        }
                        this.transactionCredit.push(aux)
                        console.log(aux);
                      }
                      this.transactionSet.sort((a,b) => b.id - a.id)
                 
                })
                .catch(error => console.log(error))
        },
        logout() {
          axios.post(`/api/logout`)
  
          .then(response => console.log('signed out!!'))
              .then
  
          return (window.location.href = "../index.html")
  
      }
    }
}
const app = createApp(options)
app.mount('#app')
