/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;
import java.util.Date;
import java.sql.Timestamp;
/**
 *
 * @author amrit
 */
public class CameraCheckout {
    private int patron_id;
    private String camera_id;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp checkout;
    private Timestamp due_time;

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
    public Timestamp getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
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

    /**
     * @return the due_time
     */
    public Timestamp getDue_time() {
        return due_time;
    }

    /**
     * @param due_time the due_time to set
     */
    public void setDue_time(Timestamp due_time) {
        this.due_time = due_time;
    }

    
}
