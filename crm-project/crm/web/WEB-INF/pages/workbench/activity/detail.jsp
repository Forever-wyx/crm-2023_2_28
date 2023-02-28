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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		$("#remarkParent").on("mouseover",".remarkDiv",function () {
			$(this).children("div").children("div").show();
		});

		$("#remarkParent").on("mouseout",".remarkDiv",function () {
			$(this).children("div").children("div").hide();
		});

		$("#remarkParent").on("mouseover",".myHref",function () {
			$(this).children("span").css("color","red");
		});

		$("#remarkParent").on("mouseout",".myHref",function () {
			$(this).children("span").css("color","#E6E6E6");
		});

		//点击保存按钮事件
		$("#saveCreateActivityRemarkBtn").click(function () {
			let noteContent = $("#remark").val();
			let activityId = '${activity.id}';
			let createBy = '${sessionScope.user.id}';
			//发送异步请求,添加备注信息
			$.ajax({
				url: "workbench/activity/saveCreateActivityRemark.do",
				data:{
					noteContent: noteContent,
					activityId: activityId,
					createBy: createBy
				},
				method: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == "1"){
						//数据添加成功,局部刷新列表,追加数据
						let htmlStr = ""
						htmlStr += "<div id=\"div_" + data.retData.id +"\" class=\"remarkDiv\" style=\"height: 60px;\">"
						htmlStr += "<img title=\"" + data.retData.createBy + "\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">"
						htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;\" >"
						htmlStr += "<h5 id=\"h5_" + data.retData.id +"\">" + data.retData.noteContent + "</h5>"
						htmlStr += "<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${requestScope.activity.name}</b> <small style=\"color: gray;\"> "+data.retData.createTime+" 由 ${sessionScope.user.name}创建</small>"
						htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">"
						htmlStr += "<a class=\"myHref\" name=\"editA\" remarkId=" + data.retData.id + " href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"
						htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;"
						htmlStr += "<a class=\"myHref\" name=\"removeA\" remarkId=" + data.retData.id + " href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"
						htmlStr += "</div>"
						htmlStr += "</div>"
						htmlStr += "</div>"
						$("#remarkDiv").before(htmlStr);

						//清空输入框内容
						$("#remark").val("")
					}else {
						alert(data.message)
					}
				}
			})
		})

		//点击删除图标事件
		$("#remarkParent").on("click","a[name='removeA']",function () {
			let id = $(this).attr("remarkId");
			alert(id)
			//异步请求,删除
			$.ajax({
				url: 'workbench/Activity/deleteActivityRemarkById.do',
				data: {
					id: id
				},
				method: "post",
				dataType: "json",
				success: function (data) {
					if (data.code == "1"){
						//删除成功,移除该div标签
						$("#div_" + id).remove();
					}else{
						alert(data.message)
					}
				}
			})
		})

		//点击修改图标事件
		$("#remarkParent").on("click","a[name='editA']",function () {
			//获取当前点击的这个活动的noteContent,给里面的文本内容赋值
			let id = $(this).attr("remarkId");
			let noteContent = $("#h5_" + id).text();
			$("#noteContent").text(noteContent);
			//隐藏域赋值
			$("#remarkId").val(id);
			//弹出模态窗口
			$("#editRemarkModal").modal("show");
		})

		//点击模态窗口中的更新按钮
		$("#updateRemarkBtn").click(function(){
			let noteContent = $.trim($("#noteContent").val());
			let id = $("#remarkId").val();
			//表单验证
			if(noteContent == ""){
				alert("备注内容不能为空")
				return;
			}
			$.ajax({
				url: "workbench/Activity/editActivityRemarkById.do",
				data: {
					noteContent: noteContent,
					id: id
				},
				method: "post",
				dataType: "json",
				success: function (data) {
					if (data.code == "1"){
						//修改成功
						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
						//刷新备注列表
						$("#h5_" + data.retData.id).text(data.retData.noteContent)
						$("#div_" + data.retData.id + " small").html(data.retData.editTime + "由" + "${sessionScope.user.name}" + "修改")
					}else {
						alert(ret.message)
						//模态窗口不关闭
						$("#editRemarkModal").modal("show");
					}
				}
			})
		})
	});
	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${requestScope.activity.name} <small>${requestScope.activity.startDate} ~ ${requestScope.activity.endDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${requestScope.activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkParent" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<c:forEach items="${requestScope.activityRemarkList}" var="remark">
			<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
				<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5 id="h5_${remark.id}">${remark.noteContent}</h5>
					<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.activity.name}</b> <small style="color: gray;"> ${remark.createTime} 由 ${remark.editFlag=='1'?remark.createBy:remark.editBy}${remark.editFlag=='1'?'创建':'修改'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name="removeA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" id="saveCreateActivityRemarkBtn" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>