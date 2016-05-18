package com.quantum.ldap;

import java.util.ArrayList;
import java.util.List;

public class LDAPEntity implements Comparable<LDAPEntity> {
	
	public String cn = "";
	
	protected List<LDAPUser> members = new ArrayList<>();
	
	public LDAPEntity() {
	}
	
	@Override
	public boolean equals(Object obj) {
		return cn.equals(((LDAPEntity)obj).cn);
	}

	@Override
	public int compareTo(LDAPEntity o) {
		return cn.compareTo(o.cn);
	}
	
	public List<LDAPUser> getMembers() {
		return members;
	}
	
	public void addMember(LDAPUser user) {
		members.add(user);
	}
	
}
