package com.quantum.ldap.testlinkusers;

public class TestLinkUser {
    String id = "";
    String login = "";
    String first = "";
    String role_id = "";
    String last = "";
    String email = "";
    String locale = "";
    boolean active = false;
    
    public TestLinkUser(String id,
	    String login,
	    String first,
	    String role_id,
	    String last,
	    String email,
	    String locale,
	    boolean active) {
    	this.login = login;
    	this.id = id;
    	this.first = first;
    	this.role_id = role_id;
    	this.last = last;
    	this.email = email;
    	this.locale = locale;  	
	}
    
    public void setId(String id) {
		this.id = id;
	}
    
    public String getId() {
		return id;
	}
    
    public void setLogin(String login) {
		this.login = login;
	}
    
    public String getLogin() {
		return login;
	}
    
    public void setLocale(String locale) {
		this.locale = locale;
	}
    
    public String getLocale() {
		return locale;
	}
    
    public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
    
    public String getRole_id() {
		return role_id;
	}
    
    public void setLast(String last) {
		this.last = last;
	}
    
    public String getLast() {
		return last;
	}
    
    public void setFirst(String first) {
		this.first = first;
	}
    
    public String getFirst() {
		return first;
	}
    
    public void setActive(boolean active) {
		this.active = active;
	}
    
    public boolean isActive() {
		return active;
	}
}
