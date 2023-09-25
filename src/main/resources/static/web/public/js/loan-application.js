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
            finalAmount:null,
            amountTotal:null,
            max:null
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
          
        },
        calculoInteres() {
            console.log(this.selectOriginAccount);
            console.log(this.selectLoan);
            console.log(this.finalAmount);
            console.log(this.loans);
            
            if (this.selectPayments == 3) {
                this.finalAmount = this.amount + (this.amount * 0.0375)
                return this.finalAmount;
            }
            else if (this.selectPayments == 6) {
                this.finalAmount = this.amount + (this.amount * 0.075)
                return this.finalAmount;
            }
            else if (this.selectPayments == 12) {
                this.finalAmount = this.amount + (this.amount * 0.105)
                return this.finalAmount;
            }
            else if (this.selectPayments == 24) {
                this.finalAmount = this.amount + (this.amount * 0.165)
                return this.finalAmount;
            }
            else if (this.selectPayments == 36) {
                this.finalAmount = this.amount + (this.amount * 0.225)
                return this.finalAmount;
            }
            else if (this.selectPayments == 48) {
                this.finalAmount = this.amount + (this.amount * 0.335)
                return this.finalAmount;
            }
            else if (this.selectPayments == 60) {
                this.finalAmount = this.amount + (this.amount * 0.395)
                return this.finalAmount;
            } else { return 0 };
        } 
    },
    methods:{
       
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
                  location.href ="/web/public/pages/accounts.html"})
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
                this.originAccounts = response.data.filter(account => account.active)  
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
          return (window.location.href = "../../index.html")
      }
    }
 }
const app = createApp(options)
app.mount('#app')