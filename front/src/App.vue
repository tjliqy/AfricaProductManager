<template>
    <div id="app">
        <div class="content">
            <!--<keep-alive>-->
            <router-view :user="user"></router-view>
            <!--</keep-alive>-->
        </div>

    </div>
</template>

<script>
    //	import NprogressContainer from 'vue-nprogress/src/NprogressContainer.vue'

    export default {
        name: 'app',
        components: {
//	        NprogressContainer
        },
        data () {
            return {
                user: Object
            }
        },
        created() {
            this.$router.push('/admin/statistics');
//            this.$http.get('/api/admin/user').then(function (response) {
//                response = response.body;
//                if (response.errno === -1) {
//                    this.user = response.user;
//                    this.$router.push('/admin');
//                } else {
//                    this.$router.push('/login');
//                }
//            });
        }
    }
</script>

<style lang="scss">
    @import '../node_modules/bulma/bulma.sass';

    html {
        background-color: whitesmoke;
    }

    /*.nprogress-container {*/
    /*position: fixed !important;*/
    /*width: 100%;*/
    /*height: 50px;*/
    /*z-index: 2048;*/
    /*pointer-events: none;*/

    /*#nprogress {*/
    /*$ color: #48e79a;*/

    /*.bar {*/
    /*background: $ color;*/
    /*}*/

    /*.peg {*/
    /*box-shadow: 0 0 10px $ color, 0 0 5px $ color;*/
    /*}*/

    /*.spinner-icon {*/
    /*border-top-color: $ color;*/
    /*border-left-color: $ color;*/
    /*}*/

    /*}*/
    /*}*/
</style>
