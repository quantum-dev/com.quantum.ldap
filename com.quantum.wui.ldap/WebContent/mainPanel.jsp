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
	////////////////////////////////////////////////////////////////
	if(session.getAttribute("session")!=null) { 
	%>
	    <jsp:include page="header.jsp"></jsp:include>
		
		<h2> Users </h2>
		<ul>
			<li> <a href="user/listUsers.jsp">List all users</a> </li>
			<li> <a href="user/createUser.jsp">Create a user</a> </li>
			<li> <a href="user/importUsers.jsp">Import users</a> </li>
			<li> <a href="user/exportUsers.jsp">Export users</a> </li>
			<li> <a href="pwd/changePassword.jsp">Change a user's password</a></li>
		</ul>
		<h2> Entities </h2>
		<ul>
			<li><a href="entity/listEntities.jsp">List all entities</a> </li>
			<li><a href="entity/createEntity.jsp">Create an entity</a> </li>
			<li><a href="entity/importEntities.jsp">Import entities</a> </li>	
			<li><a href="entity/exportEntities.jsp">Export entities</a> </li>
		</ul>
		<h2> Projects </h2>
		<ul>
			<li><a href="project/listProjects.jsp">List all projects</a> </li>
			<li><a href="project/createProject.jsp">Create a project</a> </li>
			<li><a href="project/importProjects.jsp">Import projects</a> </li>	
			<li><a href="project/exportProjects.jsp">Export projects</a> </li>
		</ul>
	<% 
	////////////////////////////////////////////////////////////////
	} else { %>
		You must be logged <br/>
		<a href="login.jsp">Goto login</a>
	<%
	////////////////////////////////////////////////////////////////
	}
	%>
	
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
