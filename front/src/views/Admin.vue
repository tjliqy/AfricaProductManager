<template>
    <div>
        <section class="hero is-primary">
            <div class="hero-body">
                <div class="container">
                    <div class="columns is-vcentered">
                        <div class="column">
                            <p class="title">
                                管理系统
                            </p>
                            <!--<p class="subtitle">-->
                                <!--This is a <strong>material management system</strong>-->
                            <!--</p>-->
                        </div>
                    </div>
                </div>
            </div>

            <div class="hero-foot">
                <div class="container">
                    <nav class="tabs is-boxed">
                        <ul>
                            <li>
                                <router-link to="/admin/statistics">统计信息</router-link>
                            </li>
                            <li>
                                <router-link to="/admin/goods">实时库存</router-link>
                            </li>
                            <li>
                                <router-link to="/admin/log/all">出入库记录</router-link>
                            </li>
                            <li>
                                <router-link to="/admin/equipment">设备</router-link>
                            </li>
                            <li>
                                <a @click="excel">输出到excel</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

        </section>

        <section>
            <router-view></router-view>
        </section>
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
            },
            excel: function () {
                window.open("/api/log/excel")
            }
        }

    }
</script>

<style>

</style>