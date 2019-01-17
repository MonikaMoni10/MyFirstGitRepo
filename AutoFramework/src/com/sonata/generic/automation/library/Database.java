package com.sonata.generic.automation.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sonata.generic.automation.library.DatabaseConnect;


/**
 * Database class is used to connect to different database, and used to execute
 * SQL commands.
 * 
 * @author Garrick Tom
 * 
 */
public class Database {

	/**
	 * Contains information required to connect to the SQL Server.
	 */
	private Connection connection;

	/**
	 * Contains the SQL statement to be executed.
	 */
	private Statement statement;

	/**
	 * The results returned by the SQL statement.
	 */
	private ResultSet results;

	private int databaseVendor;

	// Database Constants.
	private static final int MSSQL = 0;
	private static final int PERVASIVE = 1;
	private static final int ORACLE = 2;

	private static final int NUM_DATABASE_TYPES = 3;

	/**
	 * Once the Database class is initialized, a connection to the database
	 * specified is created automatically using the information provided.
	 * 
	 * @param database
	 *            the database type as defined by the database constants.
	 * @param databaseName
	 *            name of the database on the server, or in the case of Oracle,
	 *            the user name.
	 * @param password
	 *            used only for Oracle databases. All other databases must pass
	 *            a <code>null</code> parameter in place.
	 */
	public Database(final int database, final String databaseName,
			final String password) {

		databaseVendor = database;

		switch (database) {
		case MSSQL:
			connectToMSQL(DatabaseConnect.getMssqlServername(), DatabaseConnect
					.getMssqlPortnumber(), databaseName, DatabaseConnect
					.getMssqlUsername(), DatabaseConnect.getMssqlPassword());
			break;

		case PERVASIVE:
			connectToPervasive(DatabaseConnect.getPervasiveServername(),
					DatabaseConnect.getPervasivePortnumber(), databaseName);
			break;

		case ORACLE:
			connectToOracle(DatabaseConnect.getOracleServername(),
					DatabaseConnect.getOraclePortnumber(), DatabaseConnect
							.getOracleSid(), databaseName, password);
			break;

		default:
			break;
		}
	}

	/**
	 * Returns the number of database connection types. Used primarily for
	 * creating arrays to use the Database class for multiple database vendors.
	 * 
	 * @return the number of databases the Database class can connect to.
	 */
	public static int getNumberOfDatabaseTypes() {
		return NUM_DATABASE_TYPES;
	}

	/**
	 * The constant for use with MSSQL.
	 * 
	 * @return The constant for use with MSSQL.
	 */
	public static int useMSSQL() {
		return MSSQL;
	}

	/**
	 * The constant for use with Pervasive.
	 * 
	 * @return The constant for use with Pervasive.
	 */
	public static int usePervasive() {
		return PERVASIVE;
	}

	/**
	 * The constant for use with Oracle.
	 * 
	 * @return The constant for use with Oracle.
	 */
	public static int useOracle() {
		return ORACLE;
	}

	/**
	 * Returns the database type being used by the current instance of the
	 * database.
	 * 
	 * @return the database vendor constant.
	 */
	public final int getDatabaseVendor() {
		return databaseVendor;
	}

