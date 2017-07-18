<template>

    <section class="hero is-fullheight is-dark is-bold">

        <div class="hero-body">
            <div class="container">
                <div class="columns is-vcentered">
                    <!--登录-->
                    <div class="column is-4 is-offset-4" v-show="loginModel">

                        <h1 class="title" >
                            Login
                        </h1>
                        <div class="box">
                            <label class="label">Account</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="email@example.org" v-model="user.account">
                            </p>
                            <label class="label">Password</label>
                            <p class="control">
                                <input class="input" type="password" v-model="user.password">
                            </p>
                            <hr>
                            <p class="control">
                                <button class="button is-primary" :class="{'is-loading':loading}" @click="login" :disabled="loading">Login</button>
                                <button class="button is-default">Cancel</button>
                            </p>
                        </div>
                        <p class="has-text-centered">
                            <a @click="toggleModel">Register an Account</a>
                            |
                            <a href="#">Forgot password</a>
                            |
                            <a href="#">Need help?</a>
                        </p>
                    </div>
                </div>
                <!--注册-->
                <div class="column is-4 is-offset-4" v-show="!loginModel">
                    <h1 class="title">
                        Register an Account
                    </h1>
                    <div class="box">
                        <label class="label">Name</label>
                        <p class="control">
                            <input class="input" type="text" placeholder="Your Name" v-model="user.name">
                        </p>
                        <label class="label">Username</label>
                        <p class="control">
                            <input class="input" type="text" placeholder="account" v-model="user.account">
                        </p>
                        <label class="label">Password</label>
                        <p class="control">
                            <input class="input" type="password" placeholder="●●●●●●●" v-model="user.password">
                        </p>
                        <label class="label">Position</label>
                        <p class="control">
                        <div class="select">
                            <select v-model="user.position">
                                <option v-for="option in options" :value="option.value">
                                    {{ option.text }}
                                </option>
                            </select>
                        </div>
                        </p>
                        <hr>
                        <p class="control">
                            <button class="button is-primary" @click="signup" :class="{'is-loading':loading}">Register</button>
                            <button class="button is-default">Cancel</button>
                        </p>
                    </div>
                    <p class="has-text-centered">
                        <a @click="toggleModel">Login</a>
                        |
                        <a href="#">Need help?</a>
                    </p>
                </div>
            </div>
        </div>

    </section>

</template>

<script>
    export default{
        prop:{
            user: Object
        },
        data() {
            return {
                loginModel: true,
                user:{
                    account: null,
                    password: null,
                    name: null,
                    position: null
                },
                loading: false,
                options: [
                    { text: 'Super Admin', value: 0 },
                    { text: 'Seller', value: 1 },
                    { text: 'Buyer', value: 2 }
                ]
            }
        },
        methods: {
            toggleModel() {
                this.loginModel = !this.loginModel;
            },
            login() {
                this.loading = true;
                this.$http.post('/api/admin/login',this.user).then(function(response){
                    response = response.body;
                    this.loading = false;
                    if (response.errno === -1){
                        this.user = response.user;
                        window.location.href='/';
                    }else{
                        alert(response.msg);
                    }
                });
            },
            signup() {
                this.loading = true;
                this.$http.post('/api/admin/signup',this.user).then(function(response){
                    response = response.body;
                    this.loading = false;
                    if (response.errno === -1){
                        this.user = response.user;
                        alert("Success!");
                        this.loginModel = true;
                    }else{
                        alert(response.msg);
                    }
                });
            }
        }
    }
</script>

<style lang="scss">

</style>