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
public class LateFee {
    private String resource_type;
    private int fees;
    private int frequency_hours;

    /**
     * @return the resource_type
     */
    public String getResource_type() {
        return resource_type;
    }

    /**
     * @param resource_type the resource_type to set
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    /**
     * @return the fees
     */
    public int getFees() {
        return fees;
    }

    /**
     * @param fees the fees to set
     */
    public void setFees(int fees) {
        this.fees = fees;
    }

    /**
     * @return the frequency_hours
     */
    public int getFrequency_hours() {
        return frequency_hours;
    }

    /**
     * @param frequency_hours the frequency_hours to set
     */
    public void setFrequency_hours(int frequency_hours) {
        this.frequency_hours = frequency_hours;
    }
}
