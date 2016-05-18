<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPUser"%>
<%@page import="com.quantum.ldap.LDAPProject"%>
<%@page import="java.util.List"%>
<%@page import="javax.naming.directory.SearchResult"%>
<%@page import="javax.naming.NamingEnumeration"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Projects</title>
</head>
<body>
	<%  if(session.getAttribute("session")!=null) { %>
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP management</h1> </td>
			</tr>
		</table>
		<a href='../mainPanel.jsp'> Back </a> <br/>
		<h2>Projects</h2>
		
		<% 	
		String configDirectory = session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			List<LDAPProject> projects = connection.retrieveProjects();
			%>
			<table border="1">
			<%
			for(LDAPProject project : projects) {
			%>
				<tr>
					<td>
					<%= project.cn %>
					</td>
					<td>
					<%
					for(LDAPUser member : project.getMembers()) {
					%>
						
						<%= (member!=null?member.cn:"null") %> <br/>

					<%
					}
					%>
					</td>
					<td>
					<a href="updateProject.jsp?cn=<%=project.cn %>"> Update </a>
					</td>
					<td>
					<a href="deleteProject.jsp?cn=<%=project.cn %>"> Delete </a>
					</td>					
				</tr>
			<%
			}
			connection.close();
			%>
			</table>
		
		<% } catch(Exception e) {
			
		}
	} else {
		out.println("You must be logged in.");
	}
	%>
</body>
</html>