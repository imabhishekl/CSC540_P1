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
import java.sql.Timestamp;

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
        f.setNationality(rs.getString("nationality"));
        f.setDepartment(rs.getString("department"));
        f.setCategory(rs.getString("category"));
        f.setAccount_balance(rs.getString("account_balance"));
        return f;

    }

    public static int validate_login(String id, String password) throws SQLException {

        student_id = id;
        st = LibrarySystem.connection.prepareStatement("Select STUDENT_ID from student where student_id = ? and password = ?");
        st.setString(1, id);
        st.setString(2, password);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            LibrarySystem.login_id = rs.getString(1);
            LibrarySystem.patron_type = LibrarySystemConst.STUDENT;
            return 1;
        } else {
            st = LibrarySystem.connection.prepareCall("Select FACULTY_ID from faculty where faculty_id = ? and password = ?");
            st.setString(1, id);
            st.setString(2, password);

            rs = st.executeQuery();

            if (rs.next()) {
                LibrarySystem.login_id = rs.getString(1);
                LibrarySystem.patron_type = LibrarySystemConst.FACULTY;
                return 1;
            }
        }
        return 0;
    }

    public static int checkout_books(Books book_detail, String library_name) throws SQLException {
        String query = null;
        String set_clause;

        if (library_name.equals(LibrarySystemConst.HUNT)) {
            set_clause = "HUNT_AVAIL_NO";
        } else {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);

        query = "update books set " + set_clause + "= ? where isbn = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableBooks(book_detail.getIsbn_no(), set_clause));
        st.setString(2, book_detail.getIsbn_no());

        if (st.execute()) {
            /* Update the checkout_books */
            query = "insert into checkout_books (PUBLICATION_ID,PATRON_ID,START_TIME) values(?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(book_detail.getIsbn_no()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));

            if (st.execute()) {
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            }
        }
        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }


    public static ArrayList<Books> get_books() throws SQLException
    {
        int status;  
            
        Books book;

        ArrayList<Books> bookslist = new ArrayList<Books>();

        st = LibrarySystem.connection.prepareStatement("Select * from books where hunt_total_no > 0 or hill_total_no > 0");

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
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

                
                book.setAuthor_list(LibraryAPI.getAuthorList(book.getGroup_id()));
                                bookslist.add(book);

            }
        }

        return bookslist;

    }

    public static Rooms getRoom(String lib_name, int capacity, String type,Timestamp start, Timestamp end) throws SQLException {
        
        Rooms r = new Rooms();
        Reserve_room rr = new Reserve_room();
        PreparedStatement st = LibrarySystem.connection.prepareCall("select * from rooms where capacity= ? and type=? and lib_name= ?  ");
        st.setInt(1, capacity);
        st.setString(2, type);

        st.setString(3, lib_name);
        ResultSet rs = st.executeQuery();      
        while (rs.next()) {
            
            System.out.println(rs.getString("room_no"));
        }
        return r;

    }


    public static String waitlistCamera(String camera_id, Date date) throws SQLException {
        //val can be student_id or faculty_id
        Patron p = new Patron();
        PreparedStatement st1 = null;
        int a = 0;
        Timestamp tstamp1 = new Timestamp(date.getTime());
        LibrarySystem.login_id = "S1";
        st = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id=?");
        st.setString(1, LibrarySystem.login_id);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            a = rs.getInt("id");
        }
        st = LibrarySystem.connection.prepareStatement("Select * from waitlist_camera where request_time=? and patron_id=?");
        st.setTimestamp(1, tstamp1);
        st.setInt(2, a);
        String str = "";
        rs = st.executeQuery();
        if (rs.next()) {
            str = "You have already reserved this camera on this date, choose another";
        } else {

            /*Calendar cal= Calendar.getInstance();
             cal.setTime(date);
             cal.set(Calendar.HOUR_OF_DAY, 9);            
             cal.set(Calendar.MINUTE, 0);                 
             cal.set(Calendar.SECOND, 0);                 
             cal.set(Calendar.MILLISECOND, 0); 
             cal.setTime(date);
             Date zeroedDate = cal.getTime();
             Timestamp tstamp = new Timestamp(zeroedDate.getTime());                */
            //String str1 = "insert into waitlist_camera (patron_id,camera_id, id) values (?,?,?)";
            st = LibrarySystem.connection.prepareStatement("insert into waitlist_camera (patron_id,camera_id) values (?,?)");
            //Statement statement = LibrarySystem.connection.createStatement();
            
            //statement.execute(str1);            
            st.setInt(1, a);
            st.setString(2, camera_id);
            //st.setTimestamp(1, tstamp1);
            //st.setInt(3, 1);
            //System.out.println(str1);
            try {
                st.executeUpdate();
                str = "Request is accpeted";

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

                //LibrarySystem.connection.commit();
            //LibrarySystem.connection.setAutoCommit(true);

        }
        return str;

    }

    public static ArrayList<Camera> displayCameras() throws SQLException {
        Camera c;

        ArrayList<Camera> cameras = new ArrayList<Camera>();

        st = LibrarySystem.connection.prepareStatement("Select * from camera");

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
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

    public static int camera_notify(String camera_id) throws SQLException {
        Date date = new Date();
        Timestamp ts1 = new Timestamp(date.getTime());
        //System.out.println("hello");
        Patron p = new Patron();
        st = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id=?");
        st.setString(1, LibrarySystem.login_id);
        int a=0;
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            a = rs.getInt("id");
        }
        
        st = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id=" + student_id);
        rs = st.executeQuery();

        //st1 = LibrarySystem.connection.prepareStatement("insert into camera_checkout (patron_id,camera_id, start_time, end_time,checkout)"
        //              +"values ("+id+","+camera_id+","+ts1+","+tstamp1+","+tstamp+")");
        return 1;
    }

    public static int getBalance() throws SQLException {
        st = LibrarySystem.connection.prepareStatement("Select patron_type from patron where patron_id =?");
        st.setString(1, LibrarySystem.login_id);
        ResultSet rs = st.executeQuery();
        String patron_type = "";
        if (rs.next()) {
            patron_type = rs.getString("patron_type").toLowerCase();
        }

        st = LibrarySystem.connection.prepareStatement("Select account_balance from " + patron_type + " where " + patron_type + "_id = ?");
        st.setString(1, LibrarySystem.login_id);
        rs = st.executeQuery();
        if (rs.next()) {
            int a = rs.getInt("account_balance");

            return a;
        }
        return 1;

    }
}
