/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control; 

import TableStrcuture.Faculty;
import TableStrcuture.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Date;

/**
 *
 * @author chintanpanchamia
 */
public class ButtonEvents {
    static PreparedStatement st = null;
    public static Student getProfileStudent(String student_id) throws SQLException
    {
        Student s = new Student();
        
        s.setStudent_id(student_id);
        //Statement st = LibrarySystem.connection.createStatement();
        st = LibrarySystem.connection.prepareCall("Select * from student where student_id = ?");
        st.setString(1, student_id);
        
        ResultSet rs = st.executeQuery();
        
        s.setStudent_id(rs.getString("student_id"));
        s.setFirst_name(rs.getString("first_name"));
        s.setLast_name(rs.getString("last_name"));
        s.setPhone(rs.getString("phone"));
        s.setAlternate_phone(rs.getString("alternate_phone"));
        s.setAddr_city(rs.getString("addr_city"));
        s.setAddr_zip(rs.getString("addr_zip"));
        s.setAddr_street(rs.getString("addr_street"));
        s.setDob(rs.getDate("dob"));
        s.setNationality(rs.getString("nationality"));
        s.setDepartment(rs.getString("department"));
        s.setClassfication_id(rs.getString("classification_id"));
        s.setAccount_balance(rs.getString("account_balance"));
        return s;
    }
    public static Faculty getProfileFaculty(String faculty_id) throws SQLException
    {
        Faculty f = new Faculty();             
        return f;
    }
    
    public static int validate_login(String id,String password) throws SQLException
    {
        int status;      
        if(LibrarySystem.connection == null)
            System.out.println("NULL");
        st = LibrarySystem.connection.prepareStatement("Select 1 from student where student_id = ? and password = ?");
        System.out.println("validating");
        st.setString(1, id);
        st.setString(2,password);
        
        ResultSet rs = st.executeQuery();
        
        if(rs.next())
        {
            return 1;
        }
        else
        {
            st = LibrarySystem.connection.prepareCall("Select 1 from faculty where student_id = ? and password = ?");
            st.setString(1, id);
            st.setString(2,password);
        
            rs = st.executeQuery();
            
            if(rs.next())
            {
                return 1;
            }
        }
        return 0;
    }
}
