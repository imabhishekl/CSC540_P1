package my.dbproject;

import java.sql.*;

public class DBConnect {

    static final String jdbcURL 
	= "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";
    static Connection conn = null;

    public static void setConnection(String[] args) 
    {
        try 
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");

	    String user = "aslingwa";	// For example, "jsmith"
	    String passwd = "200111126";	// Your 9 digit student ID number
            
            try 
            {
		conn = DriverManager.getConnection(jdbcURL, user, passwd);
            } 
            finally 
            {
                close(conn);
            }
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
