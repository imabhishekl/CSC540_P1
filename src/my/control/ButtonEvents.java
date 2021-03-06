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
import TableStrcuture.CheckOut;
import TableStrcuture.Conf;
import TableStrcuture.Journals;
import TableStrcuture.WaitlistCamera;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class ButtonEvents {

    static PreparedStatement st = null;
    public static Student s;
    public static String student_id;

    public static Student getProfileStudent(String student_id) throws SQLException {
        s = new Student();

        s.setStudent_id(student_id);
        //Statement st = LibrarySystem.connection.createStatement();
        st = LibrarySystem.connection.prepareStatement("Select * from student where user_id = ?");
        st.setString(1, student_id);

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from classification where classification_id=?");
            st1.setInt(1, rs.getInt("classification_id"));
            ResultSet rs1 = st1.executeQuery();
            if (rs1.next()) {
                s.setClassification_name(rs1.getString("classification_name"));
                s.setDegree_program(rs1.getString("degree_program"));
                s.setYear(rs1.getInt("year"));
            }

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
            s.setAccount_balance(rs.getString("account_balance"));
        }

        return s;
    }

    public static Faculty getProfileFaculty(String faculty_id) throws SQLException {

        Date date = new Date();
        Faculty f = new Faculty();

        f.setFaculty_id(faculty_id);
        //Statement st = LibrarySystem.connection.createStatement();
        PreparedStatement st = LibrarySystem.connection.prepareCall("Select * from faculty where user_id = ?");
        st.setString(1, faculty_id);

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            f.setFaculty_id(rs.getString("faculty_id"));
            f.setFirst_name(rs.getString("first_name"));
            f.setLast_name(rs.getString("last_name"));
            f.setNationality(rs.getString("nationality"));
            f.setDepartment(rs.getString("department"));
            f.setCategory(rs.getString("category"));
            f.setAccount_balance(rs.getString("account_balance"));

        }
        return f;

    }

    public static int validate_login(String id, String password) throws SQLException {

        student_id = id;
        st = LibrarySystem.connection.prepareStatement("Select STUDENT_ID from student where user_id = ? and password = ?");
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
            st = LibrarySystem.connection.prepareCall("Select FACULTY_ID from faculty where user_id = ? and password = ?");
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
                st = LibrarySystem.connection.prepareStatement("select student_id from account_revoke where login_id = ?"); 
                st.setString(1, LibrarySystem.login_id);
                ResultSet rs1 = st.executeQuery();
                if(rs1.next())
                {
                    LibrarySystem.revoked_ind = 1;
                }
                else
                {
                    LibrarySystem.revoked_ind = 0;
                }
                return 1;
            }
        }
        return 0;
    }

    public static int checkout_books(Books book_detail, String library_name) throws SQLException {
        String query = null;
        String set_clause;
        Boolean flag = false;

        if (LibraryAPI.isBookAlreadyCheckedOut(LibrarySystem.patron_id, LibraryAPI.getPubllicationId(book_detail.getIsbn_no()))) {
            System.out.println("Already CheckedOut");
            return -2;
        }

        /*if(book_detail.getE_copy().equalsIgnoreCase("Y"))
         return 1;*/
        if (library_name.equals(LibrarySystemConst.HUNT)) {
            set_clause = "HUNT_AVAIL_NO";
        } else {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);

        query = "update books set " + set_clause + "= ? where ISBN_NO = ?";

        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableBooks(book_detail.getIsbn_no(), set_clause) - 1);
        st.setString(2, book_detail.getIsbn_no());

        if (book_detail.getE_copy().equalsIgnoreCase("Y")) {
            flag = true;
        } else {
            flag = (st.executeUpdate() != 0);
        }

        if (flag) {
            /* Update the checkout_books */
            System.out.println("upadted");
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME,E_COPY) values (?,?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);

            st.setInt(1, LibraryAPI.getPubllicationId(book_detail.getIsbn_no()));
            st.setInt(2, LibrarySystem.patron_id);
            st.setTimestamp(3, new Timestamp(new java.util.Date(System.currentTimeMillis()).getTime()));
            st.setString(4, library_name);
            st.setString(5, book_detail.getE_copy());
            if (st.executeUpdate() != 0) {
                System.out.println("Inserted in checkout");
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            } else {
                System.out.println("Error while insert into checkout");
            }
        }

        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }

    public static int checkout_journal(Journals journal_detail, String library_name) throws SQLException {
        String query = null;
        String set_clause;
        boolean flag;

        if (LibraryAPI.isAlreadyCheckedOut(LibrarySystem.patron_id, LibraryAPI.getPubllicationId(journal_detail.getIssn_no()))) {
            System.out.println("Already CheckedOut");
            return -2;
        }
        System.out.println("Reached 1");
        if (library_name.equals(LibrarySystemConst.HUNT)) {
            set_clause = "HUNT_AVAIL_NO";
        } else {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);

        query = "update journals set " + set_clause + "= ? where ISSN_NO = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableJournals(journal_detail.getIssn_no(), set_clause) - 1);
        st.setString(2, journal_detail.getIssn_no());

        System.out.println(journal_detail.getE_copy());

        if (journal_detail.getE_copy().equalsIgnoreCase("Y")) {
            flag = true;
        } else {
            flag = (st.executeUpdate() != 0);
        }

        if (flag) {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME,E_COPY) values(?,?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(journal_detail.getIssn_no()));
            st.setInt(2, LibrarySystem.patron_id);
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            st.setString(4, library_name);
            st.setString(5, journal_detail.getE_copy());

            if (st.executeUpdate() != 0) {
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            }
        }
        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }

    public static int checkout_conf(Conf conf_detail, String library_name) throws SQLException {
        String query = null;
        String set_clause;
        boolean flag;

        if (conf_detail.getE_copy().equalsIgnoreCase("Y")) {
            return 1;
        }

        if (library_name.equals(LibrarySystemConst.HUNT)) {
            set_clause = "HUNT_AVAIL_NO";
        } else {
            set_clause = "HILL_AVAIL_NO";
        }
        LibrarySystem.connection.setAutoCommit(false);

        query = "update conf set " + set_clause + "= ? where CONF_NUM = ?";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibraryAPI.getAvailableConf(conf_detail.getConfnum(), set_clause) - 1);
        st.setString(2, conf_detail.getConfnum());

        if (conf_detail.getE_copy().equalsIgnoreCase("Y")) {
            flag = true;
        } else {
            flag = (st.executeUpdate() != 0);
        }

        if (st.executeUpdate() != 0) {
            /* Update the checkout_books */
            query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME,E_COPY) values(?,?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);
            st.setInt(1, LibraryAPI.getPubllicationId(conf_detail.getConfnum()));
            st.setInt(2, LibraryAPI.getPatronId(LibrarySystem.login_id));
            st.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            st.setString(4, library_name);
            st.setString(5, conf_detail.getE_copy());

            if (st.executeUpdate() != 0) {
                LibrarySystem.connection.commit();
                LibrarySystem.connection.setAutoCommit(true);
                return 1;
            }
        }
        LibrarySystem.connection.rollback();
        LibrarySystem.connection.setAutoCommit(true);
        return -1;
    }

    public static ArrayList<Books> get_books() throws SQLException {
        Books book;

        ArrayList<Books> bookslist = new ArrayList<>();        
        
        if(LibrarySystem.patron_type.equalsIgnoreCase(LibrarySystemConst.STUDENT))
        {
            st = LibrarySystem.connection.prepareStatement
            ("select B.* from books B where B.isbn_no not in ( select R.isbn_no from reserve R, Courses_books C where R.isbn_no = C.isbn_no and C.course_id not in ( select E.course_id from enrollment E where E.student_id = ?) )");            
            st.setString(1, LibrarySystem.login_id);
        }
        else
        {
            st = LibrarySystem.connection.prepareStatement
            ("select * from books b where b.hunt_avail_no > 0 or b.hill_avail_no > 0");
        }        

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

    public static ArrayList<Conf> get_conference() throws SQLException {
        Conf conf;

        ArrayList<Conf> conf_list = new ArrayList<>();

        st = LibrarySystem.connection.prepareStatement("Select * from conf where hunt_total_no > 0 or hill_total_no > 0");

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
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

    public static ArrayList<Journals> get_journal() throws SQLException {
        Journals journal;

        ArrayList<Journals> journal_list = new ArrayList<>();

        st = LibrarySystem.connection.prepareStatement("Select * from journals where hunt_total_no > 0 or hill_total_no > 0");

        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
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

    public static void validateRoomType(String type) {

        if (LibrarySystem.patron_type.equalsIgnoreCase("student") && type.equalsIgnoreCase("conference")) {
            JOptionPane.showMessageDialog(null, "Students cannot book conference rooms, Please choose another room.");

        }
    }

    public static void print() throws Exception {

        System.out.println("print");
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("delete from reserve_room where start_time<= ?");
        stmnt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        ResultSet rs = stmnt.executeQuery();
        //System.out.println("query");
        while (rs.next()) {
            System.out.println("hi");
            //System.out.println(rs.getString("start_time"));}

        }
    }

    public static String room_notify() throws SQLException {
        Timestamp one_hour;
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("select * from reserve_room where patron_ID=? and start_time<=? and end_time>=?");
        stmnt.setInt(1, LibrarySystem.patron_id);
        stmnt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        stmnt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        ResultSet rs = stmnt.executeQuery();
        String notice = null;
        while (rs.next()) {
            /*Calendar calendar=Calendar.getInstance();
             calendar.setTimeInMillis(rs.getTimestamp("start_time").getTime());
             calendar.add(Calendar.HOUR,1);
       
             one_hour=new Timestamp(calendar.getTimeInMillis());*/
            if (System.currentTimeMillis() - rs.getTimestamp("start_time").getTime() >= (60 * 60 * 1000)) {
                JOptionPane.showMessageDialog(null, "Sorry your room " + rs.getString("room_no") + " is gone, try next time");
                return null;
            }
            System.out.println(rs.getString("room_no"));
            System.out.println(rs.getString("lib_name"));
            System.out.println(rs.getString("start_time"));
            System.out.println(rs.getString("end_time"));

            notice = "Your room " + rs.getString("room_no") + " at " + rs.getString("lib_name") + " library is ready to be checked out starting at " + rs.getTimestamp("start_time").toString();

        }
        return notice;
    }

    public static void reserve_room(String room_no, String library, Timestamp start, Timestamp end) throws Exception {

        //System.out.println(LibrarySystem.patron_id+" "+room_no+" "+library+" "+" "+start+" "+end);
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("insert into reserve_room(patron_ID,room_no,lib_name,start_time,end_time) values(?,?,?,?,?)");
        stmnt.setInt(1, LibrarySystem.patron_id);
        stmnt.setString(2, room_no);
        stmnt.setString(3, library);
        stmnt.setTimestamp(4, start);
        stmnt.setTimestamp(5, end);
        ResultSet rs = stmnt.executeQuery();
        LibrarySystem.connection.commit();
        LibrarySystem.connection.setAutoCommit(true);
        String message = "Congratulations! You have booked room " + room_no + " for " + start.toString() + " at the " + library + " library. Login at the start time to checkout!";
        //System.out.println("Done! Pooja you rock!!");
        JOptionPane.showMessageDialog(null, message);
    }

    public static ArrayList<Reserve_room> checkout_room() throws Exception {
        Timestamp one_hour;
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("select * from reserve_room where patron_ID=? and start_time<=? and end_time>=?");
        stmnt.setInt(1, LibrarySystem.patron_id);
        stmnt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        stmnt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        ResultSet rs = stmnt.executeQuery();
        //String notice=null;
        Reserve_room r = new Reserve_room();
        ArrayList<Reserve_room> room = new ArrayList<>();

        while (rs.next()) {

            if (System.currentTimeMillis() - rs.getTimestamp("start_time").getTime() >= (60 * 60 * 1000)) {
                //JOptionPane.showMessageDialog(null, "Sorry your room "+rs.getString("room_no")+ " is gone, try next time");
                return null;
            }
            r = new Reserve_room();
            System.out.println(rs.getString("room_no"));
            r.setRoom_no(rs.getString("room_no"));
            r.setLib_name(rs.getString("lib_name"));
            r.setStart_time(rs.getTimestamp("start_time"));
            room.add(r);

        }
        return room;
    }

    public static void update_checkout_room(String room_no, Timestamp start) throws Exception {
        PreparedStatement stmnt = LibrarySystem.connection.prepareCall("update reserve_room set checkout= ? where room_no= ? and patron_ID= ? and start_time= ? ");
        stmnt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        stmnt.setString(2, room_no);
        stmnt.setInt(3, LibrarySystem.patron_id);
        stmnt.setTimestamp(4, start);
        try {
            stmnt.executeUpdate();

        } catch (SQLException e) {

        }
        System.out.println("runs");
        LibrarySystem.connection.commit();
        LibrarySystem.connection.setAutoCommit(true);

    }

    public static ArrayList<Rooms> getRoom(String lib_name, int capacity, String type, Timestamp start, Timestamp end) throws SQLException {
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
            r = new Rooms();
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
        date = date1;
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

        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
        zeroedDate = cal.getTime();
        Timestamp tstamp_10 = new Timestamp(zeroedDate.getTime());
        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
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
                if (w_c.get(0).getPatron_id() == LibrarySystem.patron_id) {

                    LibrarySystem.camera_id = w_c.get(0).getCamera_id();
                    return "Ready for Hold";

                } else {
                    return "Res not available between 8 to 10. It might be available between 10 to 12";
                }

            } else if (tstamp_current.after(tstamp_10) && tstamp_current.before(tstamp_12)) {
                if (w_c.get(1).getPatron_id() == LibrarySystem.patron_id) {

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

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
        Date zeroedDate = cal.getTime();
        Timestamp tstamp_12 = new Timestamp(zeroedDate.getTime());
        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
        zeroedDate = cal.getTime();
        Timestamp tstamp_8 = new Timestamp(zeroedDate.getTime());
        String str = "";

        if (tstamp1.after(tstamp_8) && tstamp1.before(tstamp_12)) {

            LibrarySystem.hold = 1;
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.setTime(date);
            zeroedDate = cal.getTime();
            Timestamp tstamp = new Timestamp(zeroedDate.getTime());

            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera  where request_time < ?");
            st.setTimestamp(1, tstamp);
            System.out.println(tstamp.toString());
            try {

                //System.out.println(
                st.executeUpdate();

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera where camera_id=? and request_time=?");
            st.setString(1, LibrarySystem.camera_id);
            st.setTimestamp(2, tstamp);
            try {

                st.executeUpdate();
            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return "camera is on hold, pick it up before 12";

        } else if (tstamp1.after(tstamp_12)) {
            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera where request_time<?");
            st.setTimestamp(1, tstamp_12);
            try {
                st.executeUpdate();
            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return "camera cant be on hold anymore";
        }

        LibrarySystem.connection.commit();
        LibrarySystem.connection.setAutoCommit(true);
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
        Date zeroedDate = cal.getTime();

        Timestamp tstamp12 = new Timestamp(zeroedDate.getTime());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        zeroedDate = cal.getTime();
        Timestamp tstamp8 = new Timestamp(zeroedDate.getTime());
        /*LibrarySystem.hold=1;
         LibrarySystem.patron_id=1;
         LibrarySystem.camera_id="CA1";
         System.out.println(tstamp_current.toString());
         System.out.println(tstamp8.toString());
         System.out.println(tstamp12.toString());*/

        if (tstamp_current.after(tstamp8) && tstamp_current.before(tstamp12) && LibrarySystem.hold == 1) {
            st = LibrarySystem.connection.prepareStatement("insert into camera_checkout (patron_id,camera_id,start_time, checkout) values (?,?,?,?)");
            st.setInt(1, LibrarySystem.patron_id);
            st.setString(2, LibrarySystem.camera_id);
            st.setTimestamp(3, tstamp8);
            st.setTimestamp(4, tstamp_current);
            try {

                st.executeUpdate();
            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return "camera is checked out";

        } else if (tstamp_current.after(tstamp12)) {
            st = LibrarySystem.connection.prepareStatement("delete from waitlist_camera where request_time<?");
            st.setTimestamp(1, tstamp12);
            try {
                st.executeUpdate();
            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return "camera cant be checked out anymore";

        }
        LibrarySystem.camera_id = null;
        LibrarySystem.hold = 0;
        LibrarySystem.connection.commit();
        LibrarySystem.connection.setAutoCommit(true);
        return str;
    }

    public static ArrayList<Camera> camera_resources() throws SQLException {
        Date date = new Date(System.currentTimeMillis());

        Timestamp tstamp_current = new Timestamp(date.getTime());
        Camera c;
        //System.out.println(tstamp_current.toString());
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        //LibrarySystem.patron_id=1;
        st = LibrarySystem.connection.prepareStatement("Select * from camera_checkout where patron_id = ? and start_time< ?");
        st.setInt(1, LibrarySystem.patron_id);
        st.setTimestamp(2, tstamp_current);
        try {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {

                PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from camera where camera_id=?");
                st1.setString(1, rs.getString("camera_id"));
                ResultSet rs1 = st1.executeQuery();
                if (rs1.next()) {
                    c = new Camera();

                    c.setCamera_id(rs1.getString("camera_id"));
                    c.setModel(rs1.getString("model"));
                    cameras.add(c);
                }

            }
            //System.out.println(cameras);
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
        System.out.println(cameras);

        return cameras;
    }

    public static ArrayList<Camera> camera_holdresources() throws SQLException {
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        Camera c;

//System.out.println(tstamp_current.toString());
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        if (LibrarySystem.camera_id != null) {
            PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from camera where camera_id=?");
            st1.setString(1, LibrarySystem.camera_id);
            ResultSet rs1 = st1.executeQuery();
            if (rs1.next()) {
                c = new Camera();
                //System.out.println(rs1.getString("camera_id"));

                c.setCamera_id(rs1.getString("camera_id"));
                c.setModel(rs1.getString("model"));
                cameras.add(c);
                //System.out.println(rs1.getString("camera_id"));
            }

        }
        //System.out.println(" he" +cameras.get(0));
        return cameras;
    }

    public static String camera_return(String camera_id) throws SQLException {
        String str = "";
        Date date = new Date(System.currentTimeMillis());
        Timestamp tstamp_current = new Timestamp(date.getTime());
        Timestamp end_time;

        st = LibrarySystem.connection.prepareStatement("Select * from camera_checkout where patron_id =? and camera_id=?");
        st.setInt(1, LibrarySystem.patron_id);
        st.setString(2, camera_id);
        ResultSet rs = st.executeQuery();
        Timestamp tst = null;
        if (rs.next()) {
            tst = rs.getTimestamp("start_time");
            //tst.getTime()+
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(tst.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        end_time = new Timestamp(cal.getTimeInMillis());
        int hours = (int) (tstamp_current.getTime() - end_time.getTime()) / (1000 * 60 * 60);
        System.out.println(hours);

        PreparedStatement st1 = LibrarySystem.connection.prepareStatement("Select * from late_fee where resource_type = ?");
        st1.setString(1, "CAMERA");
        ResultSet rs1 = st1.executeQuery();
        if (rs1.next()) {
            int fees = rs1.getInt("fees");
            int frequency_hours = rs1.getInt("frequency_hours");

            if (hours > 0) {
                st = LibrarySystem.connection.prepareStatement("Select account_balance from " + LibrarySystem.patron_type + " where " + LibrarySystem.patron_type + "_id =?");
                st.setString(1, LibrarySystem.login_id);
                rs = st.executeQuery();
                if (rs.next()) {
                    System.out.println(fees);
                    int val = (int) (rs.getInt(1) - fees * (Math.floor((double) (hours / frequency_hours))));
                    st = LibrarySystem.connection.prepareStatement("Update " + LibrarySystem.patron_type + " set account_balance = ? where " + LibrarySystem.patron_type + "_id =?");
                    st.setInt(1, val);
                    st.setString(2, LibrarySystem.login_id);
                    try {
                        System.out.println(val);

                        st.executeUpdate();
                    } catch (SQLException e) {

                        System.out.println(e.getMessage());

                    }

                }
            }

            st = LibrarySystem.connection.prepareStatement("Delete from camera_checkout where camera_id = ? and patron_id=? and start_time=?");
            st.setString(1, camera_id);
            st.setInt(2, LibrarySystem.patron_id);
            st.setTimestamp(3, tst);
            try {
                st.executeUpdate();
            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

        }
        LibrarySystem.connection.commit();
        LibrarySystem.connection.setAutoCommit(true);
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

    public static int renew_resource(String resource_type, int p_id, String id, Date start_time)throws SQLException
    {
        int hours;
        int late_fee;
        int fees;
        int duration;
        String query = null;
        String table_name = null;
        String set_clause;
        String where_col = null;
        String library_name = null;
        String is_ecopy = null;
        Timestamp allowed_time;
        
        //LibrarySystem.connection.setAutoCommit(false);
        
        is_ecopy = LibraryAPI.isECopy(p_id, LibrarySystem.patron_id);
        System.out.println("COPY:" + is_ecopy);

        Date end_time = new Date(System.currentTimeMillis());
        Timestamp ts1 = new Timestamp(end_time.getTime());
        /* Update the check out book table */
        query = "update checkout set END_TIME = ? where PUBLICATION_ID = ? and PATRON_ID = ? and END_TIME is NULL";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setTimestamp(1, ts1);
        st.setInt(2, p_id);
        st.setInt(3, LibrarySystem.patron_id);

        if (st.executeUpdate() == 0) {
            LibrarySystem.connection.setAutoCommit(true);
            return -1;
        }

        query = "insert into checkout (PUBLICATION_ID,PATRON_ID,START_TIME,LIB_NAME,E_COPY) values (?,?,?,?,?)";
            st = LibrarySystem.connection.prepareStatement(query);

            st.setInt(1, p_id);
            st.setInt(2, LibrarySystem.patron_id);
            st.setTimestamp(3, new Timestamp(new java.util.Date(System.currentTimeMillis()).getTime()));
            st.setString(4, library_name);
            st.setString(5, is_ecopy);
            if (st.executeUpdate() != 0) {
                System.out.println("Inserted in checkout");
            } else {
                LibrarySystem.connection.setAutoCommit(true);
                System.out.println("Error while insert into checkout");
                return 1;                
            }
        if (is_ecopy.equalsIgnoreCase("Y")) {
            System.out.println("ECOPY:" + is_ecopy);
            LibrarySystem.connection.setAutoCommit(true);
            LibrarySystem.connection.commit();
            return 1;
        }

        /* Calucate the late fee charge */
        query = "select FEES,FREQUENCY_HOURS from late_fee where resource_type = ? order by frequency_hours asc";
        st = LibrarySystem.connection.prepareStatement(query);
        System.out.println("Resource Type:" + resource_type);
        st.setString(1, resource_type);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            fees = rs.getInt(1);
            hours = rs.getInt(2);
        } else {
            System.out.println("no entry in late fee");
            LibrarySystem.connection.setAutoCommit(true);
            return -1;
        }

        duration = LibraryAPI.getDuration(LibrarySystem.patron_type, resource_type);

        System.out.println("Duration : " + duration);
        System.out.println("Start Time : " + start_time.toString());
       
        Timestamp start_time_stamp = new Timestamp(start_time.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start_time_stamp.getTime());
        cal.add(Calendar.HOUR, duration);
        allowed_time = new Timestamp(cal.getTimeInMillis());

        System.out.println("Allowed : " + allowed_time.getTime());
        System.out.println("ts1" + ts1.getTime());
        
        long hours_left = (long) ((ts1.getTime() - allowed_time.getTime()));
        
        System.out.println("Hours Left:" + hours_left);

        if (hours_left <= 0) {
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return 1;
        }

        long diffInMillies = hours_left / (3600);//end_time.getTime() - start_time.getTime();
        long no_of_hours = (diffInMillies) / (1000);
        System.out.println("no_of_hours : " + no_of_hours);
        late_fee = (int) LibraryAPI.getLateFees(hours, fees, no_of_hours);

        System.out.println("Late Fee:" + late_fee);

        LibraryAPI.updateBalance(getBalance() - late_fee);

        LibrarySystem.connection.setAutoCommit(true);
        LibrarySystem.connection.commit();

        return 1;        
    }
    public static int return_resource(String resource_type, int p_id, String id, Date start_time) throws SQLException {
        int hours;
        int late_fee;
        int fees;
        int duration;
        String query = null;
        String table_name = null;
        String set_clause;
        String where_col = null;
        String library_name = null;
        String is_ecopy = null;
        Timestamp allowed_time;

        int avail_no = 0;
        System.out.println(p_id);
        //LibrarySystem.connection.setAutoCommit(false);
        library_name = LibraryAPI.getLibraryName(p_id, LibrarySystem.patron_id);
        System.out.println(":" + library_name);

        if (library_name.equals(LibrarySystemConst.HUNT)) {
            set_clause = "HUNT_AVAIL_NO";
        } else {
            set_clause = "HILL_AVAIL_NO";
        }

        System.out.println(resource_type);

        switch (resource_type) {
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
                System.out.println("default");
                LibrarySystem.connection.setAutoCommit(true);
                return -1;
        }
        is_ecopy = LibraryAPI.isECopy(p_id, LibrarySystem.patron_id);
        if (is_ecopy.equalsIgnoreCase("Y")) {
            avail_no = avail_no;
        } else {
            avail_no++;
        }

        query = "update " + table_name + " set " + set_clause + " = ? where "
                + where_col + " = ? ";
        System.out.println(table_name + ":" + where_col);
        st = LibrarySystem.connection.prepareStatement(query);

        st.setInt(1, avail_no);
        st.setString(2, id);
        //System.out.println(st.executeUpdate());
        if (st.executeUpdate() == 0) {
            System.out.println("No rows updated");
            LibrarySystem.connection.setAutoCommit(true);
            return -1;
        }
        System.out.println("::" + LibraryAPI.isECopy(p_id, LibrarySystem.patron_id));

        Date end_time = new Date(System.currentTimeMillis());
        Timestamp ts1 = new Timestamp(end_time.getTime());
        /* Update the check out book table */
        query = "update checkout set END_TIME = ? where PUBLICATION_ID = ? and PATRON_ID = ? and END_TIME is NULL";
        st = LibrarySystem.connection.prepareStatement(query);
        st.setTimestamp(1, ts1);
        st.setInt(2, p_id);
        st.setInt(3, LibrarySystem.patron_id);

        if (st.executeUpdate() == 0) {
            LibrarySystem.connection.setAutoCommit(true);
            return -1;
        }

        if (is_ecopy.equalsIgnoreCase("Y")) {
            System.out.println("ECOPY:" + is_ecopy);
            LibrarySystem.connection.setAutoCommit(true);
            LibrarySystem.connection.commit();
            return 1;
        }

        /* Calucate the late fee charge */
        query = "select FEES,FREQUENCY_HOURS from late_fee where resource_type = ? order by frequency_hours asc";
        st = LibrarySystem.connection.prepareStatement(query);
        System.out.println("Resource Type:" + resource_type);
        st.setString(1, resource_type);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            fees = rs.getInt(1);
            hours = rs.getInt(2);
        } else {
            System.out.println("no entry in late fee");
            LibrarySystem.connection.setAutoCommit(true);
            return -1;
        }

        duration = LibraryAPI.getDuration(LibrarySystem.patron_type, resource_type);

        System.out.println("Duration : " + duration);
        System.out.println("Start Time : " + start_time.toString());

        Timestamp start_time_stamp = new Timestamp(start_time.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start_time_stamp.getTime());
        cal.add(Calendar.HOUR, duration);
        allowed_time = new Timestamp(cal.getTimeInMillis());

        System.out.println("Allowed : " + allowed_time.getTime());
        System.out.println("ts1" + ts1.getTime());

        long hours_left = (long) ((ts1.getTime() - allowed_time.getTime()));

        System.out.println("Hours Left:" + hours_left);

        if (hours_left <= 0) {
            LibrarySystem.connection.commit();
            LibrarySystem.connection.setAutoCommit(true);
            return 1;
        }

        long diffInMillies = hours_left / (3600);//end_time.getTime() - start_time.getTime();
        long no_of_hours = (diffInMillies) / (1000);
        System.out.println("no_of_hours : " + no_of_hours);
        late_fee = (int) LibraryAPI.getLateFees(hours, fees, no_of_hours);

        System.out.println("Late Fee:" + late_fee);

        LibraryAPI.updateBalance(getBalance() - late_fee);

        LibrarySystem.connection.setAutoCommit(true);
        LibrarySystem.connection.commit();

        return 1;
    }

    public static ArrayList<CheckOut> checkout_book_list() throws SQLException {
        ArrayList<CheckOut> checkout_book_list = new ArrayList<>();
        CheckOut co;
        Books bk;

        String query = null;

        query = "select b.ISBN_NO,b.TITLE,b.GROUP_ID,c.PUBLICATION_ID,c.PATRON_ID,c.START_TIME,c.LIB_NAME from books b,checkout c,publication p where b.isbn_no = p.publication_id and p.id = c.publication_id and c.patron_id = ? and c.END_TIME is NULL";

        st = LibrarySystem.connection.prepareStatement(query);

        st.setInt(1, LibrarySystem.patron_id);

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            co = new CheckOut();
            bk = new Books();
            bk.setIsbn_no(rs.getString("ISBN_NO"));
            bk.setTitle(rs.getString("TITLE"));
            bk.setGroup_id(rs.getInt("GROUP_ID"));
            bk.setAuthor_list(LibraryAPI.getAuthorList(bk.getGroup_id()));
            co.setPublication_id(rs.getInt("PUBLICATION_ID"));
            co.setPatron_id(rs.getInt("PATRON_ID"));
            co.setStart_time(rs.getDate("START_TIME"));
            co.setLib_name(rs.getString("LIB_NAME"));
            co.setBooks_det(bk);
            checkout_book_list.add(co);
        }
        return checkout_book_list;
    }

    public static ArrayList<CheckOut> checkout_journal_list() throws SQLException {
        ArrayList<CheckOut> checkout_journal_list = new ArrayList<>();
        CheckOut co;
        Journals jr;

        String query = null;

        query = "select j.ISSN_NO,j.TITLE,j.GROUP_ID,c.PUBLICATION_ID,c.PATRON_ID,c.START_TIME,c.LIB_NAME from journals j,checkout c,publication p where j.ISSN_NO = p.publication_id and p.id = c.publication_id and c.patron_id = ? and c.END_TIME is NULL";

        st = LibrarySystem.connection.prepareStatement(query);

        st.setInt(1, LibrarySystem.patron_id);

        ResultSet rs = st.executeQuery();
        System.out.println("hello1");

        while (rs.next()) {
            System.out.println("hello");
            co = new CheckOut();
            jr = new Journals();
            jr.setIssn_no(rs.getString("ISSN_NO"));
            jr.setTitle(rs.getString("TITLE"));
            jr.setGroup_id(rs.getInt("GROUP_ID"));
            System.out.println("hello");
            jr.setAuthor_list(LibraryAPI.getAuthorList(jr.getGroup_id()));
            co.setPublication_id(rs.getInt("PUBLICATION_ID"));
            co.setPatron_id(rs.getInt("PATRON_ID"));
            co.setStart_time(rs.getDate("START_TIME"));
            co.setLib_name(rs.getString("LIB_NAME"));
            co.setJr(jr);
            checkout_journal_list.add(co);
        }
        return checkout_journal_list;
    }

    public static ArrayList<String> pastdue_reminder() throws SQLException {
        ArrayList<String> string_list = new ArrayList<>();
        st = LibrarySystem.connection.prepareStatement("Select * from checkout where patron_id=? and end_time is null");
        st.setInt(1, LibrarySystem.patron_id);
        Date date = new Date(System.currentTimeMillis());
        Timestamp t_cur = new Timestamp(date.getTime());
        ResultSet rs = st.executeQuery();

        while (rs.next()) {

            String str = "";

            Timestamp tst = rs.getTimestamp("start_time");

            PreparedStatement st2 = LibrarySystem.connection.prepareStatement("select publication_type, publication_id from publication where id=?");
            st2.setInt(1, rs.getInt("publication_id"));
            //st2.setInt(1, 6);
            ResultSet rs1 = st2.executeQuery();
            if (rs1.next()) {

                PreparedStatement st1 = LibrarySystem.connection.prepareStatement("select duration from checkout_duration where resource_type = ? and patron_type=?");
                st1.setString(1, rs1.getString(1));
                st1.setString(2, LibrarySystem.patron_type);
                ResultSet rs2 = st1.executeQuery();
                if (rs2.next()) {
                    int duration = rs2.getInt("duration");

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(tst.getTime());
                    cal.add(Calendar.HOUR, duration);
                    Timestamp due_time = new Timestamp(cal.getTimeInMillis());
                    long hours = (long) ((t_cur.getTime() - due_time.getTime()) / (1000 * 60 * 60));


                    if (hours >= (30 * 24) && hours < (60 * 24)) {

                        string_list.add("Your " + rs1.getString(2)+" is already dued for 30 days");
                    }
                    if (hours >= (60 * 24) && hours < (90 * 24)) {
                        string_list.add("Your " + rs1.getString(2)+" is already dued for 60 days");
                    }
                    if (hours >= (90 * 24)) {
                        string_list.add("Your " + rs1.getString(2)+" is already dued for 90 days. Your checkout privileges are revoked");
                                        PreparedStatement st3 = LibrarySystem.connection.prepareStatement("insert into account_revoke values (?,?)");
                                        st3.setString(1, LibrarySystem.login_id);
                                        st3.setString(2, "Y");
                                        try{
                                        st3.executeUpdate();
                                                }
                                        catch (SQLException e)
                                        {
                                            
                                        }

                    }

                }

            }
        }

        return string_list;
    }
        public static ArrayList<String> predue_reminder() throws SQLException {
        ArrayList<String> string_list = new ArrayList<>();

        st = LibrarySystem.connection.prepareStatement("Select * from checkout where patron_id=? and end_time is null");
        st.setInt(1, LibrarySystem.patron_id);
        Date date = new Date(System.currentTimeMillis());
        Timestamp t_cur = new Timestamp(date.getTime());
        ResultSet rs = st.executeQuery();

        while (rs.next()) {

            String str = "";

            Timestamp tst = rs.getTimestamp("start_time");

            PreparedStatement st2 = LibrarySystem.connection.prepareStatement("select publication_type, publication_id from publication where id=?");
            st2.setInt(1, rs.getInt("publication_id"));
            ResultSet rs1 = st2.executeQuery();
            if (rs1.next()) {

                PreparedStatement st1 = LibrarySystem.connection.prepareStatement("select duration from checkout_duration where resource_type = ? and patron_type=?");
                st1.setString(1, rs1.getString(1));
                st1.setString(2, LibrarySystem.patron_type);
                ResultSet rs2 = st1.executeQuery();
                if (rs2.next()) {
                    int duration = rs2.getInt("duration");
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(tst.getTime());
                    cal.add(Calendar.HOUR, duration);
                    Timestamp due_time = new Timestamp(cal.getTimeInMillis());
                    long hours = (long) ((due_time.getTime() - t_cur.getTime()) / (1000 * 60 * 60));


                    if (hours <= 24 && hours>=0) {

                        string_list.add("Your " + rs1.getString(2)+" is due in 1 day");
                    }
                    if (hours <= (3 * 24) && hours >  24) {
                        string_list.add("Your " + rs1.getString(2)+" is due in 3 days");
                    }

                }

            }
        }
        for (int i=0;i<string_list.size();i++)
        {
            System.out.println(string_list.get(i));
        }

        return string_list;
    }

    public static ArrayList<CheckOut> checkout_conf_list() throws SQLException {
        ArrayList<CheckOut> checkout_conf_list = new ArrayList<>();
        CheckOut co;
        Conf cf;

        String query = null;

        query = "select cf.CONF_NUM,cf.TITLE,cf.GROUP_ID,c.PUBLICATION_ID,c.PATRON_ID,c.START_TIME,c.LIB_NAME from conf cf,checkout c,publication p where cf.CONF_NUM = p.publication_id and p.id = c.publication_id and c.patron_id = ? and c.END_TIME is NULL";

        st = LibrarySystem.connection.prepareStatement(query);

        st.setInt(1, LibrarySystem.patron_id);

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            co = new CheckOut();
            cf = new Conf();
            cf.setConfnum(rs.getString("CONF_NUM"));
            cf.setTitle(rs.getString("TITLE"));
            cf.setGroup_id(rs.getInt("GROUP_ID"));
            cf.setAuthor_list(LibraryAPI.getAuthorList(cf.getGroup_id()));
            co.setPublication_id(rs.getInt("PUBLICATION_ID"));
            co.setPatron_id(rs.getInt("PATRON_ID"));
            co.setStart_time(rs.getDate("START_TIME"));
            co.setLib_name(rs.getString("LIB_NAME"));
            co.setCf(cf);
            checkout_conf_list.add(co);
        }
        return checkout_conf_list;
    }

    public static ArrayList<String> getNotification() throws SQLException
    {
        ArrayList<String> notification_text = new ArrayList<>();
        String room_notif = null;
        String camera_notif = null;
        String query = null;
        
        room_notif = room_notify();
        camera_notif = camera_notify();
        
        notification_text.add(room_notif);
        notification_text.add(camera_notif);
        
        CallableStatement pl_exec = LibrarySystem.connection.prepareCall( "begin insert_into_reminder(?); end;" ); 
        pl_exec.setInt(1, LibrarySystem.patron_id );              // set value of first function parameter "... javatest( ?, ..."
        if(pl_exec.executeUpdate() == 0)
        {
            System.out.println("Some errr in running PLSQL");
        }
        
        query = "select TEXT from REMINDER where PATRON_ID = ? and SEEN_IND = ?";
        
        st = LibrarySystem.connection.prepareStatement(query);
        st.setInt(1, LibrarySystem.patron_id);
        st.setString(2, "N");
        
        ResultSet rs = st.executeQuery();
        
        ArrayList<String> pre = predue_reminder();
        for(int i = 0;i < pre.size();i++)
        {
            notification_text.add(pre.get(i));
        }
        
        ArrayList<String> pos = pastdue_reminder();
        for(int i = 0;i < pos.size();i++)
        {
            notification_text.add(pos.get(i));
        }
        while(rs.next())
        {
            System.out.println("Inside");
            notification_text.add(rs.getString(1));
        }
        
        query = "update REMINDER set SEEN_IND = ? where PATRON_ID = ?";
        
        st = LibrarySystem.connection.prepareStatement(query);
        st.setString(1, "Y");
        st.setInt(2, LibrarySystem.patron_id);
 
        if(st.executeUpdate() == 0)
        {
            System.out.println("No Rows Updated");
            return null;
        }
        return notification_text;
    }    
    
    public static ArrayList<Books> get_non_available_book()
    {
        ArrayList<Books> w_book_list = new ArrayList<>();
        String query = null;
        
        query = "select * from books where hunt_avail_no = 0 and hill_avail_no = 0";
        
        return w_book_list;
    }
}
