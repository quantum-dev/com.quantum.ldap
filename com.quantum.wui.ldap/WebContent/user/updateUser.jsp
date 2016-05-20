<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="javax.naming.directory.Attributes"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Update LDAP User</title>
	<script type="text/javascript">
		<!--
		function validateForm() {
		    var sn = document.forms["myform"]["sn"].value;
		    var givenName = document.forms["myform"]["givenName"].value;
		    var initials = document.forms["myform"]["initials"].value;
		    var mail = document.forms["myform"]["mail"].value;
		    var uid = document.forms["myform"]["uid"].value;
		    
		    if (sn == null || sn == "") {
		        alert("Last Name must be filled out");
		        return false;
		    }
		    
		    if (givenName == null || givenName == "") {
		        alert("First Name must be filled out");
		        return false;
		    }
		    
		    if (initials == null || initials == "") {
		        alert("Initials must be filled out");
		        return false;
		    }	
		    
		    if (mail == null || mail == "") {
		        alert("Mail must be filled out");
		        return false;
		    }
		    
		    if (uid == null || uid == "") {
		        alert("Identifier must be filled out");
		        return false;
		    }
		    
		    return true;
		    
		}		
		//-->
		</script>
	</head>
	<body>
		<%  
		if(session.getAttribute("session")!=null) { 
			String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
			LDAPConnection connection = new LDAPConnection(configDirectory);

			Attributes attributes = connection.fetchUserByUsername(request.getParameter("cn"));
		%>
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP Management</h1> </td>
			</tr>
		</table>		
		<a href='../mainPanel.jsp'> Back </a> <br/>
		<form action="userUpdated.jsp" method="post" onsubmit="return validateForm()" id="myform">
			<table>
				<tr> 
					<td>
						Last Name 
					</td>
					<td>
						<input id="sn" name="sn" type="text" value="<%=attributes.get("sn").get()%>"/>
					</td>
				</tr>
				<tr> 
					<td>
						First Name 
					</td>
					<td>
						<input id="givenName" name="givenName" type="text" value="<%=attributes.get("givenName").get()%>"/>
					</td>
				</tr>	
				<tr> 
					<td>
						Initials 
					</td>
					<td>
						<input id="initials" name="initials" type="text" value="<%=attributes.get("initials").get()%>"/>
					</td>
				</tr>
				<tr> 
					<td>
						Identifier (UID) 
					</td>
					<td>
						<input id="uid" name="uid" type="text" value="<%=attributes.get("uid").get()%>"/>
					</td>
				</tr>				
				<tr> 
					<td>
						Mail 
					</td>
					<td>
						<input id="mail" name="mail" type="text" value="<%=attributes.get("mail").get()%>"/>
						<input id="submit" name="Submit" type="submit" value="Submit"/>
					</td>
				</tr>				
			</table>
		</form>
		
		<%
		} else {
			out.println("You must be logged in.");
		}
		%>
	</body>
</html>