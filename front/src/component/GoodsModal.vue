<template>
    <div>
        <transition
                :name="transition"
                mode="in-out"
                appear
                :appear-active-class="enterClass"
                :enter-active-class="enterClass"
                :leave-active-class="leaveClass"
                @beforeEnter="initGood"
                @afterLeave="closeModal"
        >
            <div class="modal is-active" v-if="show">
                <div class="modal-background" @click="closeModal"></div>
                <div class="modal-card">
                    <header class="modal-card-head">
                        <p class="modal-card-title"></p>
                        <button class="delete" @click="closeModal"></button>
                    </header>
                    <section class="modal-card-body">
                        <!-- Content ... -->
                        <div class="content">
                            <h2>基础信息</h2>
                            <p>商品名：{{good.name}}
                                <br>
                                单价：{{good.prize}}
                                <br>
                                数量：{{good.num}}
                                <br>
                                生产厂家：{{good.brand}}
                                <br>
                            </p>
                            <hr>
                            <div v-if="good.type === 1">
                                <h2>服装信息</h2>
                                <p>
                                    颜色：{{good.color}}
                                    <br>
                                    尺寸：{{good.size}}
                                    <br>
                                    适合人群：{{good.crowd}}
                                </p>
                            </div>
                            <div v-else-if="good.type === 2">
                                <h2>食品信息</h2>
                                <p>
                                    保质期：{{good.shelf_life}}
                                    <br>
                                    产地:{{good.origin}}
                                </p>
                            </div>

                        </div>

                    </section>
                    <footer class="modal-card-foot">
                        <a class="button is-success">Save changes</a>
                        <a class="button" @click="closeModal">Cancel</a>
                        <a style="color:#ff0000" v-show="warningMsg !== &quot;&quot;">{{warningMsg}}
                            <button class="delete is-small" @click="warningMsg = &quot;&quot;"></button>
                        </a>
                    </footer>
                </div>
            </div>
        </transition>
    </div>
</template>

<script>
    import {CardModal} from 'vue-bulma-modal';
    export default{
        props: {
            currentGood: Object,
            visible: Boolean,
            transition: {
                type: String,
                default: 'fade'
            }
        },
        data() {
            return {
                warningMsg: "",
                good: Object
            }
        },
        computed: {
            show: function () {
                return this.visible
            },
            enterClass () {
                return `${this.transition}In`
            },

            leaveClass () {
                return `${this.transition}Out`
            }
        },
        components: {
            CardModal
        },
        methods: {
            closeModal: function () {
                this.good = "";
                this.$emit('close');
            },
            initGood: function () {
                if (this.currentGood !== undefined) {
                    this.$http.get('/api/goods/detail?id=' + this.currentGood.id).then(function (res) {
                        res = res.body;
                        if (res.errno === -1) {
                            this.good = res.data.columns;
                        }
                    })
                }
            }
        },

    }
</script>

<style lang="sass">

</style>