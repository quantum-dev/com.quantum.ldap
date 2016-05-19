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
	
	if(!LDAPConnection.isConfigFilePresent(configDirectory)) {
		String serverURL = request.getParameter("serverURL");
		String principalUser = request.getParameter("principalUser");
		String principalPassword = request.getParameter("principalPassword");
		String baseDN = request.getParameter("baseDN");
		String usersBaseDN = request.getParameter("usersBaseDN");
		String projectsBaseDN = request.getParameter("projectsBaseDN");
		String entitiesBaseDN = request.getParameter("entitiesBaseDN");
		String message = new String();
		try {
			boolean check = LDAPConnection.checkConfigWithMessage(configDirectory, 
                    serverURL, 
                    principalUser, 
                    principalPassword, 
                    baseDN,
                    usersBaseDN,
                    projectsBaseDN,
                    entitiesBaseDN,
                    message
                    );
			if(!check) {
				%>
				Something goes wrong with configuration.
				Reason: <%= message %>
				<a href="../login.jsp">Go to login</a> <br/>
				<%
			} else {
				if(null==LDAPConnection.createConfigFile(configDirectory, 
						serverURL, 
						principalUser, 
						principalPassword, 
						baseDN,
						usersBaseDN,
						projectsBaseDN,
						entitiesBaseDN
						)) {
					out.print("Cannot create config file");
				} else {
					LDAPConnection connection = new LDAPConnection(configDirectory);
					connection.initDatabase();
				%>
				<a href="../login.jsp">Go to login</a> <br/>
				<%
				}
			}

			
		} catch(Exception e) {
			out.print(e);
		}

	} else {
		
	%>
		The application is already installed...
		<a href="../login.jsp">Go to login</a> <br/>
	<% 
	}
	%>
</body>
</html>
