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
                        <h3 class="title">基础信息</h3>
                        <div class="field">
                            <label class="label">商品名</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="商品名" v-model="good.name"
                                       :disabled="disabled">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">生产商</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="生产商" v-model="good.brand"
                                       :disabled="disabled">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">商品类型</label>
                            <p class="control">
                            <div class="select is-fullwidth">
                                <select v-model="good.type">
                                    <option v-for="type in types" :value="type.value" :disabled="disabled">
                                        {{type.name}}
                                    </option>
                                </select>
                            </div>
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">成本价（元/个）</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="成本价" v-model="good.cost"
                                       :disabled="disabled">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">首价（元/个）</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="售价" v-model="good.prize"
                                       :disabled="disabled">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">数量</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="数量" v-model="good.num">
                            </p>
                        </div>
                        <div v-if="good.type === 1">
                            <h3 class="title">服装信息</h3>
                            <div class="field">
                                <label class="label">颜色</label>
                                <p class="control">
                                    <input class="input" type="text" placeholder="颜色" v-model="good.color"
                                           :disabled="disabled">
                                </p>
                            </div>
                            <div class="field">
                                <label class="label">尺寸</label>
                                <p class="control">
                                    <input class="input" type="text" placeholder="尺寸" v-model="good.size"
                                           :disabled="disabled">
                                </p>
                            </div>
                            <div class="field">
                                <label class="label">适合人群</label>
                                <p class="control">
                                    <input class="input" type="text" placeholder="适合人群" v-model="good.crowd"
                                           :disabled="disabled">
                                </p>
                            </div>
                        </div>
                        <div v-if="good.type === 2">
                            <h3 class="title">食品信息</h3>
                            <div class="field">
                                <label class="label">保质期</label>
                                <p class="control">
                                    <input class="input" type="text" placeholder="保质期" v-model="good.shelf_life"
                                           :disabled="disabled">
                                </p>
                            </div>
                            <div class="field">
                                <label class="label">产地</label>
                                <p class="control">
                                    <input class="input" type="text" placeholder="产地" v-model="good.origin"
                                           :disabled="disabled">
                                </p>
                            </div>
                        </div>

                    </section>
                    <footer class="modal-card-foot">
                        <a class="button is-success" @click="save">采购</a>
                        <a class="button" @click="closeModal">取消</a>
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
                disabled: false,
                good: {
                    cost: 0,
                    num: 0,
                    origin: "",
                    name: "",
                    type: 1,
                    brand: "",
                    prize: 0,
                    shelf_life: "2017-01-22",
                    crowd: "",
                    color: "",
                    size: ""
                },
                types: [
                    {name: '服装', value: 1},
                    {name: '食品', value: 2}
                ]
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
                this.$emit('closeEdit');
            },
            initGood: function () {
                this.disabled = false;
                if (this.currentGood !== undefined) {
                    this.$http.get('/api/goods/detail?id=' + this.currentGood.id).then(function (res) {
                        res = res.body;
                        if (res.errno === -1) {
                            this.disabled = true;
                            this.good = res.data.columns;
                            this.good.num = 0;
                        }
                    })
                }
            },
            save(){
                this.$http.post('/api/goods/purchase', this.good).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        alert('保存成功');
                    }
                })
            }
        }
    }
</script>

<style lang="sass">

</style>