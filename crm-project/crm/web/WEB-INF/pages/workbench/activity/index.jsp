<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String contextPath = request.getScheme() + "://" + request.getServerName() + ":"
+ request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<meta charset="UTF-8">
	<base href="<%=contextPath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css"/>

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){

		//点击"创建"按钮的事件
		$("#createActivityBtn").click(function () {
			//初始化工作,清空form表单里面的值
			//保存成功一次后,form表单里面就有值了
			$("#createActivityForm").get(0).reset();

			//弹出模态窗口
			$("#createActivityModal").modal("show")
		})

		//点击修改"按钮"事件
		$("#updateActivityBtn").click(function () {
			//初始化工作,验证是否选中一个
			//拿到选中的activity Dom对象
			let activity = $("#tBody input[type='checkbox']:checked")
			if(activity.size() != 1){
				alert("请选择修改一个活动");
				return;
			}

			//取到id,然后查询数据库,给表单赋值
			let id = activity[0].value;
			$.ajax({
				url: 'workbench/activity/queryActivityById.do',
				data: {
					id: id
				},
				method: "post",
				dataType: "json",
				success: function (data) {
					//给表单赋值
					$("#edit-id").val(data.id)
					$("#edit-marketActivityOwner").val(data.owner)
					$("#edit-marketActivityName").val(data.name)
					$("#edit-startTime").val(data.startDate)
					$("#edit-endTime").val(data.endDate)
					$("#edit-cost").val(data.cost)
					$("#edit-describe").val(data.description)
				}
			})
			//弹出模态窗口
			$("#editActivityModal").modal("show")
		})

		//点击模态窗口中"更新"按钮事件
		$("#updateActivity").click(function () {
			//获取表单中的值
			let id = $("#edit-id").val()
			let owner = $("#edit-marketActivityOwner").val()
			let name = $("#edit-marketActivityName").val()
			let startDate = $("#edit-startTime").val()
			let endDate = $("#edit-endTime").val()
			let cost = $("#edit-cost").val()
			let description = $("#edit-describe").val()

			//表单验证
			if (owner == ""){
				alert("所有者不能为空");
				return;
			}
			if (name == ""){
				alert("名称不能为空");
				return;
			}
			//比较日期,结束日期不能小于开始日期
			if (startDate != "" && endDate != ""){
				if (startDate > endDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			//使用正则表达式验证成本只能为非负整数
			var match = /^(([1-9]\d*)|0)$/;
			if(cost != "" && match.test(cost) == false){
				//不满足非负整数
				alert("成本只能为非负整数");
				return;
			}

			//发送异步请求修改数据
			$.ajax({
				url: 'workbench/activity/updateActivityById.do',
				data: {
					id: id,
					owner: owner,
					cost: cost,
					name: name,
					startDate: startDate,
					endDate: endDate,
					description: description
				},
				method: "post",
				dataType: "json",
				success:function(data){
					if (data.code == "1"){
						//更新数据成功,关闭模态窗口
						$("#editActivityModal").modal("hide")
						//刷新页面
						queryActivityByConditionForPage(1, $("#myGroupPage").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#editActivityModal").modal("show")
					}
				}
			})
		})

		//点击模态窗口中的"保存"按钮事件
		$("#saveCreateActivityBtn").click(function () {
			//获取参数
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			//日期后期会处理,不会让用户自行输入,而是提供一个日历给用户选择
			var startDate = $("#create-startDateTime").val();
			var endDate = $("#create-endDateTime").val();

			var cost = $.trim($("#create-cost").val());
			var	description = $.trim($("#create-describe").val());

			//验证数据
			if (owner == ""){
				alert("所有者不能为空");
				return;
			}
			if (name == ""){
				alert("名称不能为空");
				return;
			}
			//比较日期,结束日期不能小于开始日期
			if (startDate != "" && endDate != ""){
				if (endDate < startDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			//使用正则表达式验证成本只能为非负整数
			var match = /^(([1-9]\d*)|0)$/;
			if(cost != "" && match.test(cost) == false){
				//不满足非负整数
				alert("成本只能为非负整数");
				return;
			}

			//使用ajax异步请求
			$.ajax({
				url: 'workbench/activity/saveCreateActivity.do',
				data: {
					owner: owner,
					name: name,
					startDate: startDate,
					endDate: endDate,
					cost: cost,
					description: description
				},
				dataType: 'json',
				method: 'post',
				success:function (data) {
					if (data.code == '1'){
						//数据保存成功,关闭模态窗口
						$("#createActivityModal").modal('hide');
						//刷新数据
						queryActivityByConditionForPage(1,$("#myGroupPage").bs_pagination('getOption', 'rowsPerPage'));

						//判断全选按钮是否选中，如果选中的话需要给新创建的市场活动也加上选中属性

					}else {
						//不关闭模态窗口,默认不关闭
						$("#createActivityModal").modal("show");
						alert(data.message)
					}
				}
			})
		})


		//容器加载完成，加载日期
		$(".myDate").datetimepicker({
			language: 'zh-CN',
			initialDate: new Date(),
			format: 'yyyy-mm-dd',
			minView: 'month',
			autoclose: true,
			clearBtn: true,
			todayBtn: true
		})

        //页面加载完成，页面发送请求响应市场活动数据,查询第一页数据，默认每页显示10条数据
		queryActivityByConditionForPage(1,10);


		//点击查询按钮的事件
        $("#queryBtn").click(function () {
			queryActivityByConditionForPage(1,$("#myGroupPage").bs_pagination('getOption', 'rowsPerPage'));
        })

		//全选按钮事件
		$("#checkAll").click(function () {
			$("#tBody input[type='checkbox']").prop("checked",this.checked)
		})

		//下面列表的单选按钮事件
		$("#tBody").on("click","input[type='checkbox']",function () {
			//列表中的所有数据和选中的数据比较就可以知道是否全部选中没有
			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()){
				//全选
				$("#checkAll").prop("checked",true)
			}else {
				$("#checkAll").prop("checked",false)
			}
		})


		//删除按钮的点击事件
		$("#deleteActivityBtn").click(function () {
			//获取选中删除市场活动的所有对象
			let checkerActivity = $("#tBody input[type='checkbox']:checked")
			if(checkerActivity.size() == 0){
				alert("请选择要删除的活动")
				return;
			}
			if (window.confirm("确定删除吗")){
				//拿到选中删除的id
				let ids = ""
				$.each(checkerActivity,function () {
					ids += "id=" + this.value + "&";
				})
				ids = ids.substr(0,ids.length - 1);
				$.ajax({
					url: "workbench/activity/deleteActivityByIds.do",
					data: ids,
					method: "post",
					dataType: "json",
					success: function (data) {
						if (data.code == "1") {
							//删除成功,刷新页面
							queryActivityByConditionForPage(1, $("#myGroupPage").bs_pagination('getOption', 'rowsPerPage'));
							//取消全选按钮
							$("#checkAll").prop("checked",false)
						} else {
							//删除失败,提示用户
							alert(data.message);
						}
					}
				})
			}
		})

		//批量导出按钮事件
		$("#exportActivityAllBtn").click(function () {
			//下载文件只能用同步请求;如果是使用异步请求的话,Ajax只能解析json,根本无法解析文件
			window.location.href = "workbench/activity/queryAllActivities.do";
		})

		//选择导出按钮事件
		$("#exportActivityXzBtn").click(function () {
			//封装选中的市场活动
			let activityList = $("#tBody input[type='checkbox']:checked");
			let ids = "";
			$.each(activityList,function () {
				ids += "id=" + this.value + "&"
			})
			ids = ids.substr(0,ids.length -1);

			//发送同步请求
			window.location.href = "workbench/activity/queryActivitiesByIds.do?" + ids;
		})

		//上传列表数据按钮事件
		$("#importActivityBtn").click(function () {
			//验证文件名后缀是否为xls,大小写都可以
			//获得上传文件的dom对象
			let activityObj = $("#activityFile")[0]
			let filename = activityObj.value;
			let suffix = filename.substr(filename.lastIndexOf(".") + 1).toLocaleLowerCase();
			if (suffix != "xls"){
				alert("只能上传xls文件");
				return;
			}
			let file = activityObj.files[0];
			if (file.size > 1024 * 1024 * 5){
				alert("只能上传不超过5M的文件");
				return;
			}
			let formData = new FormData();
			formData.append("file",file);
			$.ajax({
				url: "workbench/activity/importActivity.do",
				data: formData,
				method: "post",
				dataType: "json",
				processData: false,
				contentType: false,
				success: function (data) {
					if (data.code == "1"){
						alert("成功导入:" + data.retData + "条记录");
						//关闭模态窗口
						$("#importActivityModal").modal("hide");
						//刷新页面
						queryActivityByConditionForPage(1, $("#myGroupPage").bs_pagination('getOption', 'rowsPerPage'));
					}else {
						alert(data.message)
						//不关闭模态窗口
						$("#importActivityModal").modal("show");
					}
				}
			})
		})
	});

	function groupPage(pageNo,pageSize,totalRows) {
		//总页数
		var allPages = Math.ceil(totalRows / pageSize)
		//分页插件
		$("#myGroupPage").bs_pagination({
			currentPage: pageNo,		//显示当前页号，相当于pageNo
			rowsPerPage: pageSize,	//每页显示条数,相当于pageSize
			totalPages: allPages,	//总页数,必填
			totalRows: totalRows,		//总条数,totalRows
			visiblePageLinks: 5,	//最多可以显示的分页数
			showGoToPage: true,		//是否显示"跳转到"部分默认true-- 显不
			showRowsPerPage: true,	//是否显示"每页显示条数”部分。默认true--显不
			showRowsInfo: true,		//是含显示记录的信息，默认true-- 显示
			//每次返回切换页号之后pageNo和pageSize
			onChangePage: function(event,pageObj) {
				// returns page_num and rows_per_page after a link has clicked
				//这里使用的是代码,如果访问次数太多的话应该会奔溃,后续需要改进代码
				//刷新activityList列表
				queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
			},
		})
	}

	function queryActivityByConditionForPage(pageNo,pageSize) {
        //收集参数
        var name = $("#query_name").val();
        var owner = $("#query_owner").val();
        var startDate = $("#query_startDate").val();
        var endDate = $("#query_endDate").val();
        // var pageNo = 1
        // //默认每页10条数据
        // var pageSize = 10;
        $.ajax({
            //渲染页面
            url: "workbench/activity/queryActivityByConditionForPage.do",
            data: {
                name: name,
                owner: owner,
                startDate: startDate,
                endDate: endDate,
                pageNo: pageNo,
                pageSize: pageSize
            },
            method: 'post',
            dataType: 'json',
            success:function (data) {
				//$("#totalRowsB").html(data.totalRows)
                var htmlStr = "";
                $.each(data.activityList,function (index,obj) {

                    htmlStr += "<tr class=\"active\">"
                    htmlStr += "<td><input type=\"checkbox\" value='" + obj.id + "' /></td>"
                    htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/toDetailActivity.do?id=" + obj.id + "'\">" + obj.name + "</a></td>"
                    htmlStr += "<td>" + obj.owner + "</td>"
                    htmlStr += "<td>" + obj.startDate + "</td>"
                    htmlStr += "<td>" + obj.endDate + "</td>"
                    htmlStr += "</tr>"
                })
				$("#tBody").html(htmlStr)

				//加载分页插件
				groupPage(pageNo,pageSize,data.totalRows);
            }
        })
    }
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createActivityForm" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDateTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" readonly id="create-startDateTime">
							</div>
							<label for="create-endDateTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" readonly id="create-endDateTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivity">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="query_name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="query_owner" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query_startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query_endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="updateActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" id="deleteActivityBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button id="importActivity" type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="checkAll" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>
					</tbody>
				</table>
				<div id="myGroupPage"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>