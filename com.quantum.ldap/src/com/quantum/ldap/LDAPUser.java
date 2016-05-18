package com.quantum.ldap;

/**
 * The representing class of an LDAP User
 */
public class LDAPUser implements Comparable<LDAPUser> {
	
	public String cn;
	
	public String sn;
	
	public String givenName;
	
	public String initials;
	
	public String mail;
	
	public String uid;
	
	@Override
	public int compareTo(LDAPUser o) {
		return sn.compareTo(o.sn);
	}
	
	public static final String NEWLINE_SEPARATOR = "%0D%0A";
	
	@Override
	public boolean equals(Object obj) {
		return cn.equals(((LDAPUser)obj).cn);
	}
	
	public String generateEmail() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bonjour," + NEWLINE_SEPARATOR);
		builder.append("Un nouveau compte a été créé." + NEWLINE_SEPARATOR);
		builder.append("Voici les informations relatives à votre compte:" + NEWLINE_SEPARATOR);
		builder.append("Login: " + uid + NEWLINE_SEPARATOR);
		builder.append("Password: " + givenName.toLowerCase() + NEWLINE_SEPARATOR);
		builder.append("Cordialement," + NEWLINE_SEPARATOR);
		
		String result = builder.toString();
		
		result.replace(" ", "%20");
		
		return result;
	}
}
