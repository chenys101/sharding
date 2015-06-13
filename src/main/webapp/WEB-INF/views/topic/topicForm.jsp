<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>主题管理</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/topic/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${topic.id}"/>
		<fieldset>
			<legend><small>管理主题</small></legend>
			<div class="control-group">
				<label for="topic_title" class="control-label">主题名称:</label>
				<div class="controls">
					<input type="text" id="topic_title" name="title"  value="${topic.title}" class="input-large required" minlength="3"/>
				</div>
			</div>	
			<div class="control-group">
				<label for="intro" class="control-label">主题描述:</label>
				<div class="controls">
					<textarea id="intro" name="intro" class="input-large">${topic.intro}</textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#topic_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</body>
</html>
