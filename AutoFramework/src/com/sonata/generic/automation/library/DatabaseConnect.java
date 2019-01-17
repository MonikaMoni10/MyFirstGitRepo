package com.sonata.generic.automation.library;

/**
 * Hold connection information used by classes implementing the Database.java
 * class.
 * 
 * @author Administrator
 * 
 */
public final class DatabaseConnect {

	private static final String MSSQL_SERVERNAME = "bcraccqa05";
	//private static final String MSSQL_SERVERNAME = "localhost";
	private static final String MSSQL_PORTNUMBER = "1433";
	private static final String MSSQL_USERNAME = "sa";
	private static final String MSSQL_PASSWORD = "sa";
	//private static final String MSSQL_PASSWORD = "abc123";
	
	private static final String PERVASIVE_SERVERNAME = "bcraccqa05";
	private static final String PERVASIVE_PORTNUMBER = "1583";

	private static final String ORACLE_SERVERNAME = "10.152.11.111";
	private static final String ORACLE_PORTNUMBER = "1521";
	private static final String ORACLE_SID = "ORCL";

	/**
	 * Made private so the class cannot be initialized.
	 */
	private DatabaseConnect() {

	}

	/**
	 * Retrieves the MSSQL Server name.
	 * 
	 * @return the MSSQL Server Name.
	 */
	public static String getMssqlServername() {
		return MSSQL_SERVERNAME;
	}

	/**
	 * Retrieves the Port Number used by the MSSQL Server.
	 * 
	 * @return the MSSQL Port Number.
	 */
	public static String getMssqlPortnumber() {
		return MSSQL_PORTNUMBER;
	}

	/**
	 * Retrieves the Username used by the MSSQL Server.
	 * 
	 * @return the MSSQL Username.
	 */
	public static String getMssqlUsername() {
		return MSSQL_USERNAME;
	}

	/**
	 * Retrieves the Password used by the MSSQL User.
	 * 
	 * @return the MSSQL Password.
	 */
	public static String getMssqlPassword() {
		return MSSQL_PASSWORD;
	}

	/**
	 * Retrieves the Pervasive Servername.
	 * 
	 * @return the Pervasive Servername.
	 */
	public static String getPervasiveServername() {
		return PERVASIVE_SERVERNAME;
	}

	/**
	 * Retrieves the Port Number used by Pervasive on the server.
	 * 
	 * @return the Pervasive Port Number.
	 */
	public static String getPervasivePortnumber() {
		return PERVASIVE_PORTNUMBER;
	}

	/**
	 * Retrieves the Oracle Servername.
	 * 
	 * @return the Oracle servername.
	 */
	public static String getOracleServername() {
		return ORACLE_SERVERNAME;
	}

	/**
	 * Retrieves the Oracle Port Number used on the server.
	 * 
	 * @return the Oracle Port Number.
	 */
	public static String getOraclePortnumber() {
		return ORACLE_PORTNUMBER;
	}

	/**
	 * Retrieves the SID used by the database on the Oracle Server.
	 * 
	 * @return the Oracle SID.
	 */
	public static String getOracleSid() {
		return ORACLE_SID;
	}
}
