package bpi.most.connector.impl;

import java.sql.Connection;

/**
 * Abstraction of DataSource or DriverManager database sources
 * Extend and implement desired techique
 * Overwrite getConnection
 * @author robert.zach@tuwien.ac.at
 */
public class DbConnectionFactory {

	//TODO Implement getConnection to use DataSource or DriverManager
	public Connection getConnection() {
		return null;
	}
}
