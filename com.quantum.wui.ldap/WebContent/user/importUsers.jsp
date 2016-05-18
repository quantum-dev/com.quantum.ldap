<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Import Users</title>
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
		<h2>Import users</h2>
		
		<form action="usersImported.jsp" method="post" onsubmit="return validateForm()" 
			id="myform" enctype="multipart/form-data">
			<table>
				<tr> 
					<td>
						Last Name 
					</td>
					<td>
						<input id="csvFile" name="csvFile" type="file"/>
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
	<%
	} else {
		out.println("You must be logged in.");
	}
	%>
	</body>
</html>