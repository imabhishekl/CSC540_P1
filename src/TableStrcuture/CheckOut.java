/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;

import java.util.Date;

/**
 *
 * @author abhishek
 */
public class CheckOut 
{
    private Books books_det;
    private Journals jr;
    private Conf cf;
    private int publication_id;
    private int patron_id;
    private Date start_time;
    private Date end_time;
    private String lib_name;

    /**
     * @return the books_det
     */
    public Books getBooks_det() {
        return books_det;
    }

    /**
     * @param books_det the books_det to set
     */
    public void setBooks_det(Books books_det) {
        this.books_det = books_det;
    }

    /**
     * @return the publication_id
     */
    public int getPublication_id() {
        return publication_id;
    }

    /**
     * @param publication_id the publication_id to set
     */
    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    /**
     * @return the patron_id
     */
    public int getPatron_id() {
        return patron_id;
    }

    /**
     * @param patron_id the patron_id to set
     */
    public void setPatron_id(int patron_id) {
        this.patron_id = patron_id;
    }

    /**
     * @return the start_time
     */
    public Date getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public Date getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    /**
     * @return the lib_name
     */
    public String getLib_name() {
        return lib_name;
    }

    /**
     * @param lib_name the lib_name to set
     */
    public void setLib_name(String lib_name) {
        this.lib_name = lib_name;
    }

    /**
     * @return the jr
     */
    public Journals getJr() {
        return jr;
    }

    /**
     * @param jr the jr to set
     */
    public void setJr(Journals jr) {
        this.jr = jr;
    }

    /**
     * @return the cf
     */
    public Conf getCf() {
        return cf;
    }

    /**
     * @param cf the cf to set
     */
    public void setCf(Conf cf) {
        this.cf = cf;
    }
}
