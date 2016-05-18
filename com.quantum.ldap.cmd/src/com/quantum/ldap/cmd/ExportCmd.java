package com.quantum.ldap.cmd;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.NamingException;

import com.quantum.ldap.LDAPConnection;

public class ExportCmd {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("ldap-export <config-dir> <output-dir>");
		} else {

			File configDir = new File(args[0]);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String timestamp = format.format(new Date());

			if (configDir.exists() && configDir.isDirectory()) {
				File outputDir = new File(args[1]);

				if (outputDir.exists() && outputDir.isDirectory()) {
					try {
						LDAPConnection adc = new LDAPConnection(configDir);

						File timestampedOutputDir = new File(outputDir, timestamp);
						
						timestampedOutputDir.mkdirs();

						File usersExportFile = new File(timestampedOutputDir 
								+ File.separator + "users.cvs");
						File projectsExportFile = new File(timestampedOutputDir 
								+ File.separator + "projects.cvs");
						File entitiesExportFile = new File(timestampedOutputDir 
								+ File.separator + "entities.cvs");

						adc.exportUsers(usersExportFile);
						adc.exportProjects(projectsExportFile);
						adc.exportEntities(entitiesExportFile);

						adc.close();
					} catch (NamingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("ldap-export: Error due to export directory '" 
							+ outputDir + "' is not a directory or does not exist");
					System.exit(1);
				}
			} else {
				System.out.println("ldap-export: Error due to configuration directory '" 
						+ configDir + "' is not a directory or does not exist");
				System.exit(1);
			}
		}
	}
}
