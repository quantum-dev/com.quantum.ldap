<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPEntity"%>
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
<title>Entities</title>
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
		<h2>Entities</h2>
		
		<% 	
		String configDirectory = session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			List<LDAPEntity> entities = connection.retrieveEntities();
			%>
			<table border="1">
			<%
			for(LDAPEntity entity : entities) {
			%>
				<tr>
					<td>
					<%= entity.cn %>
					</td>
					<td>
					<%
					for(LDAPUser member : entity.getMembers()) {
					%>
						
						<%= (member!=null?member.cn:"null") 
						%> <br/>

					<%
					}
					%>
					</td>
					<td>
					<a href="updateEntity.jsp?cn=<%=entity.cn %>"> Update </a>
					</td>
					<td>
					<a href="deleteEntity.jsp?cn=<%=entity.cn %>"> Delete </a>
					</td>					
				</tr>
			<%
			}
			
			connection.close();
			%>
			<%-- <%
				for (NamingEnumeration<SearchResult> ae = projects; projects.hasMoreElements();) {
					SearchResult project = ae.next();
					String cn = (String) project.getAttributes().get("cn").get();
					%>
						<tr>
							<td>
							<%= cn %>
							</td>
							<td>
							<% NamingEnumeration<String> ne 
								= (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
							for(;ne.hasMore();) {
							%>
								<%  String member = (String) ne.next(); %> 
								
								<%= member %> <br/>

							<%
							}
							%>
							</td>
							<td>
							<% NamingEnumeration<String> ne1 = (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
							for(;ne1.hasMore();) {
							%>
								<%  String member = (String) ne1.next(); %> 
								
								<%= member %> <br/>

							<%
							}
							%>
							</td>
							<td>
							<a href="deleteProject.jsp?cn=<%=cn %>"> Delete </a>
							</td>
						</tr>
					<%
				} 
				
				connection.close();
			%> --%>
			
			</table>
		
		<% } catch(Exception e) {
			
		}
	} else {
		out.println("You must be logged in.");
	}
	%>
</body>
</html>