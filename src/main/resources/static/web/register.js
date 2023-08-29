const { createApp } = Vue;
const options = {
  data() {
    return {
        firstName:"",
        lastName:"",
        email: "",
        password: "",
    };
  },
  methods: {
    register() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {
          this.login()
        })
        
        .catch(error => {
          console.log(error.response);
          window.alert(error.response.data)
        });
    },
    
    login() {
      axios.
      post('/api/login', "email=" + this.email + "&password=" + this.password, {
          headers: {
              'content-type': 'application/x-www-form-urlencoded'
          }
      })

      .then(response => {
         
          console.log('signed in!!!');
          if(this.email=="admin@gmail.com"){
              return window.location.href ="./manager/manager.html"
          }else{
          return window.location.href = "./public/pages/accounts.html"}

      }) .catch(error => {
        console.log(error.response);
        window.alert(error.response.data)
      })

  }
  }

};
const app = createApp(options);
app.mount("#app");
