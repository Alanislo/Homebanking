const {createApp} = Vue
 const options = {
    data(){
        return{
            email: localStorage.getItem('email') || '',
            password: localStorage.getItem('password') || '',
            rememberMe: false,
            error_msg:""
        }
    },
    methods : {
        login() {
            if (this.rememberMe) {
                localStorage.setItem('email', this.email);
                localStorage.setItem('password', this.password);
              } else {
                localStorage.removeItem('email');
                localStorage.removeItem('password');
              }
                axios.post('/api/login', "email=" + this.email + "&password=" + this.password, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
            })
            .then(response => {
                console.log('signed in!!!');
                if(this.email=="admin@gmail.com"){
                    return window.location.href ="/web/manager/manager.html"
                }else{
                return window.location.href = "/web/public/pages/accounts.html"}  
            }).catch(error => {
                window.alert("The email or password is incorrect")
                console.log(error);
            })

        }
    },
    logout() {
        axios.post(`/api/logout`)
        .then(response => console.log('signed out!!'))
        return (window.location.href = "/index.html")
    }
}
const app = createApp(options)
app.mount('#app')