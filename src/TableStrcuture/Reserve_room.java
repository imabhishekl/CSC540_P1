/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;
import java.sql.Timestamp;

/**
 *
 * @author chintanpanchamia
 */
public class Reserve_room {
    private int patron_ID;
    private String room_no;
    private String lib_name;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp checkout;
    /**
     * @return the isbn_no
     */
    public String getRoom_no() {
        return room_no;
    }

    /**
     * @param isbn_no the isbn_no to set
     */
    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    /**
     * @return the title
     */
    public int getPatron_ID() {
        return patron_ID;
    }

    /**
     * @param title the title to set
     */
    public void setPatron_ID(int patron_ID) {
        this.patron_ID=patron_ID;
    }

    public String getLib_name() {
        return lib_name;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setLib_name(String lib_name) {
        this.lib_name=lib_name;
    }
    
    public void setStart_time(Timestamp start_time){
        this.start_time=start_time;
    }
    
     public Timestamp getStart_time(){
        return start_time;
    }

    /**
     * @return the end_time
     */
    public Timestamp getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    /**
     * @return the checkout
     */
    public Timestamp getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(Timestamp checkout) {
        this.checkout = checkout;
    }
}