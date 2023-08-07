const {createApp} = Vue
 const options = {
    data(){
        return{
            clients: [],
            clientsAccounts:[],
            accountSet:[]
        }
    },
    created(){
      this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients")
            .then( response => {
              this.clients = response.data
              this.clientsAccounts = this.clients[0].accountSet
              console.log(this.clientsAccounts);
              console.log(this.clients);
              for(const account of this.clientsAccounts){
                const aux = {
                     number : account.number,
                     creationDate : account.creationDate,
                     balance : account.balance
                }
                this.accountSet.push (aux)
                console.log(aux);
              }
            })
            .catch(error => console.log(error))
        }
    }
 }
const app = createApp(options)
app.mount('#app')
 