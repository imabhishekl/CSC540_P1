/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pooja Asher
 */
public class Test {
    public static void main(String arg[]){
        LibrarySystem l=new LibrarySystem();
        l.setup();
        ButtonEvents b= new ButtonEvents();
        try {
            b.displayCameras();
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
