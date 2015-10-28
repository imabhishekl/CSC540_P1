/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;

/**
 *
 * @author chintanpanchamia
 */
public class Reserve_room {
    private int patron_ID;
    private String room_no;
    private String lib_name;
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
}