	/**
	 * Connects to the SQL Server database. All connection information must be
	 * provided.
	 * 
	 * @param server
	 *            name of the SQL Server where the database resides.
	 * @param port
	 *            port number used open on the server to connect to SQL Server.
	 * @param database
	 *            name of the Database to connect to.
	 * @param user
	 *            username authorized to connect to the SQL Server.
	 * @param password
	 *            password of the named user.
	 * @return <li><code>true</code> if the connection was successful.</li> <li>
	 *         <code>false</code> if the connection was unsuccessful.</li>
	 */
	public final boolean connectToMSQL(final String server, final String port,
			final String database, final String user, final String password) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://"
					+ server + ":" + port + ";databaseName=" + database
					+ ";user=" + user + ";password=" + password + ";");
			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Connects to a Pervasive database, requiring all connection information.
	 * Note that the system assumes that security has been turned off for
	 * Pervasive.
	 * 
	 * @param server
	 *            name of the server.
	 * @param port
	 *            port number Pervasive is using on the server.
	 * @param database
	 *            database schema to use once connected to the server.
	 * @return <li><code>true</code> if the connection was successful.</li> <li>
	 *         <code>false</code> if the connection was unsuccessful.</li>
	 */
	public final boolean connectToPervasive(final String server,
			final String port, final String database) {
		try {
			Class.forName("com.pervasive.jdbc.v2.Driver");
			connection = DriverManager.getConnection("jdbc:pervasive://"
					+ server + ":" + port + "/" + database);
			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Connects to an Oracle database, requiring all connection information.
	 * 
	 * @param server
	 *            Name of the Oracle server.
	 * @param port
	 *            Port to connect to on the Oracle server.
	 * @param sid
	 *            The Oracle Service ID on the server.
	 * @param username
	 *            Username in Oracle.
	 * @param password
	 *            Password for the user in Oracle.
	 * @return <li><code>true</code> if the connection was successful.</li> <li>
	 *         <code>false</code> if the connection was unsuccessful.</li>
	 */
	public final boolean connectToOracle(final String server,
			final String port, final String sid, final String username,
			final String password) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@"
					+ server + ":" + port + ":" + sid, username, password);
			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Moves the cursor to the next row.
	 * 
	 * @return <li><code>true</code> if the cursor was moved to the next row.</li>
	 *         <li><code>false</code> if the cursor has reached the end of the
	 *         data.</li>
	 * @throws SQLException
	 *             thrown if there's a problem moving the cursor.
	 */
	public final boolean nextRow() throws SQLException {
		return results.next();
	}

	/**
	 * Executes a SQL Statement. If execution is successful, the cursor
	 * automatically moves to the first line of the results.
	 * 
	 * @param query
	 *            The SQL query to run. This must be formatted so the SQL
	 *            statement can be run in it's respective database engine.
	 * @return <li><code>true</code> if the query was executed successfully.</li>
	 *         <li><code>false</code> if the query was not executed
	 *         successfully.</li>
	 */
	public final boolean executeQuery(final String query) {
		try {
			results = statement.executeQuery(query);
			results.first();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Retrieves a string field from the results of a SELECT statement. The
	 * method will return the value based on the column provided, as well as the
	 * current location of the cursor.
	 * 
	 * @param columnName
	 *            column name of the field to be retrieved.
	 * @return string containing the value of the field.
	 * @throws SQLException
	 *             if the field was not located, a SQLException will be thrown.
	 */
	public final String getStringField(final String columnName)
			throws SQLException {
		return results.getString(columnName);
	}

	/**
	 * Retrieves all the rows for a column from the results of a SELECT
	 * statement into an Array.
	 * 
	 * @param columnName
	 *            column name of the field to be retrieved.
	 * @return array containing all the values in the column
	 * @throws SQLException
	 *             if the field was not located, a SQLException will be thrown.
	 */
	public final String[] getEntireColumn(final String columnName)
			throws SQLException {
		int numberOfRows = getNumberOfRows();
		
		String[] column = new String[numberOfRows];
		
		results.first();
		for (int index = 0; index < numberOfRows; index++) {
			column[index] = results.getString(columnName);
			
			if (results.next()) {
				break;
			}
		}
		
		return column;
	}
	
	public final int getNumberOfRows() throws SQLException {
		int rowCount = 0;
		for (;;) {
			if (results.next()) {
				break;
			}
			rowCount++;
		}
		return rowCount;
	}

	/**
	 * Checks to see if a column exists in a table.
	 * 
	 * @param columnName
	 *            name of the column to check.
	 * @return <li><code>true</code> if the column exists.</li> <li>
	 *         <code>false</code> if the column does not exist.</li>
	 */
	public final boolean doesColumnExist(final String columnName) {
		try {
			results.findColumn(columnName);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Checks to see that the value of a field is as expected. Note that because
	 * Accpac uses CHAR fields rather than VARCHAR fields, the String.contains()
	 * function is used in order to work around the extra spaces given by a CHAR
	 * field.
	 * 
	 * @param columnName
	 *            name of the column to check the field value.
	 * @param expectedValue
	 *            the value which should be in the column
	 * @return <li><code>true</code> if the value is as expected.</li> <li>
	 *         <code>false</code> if the value is not as expected.</li>
	 * @throws SQLException
	 *             thrown normally if no such columnName exists.
	 */
	public final boolean assertStringFieldValue(final String columnName,
			final String expectedValue) throws SQLException {
		return getStringField(columnName).contains(expectedValue);
	}

	/**
	 * Checks and ensures a column of values retrieved from a database is equal
	 * to an array of values passed in.
	 * 
	 * @param columnName
	 *            name of the column to check.
	 * @param bkacctAudttimeBefore
	 *            array of expected values to check against the column.
	 * @return <li><code>true</code> if the value is as expected.</li> <li>
	 *         <code>false</code> if the value is not as expected.</li>
	 * @throws SQLException
	 *             thrown normally if no such columnName exists.
	 */
	public final boolean assertArrayValues(final String columnName,
			final String[] bkacctAudttimeBefore) throws SQLException {
		return getEntireColumn(columnName).equals(bkacctAudttimeBefore);
	}
}
