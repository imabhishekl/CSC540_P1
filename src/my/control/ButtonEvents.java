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
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author chintanpanchamia
 */
public class ButtonEvents {
    public static Student getProfileStudent(String student_id) throws SQLException
    {
        Student s = new Student();
        
        s.setStudent_id(student_id);
        //Statement st = LibrarySystem.connection.createStatement();
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from student where student_id = ?");
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
    public static Faculty getProfileFaculty(String ) throws SQLException
    {
        Faculty f = new Faculty();
        
        s.setStudent_id(stud_id);
        //Statement st = LibrarySystem.connection.createStatement();
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from student where student_id = ?");
        st.setString(1, stud_id);
        
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
        return f;
    }
}
