const {createApp} = Vue
 const options = {
    data(){
        return{
            loans:[],
            originAccounts:null,
            amount:null, 
            selectLoan:null,
            selectAccounts:null,
            finalAmount:0,
            selectOriginAccount:null,
            totalAmount:null,
        
        }
    },
    created(){
      this.loadData()
    },
    computed:{
        prueba(){
            console.log(this.selectOriginAccount);
            console.log(this.selectLoan);
            console.log(this.amount);
        },  
        finAmount(){
            return this.totalAmount = this.selectLoan.amount / this.selectLoan.payments || 0
        }
    },
    methods:{
        
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
              .then(response => {
                this.clients = response.data
                console.log(response.data);
                this.originAccounts = this.clients.accountSet.filter(account => account.active)
                this.loans = this.clients.clientLoans.filter(account => account.active)     
    
              })
              .catch(error => console.log(error))
          },
          
        
        alerta(){
            let mensaje;
            console.log(this.loans);
                console.log(this.originAccounts);
                console.log(this.amount);
                console.log(this.selectLoan);
            let opcion = confirm("Do you want to pay a loan?");
       
            console.log("Hola");
            if (opcion == true) {
                axios.patch('/api/clients/current/loans/loanPayment', `loanId=${this.selectLoan.id}&accountId=${this.selectOriginAccount}&paymentAmount=${this.totalAmount}`)
                .then( response => {
                    location.href ="/web/public/pages/accounts.html"
                  })
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