const {createApp} = Vue
 const options = {
    data(){
        return{
            cardType:[],
            cardColor:[],

        }
    },
    created(){
      //this.loadData()

    },
    methods:{
        alerta(){
            let mensaje;
            let opcion = confirm("Do you want to create a new card?");
            if (opcion == true) {
             console.log(this.cardType);
             console.log(this.cardColor);
                axios.post('/api/clients/current/cards', `type=${this.cardType}&color=${this.cardColor}` )
                .then( response => {
                  location.href ="/web/public/pages/cards.html"})
                  .catch(error => {
                    console.log(error.response);
                    window.alert(error.response.data)
            })
            } else {
                mensaje = "Cancel";
            }
           // document.getElementById("ejemplo").innerHTML = mensaje;
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