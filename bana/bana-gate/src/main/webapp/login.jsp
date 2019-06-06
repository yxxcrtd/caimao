<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="No-cache">
<meta http-equiv="Cache-Control" content="no-cache">
</head>
<body>
	<div>
		<label for="email">邮箱</label> <input id="email" type="text">
		<label for="password">密码 </label> <input id="password" type="password">
		<input id="submit" type="button" value="submit">
	</div>
	<div id="returnValue"></div>
	<%
		String path = request.getContextPath();
	%>
	<script type="text/javascript"
		src="<%=path%>/js/jQuery/jquery.js"></script>
	<script type="text/javascript"
		src="<%=path%>/js/js-jsonFmt.js"></script>
	<script type="text/javascript">
		$('#submit').on('click', function(e) {
			e.preventDefault();
			
			var postData = {
					"email" : $("#email").val(),
					"password" : $("#password").val()
					};
			console.log(postData);
			$.post("<%=path%>/mobile/user/login", postData, function (result) {
		        $("#returnValue").html("<pre><code>" + JsonUti.convertToString(result) + "</pre></code>");
		    });
		});
	
	</script>
</body>
</html>