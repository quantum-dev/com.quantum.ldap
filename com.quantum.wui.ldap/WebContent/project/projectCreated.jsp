<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.util.Enumeration"%>
<%@page import="javax.naming.NameNotFoundException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.quantum.ldap.LDAPUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Create Project</title>
	</head>
	<body>
		<%  if(session.getAttribute("session")!=null) { %>
		<a href='../mainPanel.jsp'> Back </a> <br/>
	
		<%
		
		String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			try {
				ArrayList<LDAPUser> users = new ArrayList<LDAPUser>();
				Enumeration<String> parameters = request.getParameterNames();
				for(;parameters.hasMoreElements();) {
					String parameterName = parameters.nextElement();
					if(parameterName.startsWith("user-")) {
						String uid = parameterName.replace("user-", "");
						LDAPUser user = connection.retrieveUserByUid(uid);
						if(user!=null) {
							users.add(user);
						}
					}
				}
				
				String name = request.getParameter("name");
				connection.createProject(name, users);
				connection.close();
				%>
				
				<br/>
				Project '<%= name %>' has been created! <br/> <br/>
				
				Users: <br/>
				<%
				for(LDAPUser u : users) {
				%>
					- <%=u.cn%> <br/>
				<%
				}
				%>
				
			<% } catch(NamingException namingException) { %>
				Error during project creation. <br/>
				Cause: <%= namingException %>		
			<% } finally {
				
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