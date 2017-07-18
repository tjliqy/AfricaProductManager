<template>
    <div>
        <div class="box">
            <select-level :conditions="conditions" :type="'level'" @search="search"></select-level>
        </div>
        <div class="box">
            <table class="table is-striped">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>物资/配件序号</th>
                    <th>操作类型</th>
                    <th>出/入库数量</th>
                    <th>单价</th>
                    <th>交易总金额</th>
                    <th>更新时间</th>
                    <!--<th>操作</th>-->
                </tr>
                <tr v-for="log in logPage.list">
                    <td>{{log.id}}</td>
                    <td>{{log.goodId}}</td>
                    <td>{{log.type}}</td>
                    <td>{{log.num}}</td>
                    <td>{{log.cost}}</td>
                    <td>{{log.totalCost}}</td>
                    <td>{{log.updateTime | time}}</td>
                    <!--<td><a class="button modal-button" @click="openModal(log)">商品详情</a></td>-->
                </tr>
                </thead>
            </table>
            <div v-show="!hasItem" class="is-fullwidth">
                <p class="notification is-info ">没有符合条件的记录</p>
            </div>
            <nav class="pagination">
                <a v-if="logPage.firstPage" class="pagination-previous" disabled>上一页</a>
                <a v-else class="pagination-previous" @click="initPage(logPage.pageNumber - 1)">上一页</a>
                <a v-if="logPage.lastPage" class="pagination-next" disabled>下一页</a>
                <a v-else class="pagination-next" @click="initPage(logPage.pageNumber + 1)">下一页</a>
                <ul class="pagination-list">
                    <li v-for="(page,index) in pages" @click="initPage(index + 1)" style="margin-top:0">
                        <a class="pagination-link"
                           :class="{'is-current' :logPage.pageNumber === index+1}">{{page}}</a>
                    </li>
                </ul>
            </nav>
        </div>
        <goods-modal :currentGood="currentGood" :visible="show" @close="closeModal"></goods-modal>
    </div>
</template>

<script>
    import GoodsModal from './Goods/GoodsModal';
    import SelectLevel from './layout/SelectLevel'
    export default{
        components: {
            GoodsModal,
            SelectLevel
        },
        data() {
            return {
                logPage: Object,
                show: false,
                currentGood: Object,
                conditions: [
                    {name: '物资/配件序号', param: 'goodId', value: "", type: "input"},
                    {
                        name: '操作类型', param: 'type', value: "", type: "select", select: [
                        {name: '入库', value: 4},
                        {name: '出库', value: 3}
                    ]
                    },

                    {name: '单价', param: 'prize', value: "", minParam: 'minPrize', larParam: 'larPrize', type: "range"},
                    {name: '时间范围', minParam: 'minTime', larParam: 'larTime', value: "", type: "time"},
                ],
                params: "",

            }
        },
        methods: {
            initPage: function (page) {
                this.$http.get('/api/log?page=' + page + this.params).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        this.logPage = res.data;
                    }
                })
            },
            openModal: function (log) {
                this.currentGood.id = log.goodId;
                this.show = true;
            },
            closeModal: function () {
                this.show = false;
            },
            search(param){
                this.params = param.paramUrl;
                this.initPage(1);
            }
        },
        created() {
            if (this.$route.params.id !== 'all') {
//                this.conditions[0].value = this.$route.params.id;
                this.params = "&goodId=" + this.$route.params.id;
            }
            this.initPage(1);
        },
        filters: {
            time: function (value) {
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

                return y + '.' + add0(m) + '.' + add0(d) + ' ' + add0(h) + ':' + add0(min) + ':' + add0(s);
            }
        },
        computed: {
            pages() {
                let pages = [];
                for (let i = 1; i <= this.logPage.totalPage; i++) {
                    pages.push(i);
                }
                return pages;
            },
            hasItem(){
                if (!this.logPage) {
                    return false;
                } else {
                    if (this.logPage.list.length === 0) {
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