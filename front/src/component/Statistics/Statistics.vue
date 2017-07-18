<template>
    <div class="container" style="margin-top: 20px;">
        <div class="columns">
            <div class="column">
                <div class="box">
                    <e-charts :options="polar" ref="chart" auto-resize></e-charts>
                </div>
                <div class="box">
                    <e-charts :options="optionNum" auto-resize></e-charts>
                </div>
            </div>
            <div class="column is-hidden-touch">
                <div class="box">
                    <select-level :conditions="conditions" :type="'field'" @search="search"></select-level>
                    <a class="button is-fullwidth" style="margin-top: 10px" @click="excel">输出为Excel</a>
                </div>
            </div>
            <div class="tile">
                <div class="tile is-vertical ">
                    <div class="tile is-parent">

                    </div>
                    <div class="tile">
                        <div class="tile is-parent">

                        </div>
                    </div>
                </div>
                <div class="tile">
                    <article class="tile is-parent">

                    </article>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    import SelectLevel from '../layout/SelectLevel';
    import ECharts from 'vue-echarts/components/ECharts.vue';
    import 'echarts'

    export default{
        components: {
            SelectLevel,
            ECharts
        },
        data() {

            return {
                logList: Array,
                conditions: [
                    {name: '时间范围(不填则认为是当年12个月)', minParam: 'minTime', larParam: 'larTime', value: "", type: "time"},
                ],

                polar: {
                    title: {
                        text: '月统计图'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {},
                            dataZoom: {}
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#6a7985'
                            }
                        }
                    },
                    legend: {
                        data: ['销售额', '进货成本']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: []
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '销售额',
                            type: 'line',
                            areaStyle: {normal: {}},
                            data: []
                        },
                        {
                            name: '进货成本',
                            type: 'line',
                            areaStyle: {normal: {}},
                            data: []
                        }
                    ]
                },
                optionNum: {
                    title: {
                        text: '商品销售统计'
                    },
                    tooltip: {},
                    legend: {
                        data: ['销量', '销售额']
                    },
                    xAxis: {
                        data: []
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '销量'
                        },
                        {
                            type: 'value',
                            name: '销售额',
                            axisLabel: {
                                formatter: '{value} 元'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '销量',
                            type: 'bar',
                            data: []
                        },
                        {
                            name: '销售额',
                            type: 'bar',
                            yAxisIndex: 1,
                            data: []
                        }
                    ]
                },
                minTime: '',
                larTime: ''
            }
        },
        methods: {
            search(params) {
                let url = '/api/log/list?1=1';
                if (params) {
                    this.minTime = params.minTime;
                    this.larTime = params.larTime;
                    url += params.paramUrl + '&minTime=' + params.minTime + '&larTime=' + params.larTime;
                }
                this.$http.get(url).then(function (res) {
                    res = res.body;
                    if (res.errno === -1) {
                        this.logList = res.data;
                        this.polar.xAxis[0].data = res.monthList;
                        this.polar.series[0].data = res.monthSell;
                        this.polar.series[1].data = res.monthPurchase;
                        this.optionNum.xAxis.data = res.goodNames;
                        this.optionNum.series[0].data = res.goodNums;

                        this.optionNum.series[1].data = res.goodCost;

                    }
                })
            },
            excel() {
                let url = '/api/log/excel?1=1';
                url += '&minTime=' + this.minTime + '&larTime=' + this.larTime;

                window.open(url);
//                this.$http.get(url).then(function (res) {
//                    res = res.body;
//                })
            },
            month(stamp){
                let time = new Date(parseInt(stamp));
                let y = time.getFullYear();
                let m = time.getMonth() + 1;
                let d = time.getDate();

                return y + '.' + add0(m);
            }
        },
        created(){
            this.$nextTick(() => {
                this.search();
            })
        }
    }

</script>

<style lang="scss">

</style>