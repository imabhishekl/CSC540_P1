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
import TableStrcuture.Conf;
import TableStrcuture.Journals;
import TableStrcuture.Patron;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;

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
    
    public static int checkout_books(Books book_detail,String library_name)throws SQLException
    {
        String query = null;
        String set_clause;
        
        if(library_name.equals(LibrarySystemConst.HUNT))
        {
            set_clause = "HUNT_AVAIL_NO";
        }
        else
        {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);
        
        query = "update books set " + set_clause + "= ? where ISBN_NO = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableBooks(book_detail.getIsbn_no(), set_clause));
        st.setString(2, book_detail.getIsbn_no());
        
        if(st.execute())
        {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME) values(?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(book_detail.getIsbn_no()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            
            if(st.execute())
            {
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            }
        }
        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }

    public static int checkout_journal(Journals journal_detail,String library_name)throws SQLException
    {
        String query = null;
        String set_clause;
        
        if(library_name.equals(LibrarySystemConst.HUNT))
        {
            set_clause = "HUNT_AVAIL_NO";
        }
        else
        {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);
        
        query = "update journals set " + set_clause + "= ? where ISSN_NO = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableJournals(journal_detail.getIssn_no(), set_clause));
        st.setString(2, journal_detail.getIssn_no());
        
        if(st.execute())
        {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME) values(?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(journal_detail.getIssn_no()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            
            if(st.execute())
            {
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            }
        }
        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }
    
    public static int checkout_conf(Conf conf_detail,String library_name)throws SQLException
    {
        String query = null;
        String set_clause;
        
        if(library_name.equals(LibrarySystemConst.HUNT))
        {
            set_clause = "HUNT_AVAIL_NO";
        }
        else
        {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);
        
        query = "update conf set " + set_clause + "= ? where CONF_NUM = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableConf(conf_detail.getConfnum(), set_clause));
        st.setString(2, conf_detail.getConfnum());
        
        if(st.execute())
        {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME) values(?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(conf_detail.getConfnum()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            
            if(st.execute())
            {
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
        
        ArrayList<Books> bookslist = new ArrayList<>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from BOOKS where hunt_total_no > 0 or hill_total_no > 0");
        
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
                
                book.setAuthor_list(LibraryAPI.getAuthorList(book.getGroup_id()));
                
                bookslist.add(book);
                
            }
        }                
        return bookslist;
    }
    
    public static ArrayList<Conf> get_conference() throws SQLException
    {            
        Conf conf;
        
        ArrayList<Conf> conf_list = new ArrayList<>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from conf where hunt_total_no > 0 or hill_total_no > 0");
        
        try (ResultSet rs = st.executeQuery()) {
            while(rs.next())
            {
                conf = new Conf();
                
                conf.setConfnum(rs.getString("CONF_NUM"));
                conf.setConfname(rs.getString("CONF_NAME"));
                conf.setTitle(rs.getString("TITLE"));
                conf.setYear(rs.getInt("YEAR"));
                conf.setHunt_avail_no(rs.getInt("HUNT_AVAIL_NO"));
                conf.setHunt_total_no(rs.getInt("HUNT_TOTAL_NO"));
                conf.setHill_total_no(rs.getInt("HILL_AVAIL_NO"));
                conf.setHill_avail_no(rs.getInt("HILL_TOTAL_NO"));
                conf.setE_copy(rs.getString("E_COPY"));
                conf.setGroup_id(rs.getInt("GROUP_ID"));
                
                conf.setAuthor_list(LibraryAPI.getAuthorList(conf.getGroup_id()));
                
                conf_list.add(conf);               
            }
        }                
        return conf_list;
    }

    public static ArrayList<Journals> get_journal() throws SQLException
    {            
        Journals journal;
        
        ArrayList<Journals> journal_list = new ArrayList<>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from journals where hunt_total_no > 0 or hill_total_no > 0");
        
        try (ResultSet rs = st.executeQuery()) {
            while(rs.next())
            {
                journal = new Journals();
                
                journal.setIssn_no(rs.getString("ISSN_NO"));
                journal.setYear_of_publication(rs.getInt("YEAR_OF_PUBLICATION"));
                journal.setTitle(rs.getString("TITLE"));
                journal.setHunt_avail_no(rs.getInt("HUNT_AVAIL_NO"));
                journal.setHunt_total_no(rs.getInt("HUNT_TOTAL_NO"));
                journal.setHill_total_no(rs.getInt("HILL_AVAIL_NO"));
                journal.setHill_avail_no(rs.getInt("HILL_TOTAL_NO"));
                journal.setE_copy(rs.getString("E_COPY"));
                journal.setGroup_id(rs.getInt("GROUP_ID"));
                
                journal.setAuthor_list(LibraryAPI.getAuthorList(journal.getGroup_id()));
                
                journal_list.add(journal);               
            }
        }                
        return journal_list;
    }
    
    public static Rooms getRoom(String lib_name,int capacity, String type) throws SQLException
    {
        Rooms r = new Rooms();
        Reserve_room rr=new Reserve_room();
        
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from room where lib_name= ?");
        st.setString(1, lib_name);
        
        ResultSet rs = st.executeQuery();
        
        return r;
            
      
    }
    
    public static int waitlistCamera(String camera_id, Date date, String val) throws SQLException
    {
        //val can be student_id or faculty_id
        Patron p = new Patron();
        PreparedStatement st1 = null;
        int a;
        Timestamp tstamp1 = new Timestamp(date.getTime());

        //patron id cant come from patron table as dependent on other resources.
        st1 = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id="+val);
        ResultSet rs1 = st1.executeQuery();
        a = rs1.getInt("id");
        
        st = LibrarySystem.connection.prepareStatement("Select * from waitlist_camera where request_time="+tstamp1+" and patron_id="+a);
        
        ResultSet rs = st.executeQuery();
            if (!rs.next())
            {
                System.out.println("You have already reserved this camera on this date, choose another");
            }
            else
            {
               
                Calendar cal= Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 8);            
                cal.set(Calendar.MINUTE, 0);                 
                cal.set(Calendar.SECOND, 0);                 
                cal.set(Calendar.MILLISECOND, 0); 
                cal.setTime(date);
                Date zeroedDate = cal.getTime();
                Timestamp tstamp = new Timestamp(zeroedDate.getTime());                
                
                //here id needs to be autonumber in the database design; or will need to keep a counter and a query needs to be written
                Statement statement = LibrarySystem.connection.createStatement();
                statement.execute("insert into waitlist_camera (patron_id,camera_id, request_time,message_sent)"
                        +"values ("+a+","+camera_id+","+","+tstamp1+","+tstamp+")");
                
            }
        return 1;

    }
    public static ArrayList<Camera> displayCameras() throws SQLException
    {
        Camera c;
        
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        
        st = LibrarySystem.connection.prepareStatement("Select * from camera");
       
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
    
    public static int check_outcamera(String camera_id, String student_id) throws SQLException
    {
                
        st = LibrarySystem.connection.prepareStatement("Select * from patron where patron_id="+student_id);
        ResultSet rs = st.executeQuery();
        int a = rs.getInt("id");
        PreparedStatement st1 = null;
        Date date=new Date();
        Timestamp ts1 = new Timestamp(date.getTime());
        //st1 = LibrarySystem.connection.prepareStatement("insert into camera_checkout (patron_id,camera_id, start_time, end_time,due_time,checkout)"
          //              +"values ("+id+","+camera_id+","+ts1+","+tstamp1+","+tstamp+")");


        
        
        
        return 1;
    }

    public static int getBalance() throws SQLException
    {
        st = LibrarySystem.connection.prepareStatement("Select patron_type from patron where patron_id ="+student_id);
        ResultSet rs = st.executeQuery();
        
        String patron_type = rs.getString("patron_type");
        PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select account_balance from "+patron_type+" where "+patron_type+"_id = "+student_id);
        int a = Integer.parseInt(rs.getString("account_balance"));
        return a;
    }
    
    public static int return_resource(String resource_type)throws SQLException
    {
        String query = null;
        String table_name = null;
        String set_clause;
        String where_col = null;
        LibrarySystem.connection.setAutoCommit(false);
        
        //if()
        set_clause = "asd";
        
        switch(resource_type)
        {
            case LibrarySystemConst.BOOK:
                table_name = "BOOK";
                break;
            case LibrarySystemConst.JOURNAL:
                table_name = "JOURNAL";
                break;
            case LibrarySystemConst.CONFERENCE:
                table_name = "CONFERENCE";
                break;
            default:
                return -1;
        }        
        query = "update table " + table_name + " set " + set_clause + " = ? where " + 
                where_col + " = ?";
        
        return 1;
    }
}