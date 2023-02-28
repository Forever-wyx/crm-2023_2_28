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
		//点击回车登录功能
		$(window).keydown(function (e) {//这个方法是在整个窗口按下键盘的事件
			if (e.which == 13){		//按下了回车键
				//调用登录方法，或者模拟用鼠标点击了登录按钮
				$("#loginbtn").click()
			}
		})

		//入口函数
		$(function () {
			//为点击登录添加事件
			$("#loginbtn").click(function () {
				//收集参数,去掉账号和密码的空格
				var loginAct = $.trim($("#loginAct").val());
				var loginPwd = $.trim($("#loginPwd").val());
				var isRemPwd = $("#isRemPwd").prop("checked");
				//表单验证
				if (loginAct == ""){
					alert("账号不能为空")
					return;
				}
				if (loginPwd == ""){
					alert("密码不能为空")
					return;
				}

				//登录验证的事件段，显示正在登录提示用户
				$("#msg").html("正在登录...")

				//账号和密码都不为空,异步请求验证账号和密码
				$.ajax({
					url: 'settings/qx/user/login.do',
					data: {
						loginAct: loginAct,
						loginPwd: loginPwd,
						isRemPwd: isRemPwd
					},
					type: 'post',
					dataType: 'json',
					success:function (data) {
						//判断返回的数据,是否登录成功
						if (data.code == "1"){
							//登录成功,跳转页面
							window.location.href = "workbench/index.do"
						}else {
							$("#msg").html(data.message)
						}
					},
					beforeSend:function(){	//当ajax向后台发送请求之前，会自动先执行本函数
											//该函数的返回值能够决定ajax是否真正向后台发送请求
											//如果该函数返回true,则ajax会真正向后台发送请求;否则,如果该函数返回false,则ajax不发送请求

						//这里可以将用户名和密码的验证拿过来
						return true;
					}
				})
			})
		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" id="loginAct" value="${cookie.loginAct.value}" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" value="${cookie.loginPwd.value}" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct}">
								<input type="checkbox" id="isRemPwd" checked>
							</c:if>
							<c:if test="${empty cookie.loginAct}">
								<input type="checkbox" id="isRemPwd">
							</c:if>
							 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span style="color: #ff515d" id="msg"></span>
					</div>
					<button type="button" id="loginbtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>

</body>
</html>