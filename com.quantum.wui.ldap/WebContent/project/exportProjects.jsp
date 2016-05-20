<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.io.File"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Create LDAP User</title>
	<script type="text/javascript">
		<!--
		function validateForm() {
		    var csvFile = document.forms["myform"]["csvFile"].value;
		    
		    if (csvFile == null || csvFile == "") {
		        alert("csvFile must be filled out");
		        return false;
		    }
		    
		    return true;
		    
		}		
		//-->
		</script>
	</head>
	<body>
		<%  if(session.getAttribute("session")!=null) { %>
		<a href='../mainPanel.jsp'> Back </a> <br/>
		<% 
		String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			File exportFile = new File(configDirectory + File.separator + "export.csv");
			System.out.println(exportFile);
			connection.exportProjects(exportFile);
			connection.close();
		%>
			Upload projects file in CSV <a href="../export.csv"> here </a>
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