/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;
import TableStrcuture.Rooms;
import TableStrcuture.Reserve_room;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pooja Asher
 */
public class Test {

    public static void main(String arg[]) {

        ArrayList <Date> a = new ArrayList<Date>();
        /*long time = System.currentTimeMillis();
        java.sql.Date d = new java.sql.Date(time);*/
        SimpleDateFormat f = new SimpleDateFormat("MM-dd-YYYY");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        for(int i = 1;i <= 3; i++)
        {
            
            
            //System.out.println(c.get(Calendar.DAY_OF_WEEK));
            System.out.println(f.format(c.getTime()));
            c.add(Calendar.DAY_OF_WEEK, 7);
        }

        
        //System.out.println(d);
    }
}
