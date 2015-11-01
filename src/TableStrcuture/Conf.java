/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;

import java.util.ArrayList;

/**
 *
 * @author chintanpanchamia
 */
public class Conf {
    private String confnum;
    private String confname;
    private String title;
    private int year;
    private int hunt_avail_no;
    private int hunt_total_no;
    private int hill_total_no;
    private int hill_avail_no;
    private String e_copy;
    private int group_id;
    private ArrayList<String> author_list = new ArrayList<>();

    /**
     * @return the confnum
     */
    public String getConfnum() {
        return confnum;
    }

    /**
     * @param confnum the confnum to set
     */
    public void setConfnum(String confnum) {
        this.confnum = confnum;
    }

    /**
     * @return the confname
     */
    public String getConfname() {
        return confname;
    }

    /**
     * @param confname the confname to set
     */
    public void setConfname(String confname) {
        this.confname = confname;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the hunt_avail_no
     */
    public int getHunt_avail_no() {
        return hunt_avail_no;
    }

    /**
     * @param hunt_avail_no the hunt_avail_no to set
     */
    public void setHunt_avail_no(int hunt_avail_no) {
        this.hunt_avail_no = hunt_avail_no;
    }

    /**
     * @return the hunt_total_no
     */
    public int getHunt_total_no() {
        return hunt_total_no;
    }

    /**
     * @param hunt_total_no the hunt_total_no to set
     */
    public void setHunt_total_no(int hunt_total_no) {
        this.hunt_total_no = hunt_total_no;
    }

    /**
     * @return the hill_total_no
     */
    public int getHill_total_no() {
        return hill_total_no;
    }

    /**
     * @param hill_total_no the hill_total_no to set
     */
    public void setHill_total_no(int hill_total_no) {
        this.hill_total_no = hill_total_no;
    }

    /**
     * @return the hill_avail_no
     */
    public int getHill_avail_no() {
        return hill_avail_no;
    }

    /**
     * @param hill_avail_no the hill_avail_no to set
     */
    public void setHill_avail_no(int hill_avail_no) {
        this.hill_avail_no = hill_avail_no;
    }

    /**
     * @return the e_copy
     */
    public String getE_copy() {
        return e_copy;
    }

    /**
     * @param e_copy the e_copy to set
     */
    public void setE_copy(String e_copy) {
        this.e_copy = e_copy;
    }

    /**
     * @return the group_id
     */
    public int getGroup_id() {
        return group_id;
    }

    /**
     * @param group_id the group_id to set
     */
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    /**
     * @return the author_list
     */
    public ArrayList<String> getAuthor_list() {
        return author_list;
    }

    /**
     * @param author_list the author_list to set
     */
    public void setAuthor_list(ArrayList<String> author_list) {
        this.author_list = author_list;
    }


}
