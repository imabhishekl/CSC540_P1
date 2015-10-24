/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import java.sql.Connection;
import my.dbproject.DBConnect;

/**
 *
 * @author abhishek
 */
public class LibrarySystem 
{
    static Connection connection = null;
    
    public static void setup()
    {
        connection = DBConnect.getConnection();
    }
    public static void main(String arg[])
    {
        /* Set up the system before start */
        setup();
        /* Start with the login page */
        
    }
}