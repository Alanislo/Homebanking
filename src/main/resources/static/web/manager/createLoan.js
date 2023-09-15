const {createApp} = Vue
 const options = {
    data(){
        return{
           name:"",
           amount:null,
          
           payments:[],
        }
    },
    created(){
    this.loadData()
    },
    methods:{
     
        loadData(){
            axios.get('/api/loans')
            .then(response=>{
                this.accounts = response.data
                console.log(this.accounts);
            }).catch(error => console.log(error))
          },
        alert(){
            let mensaje;
            let object = {  
            "name": this.name,
            "maxAmount": this.amount,
            "payments": this.payments}

            let opcion = confirm("Do you want to create a new type of loan?");
            console.log("Hola");
            console.log(this.name);
            console.log(this.amount);
            console.log(this.payments);
            if (opcion == true) {
                axios.post('/api/loans/create', object)
                .then( response => {
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