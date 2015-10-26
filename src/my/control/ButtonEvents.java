package my.control; 

import TableStrcuture.Faculty;
import TableStrcuture.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class ButtonEvents {
    static PreparedStatement st = null;
    public static Student s;
    public static String student_id;
    public static Student getProfileStudent(String student_id) throws SQLException
    {
        s = new Student();
        
        s.setStudent_id(student_id);
        //Statement st = LibrarySystem.connection.createStatement();
        st = LibrarySystem.connection.prepareStatement("Select * from student where student_id = ?");
        st.setString(1, student_id);
        
        ResultSet rs = st.executeQuery();
        if(rs.next())
        {
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
            s.setClassification_id(rs.getString("classification_id"));
            s.setAccount_balance(rs.getString("account_balance"));
        }
        return s;
    }
    public static Faculty getProfileFaculty(String faculty_id) throws SQLException
    {

        Faculty f = new Faculty();
        
        f.setFaculty_id(faculty_id);
        //Statement st = LibrarySystem.connection.createStatement();
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from faculty where faculty_id = ?");
        st.setString(1, faculty_id);
        
        ResultSet rs = st.executeQuery();
        
        f.setFaculty_id(rs.getString("faculty_id"));
        f.setFirst_name(rs.getString("first_name"));
        f.setLast_name(rs.getString("last_name"));
        //f.set(rs.getString("phone"));
        //s.setAlternate_phone(rs.getString("alternate_phone"));
        //f.setAddr_city(rs.getString("addr_city"));
        //f.setAddr_zip(rs.getString("addr_zip"));
        //f.setAddr_street(rs.getString("addr_street"));
        //f.setDob(rs.getDate("dob"));
        f.setNationality(rs.getString("nationality"));
        f.setDepartment(rs.getString("department"));
        f.setCategory(rs.getString("category"));
        //f.setClassfication_id(rs.getString("classification_id"));
        f.setAccount_balance(rs.getString("account_balance"));
        return f;
            
      
    }
    
    public static int validate_login(String id,String password) throws SQLException
    {
        int status;      
        
        student_id = id;
        st = LibrarySystem.connection.prepareStatement("Select 1 from student where student_id = ? and password = ?");
        st.setString(1, id);
        st.setString(2,password);
        
        ResultSet rs = st.executeQuery();
        
        if(rs.next())
        {
            return 1;
        }
        else
        {
            st = LibrarySystem.connection.prepareCall("Select 1 from faculty where faculty_id = ? and password = ?");
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
