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
public class Journals {
    private String issn_no;
    private String year_of_publication;
    private String title;

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
    public String getYear_of_publication() {
        return year_of_publication;
    }

    /**
     * @param year_of_publication the year_of_publication to set
     */
    public void setYear_of_publication(String year_of_publication) {
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
}
