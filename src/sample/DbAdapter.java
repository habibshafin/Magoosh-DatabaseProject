package sample;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.sql.*;

public class DbAdapter {

    /* 01 Variables ---------------------------------------- */
    String jdbcUrl = "jdbc:postgresql://localhost:5432/Magoosh";
    String username = "postgres";
    String password = "pass624879";

    /* 02 Database variables ------------------------------- */
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    int in = 10061;

    /* 03 Constructor for DbAdapter ------------------------ */
    public DbAdapter() {
    }

    public void showVideoList(){
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call showvideolist() }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setString(2,"verb");
            properCase.setString(3,"https://www.youtube.com/watch?v=xAULP0Jc5tU");
            properCase.setDouble(4,5.5);
            properCase.setString(5,"verbal101");
            properCase.setInt(6,10001);

            properCase.execute();
            Integer result = properCase.getInt(1);
            System.out.println(result);
            //System.out.println("ekhane call hyna keno");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int addVideos(String name, String link, double duration, String lessonid, int teacherid){
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call addvideos( ? , ?, ?, ?, ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setString(2,name);
            properCase.setString(3,link);
            properCase.setDouble(4,duration);
            properCase.setString(5,lessonid);
            properCase.setInt(6,teacherid);

            properCase.execute();
            result = properCase.getInt(1);
            System.out.println(result);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  result;
    }

    public void addGre(int stid, int mscore, int vscore){
        String SQL = "CALL addgrescore(" + Integer.toString(stid) +" , " + Integer.toString(mscore)+ ","+ Integer.toString(vscore)+ ")";
        System.out.println(SQL);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void quizHistory(int quizid, int stid, int quesid, String ans ){
        String SQL = "CALL addquizhistory(" + Integer.toString(quizid) +" , " + Integer.toString(stid)+" , " + Integer.toString(quesid)+ ",'{"+ ans+"}')";
        //System.out.println(SQL);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public int SignUpStudent(String n,int age, String ad, String pass ) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call signupstudent( ? , ?, ?, ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setString(2,n);
            properCase.setInt(3,age);
            properCase.setString(4,ad);
            properCase.setString(5,pass);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  result;
    }
    public void getActors() {

        String SQL = "SELECT id,password FROM student WHERE id = " + Integer.toString(in);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            displayActor(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void displayActor(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "\t"
                    + rs.getString("password"));
            //+ rs.getString("last_name"));

        }
    }
    /**
     * Connect to a database
     */
    public void connect() {
        try {
            // Step 2 - Open connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            // Print connected
            System.out.println("DbAdapter: Connection to database established");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // connect

    /**
     * Disconnect from database
     */
    public void disconnect() {
        try {

            // Step 5 Close connection
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
            // Print connected
            System.out.println("DbAdapter: Connection to database closed");

        } catch (Exception e) {
            e.printStackTrace();
        }

    } // disconnect

    public int SignUpTeacher(String n, int age, String ad, String pass) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call signupteacher( ? , ?, ?, ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setString(2,n);
            properCase.setInt(3,age);
            properCase.setString(4,ad);
            properCase.setString(5,pass);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  result;
    }

    public String getVideoLink(int videoid) {
        String s = null;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getvideo( ?  ) }")) {
            properCase.registerOutParameter(1, Types.VARCHAR);
            properCase.setInt(2,videoid);
            properCase.execute();
            s = properCase.getString(1);
            //System.out.println(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  s;
    }

    public double getPlayTime(int stdid, int videoid) {
        Double result = null;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getplaytime( ?, ? ) }")) {
            properCase.registerOutParameter(1, Types.DOUBLE);
            properCase.setInt(2,stdid);
            properCase.setInt(3,videoid);
            properCase.execute();
            result = properCase.getDouble(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(result==null) return 0;
        else
        return  result;
    }

    public int getQuizId(int stdid) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getquizid( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2,stdid);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void feedback(int stdid, int vid, String understand, String comment) {
        String SQL = "CALL feedback(" + Integer.toString(stdid) +" , " + Integer.toString(vid)+" , '" + understand+ "','"+ comment+"')";
        System.out.println(SQL);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addViewDetails(int stdid, int vid, Double seendur, String understand, String comment) {
        String SQL = "CALL addviewdetails(" + Integer.toString(stdid) +"," + Integer.toString(vid)+","+Double.toString(seendur)+",'"+ understand+ "','"+ comment+"')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int mdeviation(int stdid) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getexpectedmscore( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2,stdid);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println("mdev "+result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public int vdeviation(int stdid) {
    int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getexpectedvscore( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2,stdid);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println("vdev"+result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public int Mscoreperc(int stdid) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getmscoreperc( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2,stdid);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println("msc"+result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public int Vscoreperc(int stdid) {
        int result = 0;
        try (
                CallableStatement properCase = conn.prepareCall("{ ? = call getvscoreperc( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2,stdid);
            properCase.execute();
            result = properCase.getInt(1);
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void updatePerformance(int stdid, String subj, String cor) {
        String SQL = "CALL updateperformance(" + Integer.toString(stdid) +",'"+subj+"','"+cor+"')" ;
        System.out.println(SQL);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}