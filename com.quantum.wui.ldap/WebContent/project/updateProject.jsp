<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.quantum.ldap.LDAPProject"%>
<%@page import="com.quantum.ldap.LDAPUser"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.naming.directory.SearchResult"%>
<%@page import="javax.naming.NamingEnumeration"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>User Chooser</title>
	<script type="text/javascript">
		<!--
		function validateForm() {
		    return true;
		}
		
		function updateFilter() {
		    var filter = document.forms["myform"]["filter"].value;
		    return true;
		}
		
		function validate() {
	        if (document.getElementById('remember').checked) {
	            alert("checked");
	        } else {
	            alert("You didn't check it! Let me check it for you.");
	        }
	    }
		//-->
		</script>
	<script type="text/javascript"
    	src="http://code.jquery.com/jquery-1.10.1.min.js">
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
				<td> <h1>LDAP Management</h1> </td>
			</tr>
		</table>		
		<a href='../mainPanel.jsp'> Back </a> <br/>
			
		<%
			String cn = request.getParameter("cn");
		%>
	
		<% String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			LDAPProject project = connection.retrieveProjectByCn(cn);
		%>	

		<br/>
		
		<form action="projectUpdated.jsp" method="post" onsubmit="return validateForm()" id="myform">
		<!--  <table>
				<tr> 
					<td>
						Name 
					</td>
					<td>
						<input id="name" name="name" type="text"/>
						<input id="submit" name="Submit" type="submit" value="Submit"/>
					</td>
				</tr>				
			</table>
		<br/>
		<br/>
    	<br/>
 -->	
 		Project '<%= cn %>' <br/>
 		Users <br/>
		<%
		List<LDAPUser> users = connection.retrieveUsers();
		%>
		<input id="submit" name="Submit" type="submit" value="Submit"/>
		<input id="cn" name="cn" type="hidden" value="<%=project.cn%>"/>
		<table border="1">
			<%
			for (LDAPUser user : users) {
			%>
				<tr>
					<td>
						<input type="checkbox" id="user-<%= user.uid %>" name="user-<%= user.uid %>"
							<% if(project.getMembers().contains(user)) {
								%>
								checked="checked"
								<%
							}
							%>
							/>
					</td>
					<td>
					<%= user.uid %>
					</td>
					<td>
					<%= user.cn %>
					</td>
					<td>
					<%= user.givenName %>
					</td>												
					<td>
					<%= user.sn %>
					</td>						
					<td>
					<%= user.mail %>
					</td>						
					<td>
					<%= user.initials %>
					</td>
				</tr>
			<%
			}
			%>

			</table>
		</form>	
		<% 
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
		%>
	<%
	} else {
		out.println("You must be logged in.");
	}
	%>

	</body>
</html>