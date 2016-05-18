package com.quantum.ldap;

import java.io.IOException;

import javax.naming.NamingException;

import com.quantum.ldap.LDAPConnection;

public class Delete {
	public static void main(String[] args) {

		try {
			LDAPConnection adc = new LDAPConnection();
			adc.deleteUser("John Doe");
			adc.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
}
