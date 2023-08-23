const {createApp} = Vue
 const options = {
    data(){
        return{
            clients: [],
            firstName:"",
            lastName:"",
            email:"",
            json: ""
        }
    },
    created(){
      this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/rest/clients")
            .then( response => {
              this.clients = response.data._embedded.clients;
              this.json = JSON.stringify(response.data, null, 1)
            })
            .catch(error => console.log(error))
        },
        checkInputs(e){
           console.log(this.firstName);
           console.log(this.lastName);
           console.log(this.email);
            if(this.firstName && this.lastName && this.email){
            e.preventDefault
                this.addClient()
            }else{
                alert('Please fill in all required fields')
            }
        },
        addClient(){
            let client = {
                firstName :this.firstName,
                lastName:this.lastName,
                email:this.email
            }
            //enviar a la api un nuevo cliente
            axios.post("http://localhost:8080/rest/clients", client)
            .then(response => {
                this.clients.push(response.data)// le envio a la data un nuevo cliente
                this.firstName="",
                this.lastName="",
                this.email="",
                this.loadData()
            })
            .catch(error => console.log(error))
        }
    }
 }
const app = createApp(options)
app.mount('#app')
 