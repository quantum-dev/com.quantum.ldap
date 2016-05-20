<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LDAP Management</title>
		<script type="text/javascript">
		<!--
		function validateForm() {
		    var uid = document.forms["myform"]["uid"].value;
		    var passwd = document.forms["myform"]["passwd"].value;
		    var newpasswd = document.forms["myform"]["newpasswd"].value;
		    var newpasswd2 = document.forms["myform"]["newpasswd2"].value;
		    
		    if (uid == null || uid == "") {
		        alert("Identifier must be filled out");
		        return false;
		    }
		    
		    if (passwd == null || passwd == "") {
		        alert("Password must be filled out");
		        return false;
		    }
		    
		    return true;
		    
		}		
		//-->
		</script>
</head>
<body>

	<br/> <img src="images/image-tree.png" alt="Tree" width="100" height="100"/> </br>
	
	<%
		String configDirectory = session.getServletContext().getRealPath("..");
		if(LDAPConnection.isDebug()) {
			out.print("Configuration directory is located in:" + configDirectory);		
		}
		if(!LDAPConnection.isConfigFilePresent(configDirectory)) {
			// install process
			%>
				<jsp:include page="install/config.jsp"></jsp:include>
			<%
		} else {
			%>
				<form action="checkLogin.jsp" method="post" onsubmit="return validateForm()" id="myform">
					<table>
						<tr> 
							<td>
								Identifier 
							</td>
							<td>
								<input id="uid" name="uid" type="text"/>
							</td>
						</tr>
						<tr> 
							<td>
								Password
							</td>
							<td>
								<input id="passwd" name="passwd" type="password"/>
								<input id="submit" name="Submit" type="submit" value="Submit"/>
							</td>
						</tr>			
					</table>
				</form>
			<%
		}
	%>
</body>
</html>
