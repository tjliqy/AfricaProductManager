<template>
    <div>
        <div class="columns">
            <div class="column is-2">
                <div class="box" style="height: 100%">
                    <aside class="menu">
                        <p class="menu-label">
                            超市管理系统
                        </p>
                        <ul class="menu-list is-marginless">
                            <li v-show="user.position === 0">
                                <router-link to="/admin/statistics">统计信息
                                </router-link>
                            </li>
                            <li v-show="user.position === 0">
                                <router-link to="/admin/goods">实时库存
                                </router-link>
                            </li>
                            <li v-show="user.position === 0">
                                <router-link to="/admin/log">历史日志
                                </router-link>
                            </li>
                            <li v-show="user.position === 2">
                                <router-link to="/admin/buy">采购商品
                                </router-link>
                            </li>
                            <li v-show="user.position === 1">
                                <router-link to="/admin/sell">销售商品
                                </router-link>
                            </li>
                        </ul>
                    </aside>
                </div>
            </div>
            <div class="column">

                <nav class="nav">
                    <div class="nav-left">
                        <a class="nav-item">
                            <!--<img src="http://bulma.io/images/bulma-logo.png" alt="Bulma logo">-->
                        </a>
                    </div>

                    <!-- This "nav-menu" is hidden on mobile -->
                    <!-- Add the modifier "is-active" to display it on mobile -->
                    <div class="nav-right nav-menu">
                        <a class="nav-item">
                            {{user.name}}
                        </a>
                        <div class="nav-item">
                            <div class="field is-grouped">
                                <p class="control">
                                    <a class="button is-primary" @click="logout">
                                        <span>Logout</span>
                                    </a>
                                </p>
                            </div>
                        </div>
                    </div>
                </nav>
                <div style="padding-top: 20px">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    export default{
        props: {
            user: Object
        },
        methods: {
            logout: function () {
                this.$http.get('/api/admin/logout').then(function (response) {
                    response = response.body;
                    if (response.errno === -1) {
                        this.$router.replace('/Login')
                    }
                })
            }
        },
        created(){
            if (this.user === undefined) {
                this.$router.push('/Login');
            }else {
                switch (this.user.position){
                    case 0:
                        this.$router.push('/admin/statistics');
                        break;
                    case 1:
                        this.$router.push('/admin/sell');
                        break;
                    case 2:
                        this.$router.push('/admin/buy');
                        break;
                }
            }
        }

    }
</script>

<style>

</style>