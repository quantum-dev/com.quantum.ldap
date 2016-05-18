package com.quantum.ldap.testlinkusers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestLinkUsers {

	public static void main(String[] args) {
		List<TestLinkUser> users = extractUsers();
		for (TestLinkUser user : users) {
			System.out.println(user);
		}
	}

	public static List<TestLinkUser> extractUsers() {
		List<TestLinkUser> users = new ArrayList<TestLinkUser>();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream("Comptes-TestLink-users.xml"));
			String expression = "/users/user";
			System.out.println(expression);
			XPath xPath =  XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
			    Node item = nodeList.item(i);
			    String id = retrieveNodeAttributeValue(xPath, item, "id");
			    String login = retrieveNodeAttributeValue(xPath, item, "login");
			    String first = retrieveNodeAttributeValue(xPath, item, "first");
			    String role_id = retrieveNodeAttributeValue(xPath, item, "role_id");
			    String last = retrieveNodeAttributeValue(xPath, item, "last");
			    String email = retrieveNodeAttributeValue(xPath, item, "email");
			    String locale = retrieveNodeAttributeValue(xPath, item, "locale");
			    String active = retrieveNodeAttributeValue(xPath, item, "active");
			    
			    TestLinkUser newUser = new TestLinkUser(id, login, first, role_id, last, email, locale, active.equals("1"));
			    users.add(newUser);  
			}
			
		    File csvFile = new File("Comptes-TestLink-users.csv");
		    if(!csvFile.exists()) {
		    	csvFile.createNewFile();
		    }
		    
			FileOutputStream fileOutputStream = new FileOutputStream(csvFile);
			for (TestLinkUser user : users) {
			    String line = user.login + "," + user.first + "," + user.last + "," + user.email + System.lineSeparator();
			    fileOutputStream.write(line.getBytes());
			}
			
			fileOutputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}

	private static String retrieveNodeAttributeValue(XPath xPath, Node item, String attributeName) throws XPathExpressionException {
		Node nameNode = (Node) xPath.compile(attributeName).evaluate(item, XPathConstants.NODE);
		return nameNode.getFirstChild().getNodeValue();
	}

}
