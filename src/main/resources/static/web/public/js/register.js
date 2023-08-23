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
        .then((response) => console.log("registered"))
        .catch((error) => {console.log(error);
        });
    },
  },

};
const app = createApp(options);
app.mount("#app");
