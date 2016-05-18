package com.quantum.ldap;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.naming.NamingException;

import com.quantum.ldap.LDAPConnection;

public class Update {

	public static void main(String[] args) {
		try {
			LDAPConnection adc = new LDAPConnection();
			adc.updatePasswordByUsername("John Doe", "xxxx");
			adc.updatePasswordByUid("jde", "xxxx");
			System.out.println(adc.checkPassword("jde", "xxxx"));
			adc.close();
		} catch (NoSuchAlgorithmException | NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
