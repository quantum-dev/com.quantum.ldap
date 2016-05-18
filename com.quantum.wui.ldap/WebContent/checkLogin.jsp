<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LDAP Management</title>
</head>
<body>
	<% 
		String configDirectory = session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			// out.println(connection);
			String uid = request.getParameter("uid");
			String passwd = request.getParameter("passwd");
			if(connection.checkPassword(uid, passwd)) {
				session.setAttribute("session","TRUE");
				session.setAttribute("user", uid);
				%>
				<jsp:include page="mainPanel.jsp"></jsp:include>
				<%
			} else {  
				out.print("Sorry, email or password error");  
	%>  
				<jsp:include page="login.jsp"></jsp:include>
	<%
			}  
		} catch(Exception e) {
			out.print(e);
		} finally {
			// connection.close();
		}
	%>
</body>
</html>
