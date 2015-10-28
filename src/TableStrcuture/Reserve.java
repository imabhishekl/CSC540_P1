/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;
import java.util.Date;

/**
 *
 * @author amrit
 */
public class Reserve {
    private String isbn_no;
    private String faculty_id;
    private Date start_time;
    private Date end_time;

    /**
     * @return the isbn_no
     */
    public String getIsbn_no() {
        return isbn_no;
    }

    /**
     * @param isbn_no the isbn_no to set
     */
    public void setIsbn_no(String isbn_no) {
        this.isbn_no = isbn_no;
    }

    /**
     * @return the faculty_id
     */
    public String getFaculty_id() {
        return faculty_id;
    }

    /**
     * @param faculty_id the faculty_id to set
     */
    public void setFaculty_id(String faculty_id) {
        this.faculty_id = faculty_id;
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

    
}
