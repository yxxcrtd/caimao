<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintStream" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<%response.setStatus(HttpServletResponse.SC_OK);%>
<html>
<head>
  <title>错误页面</title>
  <style type="text/css">
    .ex_title, .ex_content{color: #333;text-align: center;width: 800px;margin: 0 auto;padding: 20px;background: #f2dede;}
    .ex_content{text-align: left;margin-top: -5px;font-size: 13px;display: none;background: #d9edf7;}
    .show_detail{
      display: inline-block;
      float: right;
      color: #28a4c9;
      text-decoration: none;
    }
  </style>
  <script type="text/javascript">
    function show_detail(){
      document.getElementById("ex_content").style.display = "block";
    }
  </script>
</head>
<body>
<p class="ex_title">错误信息：<%=exception.getCause()%><a href="javascript:;" class="show_detail" onclick="show_detail()">显示详情</a></p>
<p class="ex_content" id="ex_content">
  <%
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    exception.printStackTrace(new PrintStream(ostr));
    out.print(ostr);
  %>
</p>

</body>
</html>
