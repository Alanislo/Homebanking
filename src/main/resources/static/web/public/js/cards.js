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
              this.cards = this.clients.cards
              this.cardsDebit= this.cards.filter(card => card.type == 'DEBIT')
              this.cardsCredit= this.cards.filter(card => card.type == 'CREDIT')
              this.thruDate = this.cards.map(card => card.thruDate.slice(2,7))
              this.fromDate = this.cards.map(card => card.fromDate.slice(2,7))
              console.log(this.cardsDebit);
              console.log(this.cardsCredit);
             
            })
            .catch(error => console.log(error))
        }, logout() {
          axios.post(`/api/logout`)
  
          .then(response => console.log('signed out!!'))
              .then
  
          return (window.location.href = "/index.html")
  
      }
    }
 }
const app = createApp(options)
app.mount('#app')
 