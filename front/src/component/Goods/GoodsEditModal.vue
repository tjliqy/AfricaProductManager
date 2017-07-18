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
                            <label class="label">物资/配件名称</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="物资/配件名称" v-model="good.name">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">物资/配件代码</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="物资/配件代码" v-model="good.number">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">单位</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="单位" v-model="good.unit">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">配件类别</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="配件类别" v-model="good.type">
                                <!--<div class="select is-fullwidth">-->
                                <!--<select v-model="good.type">-->
                                <!--<option v-for="type in types" :value="type.value" :disabled="disabled">-->
                                <!--{{type.name}}-->
                                <!--</option>-->
                                <!--</select>-->
                                <!--</div>-->
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">单价（元）</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="请输入一个正数" v-model="good.prize">
                            </p>
                        </div>
                        <div class="field" v-show="disabled">
                            <label class="label">当前剩余数量</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="" v-model="good.quantity" :disabled="disabled">
                            </p>
                        </div>

                        <div class="field" v-show="!disabled">
                            <label class="label">入库数量</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="请输入一个正数" v-model="good.purchase">
                            </p>
                        </div>


                    </section>
                    <footer class="modal-card-foot">
                        <button class="button is-success" @click="save" :class="{'is-loading':loading}">保存</button>
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
                    number: 0,
                    name: "",
                    type: "",
                    prize: 0,
                },
                loading:false,
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
                        }
                    })
                }
            },
            save(){
                this.loading = true;
                this.$http.post('/api/goods/purchase', this.good).then(function (res) {
                    this.loading = false;
                    res = res.body;
                    if (res.errno === -1) {
                        alert('保存成功');
                        this.closeModal()
                    }
                })
            }
        }
    }
</script>

<style lang="sass">

</style>