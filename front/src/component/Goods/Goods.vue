<template>
    <div>
        <div class="box">
            <select-level :conditions="conditions" :type="'level'" @search="search"></select-level>
        </div>
        <div class="box">
            <a class="button " @click="openEditModal()">新物资/配件</a>
            <table class="table is-striped">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>物资/配件名称</th>
                    <th>物资/配件代码</th>
                    <th>单位</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>配件类别</th>
                    <th>总价</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="good in goodsPage.list">
                    <td>{{good.id}}</td>
                    <td>{{good.name}}</td>
                    <td>{{good.number}}</td>
                    <td>{{good.unit}}</td>
                    <td>{{good.prize}}</td>
                    <td>{{good.quantity}}</td>
                    <td>{{good.type}}</td>
                    <td>{{good.quantity*good.prize}}</td>
                    <td>
                        <a class="button is-info" @click="openEditModal(good)">修改</a>
                        <input class="input" type="number" style="width: 110px;" placeholder="出库数量" v-model="good.sell">
                        <a class="button is-info" @click="purchase(good)">入库</a>
                        <a class="button is-info" @click="sell(good)">出库</a>
                        <a class="button" @click="log(good.id)">出入库记录</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <div v-show="!hasItem" class="is-fullwidth">
                <p class="notification is-info ">没有符合条件的记录</p>
            </div>
            <nav class="pagination">
                <a v-if="goodsPage.firstPage" class="pagination-previous" disabled>上一页</a>
                <a v-else class="pagination-previous" @click="initPage(goodsPage.pageNumber - 1)">上一页</a>
                <a v-if="goodsPage.lastPage" class="pagination-next" disabled>下一页</a>
                <a v-else class="pagination-next" @click="initPage(goodsPage.pageNumber + 1)">下一页</a>
                <ul class="pagination-list">
                    <li v-for="(page,index) in pages" @click="initPage(index + 1)">
                        <a class="pagination-link"
                           :class="{'is-current' :goodsPage.pageNumber === index+1}">{{page}}</a>
                    </li>
                </ul>
            </nav>
        </div>
        <goods-modal :currentGood="currentGood" :visible="show" @close="closeModal"></goods-modal>
        <goods-edit-modal :currentGood="currentGood" :visible="showEdit" @closeEdit="closeEditModal"></goods-edit-modal>

    </div>
</template>

<script>
    import GoodsModal from './GoodsModal';
    import SelectLevel from '../layout/SelectLevel';
    import GoodsEditModal from './GoodsEditModal';
    import Vue from 'vue'
    export default{
        components: {
            GoodsModal,
            SelectLevel,
            GoodsEditModal
        },
        data() {
            return {
                goodsPage: Object,
                show: false,
                showEdit: false,
                currentGood: Object,
                conditions: [
                    {name: '配件名称', param: 'name', value: "", type: "input"},
                    {name: '配件代码', param: 'brand', value: "", type: "input"},
                    {name: '单价', param: 'prize', value: "", minParam: 'minPrize', larParam: 'larPrize', type: "range"},
                    {name: '成本价', param: 'cost', value: "", minParam: 'minCost', larParam: 'larCost', type: "range"},
                    {
                        name: '状态', param: 'selling', value: "", type: "select", select: [
                        {name: '在售', value: 1},
                        {name: '下架', value: -1}
                    ]
                    }
//                    {name: '生产商', param: 'brand', value: ""},

                ],
                params: "",
            }
        },
        methods: {
            initPage: function (page) {
                this.$http.get('/api/goods/search?page=' + page + this.params).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        this.goodsPage = res.data;
                    }
                })
            },
            openModal: function (good) {
                this.currentGood = good;
                this.show = true;
            },
            closeModal: function () {
                this.show = false;
            },
            openEditModal: function (good) {
                this.currentGood = good;
                this.showEdit = true;
            },
            closeEditModal: function () {
                this.showEdit = false;
                this.initPage(this.goodsPage.pageNumber);
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
//            sellMode(selling) {
//                switch (selling) {
//                    case 1:
//                        return '在售';
//                    case -1:
//                        return '下架';
//                }
//            },
//            toggleSelling(good) {
//                this.$http.post('/api/goods/toggleSelling', good).then(function (res) {
//                    res = res.body;
//                    if (res.errno === -1) {
//                        good.selling = good.selling * -1;
//                    }
//                })
//            },
            search(param){
                this.params = param.paramUrl;
                this.initPage(1);
            },
            sell(good) {
                if (good.sell === undefined) {
                    alert("请输入出库数量");
                } else if (good.sell > good.quantity) {
                    alert('数量不足');
                } else {
                    this.$http.post('/api/goods/sell', {'id': good.id, 'quantity': good.sell}).then(function (res) {
                        res = res.body;
                        if (res.errno === -1) {
                            alert("出库成功")
                            this.initPage(this.goodsPage.pageNumber);
                        }
                    })
                }
            },
            purchase(good){
                if (good.sell === undefined) {
                    alert("请输入入库数量");
                } else {
                    good.purchase = good.sell;
                    this.$http.post('/api/goods/purchase', good).then(function (res) {
                        res = res.body;
                        if (res.errno === -1) {
                            alert('入库成功');
                            this.initPage(this.goodsPage.pageNumber);
                        }
                    })
                }
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
                for (let i = 1; i <= this.goodsPage.totalPage; i++) {
                    pages.push(i);
                }
                return pages;
            },
            hasItem(){
                if (!this.goodsPage) {
                    return false;
                } else {
                    if (this.goodsPage.list.length === 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }

    }

</script>

<style lang="scss">

</style>