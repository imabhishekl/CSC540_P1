/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author abhishek
 */
public class LibraryAPI 
{
    public static int getAvailableBooks(String isbn,String select_clause) throws SQLException
    {
        String query = "select " + select_clause + " from books where ISBN_NO = ?";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static int getAvailableJournals(String issn,String select_clause) throws SQLException
    {
        String query = "select " + select_clause + " from journals where ISSN_NO = ?";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setString(1, issn);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static int getAvailableConf(String conf,String select_clause) throws SQLException
    {
        String query = "select " + select_clause + " from conf where CONF_NUM = ?";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setString(1, conf);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static int getPubllicationId(String isbn)throws SQLException
    {
        String query;
        
        query = "select id from publication where publication_id = ?";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        ps.setString(1, isbn);
        
        ResultSet rs = ps.executeQuery();
        
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static int getPatronId(String patron_id)throws SQLException
    {
        String query = "Select id from patron where patron_id = ?";
        
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        ps.setString(1, patron_id);
        
        ResultSet rs = ps.executeQuery();
        
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static ArrayList<String> getAuthorList(int group_id)throws SQLException
    {
        ArrayList<String> author_list = new ArrayList<>();
        String query;
        
        query = "select FIRST_NAME,LAST_NAME from authors where author_id IN (Select author_id from author_group where group_id = ?)";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        ps.setInt(1, group_id);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next())
        {
            author_list.add(rs.getString(1) + " " + rs.getString(2));
        }
        return author_list;
    }
    
    public static String getLibraryName(int r_id,int login_id)throws SQLException
    {
        String query = null;
        
        query = "select LIB_NAME from checkout where PUBLICATION_ID = ? and PATRON_ID = ? and END_TIME is NULL";
        
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setInt(1, r_id);
        ps.setInt(2, login_id);
        
        ResultSet rs = ps.executeQuery();
        
        if(rs.next())
        {
            return rs.getString(1);
        }
        return null;
    }

    public static String isECopy(int r_id,int login_id)throws SQLException
    {
        String query = null;
        
        query = "select * from checkout where PUBLICATION_ID = ? and PATRON_ID = ? and END_TIME is NULL";
        
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setInt(1, r_id);
        ps.setInt(2, login_id);
        
        ResultSet rs = ps.executeQuery();
        System.out.println("Checking : " + r_id  + ": " + login_id);
        if(rs.next())
        {
            System.out.println(rs.getString("E_COPY"));
            return rs.getString("E_COPY");
        }
        return null;
    }

    
    public static int getDuration(String patron_type,String resource_type)throws SQLException
    {
        String query = null;
        
        query = "select DURATION from checkout_duration where PATRON_TYPE = ? and RESOURCE_TYPE = ?";
        
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setString(1, patron_type);
        ps.setString(2, resource_type);
        
        ResultSet rs = ps.executeQuery();
        
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }
    
    public static int updateBalance(int balance)throws SQLException
    {
        String query = null;
        
        query = "update table " + LibrarySystem.patron_type + " set ACCOUNT_BALANCE = ? where " + LibrarySystem.patron_type + "_id = ?";
        
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setInt(1, balance);
        ps.setString(2, LibrarySystem.login_id);        
        
        if(ps.execute())
        {
            return 1;
        }
        return -1;
    }
    
    public static double getLateFees(int hours,int fees,long no_of_hours)
    {
        double late_fee;
        
        late_fee = fees*Math.floor((double)(no_of_hours/hours));
        
        return late_fee;
    }
}