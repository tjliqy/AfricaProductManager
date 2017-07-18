<template>
    <div>
        <div class="box">
            <select-level :conditions="conditions" :type="'level'" @search="search"></select-level>
        </div>
        <div class="box">
            <a class="button " @click="openEditModal()">采购新设备</a>
            <table class="table is-striped">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>管理编号</th>
                    <th>牌照号码</th>
                    <th>设备名称</th>
                    <th>设备型号</th>
                    <th>设备规格</th>
                    <th>发动机号</th>
                    <th>底盘号</th>
                    <th>所在项目</th>
                    <th>技术状况</th>
                    <th>生产厂家</th>
                    <th>设备原值</th>
                    <th>累计折旧</th>
                    <th>折旧年限</th>
                    <th>年折旧率</th>
                    <th>设备净值</th>
                    <th>采购时间</th>
                    <th>操作</th>

                </tr>
                </thead>
                <tbody>
                <tr v-for="item in items">
                    <td>{{item.id}}</td>
                    <td>{{item.number}}</td>
                    <td>{{item.plate}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.model}}</td>
                    <td>{{item.specifications}}</td>
                    <td>{{item.engine}}</td>
                    <td>{{item.chassis}}</td>
                    <td>{{item.product}}</td>
                    <td>{{item.situation}}</td>
                    <td>{{item.manufacturer}}</td>
                    <td>{{item.originalValue}}</td>
                    <td>{{item.depreciationRate}}</td>
                    <td>{{item.depreciationAge}}</td>
                    <td>{{rate(item.depreciationAge)}}</td>
                    <td>{{item.lastValue}}</td>
                    <td>{{item.purchaseDate}}</td>
                    <td>
                        <a class="button is-info" @click="openEditModal(item)">修改</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <div v-show="!hasItem" class="is-fullwidth">
                <p class="notification is-info ">没有符合条件的记录</p>
            </div>
            <nav class="pagination">
                <a v-if="page.firstPage" class="pagination-previous" disabled>上一页</a>
                <a v-else class="pagination-previous" @click="initPage(page.pageNumber - 1)">上一页</a>
                <a v-if="page.lastPage" class="pagination-next" disabled>下一页</a>
                <a v-else class="pagination-next" @click="initPage(page.pageNumber + 1)">下一页</a>
                <ul class="pagination-list">
                    <li v-for="(page,index) in pages" @click="initPage(index + 1)">
                        <a class="pagination-link"
                           :class="{'is-current' :page.pageNumber === index+1}">{{page}}</a>
                    </li>
                </ul>
            </nav>
        </div>
        <goods-modal :currentItem="currentItem" :visible="show" @close="closeModal"></goods-modal>
        <equipment-edit-modal :currentItem="currentItem" :visible="showEdit" @closeEdit="closeEditModal"></equipment-edit-modal>

    </div>
</template>

<script>
    import GoodsModal from '../Goods/GoodsModal';
    import SelectLevel from '../layout/SelectLevel';
    import EquipmentEditModal from './EquipmentEditModal.vue';
    import Vue from 'vue'
    export default{
        components: {
            GoodsModal,
            SelectLevel,
            EquipmentEditModal
        },
        data() {
            return {
                page: {
                    list:[]
                },
                show: false,
                showEdit: false,
                currentItem: Object,
                conditions: [
                    {name: '设备名称', param: 'name', value: "", type: "input"},
                    {name: '管理编码', param: 'number', value: "", type: "input"},
//                    {name: '单价', param: 'prize', value: "", minParam: 'minPrize', larParam: 'larPrize', type: "range"},
//                    {name: '成本价', param: 'cost', value: "", minParam: 'minCost', larParam: 'larCost', type: "range"},
//                    {
//                        name: '状态', param: 'selling', value: "", type: "select", select: [
//                        {name: '在售', value: 1},
//                        {name: '下架', value: -1}
//                    ]
//                    }
//                    {name: '生产商', param: 'brand', value: ""},

                ],
                params: "",
            }
        },
        methods: {
            initPage: function (page) {
                this.$http.get('/api/equipment/search?page=' + page + this.params).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        this.page = res.data;
                    }
                })
            },
            openModal: function (item) {
                this.currentItem = item;
                this.show = true;
            },
            closeModal: function () {
                this.show = false;
            },
            openEditModal: function (item) {
                this.currentItem = item;
                this.showEdit = true;
            },
            closeEditModal: function () {
                this.showEdit = false;
                this.initPage(this.page.pageNumber);
            },
            addCondition: function () {
                if (this.inputCondition !== "") {
                    this.searchCondition[this.selectedCondition].value = this.inputCondition;
                }
            },
            params() {
                if (this.searchCondition) {
                    let paramString = "";
                    this.searchCondition.forEach(function (condition) {
                        if (condition.value !== "") {
                            paramString += ("&" + condition.param + "=" + condition.value);
                        }
                    });
                    return paramString;
                } else {
                    return "";
                }
            },
            log(id){
                this.$router.push('/admin/log/'+id)
            },
            search(param){
                this.params = param.paramUrl;
                this.initPage(1);
            },
            rate(year){
                return (100/year).toFixed(2)+"%"
            },
            time(value) {
                function add0(m) {
                    return m < 10 ? '0' + m : m
                }

                let time = new Date(parseInt(value));
                let y = time.getFullYear();
                let m = time.getMonth() + 1;
                let d = time.getDate();
                let h = time.getHours();
                let min = time.getMinutes();
                let s = time.getMilliseconds();

                return y + '.' + add0(m) + '.' + add0(d);
            },
            depreciationRate(equipment){
                let date = new Date(parseInt(equipment.purchaseTime));
                let currentDay = Math.floor((new Date().getTime() - date.getTime())/(24*3600*1000));
                let depreciationDay = equipment.depreciationAge*365;
                return currentDay/depreciationDay;

            },
            lastValue(equipment){
                return (equipment.originalValue*(1-equipment.depreciationRate)).toFixed(2);
            }
        },
        created()
        {
            this.initPage(1);
        },
        computed: {
            pages()
            {
                let pages = [];
                for (let i = 1; i <= this.page.totalPage; i++) {
                    pages.push(i);
                }
                return pages;
            },
            hasItem(){
                if (!this.page) {
                    return false;
                } else {
                    if (this.page.list.length === 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            },
            items(){
                let items = [];
                let me = this;
                this.page.list.forEach(function (item) {
                    item.depreciationRate = me.depreciationRate(item);
                    item.lastValue = me.lastValue(item);
                    item.depreciationRate = item.depreciationRate.toFixed(2) + "%";
                    item.purchaseDate = me.time(item.purchaseTime);
                    items.push(item);
                });
//                for (let i = 0; i <= this.page.list.length; i++) {
//                    let item = this.page.list[i];
//
//                }
                return items;
            }
        }

    }

</script>

<style lang="scss">

</style>