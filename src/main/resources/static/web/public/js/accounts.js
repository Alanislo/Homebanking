const {createApp} = Vue
 const options = {
    data(){
        return{
            clients: [],
            clientsAccounts:[],
            loans:[],
            date:[]
          
        }
    },
    created(){
      this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then( response => {
              this.clients = response.data
              console.log(response.data);
              this.clientsAccounts = this.clients.accountSet
              this.loans = this.clients.clientLoans
              console.log(this.clientsAccounts);
              console.log(this.loans);
             
            })
            .catch(error => console.log(error))
        }
    }
 }
const app = createApp(options)
app.mount('#app')
 