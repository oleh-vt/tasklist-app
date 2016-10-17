package app.tasklist.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceRetriever {
	public static DataSource getMySqlDataSource(){
		MysqlDataSource mysqlDs = null;
		//FileInputStream fis = null;
		Properties prop = new Properties();
		try(FileInputStream fis = new FileInputStream("db_connection.properties")){
			prop.load(fis);
			mysqlDs = new MysqlDataSource();
			mysqlDs.setUrl(prop.getProperty("URL"));
			mysqlDs.setUser(prop.getProperty("USERNAME"));
			mysqlDs.setPassword(prop.getProperty("PASSWORD"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return mysqlDs;
	}
}
