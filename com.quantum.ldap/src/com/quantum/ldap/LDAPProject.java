package com.quantum.ldap;

import java.util.ArrayList;
import java.util.List;

public class LDAPProject implements Comparable<LDAPProject> {
	
	public String cn = "";
	
	protected List<LDAPUser> members = new ArrayList<>();
	
	public LDAPProject() {
	}
	
	@Override
	public boolean equals(Object obj) {
		return cn.equals(((LDAPProject)obj).cn);
	}

	@Override
	public int compareTo(LDAPProject o) {
		return cn.compareTo(o.cn);
	}
	
	public List<LDAPUser> getMembers() {
		return members;
	}
	
	public void addMember(LDAPUser user) {
		members.add(user);
	}
	
}
