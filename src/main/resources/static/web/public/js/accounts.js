const { createApp } = Vue
const options = {
  data() {
    return {
      clients: [],
      clientsAccounts: [],
      loans: [],
      date: [],
      accountType:[]
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
          this.clients = response.data
          console.log(response.data);
          this.clientsAccounts = this.clients.accountSet.filter(account => account.active)
          this.loans = this.clients.clientLoans
          console.log(this.clientsAccounts);
          console.log(this.loans);

        })
        .catch(error => console.log(error))
    },
    logout() {
      axios.post(`/api/logout`)

        .then(response => console.log('signed out!!'))
        .then

      return (window.location.href = "../../index.html")

    },
    eliminateAccount(id) {
      console.log(id);
      let mensaje;
      let opcion = confirm("Do you want eliminate a this card?");
      console.log("Hola");
      if (opcion == true) {
        axios.patch('/api/clients/current/accounts/disable', `id=${id}`)
          .then(response => {
            location.href ="/web/public/pages/accounts.html"
          })
          .catch(error => {
            console.log(error.response);
            window.alert(error.response.data)
            location.href="/web/public/pages/transfers.html"
          })
      } else {
        mensaje = "Cancel";
      }
    },

    createNewAccount() {
      let mensaje;
      let opcion = confirm("Do you want to create a new account?");
      if (opcion == true) {
        console.log(this.accountType);
        axios.post('http://localhost:8080/api/clients/current/accounts', `type=${this.accountType}`)
          .then(response => {
            location.href = "/web/public/pages/accounts.html"
          }).catch(error => {
            console.log(error.response);
            window.alert(error.response.data)
          })

      } else {
        mensaje = "Cancel";
      }
    },

  }
}
const app = createApp(options)
app.mount('#app')
