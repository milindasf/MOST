package bpi.most.connector.impl;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides db connections based on DriverManager interface
 * TODO impelement DataSource Connection Factory
 */
public class DbConnectionFactoryDriverManager extends DbConnectionFactory{
		private String sqlDriverManager = null;
		private String sqlDriverManagerUrl = null;
		
		public DbConnectionFactoryDriverManager(String sqlDriverManager, String sqlDriverManagerUrl) {
			//TODO add fault detection
			this.sqlDriverManager= sqlDriverManager;
			this.sqlDriverManagerUrl = sqlDriverManagerUrl;
		}
		public Connection getConnection() {
			try {
				Class.forName(sqlDriverManager);				
				return DriverManager.getConnection(sqlDriverManagerUrl);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
			
}
