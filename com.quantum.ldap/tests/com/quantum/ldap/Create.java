package com.quantum.ldap;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.naming.NamingException;

import com.quantum.ldap.LDAPConnection;

public class Create {
	public static void main(String[] args) {

		try {
			LDAPConnection adc = new LDAPConnection();
			adc.createUser("jde", "John Doe", "Doe", "John", "JDE", "john.doe@domain.com");
			adc.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
