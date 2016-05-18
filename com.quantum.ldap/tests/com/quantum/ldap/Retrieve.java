package com.quantum.ldap;

import javax.naming.directory.Attributes;

import com.quantum.ldap.LDAPConnection;

public class Retrieve {
	public static void main(String[] args) {
		try {
			LDAPConnection adc = new LDAPConnection();
			Attributes a = adc.fetchUserByUsername("John Doe");
			Attributes b = adc.fetchUserAttributesByUid("jde");
			adc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
