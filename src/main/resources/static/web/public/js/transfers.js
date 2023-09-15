const {createApp} = Vue
 const options = {
    data(){
        return{
            accountOrigen:null,
            accountDestino:null,
            amount:null,
            description:"",
            accounts:[],
            myTransfer: true,
        }
    },
    created(){
    this.loadData()
    },
    computed:{
        prueba(){
            console.log(this.accountOrigen);
            console.log(this.accountDestino);
        }
    },
    methods:{
        transfer(){
            this.myTransfer = false;
        },
        transfer2(){
            this.myTransfer = true;
        },
        loadData(){
            axios.get('/api/clients/current/accounts')
            .then(response=>{
                this.accounts = response.data
                console.log(this.accounts);
            }).catch(error => console.log(error))
          },
        alert(){
            let mensaje;
            let opcion = confirm("Do you want to create a new transfer?");
            console.log("Hola");
            if (opcion == true) {
             console.log(this.accountOrigen);
             console.log(this.accountDestino);
             console.log(this.amount);
             console.log(this.description);
                axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&origin=${this.accountOrigen}&destination=${this.accountDestino}`)
                .then( response => {
                  location.href ="./web/public/pages/account.html"})
                  .catch(error => {
                    console.log(error.response);
                    window.alert(error.response.data)
            })
            } else {
                mensaje = "Cancel";
            }
           
        },
       logout() {
          axios.post(`/api/logout`)
          .then(response => console.log('signed out!!'))
          return (window.location.href = "../../index.html")
      }
    }
 }
const app = createApp(options)
app.mount('#app')