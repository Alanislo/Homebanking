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
            errors: [],
            filteredAccounts: [],
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
            filterDestinationOptions() {
              const selectedOriginAccount = this.accountOrigen;
              this.filteredAccounts = this.accounts.filter(account => account.number !== selectedOriginAccount);
            },
          
        transfer(){
            this.myTransfer = false;
        },
        transfer2(){
            this.myTransfer = true;
        },
        loadData(){    
            axios.get('/api/clients/current/accounts')
            .then(response=>{
                this.accounts = response.data.filter(account => account.active)
                console.log(this.accounts);
            }).catch(error => {
                console.log("Hubo un error!!!:")
                error.log(error)})
          },
          checkForm: function (e) {
            this.errors = [];
      
            if (this.amount <= 0 )  {
              this.errors.push("The amount must be positive");
            }
            if (!this.accountOrigen) {
                this.errors.push('Choose source account');
              }
              if (!this.accountDestino) {
                this.errors.push('Choose the destination account');
              }
              if (!this.description) {
                this.errors.push('Enter a description');
              }
            else{
                let mensaje;
                let opcion = confirm("Do you want to create a new transfer?");
                if (opcion == true) {
                 console.log(this.accountOrigen);
                 console.log(this.accountDestino);
                 console.log(this.amount);
                 console.log(this.description);
                    axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&origin=${this.accountOrigen}&destination=${this.accountDestino}`)
                    .then( response => {
                        location.href ="/web/public/pages/accounts.html"
                      })
                      .catch(error => {
                        console.log(error.response.data);
                        window.alert(error.response.data)
                })
                } else {
                    mensaje = "Cancel";
                }
            }
            e.preventDefault();
          },
        // alert(){
        //     let mensaje;
        //     let opcion = confirm("Do you want to create a new transfer?");
        //     if (opcion == true) {
        //      console.log(this.accountOrigen);
        //      console.log(this.accountDestino);
        //      console.log(this.amount);
        //      console.log(this.description);
        //         axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&origin=${this.accountOrigen}&destination=${this.accountDestino}`)
        //         .then( response => {
        //             location.href ="/web/public/pages/accounts.html"
        //           })
        //           .catch(error => {
        //             console.log(error.response.data);
        //             window.alert(error.response.data)
        //     })
        //     } else {
        //         mensaje = "Cancel";
        //     }
           
        // },
       logout() {
          axios.post(`/api/logout`)
          .then(response => console.log('signed out!!'))
          return (window.location.href = "../../index.html")
      }
    }
 }
const app = createApp(options)
app.mount('#app')