/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author abhishek
 */
public class LibraryAPI 
{
    public static int getAvailableBooks(String isbn,String select_clause) throws SQLException
    {
        String query = "select " + select_clause + " from books where isbn_no = ?";
        PreparedStatement ps = LibrarySystem.connection.prepareStatement(query);
        
        ps.setString(1, isbn);
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
        
        query = "select id from publication where where publication_id = ?";
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
}