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
public class Journals {
    private String issn_no;
    private int year_of_publication;
    private String title;
    private int hunt_avail_no;
    private int hunt_total_no;
    private int hill_total_no;
    private int hill_avail_no;
    private int group_id;
    private String e_copy;
    private ArrayList<String> author_list = new ArrayList<>();

    public Journals() {
    }

    /**
     * @return the issn_no
     */
    public String getIssn_no() {
        return issn_no;
    }

    /**
     * @param issn_no the issn_no to set
     */
    public void setIssn_no(String issn_no) {
        this.issn_no = issn_no;
    }

    /**
     * @return the year_of_publication
     */
    public int getYear_of_publication() {
        return year_of_publication;
    }

    /**
     * @param year_of_publication the year_of_publication to set
     */
    public void setYear_of_publication(int year_of_publication) {
        this.year_of_publication = year_of_publication;
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

    /**
     * @return the issn_no
     */
   
}
