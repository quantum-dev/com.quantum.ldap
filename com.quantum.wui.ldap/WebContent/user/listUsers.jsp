<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPUser"%>
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
<title>Users</title>
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
		<h2>Users</h2>
		
		<%
		String configDirectory = session.getServletContext().getRealPath("..");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			List<LDAPUser> users = connection.retrieveUsers();
			%>
			<table border="1">
			<%
			for (LDAPUser user : users) {
				%>
					<tr>
						<td>
						<%= user.uid %>
						</td>
						<td>
						<%= user.cn %>
						</td>
						<td>
						<%= user.givenName %>
						</td>												
						<td>
						<%= user.sn %>
						</td>						
						<td>
						<%= user.mail %>
						</td>						
						<td>
						<%= user.initials %>
						</td>
						<td>
							<a href="updateUser.jsp?cn=<%= user.cn %>"> Update </a>
						</td>						
						<td>
							<a href="deleteUser.jsp?cn=<%= user.cn %>"> Delete </a>
						</td>
						<td>
							<a href="mailto:<%= user.mail %>?subject=Comptes&body=<%= user.generateEmail() %>"> Send Email </a>
						</td>
					</tr>
				<%
			} %>
			
			</table>
					
		<% 
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
		%>
		
		<%
		} else {
			out.println("You must be logged in.");
		}
		%>
	</body>
</html>