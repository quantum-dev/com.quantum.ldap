<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Change password</title>
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
		<table>
			<tr>
				<td> <img src="../images/image-tree.png" alt="Tree" width="100" height="100"/> </td>
				<td> <h1>LDAP management</h1> </td>
			</tr>
		</table>
		<h2>Change password</h2>
			
		<form action="passwordChanged.jsp" method="post" onsubmit="return validateForm()" id="myform">
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
						Current Password
					</td>
					<td>
						<input id="passwd" name="passwd" type="password"/>
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
	</body>
</html>