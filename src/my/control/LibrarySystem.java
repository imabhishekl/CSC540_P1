/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import java.sql.Connection;
import my.dbproject.DBConnect;
import my.dbproject.LoginForm;

/**
 *
 * @author abhishek
 */
public class LibrarySystem 
{
    static Connection connection = null;
    
    public static void setup()
    {
        DBConnect.setConnection();
        connection = DBConnect.getConnection();
        if(connection == null)
            System.out.println("NULL:CO");
    }
    public static void main(String arg[])
    {
        /* Set up the system before start */
        setup();
        /* Start with the login page */
        render_login();
    }
    
    public static void render_login()
    {
        LoginForm.init();
        System.out.println("render login");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}