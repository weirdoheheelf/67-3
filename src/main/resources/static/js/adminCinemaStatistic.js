$(document).ready(function() {

    getScheduleRate();
    
    getBoxOffice();

    getAudiencePrice();

    getPlacingRate();

    getPolularMovie();

    function getScheduleRate() {

        getRequest(
            '/statistics/scheduleRate',
            function (res) {
                var data = res.content||[];
                var tableData = data.map(function (item) {
                   return {
                       value: item.time,
                       name: item.name
                   };
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '今日排片率',
                        subtext: new Date().toLocaleDateString(),
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        x : 'center',
                        y : 'bottom',
                        data:nameList
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    series : [
                        {
                            name:'面积模式',
                            type:'pie',
                            radius : [30, 110],
                            center : ['50%', '50%'],
                            roseType : 'area',
                            data:tableData
                        }
                    ]
                };
                var scheduleRateChart = echarts.init($("#schedule-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function getBoxOffice() {

        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '所有电影票房',
                        subtext: '截止至'+new Date().toLocaleDateString(),
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#box-office-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getAudiencePrice() {
        getRequest(
            '/statistics/audience/price',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.price;
                });
                var nameList = data.map(function (item) {
                    return formatDate(new Date(item.date));
                });
                var option = {
                    title : {
                        text: '每日客单价',
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'line'
                    }]
                };
                var scheduleRateChart = echarts.init($("#audience-price-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getPlacingRate() {
        // todo
        var placeRateDate=formatDate(new Date());
        $('#place-rate-date-input').val(placeRateDate);
        $('#place-rate-date-input').change(function () {
            placeRateDate = $('#place-rate-date-input').val();
        });

        getRequest(
            'statistics/PlacingRate?date='+placeRateDate,
            function (res) {
                var data = res.content || [];
                var tableData=data.map(function (item) {
                    return  item.placingRateByDate;
                });
                var nameList=data.map(function (item) {
                    return item.name;
                });
                var option={
                    title:{
                        text:'上座率',
                        x:'center'
                    },
                    xAxis:{
                        type:'catagory',
                        data:nameList
                    },
                    yAxis:{
                        type:'value'
                    },
                    series:[{
                        data:tableData,
                        type:'line'
                    }]
                };
                var placecRateChart=echarts.init($("#place-rate-container")[0]);
                placecRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getPolularMovie() {
        // todo
        getRequest(
            'statistics/popular/movie',
            function (res) {
                var data=res.content || [];
                var tableData=data.map(function (item) {
                    return item.rank;
                });
                var nameList=data.map(function (item) {
                    return item.name;
                });
                var option={
                    title:{
                        text:'受欢迎电影排行榜',
                        x:'center'
                    },
                    xAxis:{
                        type:'value'
                    },
                    yAxis:{
                        type:'catagory',
                        data:nameList
                    },
                    series:[{
                        type:'bar',
                        data:tableData
                    }]
                };
                var popularMovieChart=echarts.init($("#popular-movie-container")[0]);
                popularMovieChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }
});