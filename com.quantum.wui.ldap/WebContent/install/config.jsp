<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.io.File"%>
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
		    var serverURL = document.forms["myform"]["serverURL"].value;
		    var principalUser = document.forms["myform"]["principalUser"].value;
		    var principalPassword = document.forms["myform"]["principalPassword"].value;
		    var baseDN = document.forms["myform"]["baseDN"].value;
		    var usersBaseDN = document.forms["myform"]["usersBaseDN"].value;
		    
		    if (serverURL == null || serverURL == "") {
		        alert("serverURL must be filled out");
		        return false;
		    }
		    
		    if (principalUser == null || principalUser == "") {
		        alert("principalUser must be filled out");
		        return false;
		    }
		    
		    if (principalPassword == null || principalPassword == "") {
		        alert("principalPassword must be filled out");
		        return false;
		    }
		    
		    if (baseDN == null || baseDN == "") {
		        alert("baseDN must be filled out");
		        return false;
		    }
		    
		    if (usersBaseDN == null || usersBaseDN == "") {
		        alert("baseDN must be filled out");
		        return false;
		    }
		    
		    return true;
		    
		}		
		//-->
		</script>
</head>
<body>		
		<form action="install/install.jsp" method="post" onsubmit="return validateForm()" id="myform">
			<table>
				<tr> 
					<td>
						Server URL 
					</td>
					<td>
						<input id="serverURL" name="serverURL" type="text" value="ldap://localhost:389"/>
					</td>
				</tr>
				<tr> 
					<td>
						Principal User 
					</td>
					<td>
						<input id="principalUser" name="principalUser" type="text" value="cn=admin,dc=domain,dc=com"/>
					</td>
				</tr>
				<tr> 
					<td>
						Principal Password  
					</td>
					<td>
						<input id="principalPassword" name="principalPassword" type="password"/>
					</td>
				</tr>				
				<tr> 
					<td>
						Base DN 
					</td>
					<td>
						<input id="baseDN" name="baseDN" type="text" value="dc=deveryware,dc=com"/>
					</td>
				</tr>									
				<tr> 
					<td>
						Users Base DN
					</td>
					<td>
						<input id="usersBaseDN" name="usersBaseDN" type="text" value="ou=Users,dc=deveryware,dc=com"/>
					</td>
				</tr>
				<tr> 
					<td>
						Projects Base DN
					</td>
					<td>
						<input id="projectsBaseDN" name="projectsBaseDN" type="text" value="ou=Projects,dc=deveryware,dc=com"/>
					</td>
				</tr>
				<tr> 
					<td>
						Entities Base DN
					</td>
					<td>
						<input id="entitiesBaseDN" name="entitiesBaseDN" type="text" value="ou=Entities,dc=deveryware,dc=com"/>
					</td>
				</tr>								
				<tr> 
					<td>
					</td>
					<td>
						<input id="submit" name="Submit" type="submit" value="Submit"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
