/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;

import TableStrcuture.Rooms;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Pooja Asher
 */
public class Test {

    public static void main(String arg[]) throws ParseException {

        
        try {            
            LibrarySystem.setup();
            //LibrarySystem.connection.setAutoCommit(true);
            //LibrarySystem.connection.commit();
            //System.out.println("::" + LibraryAPI.isECopy(27, 1));
            //Date date = new Date();
        //    System.out.println("run");
            ButtonEvents.predue_reminder();
            
            //Date date = new Date();
       
            // ButtonEvents.getRoom("Hunt", 3, "study",);
            //System.out.println("main");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(d);
        try{
        //ButtonEvents.update_checkout_room("R2", new Timestamp(System.currentTimeMillis()));
            //System.out.println("run");
//            ArrayList<Rooms> a=ButtonEvents.checkout_room();
            
            
        }
        catch (Exception e) {
        
    }
    }
}
