<template>
    <div>
        <div class="box">
            <select-level :conditions="conditions" :type="'level'" @search="search"></select-level>
        </div>
        <div class="box">
            <a class="button" @click="openEditModal()">采购新商品</a>
            <table class="table is-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>商品名</th>
                    <th>成本价</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>生产厂家</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="good in goodsPage.list">
                    <td>{{good.id}}</td>
                    <td>{{good.name}}</td>
                    <td>{{good.cost}}</td>
                    <td>{{good.prize}}</td>
                    <td>{{good.num}}</td>
                    <td>{{good.brand}}</td>
                    <td>{{sellMode(good.selling)}}</td>
                    <td><a class="button modal-button" @click="openModal(good)">详情</a>
                        <a class="button is-danger" @click="toggleSelling(good)" v-if="good.selling === 1">下架</a>
                        <a class="button is-success" @click="toggleSelling(good)" v-else>上架</a>
                        <a class="button is-success" @click="openEditModal(good)">采购</a>
                        <input class="input" type="number" style="width: 110px;" placeholder="销售数量" v-model="good.sell">
                        <a class="button is-success" @click="sell(good)">销售</a>
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
    import SelectLevel from './layout/SelectLevel';
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
                    {name: '商品名', param: 'name', value: "", type: "input"},
                    {name: '生产商', param: 'brand', value: "", type: "input"},
                    {name: '单价', param: 'prize', value: "", minParam: 'minPrize', larParam: 'larPrize', type: "range"},
                    {name: '成本价', param: 'cost', value: "", minParam: 'minCost', larParam: 'larCost', type: "range"},
                    {
                        name: '状态', param: 'selling', value: "", type: "select", select: [
                        {name: '在售', value: 1},
                        {name: '下架', value: -1}
                    ]
                    },
                    {
                        name: '种类', param: 'type', value: "", type: "select", select: [
                        {name: '服装', value: 1},
                        {name: '食品', value: 2}
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
            sellMode(selling) {
                switch (selling) {
                    case 1:
                        return '在售';
                    case -1:
                        return '下架';
                }
            },
            toggleSelling(good) {
                this.$http.post('/api/goods/toggleSelling', good).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        good.selling = good.selling * -1;
                    }
                })
            },
            search(param){
                this.params = param.paramUrl;
                this.initPage(1);
            },
            sell(good) {
                if (good.selling === -1) {
                    alert('该商品出去下架状态，无法销售');
                    return;
                }
                if (good.sell === undefined) {
                    alert("请输入销售数量");
                } else if (good.sell > good.num) {
                    alert('数量不足');
                } else {
                    this.$http.post('/api/goods/sell', {'id': good.id, 'num': good.sell}).then(function (res) {
                        res = res.body;
                        if (res.errno === -1) {
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