<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Create User</title>
	<script type="text/javascript">
		<!--
		function validateForm() {
		    var sn = document.forms["myform"]["sn"].value;
		    var givenName = document.forms["myform"]["givenName"].value;
		    var initials = document.forms["myform"]["initials"].value;
		    var mail = document.forms["myform"]["mail"].value;
		    var uid = document.forms["myform"]["uid"].value;
		    var newpasswd = document.forms["myform"]["newpasswd"].value;
		    var newpasswd2 = document.forms["myform"]["newpasswd2"].value;
		    
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

		    
		    if (newpasswd == null || newpasswd == "") {
		        alert("New password must be filled out");
		        return false;
		    }

		    if (newpasswd2 == null || newpasswd2 == "") {
		        alert("Retyped new password must be filled out");
		        return false;
		    }
		    
		    if (newpasswd != newpasswd2) {
		        alert("New passwords not identical");
		        return false;
		    }
		    
		    return true;
		    
		}		
		//-->
	</script>
</head>
<body>
	<%
	////////////////////////////////////////////////////////////////
	if(session.getAttribute("session")!=null) { 
	%>
		
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP management</h1> </td>
			</tr>
		</table>
	    
		<a href='../mainPanel.jsp'> Back </a> <br/>
		<h2>Create a new user</h2>
		
		<form action="userCreated.jsp" method="post" onsubmit="return validateForm()" id="myform">
			<table>
				<tr> 
					<td>
						Last Name 
					</td>
					<td>
						<input id="sn" name="sn" type="text"/>
					</td>
				</tr>
				<tr> 
					<td>
						First Name 
					</td>
					<td>
						<input id="givenName" name="givenName" type="text"/>
					</td>
				</tr>	
				<tr> 
					<td>
						Initials 
					</td>
					<td>
						<input id="initials" name="initials" type="text"/>
					</td>
				</tr>
				<tr> 
					<td>
						Identifier (UID) 
					</td>
					<td>
						<input id="uid" name="uid" type="text"/>
					</td>
				</tr>				
				<tr> 
					<td>
						Mail 
					</td>
					<td>
						<input id="mail" name="mail" type="text"/>
					</td>
				</tr>							
				<tr> 
					<td>				
						New Password 
					</td>
					<td>
						<input id="newpasswd" name="newpasswd" type="password"/>
					</td>
				</tr>
				<tr>					
					<td>
						Retype new Password 
					</td>
					<td>
						<input id="newpasswd2" name="newpasswd2" type="password"/>
						<input id="submit" name="Submit" type="submit" value="Submit"/>
					</td>
				</tr>				
			</table>
		</form>
	<% 
	////////////////////////////////////////////////////////////////
	} else { %>
		You must be logged <br/>
		<a href="login.jsp">Goto login</a>
	<%
	////////////////////////////////////////////////////////////////
	}
	%>
	<jsp:include page="../footer.jsp"></jsp:include>
	
	</body>
</html>