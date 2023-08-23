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
            axios.get("http://localhost:8080/api/clients/1")
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
        }
    }
 }
const app = createApp(options)
app.mount('#app')
 