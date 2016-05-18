<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LDAP Management</title>
</head>
<body>
	<br/> <img src="images/image-tree.png" alt="Tree" width="100" height="100"/> <br/>
	
	You are logout of the application. <br/>
	<% 
		session.removeAttribute("session");
		session.removeAttribute("user");
	%>
	<a href="login.jsp">Go to login</a> <br/>
</body>
</html>
