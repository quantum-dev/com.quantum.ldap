package com.quantum.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

/**
 * Connection to an LDAP directory server.
 * The connection manages:
 * <ul>
 * 	<li>Users</li>
 * 	<li>Entities containing Users (refs)</li>
 * 	<li>Projects containing Users (refs)</li>
 * </ul>
 * 
 * @author omo-dw
 */
public class LDAPConnection {

	/**
	 * The LDAP Context.
	 */
	protected DirContext ldapContext;
	
	/**
	 * The LDAP Context for a user.
	 */
	protected DirContext ldapUserContext;
	
	/**
	 * The LDAP server URL.
	 */
	protected String serverURL;
	
	/**
	 * The principal LDAP user.
	 */
	protected String principalUser;
	
	/**
	 * The principal LDAP user's password.
	 */
	protected String principalPasswd;

	/**
	 * The base DN
	 */
	private String baseDN;

	/**
	 * The users' baseDN.
	 */
	private String usersBaseDN;

	/**
	 * The projects' baseDN.
	 */
	private String projectsBaseDN;

	/**
	 * The entities' baseDN.
	 */
	private String entitiesBaseDN;
	
	/**
	 * Debug mode.
	 */
	public static boolean debug = false;
	
	/**
	 * Are we in debug mode?
	 * @return <code>true</code> if so
	 */
	public static boolean isDebug() {
		return debug;
	}
	
	/**
	 * The configuration file location.
	 */
	protected static String baseConfigDirectory;
	
	/**
	 * The configuration file location
	 */
	protected static String configFileName 
		= "config.properties";

	/**
	 * Default constructor.
	 * Automatic connection by calling {@link #connect(File)}
	 * @throws NamingException
	 * @throws IOException
	 */
	public LDAPConnection() throws NamingException, IOException {
		connect(new File(configFileName));
	}
	
	/**
	 * Default constructor.
	 * Automatic connection by calling {@link #connect(File)}
	 * @throws NamingException
	 * @throws IOException 
	 */
	public LDAPConnection(String configDirectory) throws NamingException, IOException {
		connect(new File(configDirectory + "/" + configFileName));
	}
	
	public LDAPConnection(File configDirectory) throws NamingException, IOException {
		connect(new File(configDirectory, configFileName));
	}
	
	/**
	 * Default constructor.
	 * Automatic connection with properties
	 * @param properties
	 * @throws NamingException
	 * @throws IOException
	 */
	public LDAPConnection(Properties properties) throws NamingException, IOException {
		connect(properties);
	}
	
	/**
	 * Retrieves the configuration file.
	 * @return The config file
	 */
	public static File getConfigFile(String configDirectory) {
		return new File(configFileName);
	}
	
