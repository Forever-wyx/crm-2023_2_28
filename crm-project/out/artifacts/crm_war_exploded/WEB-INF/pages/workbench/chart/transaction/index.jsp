<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String contextPath = request.getScheme() + "://" + request.getServerName() + ":"
+ request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=contextPath%>">
    <title>ECharts</title>
    <!-- 引入刚刚下载的 ECharts 文件 -->
    <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function () {
        // 基于准备好的dom，初始化echarts实例


            $.ajax({
                url: "workbench/chart/transaction/queryCountOfTranGroupByStage.do",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var myChart = echarts.init(document.getElementById('main'));

                    option = {
                        title: {
                            text: 'Funnel'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c}%'
                        },
                        toolbox: {
                            feature: {
                                dataView: { readOnly: false },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                        },
                        series: [
                            {
                                name: 'Expected',
                                type: 'funnel',
                                left: '10%',
                                width: '80%',
                                label: {
                                    formatter: '{b}Expected'
                                },
                                labelLine: {
                                    show: false
                                },
                                itemStyle: {
                                    opacity: 0.7
                                },
                                emphasis: {
                                    label: {
                                        position: 'inside',
                                        formatter: '{b}Expected: {c}%'
                                    }
                                },
                                data: data
                            },
                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })


        })
    </script>
</head>
<body>
    <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="main" style="width: 100%;height: 100%;"></div>
</body>
</html>