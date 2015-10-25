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
public class Books {
    private String isbn_no;
    private String title;
    private String edition;
    private String year_of_publication;
    private String publisher;

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
     * @return the edition
     */
    public String getEdition() {
        return edition;
    }

    /**
     * @param edition the edition to set
     */
    public void setEdition(String edition) {
        this.edition = edition;
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
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
