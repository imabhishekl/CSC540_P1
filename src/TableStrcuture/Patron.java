/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableStrcuture;

/**
 *
 * @author amrit
 */
public class Patron {
    private String patron_id;
    private String patron_type;
    private int id;

    /**
     * @return the patron_id
     */
    public String getPatron_id() {
        return patron_id;
    }

    /**
     * @param patron_id the patron_id to set
     */
    public void setPatron_id(String patron_id) {
        this.patron_id = patron_id;
    }

    /**
     * @return the patron_type
     */
    public String getPatron_type() {
        return patron_type;
    }

    /**
     * @param patron_type the patron_type to set
     */
    public void setPatron_type(String patron_type) {
        this.patron_type = patron_type;
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
