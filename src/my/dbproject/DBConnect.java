package my.dbproject;

import java.sql.*;

public class DBConnect {

    static final String jdbcURL 
	= "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";
    static Connection conn = null;

    public static void setConnection() 
    {
        try 
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");

	    String user = "gmeneze";	// For example, "jsmith"
	    String passwd = "200111263";	// Your 9 digit student ID number
            
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
        } 
        catch(Throwable oops) 
        {
            oops.printStackTrace();
        }
    }
    
    public static Connection getConnection()
    {
        return conn;
    }

    static void close(Connection conn) 
    {
        if(conn != null) 
        {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }
}
