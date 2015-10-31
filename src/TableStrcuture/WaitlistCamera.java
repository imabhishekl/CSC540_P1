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
public class WaitlistCamera {
    private int patron_id;
    private String camera_id;
    private int id;
    private Timestamp request_time;

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
     * @return the request_time
     */
    public Timestamp getRequest_time() {
        return request_time;
    }

    /**
     * @param request_time the request_time to set
     */
    public void setRequest_time(Timestamp request_time) {
        this.request_time = request_time;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    
}
