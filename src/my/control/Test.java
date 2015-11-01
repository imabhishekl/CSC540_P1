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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pooja Asher
 */
public class Test {

    public static void main(String arg[]) throws ParseException {

        String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        java.util.Date date = format.parse(string);
        System.out.println(date);

        LibrarySystem l = new LibrarySystem();
        l.setup();

        try {
            //Date date = new Date();
            System.out.println(ButtonEvents.waitlistCamera("CA1",date));
            // ButtonEvents.getRoom("Hunt", 3, "study",);
            System.out.println("main");
        } catch (Exception e) {
        };

        //System.out.println(d);
    }
}
