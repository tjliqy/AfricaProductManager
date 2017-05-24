<template>
    <div>
        <nav class="level" v-show="type === 'level'">
            <!-- Left side -->
            <div class="level-left">
                <div class="level-item">
                    <div class="field has-addons">
                        <!--搜索选项-->
                        <p class="control">
    <span class="select">
      <select v-model="selectedCondition">
        <option v-for="(condition,index) in conditions" :value="index">{{condition.name}}</option>
      </select>
    </span>
                        </p>

                        <!--输入框-->
                        <p class="control" v-if="conIndexType(selectedCondition) === 0">
                            <input class="input" type="text" placeholder="搜索条件" v-model="inputValue">
                        </p>

                        <!--子选项菜单-->
                        <p class="control" v-else-if="conIndexType(selectedCondition) === 1">
    <span class="select">
      <select v-model="selectValue">
        <option v-for="condition in conditions[selectedCondition].select"
                :value="condition.value">{{condition.name}}</option>
      </select>
    </span>
                        </p>

                        <!--范围搜索框-->
                        <div class="field has-addons" v-if="conIndexType(selectedCondition) === 2">
                            <p class="control">
                                <input class="input" type="number" placeholder="最小值" v-model="minValue">
                            </p>
                            <p class="control">
                                <input class="input" type="number" placeholder="最大值" v-model="larValue">
                            </p>
                        </div>

                        <!--时间范围搜索框-->
                        <div class="field has-addons" v-if="conIndexType(selectedCondition) === 3">
                            <p class="control">
                                <datepicker class="input" :date="startTime" :option="optionS"></datepicker>
                            </p>
                            <p class="control">
                                <datepicker class="input" :date="endTime" :option="optionE"></datepicker>
                            </p>
                        </div>
                        <p class="control">
                            <a class="button is-info" @click="addCondition">
                                增加搜索条件
                            </a>
                        </p>
                    </div>
                </div>
                <div class="level-item " v-for="condition in resConditions">
                        <span v-show="condition.value" class="tag is-success is-medium">{{condition.name}}:{{condition.showValue}}
                        <button class="delete is-small" @click="condition.value=&quot;&quot;"></button></span>
                </div>
            </div>

            <!-- Right side -->
            <div class="level-right">
                <a class="button" @click="search">开始搜索</a>
            </div>
        </nav>
        <div v-show="type === 'field'">
            <div class="field" v-for="(condition,index) in conditions">
                <label class="label">{{condition.name}}</label>
                <!--输入框-->
                <p class="control" v-if="conIndexType(index) === 0">
                    <input class="input" type="text" :placeholder="condition.name" v-model="condition.value">
                </p>

                <p class="control" v-else-if="conIndexType(index) === 1">
                <span class="select is-fullwidth">
                    <select v-model="condition.value">
                        <option v-for="select in condition.select" :value="select.value">
                            {{select.name}}
                        </option>
                    </select>
                </span>
                </p>

                <div class="field has-addons" v-else-if="conIndexType(index) === 2">
                    <p class="control">
                        <input class="input" type="number" placeholder="最小值" v-model="condition.larValue">
                    </p>
                    <p class="control">
                        <input class="input" type="number" placeholder="最大值" v-model="condition.minValue">
                    </p>
                </div>

                <!--时间范围搜索框-->
                <div class="field" v-else-if="conIndexType(index) === 3">
                    <p class="control">
                        <datepicker class="input" :date="startTime" :option="optionS"></datepicker>
                    </p>
                    <p class="control">
                        <datepicker class="input" :date="endTime" :option="optionE"></datepicker>
                    </p>
                </div>
            </div>
            <a class="button is-success is-fullwidth" @click="search">开始搜索</a>
        </div>
    </div>
</template>

<script>
    import Datepicker from 'vue-datepicker'

    const INPUT = 0;
    const SELECT = 1;
    const RANGE = 2;
    const TIME = 3;

    export default{
        props: {
            conditions: Array,
            type: '',
        },
        components: {
            Datepicker
        },
        data() {
            return {
                resConditions: Array,
                selectedCondition: 0,
                inputValue: "",
                selectValue: "",
                larValue: "",
                minValue: "",

                startTime: {
                    time: ''
                },
                endTime: {
                    time: ''
                },

                pl: 'asda',

                optionS: {
                    type: 'day',
                    week: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'],
                    month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                    format: 'YYYY-MM-DD',
                    placeholder: '起始时间?',
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
                },
                optionE: {
                    type: 'day',
                    week: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'],
                    month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                    format: 'YYYY-MM-DD',
                    placeholder: '结束时间?',
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
        methods: {
            params() {
                let me = this;
                if (this.resConditions) {
                    let paramString = "";
                    this.resConditions.forEach(function (condition) {
                        switch (me.conType(condition)) {
                            case RANGE:
                            case TIME:
                                if (!me.isBlank(condition.larValue)) {
                                    paramString += ("&" + condition.larParam + "=" + condition.larValue);
                                }
                                if (!me.isBlank(condition.minValue)) {
                                    paramString += ("&" + condition.minParam + "=" + condition.minValue);
                                }
                                break;
                            default:
                                if (condition.value !== "") {
                                    paramString += ("&" + condition.param + "=" + condition.value);
                                }
                        }
                    });
                    return paramString;
                } else {
                    return "";
                }
            },
            initTime(index){
                this.selectedCondition = index;
                this.addCondition();
            },
            isBlank: function (value) {
                return value === undefined || value === "" || value === '';
            },
            addCondition: function () {
                let condition = this.resConditions[this.selectedCondition];
                switch (this.conType(condition)) {
                    case INPUT:
                        if (this.inputValue !== "") {
                            condition.value = this.inputValue;
                            condition.showValue = this.inputValue;
                        }
                        break;
                    case SELECT:
                        if (this.selectValue !== "") {
                            let me = this;
                            condition.value = this.selectValue.toString();
                            condition.select.forEach(function (select) {
                                if (select.value === me.selectValue) {
                                    condition.showValue = select.name;
                                }
                            });
                        }
                        break;
                    case RANGE:
                        let res = "";
                        if (this.minValue !== "") {
                            condition.value = "show";
                            condition.minValue = this.minValue;
                            res += '(最小：' + this.minValue + ')';
                        }
                        if (this.larValue !== "") {
                            condition.value = "show";
                            condition.larValue = this.larValue;
                            res += '(最大：' + this.larValue + ')';
                        }
                        condition.showValue = res;
                        break;
                    case TIME:
                        let res2 = '';
                        if (this.startTime.time !== '') {
                            condition.value = "show";
                            res += ' FROM:' + this.startTime.time;
                            condition.minValue = this.startTime.time;
                        }
                        if (this.endTime.time !== '') {
                            condition.value = "show";
                            res += ' TO:' + this.endTime.time;
                            condition.larValue = this.endTime.time;
                        }
                        condition.showValue = res;
                        break;
                }
            },
            conIndexType(index) {
                return this.conType(this.conditions[index]);
            },
            conType(condition){
                switch (condition.type) {
                    case "input":
                        return INPUT;
                    case "select":
                        return SELECT;
                    case "time":
                        return TIME;
                    case "range":
                        return RANGE;
                }
            },
            search() {
                this.$emit('search', {paramUrl: this.params(), minTime: this.startTime.time, larTime: this.endTime.time});
            }
        },
        created() {
            this.resConditions = this.conditions;
        }
    }
</script>

<style>

</style>