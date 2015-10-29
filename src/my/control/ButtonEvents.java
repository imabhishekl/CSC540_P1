package my.control;

import TableStrcuture.Camera;
import TableStrcuture.Faculty;
import TableStrcuture.Rooms;
import TableStrcuture.Reserve_room;
import TableStrcuture.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import TableStrcuture.Books;
import TableStrcuture.Patron;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class ButtonEvents {

    static PreparedStatement st = null;
    public static Student s;
    public static String student_id;

    public static Student getProfileStudent(String student_id) throws SQLException {
        s = new Student();

        s.setStudent_id(student_id);
        //Statement st = LibrarySystem.connection.createStatement();
        st = LibrarySystem.connection.prepareStatement("Select * from student where student_id = ?");
        st.setString(1, student_id);

        ResultSet rs = st.executeQuery();
        if (rs.next()) {

            s.setStudent_id(rs.getString("student_id"));
            s.setFirst_name(rs.getString("first_name"));
            s.setLast_name(rs.getString("last_name"));
            s.setPhone(rs.getString("phone"));
            s.setAlternate_phone(rs.getString("alternate_phone"));
            s.setAdd_city(rs.getString("add_city"));
            s.setAdd_zip(rs.getString("add_zip"));
            s.setAdd_street(rs.getString("add_street"));
            s.setAdd_state(rs.getString("add_state"));
            s.setSex(rs.getString("sex"));
            s.setDob(rs.getDate("dob"));
            s.setNationality(rs.getString("nationality"));
            s.setDepartment(rs.getString("department"));
            s.setClassification_id(rs.getString("classification_id"));
            s.setAccount_balance(rs.getString("account_balance"));
        }

        return s;
    }

    public static Faculty getProfileFaculty(String faculty_id) throws SQLException {

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

    public static int validate_login(String id, String password) throws SQLException {
        int status;

        student_id = id;
        st = LibrarySystem.connection.prepareStatement("Select 1 from student where student_id = ? and password = ?");
        st.setString(1, id);
        st.setString(2, password);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return 1;
        } else {
            st = LibrarySystem.connection.prepareCall("Select 1 from faculty where faculty_id = ? and password = ?");
            st.setString(1, id);
            st.setString(2, password);

            rs = st.executeQuery();

            if (rs.next()) {
                return 1;
            }
        }
        return 0;
    }


    public static ArrayList<Books> get_books() throws SQLException
    {
        int status;  
        
        Books book;
        
        ArrayList<Books> bookslist = new ArrayList<Books>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from books where hunt_total_no > 0 or hill_total_no > 0");
        
        try (ResultSet rs = st.executeQuery()) {
            while(rs.next())
            {
                book = new Books();
                
                book.setIsbn_no(rs.getString("isbn_no"));
                book.setTitle(rs.getString("title"));
                book.setEdition(rs.getString("edition"));
                book.setYear_of_publication(rs.getInt("year_of_publication"));
                book.setPublisher(rs.getString("publisher"));
                book.setHunt_avail_no(rs.getInt("hunt_avail_no"));
                book.setHunt_total_no(rs.getInt("hunt_total_no"));
                book.setHill_avail_no(rs.getInt("hill_avail_no"));
                book.setHill_total_no(rs.getInt("hill_total_no"));
                book.setE_copy(rs.getString("e_copy"));
                book.setGroup_id(rs.getInt("group_id"));
                
                bookslist.add(book);
                
            }
        }
        
        
        return bookslist;

    }

    
    
    public static Rooms getRoom(String lib_name,int Capacity, String type) throws SQLException
    {

        Rooms r = new Rooms();
        Reserve_room rr=new Reserve_room();
        
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from room where lib_name= ?");
        st.setString(1, lib_name);
        
        ResultSet rs = st.executeQuery();
        
        return r;
            
      
    }
    
    public static int waitlistCamera(String camera_id, Date time, String val) throws SQLException
    {
        //val can be student_id or faculty_id
        Patron p = new Patron();
        PreparedStatement st1 = null;
        int a;
        //patron id cant come from patron table as dependent on other resources.
        st1 = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id=val");
        ResultSet rs1 = st.executeQuery();
        a = rs1.getInt("id");
        
        st = LibrarySystem.connection.prepareStatement("Select * from waitlist_camera where request_time=time and patron_id=a");
        
        System.out.println(st);
       
        ResultSet rs = st.executeQuery();
            if (!rs.next())
            {
                System.out.println("You have already reserved this camera on this date, choose another");
            }
            else
            {
                //here id needs to be autonumber in the database design; or will need to keep a counter and a query needs to be written
                Statement statement = LibrarySystem.connection.createStatement();
                statement.execute("insert into waitlist_camera"+"(patron_id,camera_id,request_time,message_sent)"
                        +"values ("+a+","+camera_id+","+time+","+time);
                
            }
        return 1;

    }
    public static ArrayList<Camera> displayCameras() throws SQLException
    {
        Camera c;
        
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from camera");
        System.out.println(st);
       
        try (ResultSet rs = st.executeQuery()) {
            while(rs.next())
            {
                c = new Camera();
                c.setCamera_id(rs.getString("camera_id"));
                c.setModel(rs.getString("model"));
                c.setLens(rs.getString("lens"));
                c.setMemory_available(rs.getString("memory_available"));
                c.setMake(rs.getString("make"));
                c.setLib_name(rs.getString("lib_name"));
                
                
                cameras.add(c);
                
            }
        }
        return cameras;

    }
    
}




    


