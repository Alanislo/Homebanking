const {createApp} = Vue
 const options = {
    data(){
        return{
            clients: [],
            cards:[],
            cardsDebit:[],
            cardsCredit:[],
            thruDate:[],
            fromDate:[],
            cvv:null,
            id: null,
            dateNao: new Date().toISOString().slice(0, 10),
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
              console.log(this.clients);
              this.cards = this.clients.cards.filter(card => card.active)
              console.log(this.cards);
              this.cardsDebit= this.cards.filter(card => card.type == 'DEBIT')
              this.cardsCredit= this.cards.filter(card => card.type == 'CREDIT')
              this.thruDate = this.cards.map(card => card.thruDate.slice(2,7))
              this.fromDate = this.cards.map(card => card.fromDate.slice(2,7))
              this.cvv = this.cards
           
            })
            .catch(error => console.log(error))
          },
        eliminateCard(id){
            let mensaje;
            let opcion = confirm("Do you want eliminate a this card?");
            console.log("Hola");
            if (opcion == true) {
             console.log(this.cardType);
             console.log(this.cardColor);
             axios.patch('/api/clients/current/cards/deactivate' ,`id=${id}`)
             .then( response => {
                  location.href ="/web/public/pages/cards.html"})
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
              .then
  
          return (window.location.href = "../../index.html")
      }
    }
  }
const app = createApp(options)
app.mount('#app')
 