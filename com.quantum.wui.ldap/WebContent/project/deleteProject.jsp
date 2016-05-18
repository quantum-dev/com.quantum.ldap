<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Delete Project</title>
	</head>
	<body>
		<%  if(session.getAttribute("session")!=null) { %>
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP Management</h1> </td>
			</tr>
		</table>		
		<% String entityCn = request.getParameter("cn");  %>
		Do you want to delete project <%= entityCn %> ? <br/>
		<a href='projectDeleted.jsp?cn=<%= entityCn %>'> OK </a> <br/>
		<a href='listProjects.jsp'> Cancel </a> <br/>
		<%
		} else {
			out.println("You must be logged in.");
		}
		%>
	</body>
</html>