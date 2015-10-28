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
public class Rooms {
    private String room_no;
    private int floor_no;
    private int capacity;
    private String type;
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
    public int getFloor_no() {
        return floor_no;
    }

    /**
     * @param title the title to set
     */
    public void setFloor_no(int floor_no) {
        this.floor_no=floor_no;
    }

    /**
     * @return the edition
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param edition the edition to set
     */
    public void setCapacity(int capacity) {
        this.capacity=capacity;
    }

    /**
     * @return the year_of_publication
     */
    public String getType() {
        return type;
    }

    /**
     * @param year_of_publication the year_of_publication to set
     */
    public void setType(String type) {
        this.type=type;
    }

    /**
     * @return the publisher
     */
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