	/**
	 * Creates the configuration file.
	 * @param configDirectory The directory on the server where the config file is stored
	 * @param serverURL The LDAP server URL
	 * @param principalUser The principal LDAP user
	 * @param principalPassword The principal LDAP user's password
	 * @param baseDN The base DN
	 * @param usersBaseDN The users' DN
	 * @param projectsBaseDN The projects' DN
	 * @param entitiesBaseDN The entities' DN
	 * @return The configuration file on the server, <code>null</code> if the file did not close properly
	 * @throws Exception If there is an exception during the properties file creation
	 */
	public static File createConfigFile(String configDirectory, 
			String serverURL, 
			String principalUser, String principalPassword, 
			String baseDN, 
			String usersBaseDN,
			String projectsBaseDN,
			String entitiesBaseDN		
			) 
			throws Exception {
		FileOutputStream outputstream = null;
		File configFile = new File(configFileName);
		if(!configFile.exists()) {
			try {
				String configFilePath = configDirectory + configFile;
				outputstream = new FileOutputStream(configFilePath);
				Properties prop = new Properties();
				prop.setProperty("serverURL", serverURL);
				prop.setProperty("principalUser", principalUser);
				prop.setProperty("principalPassword", principalPassword);			
				prop.setProperty("baseDN", baseDN);
				prop.setProperty("usersBaseDN", usersBaseDN);
				prop.setProperty("projectsBaseDN", projectsBaseDN);
				prop.setProperty("entitiesBaseDN", entitiesBaseDN);				
				prop.store(outputstream, "Generated by configurator");
			} catch(Exception e) {
				throw e;
			} finally {
				if (outputstream != null) {
					try {
						outputstream.close();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		
		return configFile;
	}

	/**
	 * Checks the configuration, it consists in a connect on the configuration file in parameter 
	 * with a try catch close. If there is an exception, the method returns <code>false</code>.
	 * @param configDirectory The directory on the server where the config file is stored
	 * @param serverURL The LDAP server URL
	 * @param principalUser The principal LDAP user
	 * @param principalPassword The principal LDAP user's password
	 * @param baseDN The base DN
	 * @param usersBaseDN The users' DN
	 * @param projectsBaseDN The projects' DN
	 * @param entitiesBaseDN The entities' DN
	 * @return
	 */
	public static boolean checkConfig(String configDirectory, 
			String serverURL, 
			String principalUser, String principalPassword, 
			String baseDN, 
			String usersBaseDN,
			String projectsBaseDN,
			String entitiesBaseDN		
			) {
		Properties prop = new Properties();
		prop.setProperty("serverURL", serverURL);
		prop.setProperty("principalUser", principalUser);
		prop.setProperty("principalPassword", principalPassword);			
		prop.setProperty("baseDN", baseDN);
		prop.setProperty("usersBaseDN", usersBaseDN);
		prop.setProperty("projectsBaseDN", projectsBaseDN);
		prop.setProperty("entitiesBaseDN", entitiesBaseDN);
		
		try {
			LDAPConnection connection = new LDAPConnection(prop);
			connection.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * Specifies whether the configuration file is present or not
	 * @return return <code>true</code> if present
	 */
	public static boolean isConfigFilePresent(String configDirectory) {
		File configFile = new File(configDirectory + "/" + configFileName);
		return configFile.exists();
	}
	
	/**
	 * Attempts to connect to the LDAP repository
	 * @param configFile The config file
	 * @throws IOException 
	 * @throws NamingException 
	 */
	private void connect(File configFile) throws IOException, NamingException {
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(configFile);
		prop.load(input);

		connect(prop);
	}

	private void connect(Properties prop) throws NamingException {
		String serverURL = prop.getProperty("serverURL");
		String principalUser = prop.getProperty("principalUser");
		String principalPasswd = prop.getProperty("principalPassword");
		String baseDN = prop.getProperty("baseDN");
		String usersBaseDN = prop.getProperty("usersBaseDN");
		String projectsBaseDN = prop.getProperty("projectsBaseDN");
		String entitiesBaseDN = prop.getProperty("entitiesBaseDN");
		boolean ssl = serverURL.startsWith("ldaps://");
		connect(serverURL, principalUser, principalPasswd, baseDN, usersBaseDN, projectsBaseDN, entitiesBaseDN, ssl);
		
	}
	
	/**
	 * Attempts to connect to the LDAP repository regarding a configuration
	 * @param serverURL The server URL
	 * @param principalUser The principal user
	 * @param principalPasswd The principal user's password
	 * @param baseDN The base DN
	 * @throws NamingException
	 */
	public void connect(String serverURL, String principalUser, String principalPasswd, 
			String baseDN, String usersBaseDN, String projectsBaseDN, String entitiesBaseDN, boolean ssl) 
			throws NamingException {
		this.serverURL = serverURL;
		this.principalUser = principalUser;
		this.principalPasswd = principalPasswd;
		this.baseDN = baseDN;
		this.usersBaseDN = usersBaseDN;
		this.projectsBaseDN = projectsBaseDN;
		this.entitiesBaseDN = entitiesBaseDN;
		
		Hashtable<String,String> ldapEnv = new Hashtable<>(11);
		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnv.put(Context.PROVIDER_URL, serverURL);
		ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		ldapEnv.put(Context.SECURITY_PRINCIPAL, this.principalUser);
		ldapEnv.put(Context.SECURITY_CREDENTIALS, this.principalPasswd);
		if(ssl) {
			ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
		}
		
		this.ldapContext = new InitialDirContext(ldapEnv);
	}
	
	public void initDatabase() throws NoSuchAlgorithmException, NamingException {
		try {
			createGroup("Users");			
		} catch(Exception e) {
			
		}
		try {
			createGroup("Projects");
		} catch(Exception e) {
			
		}
		try {
			createGroup("Entities");
		} catch(Exception e) {
			
		}
		
		try {
			createUser("admin", "Administrator", "Administrator", "Administrator", "admin", "Administrator");
		} catch(Exception e) {
			
		}
		
	}

	/**
	 * Creates a new LDAP user
	 * @param uid The Unique IDentifier
	 * @param username The user name
	 * @param surname The user surname
	 * @param givenName The user givenName
	 * @param initials The user initials
	 * @param email The user's email
	 * @return The required LDAP attributes for the LDAP transaction
	 * @throws NoSuchAlgorithmException
	 * @throws NamingException
	 */
	public Attributes createUser(String uid, 
			String username, String surname, String givenName, String initials, String email) 
					throws NoSuchAlgorithmException, NamingException {
		String distinguishedName = "cn=" + username + "," + usersBaseDN;
		Attributes newAttributes = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("top");
		oc.add("person");
		oc.add("organizationalPerson");
		oc.add("inetOrgPerson");
		newAttributes.put(oc);
		newAttributes.put(new BasicAttribute("uid", uid));
		newAttributes.put(new BasicAttribute("initials", initials));
		newAttributes.put(new BasicAttribute("mail", email));
		newAttributes.put(new BasicAttribute("cn", username));
		newAttributes.put(new BasicAttribute("sn", surname));
		newAttributes.put(new BasicAttribute("givenName", givenName));
		newAttributes.put(new BasicAttribute("userPassword", digest("1234567")));
		System.out.println("Name: " + username + " Attributes: " + newAttributes);
		ldapContext.createSubcontext(distinguishedName, newAttributes);
		return newAttributes;
	}
	
	/**
	 * Creates a new LDAP user
	 * @param uid The Unique IDentifier
	 * @param username The user name
	 * @param surname The user surname
	 * @param givenName The user givenName
	 * @param initials The user initials
	 * @param email The user's email
	 * @return The required LDAP attributes for the LDAP transaction
	 * @throws NoSuchAlgorithmException
	 * @throws NamingException
	 */
	public Attributes createUser(String uid, 
			String username, String surname, String givenName, String initials, String email, String password) 
					throws NoSuchAlgorithmException, NamingException {
		String distinguishedName = "cn=" + username + "," + usersBaseDN;
		Attributes newAttributes = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("top");
		oc.add("person");
		oc.add("organizationalPerson");
		oc.add("inetOrgPerson");
		newAttributes.put(oc);
		newAttributes.put(new BasicAttribute("uid", uid));
		newAttributes.put(new BasicAttribute("initials", initials));
		newAttributes.put(new BasicAttribute("mail", email));
		newAttributes.put(new BasicAttribute("cn", username));
		newAttributes.put(new BasicAttribute("sn", surname));
		newAttributes.put(new BasicAttribute("givenName", givenName));
		newAttributes.put(new BasicAttribute("userPassword", digest(password)));
//		System.out.println("Name: " + username + " Attributes: " + newAttributes);
		ldapContext.createSubcontext(distinguishedName, newAttributes);
		return newAttributes;
	}

	public Attributes createProject(String entity, List<LDAPUser> ldapUsers) throws NamingException {
		String distinguishedName = "cn=" + entity + "," + projectsBaseDN;
		Attributes newAttributes = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("top");
		oc.add("groupOfUniqueNames");
		newAttributes.put(oc);
		System.out.println("Name: " + entity + " Attributes: " + newAttributes);

		Attribute uniqueMembers = new BasicAttribute("uniqueMember");
		for (int i = 0; i < ldapUsers.size(); i++) {
			String userDN = "cn=" + ldapUsers.get(i).cn + "," + usersBaseDN;
			uniqueMembers.add(userDN);
		}
		newAttributes.put(uniqueMembers);
		
		ldapContext.createSubcontext(distinguishedName, newAttributes);
		
		return newAttributes;
	}
	
	public void updateProject(String projectName, List<LDAPUser> ldapUsers) throws NamingException {
		String distinguishedName = "cn=" + projectName + "," + projectsBaseDN;
		
		Attribute uniqueMembers = new BasicAttribute("uniqueMember");
		for (int i = 0; i < ldapUsers.size(); i++) {
			String userDN = "cn=" + ldapUsers.get(i).cn + "," + usersBaseDN;
			uniqueMembers.add(userDN);
		}
		
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, uniqueMembers);
		ldapContext.modifyAttributes(distinguishedName, mods);
	}
	
	
	public void updateEntity(String entityName, List<LDAPUser> ldapUsers) throws NamingException {
		String distinguishedName = "cn=" + entityName + "," + entitiesBaseDN;
		
		Attribute uniqueMembers = new BasicAttribute("uniqueMember");
		for (int i = 0; i < ldapUsers.size(); i++) {
			String userDN = "cn=" + ldapUsers.get(i).cn + "," + usersBaseDN;
			uniqueMembers.add(userDN);
		}
		
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, uniqueMembers);
		ldapContext.modifyAttributes(distinguishedName, mods);
	}
	
	
	public Attributes createEntity(String entity, List<LDAPUser> ldapUsers) throws NamingException {
		String distinguishedName = "cn=" + entity + "," + entitiesBaseDN;
		Attributes newAttributes = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("top");
		oc.add("groupOfUniqueNames");
		newAttributes.put(oc);
		System.out.println("Name: " + entity + " Attributes: " + newAttributes);
		Attribute uniqueMembers = new BasicAttribute("uniqueMember");
		for (int i = 0; i < ldapUsers.size(); i++) {
			String userDN = "cn=" + ldapUsers.get(i).cn + "," + usersBaseDN;
			System.out.println(userDN);
			uniqueMembers.add(userDN);
		}
		newAttributes.put(uniqueMembers);
		
		ldapContext.createSubcontext(distinguishedName, newAttributes);

		return newAttributes;
	}

	/**
	 * Creates a group
	 * @param group The group name
	 * @return The LDAP attributes
	 * @throws NamingException
	 */
	public Attributes createGroup(String group) throws NamingException {
		String distinguishedName = "ou=" + group + "," + baseDN;
		Attributes newAttributes = new BasicAttributes(true);
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("top");
		oc.add("organizationalUnit");
		newAttributes.put(oc);
		ldapContext.createSubcontext(distinguishedName, newAttributes);
		return newAttributes;
	}
	
	/**
	 * Deletes an existing LDAP user by his/her name.
	 * If the user belongs to a project and/or an entity, the user is removed from them.
	 * If the user is the last element in the project and/or the entity, the project or the entity is removed.
	 * @param username The user's name
	 * @throws NamingException
	 */
	public void deleteUser(String username) 
			throws NamingException {
		deleteUserInEntities(username);
		deleteUserInProjects(username);
		String distinguishedName = "cn=" + username + "," + usersBaseDN;		
		ldapContext.destroySubcontext(distinguishedName);
	}
	
	public void deleteUserInEntities(String username) throws NamingException {
		List<LDAPEntity> entities = retrieveEntities();
		for (LDAPEntity ldapEntity : entities) {
			deleteUserInEntity(ldapEntity, username);
		}
	}

	private void deleteUserInEntity(LDAPEntity ldapEntity, String username) throws NamingException {
		List<LDAPUser> ldapUsers = ldapEntity.getMembers();
		List<LDAPUser> updatedLdapUsers = new ArrayList<>();
		for (LDAPUser ldapUser : ldapUsers) {
			if(!ldapUser.cn.equals(username)) {
				updatedLdapUsers.add(ldapUser);
			}
		}
		try {
			updateEntity(ldapEntity.cn, updatedLdapUsers);			
		} catch(Exception e) {
			deleteEntity(ldapEntity.cn);
		}
	}

	public void deleteUserInProjects(String username) throws NamingException {
		List<LDAPProject> projects = retrieveProjects();
		for (LDAPProject project : projects) {
			deleteUserInProject(project, username);
		}
	}

	private void deleteUserInProject(LDAPProject project, String username) throws NamingException {
		List<LDAPUser> ldapUsers = project.getMembers();
		List<LDAPUser> updatedLdapUsers = new ArrayList<>();
		for (LDAPUser ldapUser : ldapUsers) {
			if(!ldapUser.cn.equals(username)) {
				updatedLdapUsers.add(ldapUser);
			}
		}
		try {
		updateProject(project.cn, updatedLdapUsers);
		} catch(Exception e) {
			deleteProject(project.cn);
		}
	}

	/**
	 * Deletes an existing LDAP user by his/her name.
	 * @param entityName The user's name
	 * @throws NamingException
	 */
	public void deleteProject(String entityName) 
			throws NamingException {
		String distinguishedName = "cn=" + entityName + "," + projectsBaseDN;		
		ldapContext.destroySubcontext(distinguishedName);
	}
	
	/**
	 * Deletes an existing LDAP user by his/her name.
	 * @param entityName The user's name
	 * @throws NamingException
	 */
	public void deleteEntity(String entityName) 
			throws NamingException {
		String distinguishedName = "cn=" + entityName + "," + entitiesBaseDN;		
		ldapContext.destroySubcontext(distinguishedName);
	}

	/**
	 * Creates a SHA3 digest of a clear password
	 * @param password The password
	 * @return The SHA3 digest
	 * @throws NoSuchAlgorithmException
	 */
	private String digest(String password) throws NoSuchAlgorithmException {
		return SSHA.SHA3.createDigest(password);
	}

	/**
	 * Updates a LDAP user.
	 * @param uid The Unique IDentifier
	 * @param username The user name
	 * @param surname The user surname
	 * @param givenName The user givenName
	 * @param initials The user initials
	 * @param email The user's email
	 * @throws NamingException
	 */
	public void updateUser(String uid, 
			String username, String surname, String givenName, String initials, String email) 
					throws NamingException {
		ModificationItem[] mods = new ModificationItem[6];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("uid", uid));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("initials", initials));
		mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mail", email));
		mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("cn", username));
		mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sn", surname));
		mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("givenName", givenName));
		ldapContext.modifyAttributes("cn=" + username + "," + usersBaseDN, mods);
	}

