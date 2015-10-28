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
public class CameraCheckout {
    private int patron_id;
    private String camera_id;
    private Date start_time;
    private Date end_time;
    private Date checkout;
    private Date due_time;

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
     * @return the camera_id
     */
    public String getCamera_id() {
        return camera_id;
    }

    /**
     * @param camera_id the camera_id to set
     */
    public void setCamera_id(String camera_id) {
        this.camera_id = camera_id;
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
     * @return the checkout
     */
    public Date getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the due_time
     */
    public Date getDue_time() {
        return due_time;
    }

    /**
     * @param due_time the due_time to set
     */
    public void setDue_time(Date due_time) {
        this.due_time = due_time;
    }

}
