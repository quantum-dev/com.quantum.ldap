<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="javax.naming.NameNotFoundException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Delete User</title>
	</head>
	<body>
	<%  if(session.getAttribute("session")!=null) { %>
		<a href='../mainPanel.jsp'> Back </a> <br/>
	
		<% 
		String configDirectory = session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			try {
				String cn = request.getParameter("cn");
				connection.deleteUser(cn);
				%>
				
				User <%= cn %> has been deleted!
				
			<% } catch(NamingException namingException) { %>
				Error during update of your password. <br/>
				Cause: <%= namingException %>		
			<% } finally {
				connection.close();
			} %>
			
		
		<% } catch(Exception e) { %>
			Cannot connect. <br/>
			Cause: <%= e %>		
		<% } %>

		<%
		} else {
			out.println("You must be logged in.");
		}
		%>
	</body>
</html>