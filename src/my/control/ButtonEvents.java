package my.control;

import TableStrcuture.Faculty;
import TableStrcuture.Rooms;
import TableStrcuture.Reserve_room;
import TableStrcuture.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import TableStrcuture.Books;
import TableStrcuture.Camera;
import TableStrcuture.CameraCheckout;
import TableStrcuture.Conf;
import TableStrcuture.Journals;
import TableStrcuture.WaitlistCamera;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import my.dbproject.StudyRoomForm;

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

        Date date = new Date();
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
            st = LibrarySystem.connection.prepareStatement("Select id from patron where patron_id = ?");
            st.setString(1, LibrarySystem.login_id);
            rs = st.executeQuery();
            if (rs.next()) {
                LibrarySystem.patron_id = rs.getInt(1);
            }
            return 1;
        } else {
            st = LibrarySystem.connection.prepareCall("Select FACULTY_ID from faculty where faculty_id = ? and password = ?");
            st.setString(1, id);
            st.setString(2, password);

            rs = st.executeQuery();

            if (rs.next()) {
                LibrarySystem.login_id = rs.getString(1);
                LibrarySystem.patron_type = LibrarySystemConst.FACULTY;
                st = LibrarySystem.connection.prepareStatement("Select id from patron where patron_id = ?");
                st.setString(1, LibrarySystem.login_id);
                rs = st.executeQuery();
                if (rs.next()) {
                    LibrarySystem.patron_id = rs.getInt(1);
                }
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
        
        query = "update books set " + set_clause + "= ? where ISBN_NO = ?";

        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableBooks(book_detail.getIsbn_no(), set_clause));
        st.setString(2, book_detail.getIsbn_no());

        if (st.execute()) {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME) values(?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(book_detail.getIsbn_no()));
            st.setInt(2, LibrarySystem.patron_id);
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            st.setString(4, library_name);

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
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME) values(?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(journal_detail.getIssn_no()));
            st.setInt(2, LibrarySystem.patron_id);
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            st.setString(4, library_name);
            
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
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME) values(?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(conf_detail.getConfnum()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            st.setString(4, library_name);
            
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

    public static void validateRoomType(String type){
        
        if(LibrarySystem.patron_type.equalsIgnoreCase("student")&&type.equalsIgnoreCase("conference")){
        JOptionPane.showMessageDialog(null, "Students cannot book conference rooms, Please choose another room.");
        
        }
    }
    
    public static void reserve_room(){}
    
    public static ArrayList<Rooms> getRoom(String lib_name, int capacity, String type, Timestamp start, Timestamp end) throws SQLException 
    {
        Rooms r = new Rooms();
        Reserve_room rr = new Reserve_room();
        
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("Select R.room_no, R.floor_no, R.type, R.capacity,R.lib_name from rooms R where R.capacity= ? and R.type=? and R.lib_name= ? MINUS (Select R.room_no, R.floor_no, R.type, R.capacity,R.lib_name from rooms R, reserve_room R1 where R.room_no=R1.room_no and ((R1.start_time  <= ? and R1.end_time>= ? ) or (R1.start_time  <= ? and R1.end_time>= ? )))");
        //PreparedStatement stmnt = LibrarySystem.connection.prepareCall("Select R.room_no, R.floor_no, R.type, R.capacity,R.lib_name from rooms R, reserve_room R1 where R.capacity= ? and R.type=? and R.lib_name= ? MINUS (Select R.room_no, R.floor_no, R.type, R.capacity,R.lib_name from rooms R, reserve_room R1 where R.room_no=R1.room_no)");
               // and (R.start_time  <= ? and R.end_time>=?) or (R.start_time  <= ? and R.end_time>=?)");
      //System.out.println("query");
        ArrayList<Rooms> room = new ArrayList<>();
        
        stmnt.setInt(1, capacity);
        stmnt.setString(2, type);

        stmnt.setString(3, lib_name);
        stmnt.setTimestamp(4, start);
        stmnt.setTimestamp(5, start);
        stmnt.setTimestamp(6, end);
        stmnt.setTimestamp(7, end);
        
        ResultSet rs = stmnt.executeQuery();      
          System.out.println("query");
        while (rs.next()) {
            System.out.println(rs.getString("room_no"));
            r.setRoom_no(rs.getString("room_no"));
            r.setFloor_no(rs.getInt("floor_no"));
            r.setCapacity(rs.getInt("capacity"));
            r.setLib_name(rs.getString("lib_name"));
            r.setType(rs.getString("type"));
            room.add(r);
        }
        return room;
    }

    public static String waitlistCamera(String camera_id, Date date1) throws SQLException {

    //val can be student_id or faculty_id
        //LibrarySystem.login_id = "S1";
        //LibrarySystem.patron_id=5;
        Date date = new Date(System.currentTimeMillis());
        date=date1;
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        Timestamp tstamp = new Timestamp(zeroedDate.getTime());
        String str = "";
        
        st = LibrarySystem.connection.prepareStatement("Select * from waitlist_camera where request_time=? and patron_id=?");
        st.setTimestamp(1, tstamp);
        st.setInt(2, LibrarySystem.patron_id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            str = "You have already reserved this camera on this date, choose another";
        } else {

            //String str1 = "insert into waitlist_camera (patron_id,camera_id, id) values (?,?,?)";
            st = LibrarySystem.connection.prepareStatement("insert into waitlist_camera (patron_id,camera_id, request_time) values (?,?,?)");
         //Statement statement = LibrarySystem.connection.createStatement();

            //statement.execute(str1);            
            st.setInt(1, LibrarySystem.patron_id);
            st.setString(2, camera_id);
            st.setTimestamp(3, tstamp);
            //st.setTimestamp(1, tstamp1);
            //st.setInt(3, 1);
            //System.out.println(str1);
            try {
                st.executeUpdate();
                str = "Request is accpeted";

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
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

    public static String camera_notify() throws SQLException {
        //friday - add waitlist
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        Timestamp tstamp_tocheck = new Timestamp(zeroedDate.getTime());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        Timestamp tstamp_8 = tstamp_tocheck;

        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.setTime(date);
        zeroedDate = cal.getTime();
        Timestamp tstamp_10 = new Timestamp(zeroedDate.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.setTime(date);
        zeroedDate = cal.getTime();
        Timestamp tstamp_12 = new Timestamp(zeroedDate.getTime());

        ArrayList<WaitlistCamera> w_c = new ArrayList<WaitlistCamera>();

        st = LibrarySystem.connection.prepareStatement("Select * from waitlist_camera where request_time=? order by id");
        st.setTimestamp(1, tstamp_tocheck);
        int status = 0;
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            status = 1;
            WaitlistCamera wc = new WaitlistCamera();
            wc.setRequest_time(rs.getTimestamp("request_time"));
            wc.setCamera_id(rs.getString("camera_id"));
            wc.setPatron_id(rs.getInt("patron_id"));
            w_c.add(wc);

        }
        String str = "";
        if (status == 0) {
            str = "No reservation for camera";
            return str;
        }
        try {
            if (tstamp_current.after(tstamp_8) && tstamp_current.before(tstamp_10)) {

                if (w_c.get(0).getId() == LibrarySystem.patron_id) {

                    LibrarySystem.camera_id = w_c.get(0).getCamera_id();
                    return "Ready for Hold";

                } else {
                    return "Res not available between 8 to 10. It might be available between 10 to 12";
                }

            } else if (tstamp_current.after(tstamp_10) && tstamp_current.before(tstamp_12)) {
                if (w_c.get(1).getId() == LibrarySystem.patron_id) {

                    LibrarySystem.camera_id = w_c.get(1).getCamera_id();
                    return "Ready for hold";

                } else {
                    return "Res not available between 10 to 12. It wont be available anymore";

                }
            } else {
                str = "You cant checkout the camera, time passed";
                return str;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array is out of Bounds" + e);
            str = "You are not in waitlist";
            return str;

        }

        //st1 = LibrarySystem.connection.prepareStatement("insert into camera_checkout (patron_id,camera_id, start_time, end_time,checkout)"
        //              +"values ("+id+","+camera_id+","+ts1+","+tstamp1+","+tstamp+")");
        //return str;s
    }

    public static String camera_hold() throws SQLException {
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp1 = new Timestamp(date.getTime());

        st = LibrarySystem.connection.prepareStatement("insert into camera_checkout (patron_id,camera_id, checkout_time) values (?,?,?)");
        st.setInt(1, LibrarySystem.patron_id);
        st.setString(2, LibrarySystem.camera_id);
        st.setTimestamp(3, tstamp1);
        String str = "";
//        System.out.println();
        if (st.executeUpdate() == 1) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.setTime(date);
            Date zeroedDate = cal.getTime();
            Timestamp tstamp = new Timestamp(zeroedDate.getTime());

            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera where request_time<?");
            st.setTimestamp(1, tstamp);
            try{
            st.executeUpdate();}
            catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera where camera_id=? and request_time=?");
            st.setString(1, LibrarySystem.camera_id);
            st.setTimestamp(2, tstamp);
            try{
            st.executeUpdate();}
            catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
        }

        str = "Camera is on hold. Pick it up before 12";
        return str;
    }

    public static String camera_pickup() throws SQLException {
        String str = "";
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTime(date);
        Date zeroedDate = cal.getTime();

        Timestamp tstamp12 = new Timestamp(zeroedDate.getTime());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.setTime(date);
        zeroedDate = cal.getTime();
        Timestamp tstamp8 = new Timestamp(zeroedDate.getTime());
        if (tstamp_current.after(tstamp8) && tstamp_current.before(tstamp12)) {

            st = LibrarySystem.connection.prepareStatement("update camera_checkout set start_time = ? where patron_id =? and camera_id = ?");
            st.setInt(2, LibrarySystem.patron_id);
            st.setString(3, LibrarySystem.camera_id);
            st.setTimestamp(1, tstamp_current);
            st.executeUpdate();
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
        }

        return str;
    }

    public static ArrayList<Camera> camera_resources() throws SQLException 
    {
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        Camera c;

        ArrayList<Camera> cameras = new ArrayList<Camera>();

        st = LibrarySystem.connection.prepareStatement("Select * from camera_checkout where patron_id =? and start_time<?");
        st.setInt(1, LibrarySystem.patron_id);
        st.setTimestamp(2,tstamp_current );
        ResultSet rs = st.executeQuery();
        Timestamp tst = null;
        while (rs.next()){
            c = new Camera();
            PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from camera where camera_id=?");
            st1.setString(1,rs.getString("camera_id"));
            ResultSet rs1=st.executeQuery();
            if (rs1.next()){
                c.setCamera_id(rs1.getString("camera_id"));
                c.setModel(rs1.getString("model"));
                cameras.add(c);
            }
  
        }
      
        return cameras;
    }
    public static ArrayList<Camera> camera_holdresources() throws SQLException 
    {
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        Camera c;

        ArrayList<Camera> cameras = new ArrayList<Camera>();
        if (LibrarySystem.camera_id!=null){
          
            c = new Camera();
            PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from camera where camera_id=?");
            st1.setString(1,LibrarySystem.camera_id);
            ResultSet rs1=st.executeQuery();
            if (rs1.next()){
                c.setCamera_id(rs1.getString("camera_id"));
                c.setModel(rs1.getString("model"));
                cameras.add(c);
            }
  
        }
        
      
        return cameras;
    }
    
    public static String camera_return(String camera_id) throws SQLException {
        String str = "";
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp_current = new Timestamp(date.getTime());
                Timestamp end_time;

        st = LibrarySystem.connection.prepareStatement("Select * from camera_checkout where patron_id =? and camera_id=?");
        st.setInt(1, LibrarySystem.patron_id);
        st.setString(2, LibrarySystem.camera_id);
        ResultSet rs = st.executeQuery();
        Timestamp tst = null;
        if (rs.next()){
            tst=rs.getTimestamp("start_time");
            //tst.getTime()+
        }

        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(tst.getTime());
        cal.add(Calendar.DAY_OF_YEAR,6);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        end_time=new Timestamp(cal.getTimeInMillis());
        System.out.println(end_time.toString());
        long hours = (tstamp_current.getTime() - end_time.getTime())/(1000*60*60);
        
        st = LibrarySystem.connection.prepareStatement("Select * from late_fee where resource_type =camera");
        rs = st.executeQuery();
        if (rs.next()){
            
        }
        return str;
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
    
    public static int return_resource(String resource_type,int p_id,String id,Date start_time)throws SQLException
    {
        int hours;
        int late_fee;
        int fees;
        String query = null;
        String table_name = null;
        String set_clause;
        String where_col = null;
        String library_name = null;
        LibrarySystem.connection.setAutoCommit(false);
        int avail_no = 0;
        Calendar cal = Calendar.getInstance();
        
        library_name = LibraryAPI.getLibraryName(p_id,LibrarySystem.patron_id); 
        
        if (library_name.equals(LibrarySystemConst.HUNT)) 
        {
            set_clause = "HUNT_AVAIL_NO";
        } 
        else 
        {
            set_clause = "HILL_AVAIL_NO";
        }
        
        switch(resource_type)
        {
            case LibrarySystemConst.BOOK:
                table_name = "BOOKS";
                where_col = "ISBN_NO";
                avail_no = LibraryAPI.getAvailableBooks(id, set_clause);
                break;
            case LibrarySystemConst.JOURNAL:
                table_name = "JOURNALS";
                where_col = "ISSN_NO";
                avail_no = LibraryAPI.getAvailableJournals(id, set_clause);
                break;
            case LibrarySystemConst.CONFERENCE:
                table_name = "CONF";
                where_col = "CONF_NUM";
                avail_no = LibraryAPI.getAvailableConf(id, set_clause);
                break;
            default:
                return -1;
        }                              
        
        query = "update table " + table_name + " set " + set_clause + " = ? where " + 
                where_col + " = ? ";
        
        st = LibrarySystem.connection.prepareStatement(query);
        
        st.setInt(1, avail_no);
        st.setString(2, id);       
        
        if(!st.execute())
        {
            return -1;
        }
        
        cal.setTime(start_time);
        cal.add(Calendar.HOUR,LibraryAPI.getDuration(resource_type, resource_type));
        Date end_time = new Date(System.currentTimeMillis());
        
        /* Update the check out book table */
        query = "update table checkout set END_TIME = ? where PUBLICATION_ID = ? and PATRON_ID = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setDate(1, new java.sql.Date(end_time.getTime()));
        st.setInt(2, p_id);
        st.setInt(3, LibrarySystem.patron_id);
        
        if(!st.execute())
        {
            return -1;
        }
        
        /* Calucate the late fee charge */
        query = "select FEES,FREQUENCY_HOURS from late_fee where resource_type = ? order by frequency_hours_number asc";
        st = LibrarySystem.connection.prepareStatement(query);
        
        st.setString(1, resource_type);
        
        ResultSet rs = st.executeQuery();
        
        if(rs.next())
        {
            fees = rs.getInt(1);
            hours = rs.getInt(2);
        }
        else
        {
            fees = 2;
            hours = 1;
        }
        
        long diffInMillies = end_time.getTime() - start_time.getTime();
        long no_of_hours = (diffInMillies)/(1000*60);
        
        late_fee = (int)LibraryAPI.getLateFees(hours,fees,no_of_hours);
        
        LibraryAPI.updateBalance(getBalance() - late_fee);
        
        return 1;
    }
}