	/**
	 * Updates the password's user regarding his/her name.
	 * @param username The user's name
	 * @param password The user's password
	 * @throws NamingException
	 * @throws NoSuchAlgorithmException
	 */
	public void updatePasswordByUsername(String username, String password)  
			throws NamingException, NoSuchAlgorithmException {
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
				new BasicAttribute("userPassword", digest(password)));
		ldapContext.modifyAttributes("cn=" + username + "," + usersBaseDN, mods);
	}
	
	/**
	 * Updates the password's user regarding his/her Unique ID.
	 * @param uid The Unique IDentifier
	 * @param password The user's password
	 * @throws NamingException
	 * @throws NoSuchAlgorithmException
	 */
	public void updatePasswordByUid(String uid, String password) 
			throws NamingException, NoSuchAlgorithmException {
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
					new BasicAttribute("userPassword", digest(password)));
		Attributes a = fetchUserAttributesByUid(uid);
		String username = (String) a.get("cn").get(0);
		updatePasswordByUsername(username, password);
	}

	/**
	 * Queries a LDAP user with his/her username.
	 * @param username The user name
	 * @return The LDAP attributes of the founded user
	 * @throws NamingException
	 */
	public Attributes fetchUserByUsername(String username) throws NamingException {
		Attributes attributes = null;
		DirContext o = (DirContext) ldapContext.lookup("cn=" + username + "," + usersBaseDN);
		attributes = o.getAttributes("");
		for (NamingEnumeration<?> ae = attributes.getAll(); ae
				.hasMoreElements();) {
			Attribute attr = (Attribute) ae.next();
			String attrId = attr.getID();
			for (NamingEnumeration<?> vals = attr.getAll(); vals.hasMore();) {
				String thing = vals.next().toString();
//				System.out.println(attrId + ": " + thing);
			}
		}
		return attributes;
	}
	
	/**
	 * Queries a LDAP user attributes with his/her Unique ID.
	 * @param uid The Unique IDentifier
	 * @return The LDAP attributes of the founded user
	 * @throws NamingException
	 */
	public Attributes fetchUserAttributesByUid(String uid) throws NamingException {
	    String[] attrIDs = { "sn", "uid", "cn", "userPassword"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(uid=" + uid + ")", ctls);
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult result = ae.next();
			return result.getAttributes();
		}
		return null;
	}
	
	/**
	 * Queries a LDAP user with his/her Unique ID.
	 * @param uid The Unique IDentifier
	 * @return The LDAP attributes of the founded user
	 * @throws NamingException
	 */
	public SearchResult fetchUserByUid(String uid) throws NamingException {
		// Create a filter
	    String[] attrIDs = { "sn", "uid", "cn", "userPassword"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
	    
	    
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(uid=" + uid + ")", ctls);
		
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult result = ae.next();
			return result;
		}
		return null;
	}
	
	
	public NamingEnumeration<SearchResult> fetchUsers() throws NamingException {
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(cn=*)", null);

		return enumerations;
	}
	
	/**
	 * Retrieves all the users stored in the database
	 * @return The list of users (see {@link LDAPUser})
	 * @throws NamingException
	 */
	public List<LDAPUser> retrieveUsers() throws NamingException {
		List<LDAPUser> ldapUsers = new ArrayList<LDAPUser>();
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(&(objectClass=inetOrgPerson)(cn=*))", null);

		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult user = ae.next();
			LDAPUser ldapUser = new LDAPUser();
			try {
				ldapUser.cn = (String) user.getAttributes().get("cn").get();
				ldapUser.sn = (String) user.getAttributes().get("sn").get();
				ldapUser.givenName = (String) user.getAttributes().get("givenName").get();
				ldapUser.mail = (String) user.getAttributes().get("mail").get();
				ldapUser.uid = (String) user.getAttributes().get("uid").get();
				ldapUser.initials = (String) user.getAttributes().get("initials").get();
				ldapUsers.add(ldapUser);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		Collections.sort(ldapUsers);
		
		return ldapUsers;
	}
	
	public LDAPUser retrieveUserByCn(String username) throws NamingException {
		List<LDAPUser> ldapUsers = new ArrayList<LDAPUser>();
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(&(objectClass=inetOrgPerson)(cn=" + username + "))", null);

		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult user = ae.next();
			LDAPUser ldapUser = new LDAPUser();
			try {
				ldapUser.cn = (String) user.getAttributes().get("cn").get();
				ldapUser.sn = (String) user.getAttributes().get("sn").get();
				ldapUser.givenName = (String) user.getAttributes().get("givenName").get();
				ldapUser.mail = (String) user.getAttributes().get("mail").get();
				ldapUser.uid = (String) user.getAttributes().get("uid").get();
				ldapUser.initials = (String) user.getAttributes().get("initials").get();
				ldapUsers.add(ldapUser);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!ldapUsers.isEmpty()) {
			return ldapUsers.get(0);			
		} else {
			return null;
		}
	}
	
	public LDAPUser retrieveUserByRdn(String cn) throws NamingException {
 		List<LDAPUser> ldapUsers = new ArrayList<LDAPUser>();
		
		LdapName name = new LdapName(cn);
		
		int size = name.getRdns().size();
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(name.getPrefix(size-1), 
					"(&(objectClass=*)(" + name.getSuffix(size-1) + "))", 
					null);

		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult user = ae.next();
			LDAPUser ldapUser = new LDAPUser();
			try {
				ldapUser.cn = (String) user.getAttributes().get("cn").get();
				ldapUser.sn = (String) user.getAttributes().get("sn").get();
				ldapUser.givenName = (String) user.getAttributes().get("givenName").get();
				ldapUser.mail = (String) user.getAttributes().get("mail").get();
				ldapUser.uid = (String) user.getAttributes().get("uid").get();
				ldapUser.initials = (String) user.getAttributes().get("initials").get();
				ldapUsers.add(ldapUser);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!ldapUsers.isEmpty()) {
			return ldapUsers.get(0);			
		} else {
			return null;
		}
	}
	
	public LDAPUser retrieveUserByUid(String uid) throws NamingException {
		List<LDAPUser> ldapUsers = new ArrayList<LDAPUser>();
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(usersBaseDN, "(&(objectClass=inetOrgPerson)(uid=" + uid + "))", null);

		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult user = ae.next();
			LDAPUser ldapUser = new LDAPUser();
			try {
				ldapUser.cn = (String) user.getAttributes().get("cn").get();
				ldapUser.sn = (String) user.getAttributes().get("sn").get();
				ldapUser.givenName = (String) user.getAttributes().get("givenName").get();
				ldapUser.mail = (String) user.getAttributes().get("mail").get();
				ldapUser.uid = (String) user.getAttributes().get("uid").get();
				ldapUser.initials = (String) user.getAttributes().get("initials").get();
				ldapUsers.add(ldapUser);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(ldapUsers.size()==1) {
			return ldapUsers.get(0);			
		} else {
			return null;
		}
	}
	
	/**
	 * Queries all the projects (pure LDAP)
	 * @return The list of projects
	 * @throws NamingException
	 */
	public NamingEnumeration<SearchResult> fetchProjects() throws NamingException {
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(projectsBaseDN, "(cn=*)", ctls);

		return enumerations;
	}
	
	/**
	 * Retrieve all the projects stored in the database
	 * @return The list of projects (see {@link LDAPProject})
	 * @throws NamingException
	 */
	public List<LDAPProject> retrieveProjects() throws NamingException {
		List<LDAPProject> ldapProjects = new ArrayList<LDAPProject>();
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(projectsBaseDN, "(cn=*)", ctls);
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult project = ae.next();
			LDAPProject ldapProject = new LDAPProject();
			try {
				ldapProject.cn = (String) project.getAttributes().get("cn").get();
				ldapProjects.add(ldapProject);
				NamingEnumeration<String> ne 
					= (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
				for(;ne.hasMore();) {
					String member = (String) ne.next();
					LDAPUser ldapUser = retrieveUserByRdn(member);
					ldapProject.addMember(ldapUser);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return ldapProjects;
	}
	
	/**
	 * Retrieves the project by its name
	 * @param entityName The project name
	 * @return The project, <code>null</code> if not found or multiple results
	 * @throws NamingException
	 */
	public LDAPProject retrieveProjectByCn(String projectName) throws NamingException {
		List<LDAPProject> ldapProjects = new ArrayList<LDAPProject>();
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(projectsBaseDN, "(cn="+ projectName + ")", ctls);
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult project = ae.next();
			LDAPProject ldapProject = new LDAPProject();
			try {
				ldapProject.cn = (String) project.getAttributes().get("cn").get();
				ldapProjects.add(ldapProject);
				NamingEnumeration<String> ne 
					= (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
				for(;ne.hasMore();) {
					String member = (String) ne.next();
					LDAPUser ldapUser = retrieveUserByRdn(member);
					ldapProject.addMember(ldapUser);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!ldapProjects.isEmpty())
			return ldapProjects.get(0);
		else
			return null;
	}
	
	/**
	 * Retrieves the entity by its name
	 * @param entityName The entity name
	 * @return The entity, <code>null</code> if not found or multiple results
	 * @throws NamingException
	 */
	public LDAPEntity retrieveEntityByCn(String entityName) throws NamingException {
		List<LDAPEntity> ldapEntities = new ArrayList<LDAPEntity>();
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(entitiesBaseDN, "(cn="+ entityName + ")", ctls);
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult project = ae.next();
			LDAPEntity ldapEntity = new LDAPEntity();
			try {
				ldapEntity.cn = (String) project.getAttributes().get("cn").get();
				ldapEntities.add(ldapEntity);
				NamingEnumeration<String> ne 
					= (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
				for(;ne.hasMore();) {
					String member = (String) ne.next();
					LDAPUser ldapUser = retrieveUserByRdn(member);
					ldapEntity.addMember(ldapUser);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!ldapEntities.isEmpty())
			return ldapEntities.get(0);
		else
			return null;
	}
	
	
	/**
	 * Queries all the entities (pure LDAP)
	 * @return The list of entities
	 * @throws NamingException
	 */
	public NamingEnumeration<SearchResult> fetchEntities() throws NamingException {
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(entitiesBaseDN, "(cn=*)", ctls);

		return enumerations;
	}
	
	/**
	 * Retrieve all the entities stored in the database
	 * @return The list of entities (see {@link LDAPEntity})
	 * @throws NamingException
	 */
	public List<LDAPEntity> retrieveEntities() throws NamingException {
		List<LDAPEntity> ldapEntities = new ArrayList<LDAPEntity>();
	    String[] attrIDs = { "cn", "uniqueMember"};
	    SearchControls ctls = new SearchControls();
	    ctls.setReturningAttributes(attrIDs);
		NamingEnumeration<SearchResult> enumerations 
			= ldapContext.search(entitiesBaseDN, "(cn=*)", ctls);
		for (NamingEnumeration<SearchResult> ae = enumerations; enumerations.hasMoreElements();) {
			SearchResult project = ae.next();
			LDAPEntity ldapEntity = new LDAPEntity();
			try {
				ldapEntity.cn = (String) project.getAttributes().get("cn").get();
				ldapEntities.add(ldapEntity);
				NamingEnumeration<String> ne 
					= (NamingEnumeration<String>) project.getAttributes().get("uniqueMember").getAll(); 
				for(;ne.hasMore();) {
					String member = (String) ne.next();
					LDAPUser ldapUser = retrieveUserByRdn(member);
					ldapEntity.addMember(ldapUser);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return ldapEntities;
	}
	
	/**
	 * Checks a user's password
	 * @param uid The user's unique id
	 * @param password The password to be checked
	 * @return <code>true</code> if the user has been authenticated
	 * @throws NamingException
	 */
	public boolean checkPassword(String uid, String password) throws NamingException {
		if(this.ldapContext != null) {
			SearchResult a = fetchUserByUid(uid);

			if(a!=null) {
				Hashtable<String,String> environment 
					= (Hashtable<String,String>) ldapContext.getEnvironment().clone();
				
				environment.put(Context.SECURITY_PRINCIPAL, a.getNameInNamespace());
				environment.put(Context.SECURITY_CREDENTIALS, password);
				try {
					ldapUserContext = new InitialDirContext(environment);
					return true;
				} catch(Exception e) {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return false;
		}
	}
		
	/**
	 * Imports a list of users from a CSV file as an input stream
	 * @param csvStream The CSV input stream
	 * @return The file content
	 * @throws IOException
	 */
	public String importUsers(InputStream csvStream) throws IOException {
	    int size = csvStream.available();
	    char[] theChars = new char[size];
	    byte[] bytes    = new byte[size];

	    csvStream.read(bytes, 0, size);
	    for (int i = 0; i < size;) {	    	
	        theChars[i] = (char)(bytes[i++]&0xff);
	    }
	    
	    String fileContent = new String(theChars);
	    
	    int index = 0;
	    while ((index = fileContent.indexOf(System.lineSeparator()))!=-1) {
			String line = fileContent.substring(0, index);
			String[] elements = line.split(",", -1);
			try {
				String uid = elements[0];
				elements[1] = elements[1].replace("\"", "");
				elements[2] = elements[2].replace("\"", "");
				
				String username = elements[1] + " " + elements[2];
				String surname = elements[1];
				String givenname = elements[2];
				String email = elements[3];
				int index1 = uid.indexOf('-');
				String initials = new String(uid);
				String organization = "dw";
				if(index1!=-1) {
					initials = uid.substring(0, index1);
					organization = uid.substring(index1);
				}
				initials = initials.toUpperCase();
				try {
					createUser(uid.trim(), username.trim(), surname.trim(), givenname.trim(), initials.trim(), email.trim());				
				} catch(Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileContent = fileContent.substring(index+1);
		}
	    
	    return fileContent;
	}
	
	/**
	 * Imports a list of users from a CSV file as an input stream
	 * @param csvStream The CSV input stream
	 * @return The file content
	 * @throws IOException
	 */
	public String importProjects(InputStream csvStream) throws IOException {
	    int size = csvStream.available();
	    char[] theChars = new char[size];
	    byte[] bytes    = new byte[size];

	    csvStream.read(bytes, 0, size);
	    for (int i = 0; i < size;) {	    	
	        theChars[i] = (char)(bytes[i++]&0xff);
	    }
	    
	    String fileContent = new String(theChars);
	    
		List<LDAPUser> users = new ArrayList<>();
	    boolean first = true;
	    int index = 0;
	    while ((index = fileContent.indexOf(System.lineSeparator()))!=-1) {
			String line = fileContent.substring(0, index);
			String[] elements = line.split(",", -1);
			try {
				String projectCn = elements[0];
				elements[1] = elements[1].replace("\"", "");
				String userCn = elements[1];
				
				LDAPUser user = retrieveUserByCn(userCn);
				users.add(user);
				try {
					if(!isProjectExisting(projectCn)) {
						createProject(projectCn, users);
						first = false;
					} else {
						updateProject(projectCn, users);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileContent = fileContent.substring(index+1);
		}
	    
	    return fileContent;
	}
	
	/**
	 * Imports a list of users from a CSV file as an input stream
	 * @param csvStream The CSV input stream
	 * @return The file content
	 * @throws IOException
	 */
	public String importEntities(InputStream csvStream) throws IOException {
	    int size = csvStream.available();
	    char[] theChars = new char[size];
	    byte[] bytes    = new byte[size];

	    csvStream.read(bytes, 0, size);
	    for (int i = 0; i < size;) {	    	
	        theChars[i] = (char)(bytes[i++]&0xff);
	    }
	    
	    String fileContent = new String(theChars);
	    
		List<LDAPUser> users = new ArrayList<>();
	    int index = 0;
	    while ((index = fileContent.indexOf(System.lineSeparator()))!=-1) {
			String line = fileContent.substring(0, index);
			String[] elements = line.split(",", -1);
			try {
				String entityCn = elements[0];
				elements[1] = elements[1].replace("\"", "");
				String userCn = elements[1];
				
				LDAPUser user = retrieveUserByCn(userCn);

				users.add(user);
				try {
					if(!isEntityExisting(entityCn)) {
						createEntity(entityCn, users);
					} else {
						updateEntity(entityCn, users);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileContent = fileContent.substring(index+1);
		}
	    
	    return fileContent;
	}
	
	private boolean isEntityExisting(String entityName) throws NamingException {
		return retrieveEntityByCn(entityName)!=null;
	}

	private boolean isProjectExisting(String projectName) throws NamingException {
		return retrieveProjectByCn(projectName)!=null;
	}

	/**
	 * The CSV separator
	 */
	public static final String CSV_SEPARATOR = ";";
	
	/**
	 * Exports the users
	 * @param file The output file
	 * @throws IOException In case of file output stream error
	 * @throws NamingException
	 */
	public void exportUsers(File file) throws IOException, NamingException {
		FileOutputStream os = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(os);
		List<LDAPUser> users = retrieveUsers();
		for (LDAPUser user : users) {
			String line = user.cn + CSV_SEPARATOR 
					+ user.sn + CSV_SEPARATOR
					+ user.givenName + CSV_SEPARATOR
					+ user.initials + CSV_SEPARATOR
					+ user.uid + CSV_SEPARATOR
					+ user.mail + CSV_SEPARATOR
					;
			writer.append(line + System.lineSeparator());
		}
		writer.close();
		os.close();
	}
	
	/**
	 * Exports the projects
	 * @param file The output file
	 * @throws IOException In case of file output stream error
	 * @throws NamingException
	 */
	public void exportProjects(File file) throws IOException, NamingException {
		FileOutputStream os = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(os);
		List<LDAPProject> projects = retrieveProjects();
		for (LDAPProject project : projects) {
			for (LDAPUser user : project.getMembers()) {
				String line = project.cn + CSV_SEPARATOR 
						+ user.cn + CSV_SEPARATOR
						;
				writer.append(line + System.lineSeparator());
			}

		}
		writer.close();
		os.close();
	}
	
	/**
	 * Exports the entities
	 * @param file The output file
	 * @throws IOException In case of file output stream error
	 * @throws NamingException
	 */
	public void exportEntities(File file) throws IOException, NamingException {
		FileOutputStream os = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(os);
		List<LDAPEntity> entities = retrieveEntities();
		for (LDAPEntity entity : entities) {
			for (LDAPUser user : entity.getMembers()) {
				String line = entity.cn + CSV_SEPARATOR 
						+ user.cn + CSV_SEPARATOR
						;
				writer.append(line + System.lineSeparator());
			}

		}
		writer.close();
		os.close();
	}

	
	/**
	 * Closes the current LDAP Context
	 */
	public void close() {
		try {
			ldapContext.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.serverURL + "  " + this.principalUser + "  " + this.baseDN + " " + this.ldapContext;
	}
}
