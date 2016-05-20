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
	<title>Export Entities</title>
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
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP management</h1> </td>
			</tr>
		</table>
		<a href='../mainPanel.jsp'> Back </a> <br/>
		<h2>Export entities</h2>
		
		<% 
		String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			File exportFile = new File(configDirectory + File.separator + "export.csv");
			System.out.println(exportFile);
			connection.exportEntities(exportFile);
			connection.close();
		%>
			Upload entities file in CSV <a href="../export.csv"> here </a>
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