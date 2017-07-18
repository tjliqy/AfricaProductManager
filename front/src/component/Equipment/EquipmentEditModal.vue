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
                            <label class="label">设备名称</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="设备名称" v-model="item.name">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">管理编号</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="管理编号" v-model="item.number">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">牌照号码</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="牌照号码" v-model="item.plate">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">设备型号</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="设备型号" v-model="item.model">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">设备规格</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="请输入一个正数" v-model="item.specifications">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">发动机号</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="发动机号" v-model="item.engine">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">底盘号</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="底盘号" v-model="item.chassis">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">所在项目</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="所在项目" v-model="item.product">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">技术状况</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="技术状况" v-model="item.situation">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">生产厂家</label>
                            <p class="control">
                                <input class="input" type="text" placeholder="生产厂家" v-model="item.manufacturer">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">设备原值</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="设备原值" v-model="item.originalValue">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">折旧年限</label>
                            <p class="control">
                                <input class="input" type="number" placeholder="请输入一个正数" v-model="item.depreciationAge">
                            </p>
                        </div>
                        <div class="field">
                            <label class="label">采购时间</label>
                            <p class="control">
                                <datepicker class="input" :date="this.purchaseTime" :option="option"></datepicker>
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
    import Datepicker from 'vue-datepicker'

    export default {
        props: {
            currentItem: Object,
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
                item: {
                    number: 0,
                    originalValue: 0,
                    depreciationAge: 0
                },
                loading:false,
                purchaseTime:{
                    time:''
                },
                option: {
                    type: 'day',
                    week: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'],
                    month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                    format: 'YYYY-MM-DD',
                    placeholder: '点击此处选择时间',
                    color: {
                        header: '#ccc',
                        headerText: '#f00'
                    },
                    buttons: {
                        ok: 'Ok',
                        cancel: 'Cancel'
                    },
                    overlayOpacity: 0.5, // 0.5 as default
                    dismissible: true // as true as default
                }
            }
        },
        computed: {
            show: function () {
                return this.visible
            },
            enterClass() {
                return `${this.transition}In`
            },

            leaveClass() {
                return `${this.transition}Out`
            }
        },
        components: {
            CardModal,
            Datepicker
        },
        methods: {
            closeModal: function () {
                this.item = "";
                this.$emit('closeEdit');
            },
            initGood: function () {
                this.disabled = false;
                if (this.currentItem !== undefined) {
//                    this.$http.get('/api/goods/detail?id=' + this.currentItem.id).then(function (res) {
//                        res = res.body;
//                        if (res.errno === -1) {
//                            this.disabled = true;
//                            this.item = res.data.columns;
//                        }
//                    })
                    this.item = this.currentItem;

                }
            },
            save() {
                this.loading = true;
                if(this.purchaseTime.time != ''){
                    this.item.purchaseTime = this.purchaseTime.time;
                }
                this.$http.post('/api/equipment/edit', this.item).then(function (res) {
                    res = res.body;
                    this.loading = false;
                    if (res.errno === -1) {
                        alert('保存成功');
                        this.closeModal()
                    }else {
                        alert('未知错误');
                    }
                })
            }
        }
    }
</script>

<style lang="sass">

</style>