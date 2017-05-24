<template>
    <div>
        <h1>{{tableName}}</h1>
        <div class="box">
            <table class="table is-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>表名</th>
                    <th>中文表名</th>
                    <th>操作</th>
                </tr>
                <tr v-for="table in tablePage.list">
                    <td>{{table.id}}</td>
                    <td>{{table.name}}</td>
                    <td>{{table.cnName}}</td>
                    <td><a class="button modal-button" @click="openModal(table)">修改</a>
                        <a class="button is-danger">删除</a></td>
                </tr>
                </thead>
            </table>

            <a class="button is-primary" @click="openModal">增加更多表单</a>

            <nav class="pagination">
                <a v-if="tablePage.firstPage" class="pagination-previous" disabled>上一页</a>
                <a v-else class="pagination-previous">上一页</a>
                <a v-if="tablePage.lastPage" class="pagination-next" disabled>下一页</a>
                <a v-else class="pagination-next">下一页</a>
                <ul class="pagination-list">
                    <li v-for="(page,index) in pages">
                        <a class="pagination-link"
                           :class="{'is-current' :tablePage.pageNumber === index+1}">{{page}}</a>
                    </li>
                </ul>
            </nav>
        </div>
        <table-edit-modal :currentTable="currentTable" :visible="showModal" @close="closeModal"></table-edit-modal>
    </div>
</template>

<script>

    export default{
        data() {
            return {
                tablePage: Object,
                showModal: false,
                currentTable: Object
            }
        },
        methods: {
            initTable: function (page) {
                this.$http.get('/api/table/list?page=' + page).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        this.tablePage = res.tablePage;
                    }
                })
            },
            openModal: function (table) {
                this.currentTable = table;
                this.showModal = true;
            },
            closeModal: function () {
                this.showModal = false;
            }
        },
        created() {
            this.initTable(1);
        },
        computed: {
            pages() {
                let pages = [];
                for (let i = 1; i <= this.tablePage.totalPage; i++) {
                    pages.push(i);
                }
                return pages;
            }
        }

    }

</script>

<style lang="scss">

</style>