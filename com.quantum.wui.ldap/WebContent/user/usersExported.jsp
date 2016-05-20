<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.tomcat.util.http.fileupload.FileItem"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="javax.naming.NameNotFoundException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="com.quantum.ldap.LDAPConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Users Exported</title>
	</head>
	<body>
	<%  if(session.getAttribute("session")!=null) { %>
		<a href='../mainPanel.jsp'> Back </a> <br/>
	
		<%		
		String configDirectory = LDAPConnection.DEFAULT_CONFIG_DIR; // session.getServletContext().getRealPath("/");
		try {
			LDAPConnection connection = new LDAPConnection(configDirectory);
			try {
			    int maxFileSize = 5000 * 1024;
			    int maxMemSize = 5000 * 1024;
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletContext context = pageContext.getServletContext();
				String filePath = context.getInitParameter("file-download");
			    factory.setSizeThreshold(maxMemSize);	
				String cvsFile = request.getParameter("cvsFile");
				factory.setRepository(new File("/tmp/"));
				ServletFileUpload upload = new ServletFileUpload(factory);
			    upload.setSizeMax( maxFileSize );

			    try {
				    Map<String, List<FileItem>> fileItems = upload.parseParameterMap(request);
				    out.print(fileItems);
				    InputStream is = fileItems.get("csvFile").get(0).getInputStream();
				    connection.importUsers(is);
			    } catch(Exception e) {
			    	e.printStackTrace();
			    }

				%>
				
				Users have been exported!
					
			<% } finally {
				connection.close();
			}
			%>
		
		<% } catch(Exception e) { %>
			Cannot connect. <br/>
			Cause: <%= e %>		
		<% }
		%>
		<%
		} else {
			out.println("You must be logged in.");
		}
		%>
		
	</body>
</html>