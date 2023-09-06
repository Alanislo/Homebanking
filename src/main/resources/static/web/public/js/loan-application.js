const {createApp} = Vue
 const options = {
    data(){
        return{
            loans:[],
            originAccounts:null,
            amount:null,
            selectLoan:{},
            selectOriginAccount:{},
            selectPayments:{},
            finalAmount: null,
        }
    },
    created(){
      this.loadData()
      this.loadLoans()
    },
    computed:{
        prueba(){
            console.log(this.selectOriginAccount);
            console.log(this.selectLoan);
        }   

    },
    methods:{
        finAmount(){
            this.finalAmount = this.amount * 1.2
        },
        alerta(){
            let mensaje;
            let opcion = confirm("Do you want to create a new loan?");
            let object = {
                "id": this.selectLoan.id,
                "amount": this.amount,
                "payments": this.selectPayments,
                "destinationAccount":this.selectOriginAccount
            }
            console.log(object);
            console.log("Hola");
            if (opcion == true) {
                console.log(object);
                axios.post('/api/loans', object)
                .then( response => {
                  location.href ="/web/public/pages/accounts.html"}
            )
                  .catch(error => {
                    console.log(error.response);
                    window.alert(error.response.data)
            })
            } else {
                mensaje = "Cancel";
            }
      
        },
        loadData(){
            axios.get('/api/clients/current/accounts')
            .then(response=>{
                this.originAccounts = response.data
                console.log(this.originAccounts);
            }).catch(error => console.log(error))
        },
        loadLoans(){
            axios.get('/api/loans')
            .then(response=>{
                this.loans = response.data
                console.log(this.loans);
            }).catch(error => console.log(error))
        },
 
       logout() {
          axios.post(`/api/logout`)
          .then(response => console.log('signed out!!'))
              .then
          return (window.location.href = "../../index.html")
      }
    }
 }
const app = createApp(options)
app.mount('#app')