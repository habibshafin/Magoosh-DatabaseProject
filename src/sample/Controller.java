package sample;

import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {

    public Button TeacherList;
    String decider;
    DbAdapter dbAdapter;
    @FXML
    private TextArea textar;
    @FXML
    private Button loginStudent;
    @FXML
    private Button verbalQues;
    @FXML
    private Button loginStudent1;
    @FXML
    private Button updatePerfromance;
    @FXML
    private Button signupstudent;
    @FXML
    private Button trigger2;
    private Main main;
    @FXML
    private TextField in1;
    @FXML
    private TextField in2;
    @FXML
    private TextField in3;
    @FXML
    private TextField in4;
    @FXML
    private TextField in5;
    @FXML
    private Button run;
    @FXML
    private Button connect;
    @FXML
    private Button signuptutor;
    @FXML
    private Button videolist;
    @FXML
    private Button custompractice;
    @FXML
    private Button addvideos;
    @FXML
    private Button GREScore;
    @FXML
    private Button TopScorer;
    @FXML
    private Button addgrescore;
    @FXML
    private Button playvideo;
    @FXML
    private Button review;
    @FXML
    private Button mathQues;
    @FXML
    private Button loginTeacher;
    @FXML
    private Button viewDetails;
    @FXML
    void runAction(ActionEvent event) {
        if(decider=="teacherlist"){
            String SQL = "select name,age from person where person.id in (select id from teacher)";
            System.out.println(SQL);
            String res = "name \t age\n";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                while(rs.next()){
                    res = res +  rs.getString("name") +"\t\t"+ rs.getInt("age")+ "\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            textar.setText(res);
        }
        if(decider=="gregre"){
            int stdid = Integer.parseInt(in1.getText());
            String SQL = "select gremscore,grevscore from grescores where stdid = "+Integer.toString(stdid);
            System.out.println(SQL);
            String res = "MathScore \t Verbal Score\n";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                while(rs.next()){
                    res = res +  rs.getInt("gremscore") +"\t\t\t\t"+ rs.getInt("grevscore")+ "\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            textar.setText(res);
        }
        if(decider=="trigger2"){
            int quizid = Integer.parseInt(in1.getText());
            int stdid = Integer.parseInt(in2.getText());
            int qid = Integer.parseInt(in3.getText());
            String ans = in4.getText();
            String SQL = "insert into quizhistory values ("+Integer.toString(quizid)+","+Integer.toString(stdid)+","+ Integer.toString(qid)+",'"+ans+"')";
            System.out.println(SQL);
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(decider=="topscorer"){
            String SQL = "select stdid , max(gremscore+grevscore) as \"score\"\n" +
                    "from \"person\" , \"grescores\"\n" +
                    "where \"person\".id = \"grescores\".stdid\n" +
                    "group by \"grescores\".stdid;";
            System.out.println(SQL);
            String res = "Student id \t Score\n";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                while(rs.next()){
                    res = res + rs.getString("stdid") + "\t\t"+ rs.getString("score")+"\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            textar.setText(res);
        }
        if(decider=="mathQuestion"){
            int stdid = Integer.parseInt(in1.getText());
            String pass = null;
            String SQL = "select * from \"mathquestion\"\n" +
                    "where testid = (select lasttest from student where id = " + Integer.toString(stdid)+")";
            System.out.println(SQL);
            String res="Quizid\tqid\tques\tans\toptn\texplanation\tlink \n";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                while(rs.next()){
                    res = res + rs.getInt("testid")+"\t"+rs.getInt("mid")+"\t"+ rs.getString("question")+
                            "\t"+rs.getArray("option")+"\t"+rs.getArray("ans")+"\t"+rs.getString("explanation")+"\t"+rs.getString("link")+"\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            textar.setText(res);
            decider = "vacant";
        }
        if(decider=="verbalQuestion"){
            int stdid = Integer.parseInt(in1.getText());
            String pass = null;
            String SQL = "select * from \"verbalquestion\"\n" +
                    "where testid = (select lastverbaltest from student where id = " + Integer.toString(stdid)+")";
            System.out.println(SQL);
            String res="Quizid\\tqid\\tques\\tans\\toptn\\texplanation\\tlink\n";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                while(rs.next()){
                    res = res + rs.getInt("testid")+"\t"+rs.getInt("vid")+"\t"+ rs.getString("question")+
                            "\t"+rs.getArray("option")+"\t"+rs.getArray("ans")+"\t"+rs.getString("explanation")+"\t"+rs.getString("link")+"\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            textar.setText(res);
            decider = "vacant";
        }
        if(decider=="expectedScore"){
            int stdid = Integer.parseInt(in1.getText());
            int mdeviation = dbAdapter.mdeviation(stdid);
            int vdeviation = dbAdapter.vdeviation(stdid);
            int percmscore = dbAdapter.Mscoreperc(stdid);
            int percvscore = dbAdapter.Vscoreperc(stdid);
            int maxm = ((percmscore + mdeviation) * 170)/100;
            //int minm = ((percmscore -mdeviation)* 170)/100;
            int maxv = ((percvscore + vdeviation)* 170)/100;
            //int minv = ((percvscore - vdeviation)* 170)/100;
            textar.setText("so the expected score for "+stdid+"\n"+
            "math ----" + maxm +"\n"
            +"verbal----"+ maxv +" ");
            decider ="vacant";
        }
        if(decider=="addViewDetails"){
            String understand = null;
            String comment = null;
            int stdid = 0;
            int vid = 0;
            Double seendur = 0.0;
            stdid = Integer.parseInt(in1.getText());
            vid = Integer.parseInt(in2.getText());
            seendur = Double.parseDouble(in3.getText());
            understand = in4.getText();
            comment = in5.getText();
            dbAdapter.addViewDetails(stdid,vid,seendur,understand,comment);
        }
        if(decider=="loginStudent") {
            int id = Integer.parseInt(in1.getText());
            String enteredpass = in2.getText();
            String pass = null;
            String SQL = "select password from student where id = " + Integer.toString(id);
            //System.out.println(SQL);
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                rs.next();
                pass = rs.getString("password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(pass.equalsIgnoreCase(enteredpass))
                textar.setText("login successful ");
            else textar.setText("wrong user id or password");
        }
        if(decider=="loginTeacher"){
            int id = Integer.parseInt(in1.getText());
            String enteredpass = in2.getText();
            String pass = null;
            String SQL = "select password from teacher where id = " + Integer.toString(id);
            //System.out.println(SQL);
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                rs.next();
                pass = rs.getString("password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(pass.equalsIgnoreCase(enteredpass))
                textar.setText("login successful ");
            else textar.setText("wrong user id or password");
        }
        if(decider=="review"){
            int stdid,vid;
            String understand,comments;
            stdid = Integer.parseInt(in1.getText());
            vid = Integer.parseInt(in2.getText());
            understand= in3.getText();
            comments = in4.getText();
            decider = "vacant";
            dbAdapter.feedback(stdid,vid,understand,comments);
        }
        if(decider == "addgrescore"){
            int stid,mscore,vscore;
            stid = Integer.parseInt(in1.getText());
            mscore = Integer.parseInt(in2.getText());
            vscore = Integer.parseInt(in3.getText());
            dbAdapter.addGre(stid,mscore,vscore);
            textar.setText("score added");
            decider = "vacant";
        }
        if(decider=="addvideos"){
            String name, link, lessonid;
            int teacherid,result;
            double duration;
            name = in1.getText();
            link = in2.getText();
            duration = Double.parseDouble(in3.getText());
            lessonid = in4.getText();
            teacherid = Integer.parseInt(in5.getText());
            result = dbAdapter.addVideos(name, link, duration, lessonid, teacherid);
            textar.setText("the video id of the newly added video is "+ result);
            decider = "vacant";
        }
        if(decider=="signupstudent"){
            String name,address,pass;
            int age;
            name = in1.getText();
            address = in3.getText();
            pass = in4.getText();
            age = Integer.parseInt(in2.getText());
            int result = dbAdapter.SignUpStudent(name,age,address,pass);
            textar.setText(Integer.toString(result));
            decider = "vacant";
        }
        if(decider=="signuptutor"){
            String name,address,pass;
            int age;
            name = in1.getText();
            address = in3.getText();
            pass = in4.getText();
            age = Integer.parseInt(in2.getText());
            int result = dbAdapter.SignUpTeacher(name,age,address,pass);
            textar.setText(Integer.toString(result));
            decider = "vacant";
        }
        if(decider=="showvideo"){
            String SQL = "select * from showvideo() ";
            try (Statement stmt = dbAdapter.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL)) {
                String res = "";
                while(rs.next()){
                    res = res + rs.getInt("vid")+"\t"+rs.getString("names")+"\t"
                            +"\t"+ rs.getString("dur") +"\n";
                }
                textar.setText(res);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if(decider=="playVideo"){
           String link;
           int videoid,stdid;
           double playtime;
           videoid = Integer.parseInt(in1.getText());
           stdid = Integer.parseInt(in2.getText());
           link = dbAdapter.getVideoLink(videoid);
           playtime = dbAdapter.getPlayTime(stdid,videoid);
           textar.setText("video from "+link + " will be played from "+ playtime);
            Desktop d = Desktop.getDesktop();
            try {
                d.browse(new URI(link));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            decider = "vacant";
        }
        if(decider=="customPractice"){
            decider = "vacant";
            String topic,subtopic,ans,mode;
            topic = in1.getText();
            subtopic = in2.getText();
            ans = in3.getText();
            mode = in4.getText();
            int stdid = Integer.parseInt(in5.getText());
            if(mode.equalsIgnoreCase("practice")){
                String SQL = "select * from customquestions('"+topic+"' , '"+subtopic+"' , '"+ans+"' ) ";
                //System.out.println(SQL);
                try (Statement stmt = dbAdapter.conn.createStatement();
                     ResultSet rs = stmt.executeQuery(SQL)) {
                    String res = "";
                    while(rs.next()){
                        res = res + rs.getInt("qid")+"\t"+rs.getString("ques")+"\t"+rs.getArray("opt")
                                +"\t"+ rs.getString("expl")+"\t"+rs.getString("lnk")+"\n";
                    }
                    textar.setText(res);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else if(mode.equalsIgnoreCase("quiz")){
                int quizid = dbAdapter.getQuizId(stdid);
                String res = "";
                res = "quiz id generated = "+ quizid+" \n"+
                "now the quiz questions and ans will be saved to quizhistory and then shown at the end\n\n";
                String SQL = "select * from customquestions('"+topic+"' , '"+subtopic+"' , '"+ans+"' ) ";
                try (Statement stmt = dbAdapter.conn.createStatement();
                     ResultSet rs = stmt.executeQuery(SQL)) {

                    while(rs.next()){
                        res = res + rs.getInt("qid")+"\t"+rs.getString("ques")+"\t"+rs.getArray("opt")
                                +"\t"+ rs.getString("expl")+"\t"+rs.getString("lnk")+"\n";
                    }
                    //System.out.println(res);
                    textar.setText(res);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if(decider=="addQuizHistory"){
            String ans;
            int quizid,stdid,quesid;
            quizid = Integer.parseInt(in1.getText());
            stdid = Integer.parseInt(in2.getText());
            quesid = Integer.parseInt(in3.getText());
            ans = in4.getText();
            decider = "vacant";
            dbAdapter.quizHistory(quizid,stdid,quesid,ans);
        }

    }
    public void customPracticeAction(ActionEvent actionEvent) {
        textar.setText("custom question function is called which returns a table of questions\naccording to the requirement\n"+
                "begin\n" +
                "return query select questionid,question,options,correctans,explanation,link from \"questions\"\n" +
                "where topic = tpc AND subtopic = stpc AND answered = ans;\n" +
                "end;");
        in1.setPromptText("math/verbal");
        in2.setPromptText("enter the subtopic");
        in3.setPromptText("answered? enter (yes/no)");
        in4.setPromptText("enter the mode(practice/quiz)");
        in5.setPromptText("enter student id");
        decider = "customPractice";
    }
    public void playVideoAction(ActionEvent actionEvent) {
        textar.setText("select \"Videos\".link,\"viewdetails\".seenduration\n" +
                "from \"Videos\",\"viewdetails\"\n" +
                "where \"viewdetails\".studentid = studentid \nAND \n\"Videos\".videoid = videoid AND \"viewdetails\".videoid = videoid;");
        in1.setPromptText("enter the video id");
        in2.setPromptText("enter the student id");
        in4.setPromptText("not needed");
        in5.setPromptText("not needed");
        in3.setPromptText("not needed");
        decider = "playVideo";
    }
    @FXML
    void addGreScoreAction(ActionEvent event) {
        textar.setText("insert into \"grescores\" \nvalues \n(studentid,mathscore,verbalscore,(select (mqcorrect*100)/mqattempted\n" +
                "from \"PerformanceRecord\"\n" +
                "where id = studentid),\n(select (vqcorrect*100)/vqattempted\n" +
                "from \"PerformanceRecord\"\n" +
                "where id = studentid));");
        in1.setPromptText("enter the student id");
        in2.setPromptText("enter the math score");
        in3.setPromptText("enter the verbal score");
        in4.setPromptText("not needed");
        in5.setPromptText("not needed");
        decider = "addgrescore";
    }

    @FXML
    void addvideoAction(ActionEvent event) {
        textar.setText("insert into \"Videos\" \nvalues \n(select max(videoid)+ 1 into i from \"Videos\",\nname,dur,link,teacher,lid );\n");
        decider = "addvideos";
        in1.setPromptText("enter the name of the video");
        in2.setPromptText("enter the link address of the video");
        in3.setPromptText("enter the duration of the video");
        in4.setPromptText("enter the lesson id");
        in5.setPromptText("enter your teacher id");
    }
    @FXML
    void signuptutor(ActionEvent event) {
        textar.setText("calls the signupteacher function which adds new teacher and returns new id \ndeclare\n" +
                "i integer;\n" +
                "begin\n" +
                "select max(id) into i from person;\n" +
                "i:= i + 1;\n" +
                "insert into Person values ( i ,name,age, adr 'student');\n" +
                "insert into teacher values (i , pass);\n" +
                "return i;\n" +
                "end;\n");
        in1.setPromptText("enter name");
        in2.setPromptText("enter age");
        in3.setPromptText("enter address");
        in4.setPromptText("enter password");
        in5.setPromptText("not needed");
        decider = "signuptutor";
    }


    @FXML
    void signupstudent(ActionEvent event) {
        textar.setText("calls the signupstudent function which adds new student and returns new id \ndeclare\n" +
                "i integer;\n" +
                "begin\n" +
                "select max(id) into i from person;\n" +
                "i:= i + 1;\n" +
                "insert into Person values ( i ,name,age, adr 'student');\n" +
                "insert into student values (i , pass);\n" +
                "return i;\n" +
                "end;\n");
        in1.setPromptText("enter name");
        in2.setPromptText("enter age");
        in3.setPromptText("enter address");
        in4.setPromptText("enter password");
        decider = "signupstudent";
    }


    void setMain(Main main) {
        this.main = main;
        dbAdapter = new DbAdapter();
        dbAdapter.connect();
    }

    public void connectAction(ActionEvent actionEvent) {
        dbAdapter = new DbAdapter();
        dbAdapter.connect();
    }

    public void videolistAction(ActionEvent actionEvent) {
        textar.setText("select videoid,name,duration \nfrom \"Videos\";\n");
        decider = "showvideo";
    }


    public void addQuizHistory(ActionEvent actionEvent) {
        textar.setText("calls addquizistory function and adds the q and a for the quiz to show later\n\n"
                +"begin\n" +
                "insert into \"quizhistory\"\n" +
                "values (quizid,stid, qid, ans);\n" +
                "end;");
        in1.setPromptText("enter the quizid");
        in2.setPromptText("enter the student id");
        in3.setPromptText("enter the question id");
        in4.setPromptText("enter the answer");
        decider = "addQuizHistory";
    }

    public void reviewAction(ActionEvent actionEvent) {
        textar.setText("here feedback function is called and thus saved in viewdetails table\n"
                +"CREATE OR REPLACE PROCEDURE public.feedback(\n" +
                "\tstdid integer,\n" +
                "\tvid integer,\n" +
                "\tundstnd character varying,\n" +
                "\tcomment character varying)\n" +
                "LANGUAGE 'plpgsql'\n" +
                "\n" +
                "AS $BODY$begin\n" +
                "update viewdetails\n" +
                "set review = comment\n" +
                "where videoid=vid AND studentid = stid;\n" +
                "update viewdetails\n" +
                "set understand = undstnd\n" +
                "where videoid=vid AND studentid = stid;\n" +
                "end;$BODY$;");
        in1.setPromptText("enter student id");
        in2.setPromptText("enter video id");
        in3.setPromptText("understand?(yes/no)");
        in4.setPromptText("comments");
        in5.setPromptText("no need");
        decider = "review";
    }

    public void expectedScoreAction(ActionEvent actionEvent) {
        textar.setText("this function returns deviation of gre math score from percentage at that time\n"
                +"declare\n" + "n integer;\n" + "v integer;\n" + "begin\n" + "select count(*)into n from grescores\n" +
                "where stdid = stid;\n" + "select sum(((gremscore*100)/170)-percentmscore) into v\n" +
                "from grescores \n" + "where stdid = stid;\n" + "return v/n;\n" +
                "end;"+
                "\nthis function returns deviation of gre math score from percentage at that time\n"+
                "declare\n" +   "n integer;\n" +    "v integer;\n" +  "begin\n" +  "select count(*)into n from grescores\n" +
                "where stdid = stid;\n" + "select sum(((grevscore*100)/170)-percentvscore) into v\n" +  "from grescores \n" +
                "where stdid = stid;\n" +  "return v/n;\n" +  "end;$BODY$;\n"+
                "then corresponding to deviation expected mark is obtained from present performance");
        decider = "expectedScore";
        in1.setPromptText("enter the student id");
    }
    @FXML
    void loginStudentAction(ActionEvent event) {
        textar.setText("password will be retreived running the query\n"+
        "select password \nfrom student \nwhere id = 'givenid'\n"
        +"then if passwords are same then login successful");
        in1.setPromptText("Enter your user id");
        in2.setPromptText("enter your password");
        decider ="loginStudent";
    }

    public void loginTeacherAction(ActionEvent actionEvent) {
        textar.setText("password will be retreived running the query\n"+
                "select password \nfrom teacher \nwhere id = 'givenid'\n"
                +"then if passwords are same then login successful");
        in1.setPromptText("Enter your user id");
        in2.setPromptText("enter your password");
        decider ="loginTeacher";
    }

    public void viewDetailsAction(ActionEvent actionEvent) {
        textar.setText("CREATE OR REPLACE PROCEDURE public.addviewdetails(\n" +
                "\tstdid integer,\n" +
                "\tvid integer,\n" +
                "\tseen double precision,\n" +
                "\tund character varying,\n" +
                "\tcomment character varying)\n" +
                "LANGUAGE 'plpgsql'\n" +
                "\n" +
                "AS $BODY$begin\n" +
                "insert into \"viewdetails\"\n" +
                "values (stdid,vid,seen,und,comment);\n" +
                "end;$BODY$;");
        in1.setPromptText("Enter student id");
        in2.setPromptText("Enter video id");
        in3.setPromptText("Enter seen duration");
        in4.setPromptText("understand?(yes/no)");
        in5.setPromptText("Enter review");
        decider = "addViewDetails";
    }

    public void updatePerfromanceAction(ActionEvent actionEvent) {
        textar.setText("begin\n" +
                "if subj = 'math' then\n" +
                "if job = 'correct' then\n" +
                "update \"PerformanceRecord\"\n" +
                "set mqattempted = mqattempted +1,mqcorrect = mqcorrect +1\n" +
                "where id = stdid;\n" +
                "else\n" +
                "update \"PerformanceRecord\"\n" +
                "set mqattempted = mqattempted +1\n" +
                "where id = stdid;\n" +
                "end if;\n" +
                "else\n" +
                "if job = 'correct' then\n" +
                "update \"PerformanceRecord\"\n" +
                "set vqattempted = vqattempted +1,vqcorrect = vqcorrect +1\n" +
                "where id = stdid;\n" +
                "else\n" +
                "update \"PerformanceRecord\"\n" +
                "set vqattempted = vqattempted +1\n" +
                "where id = stdid;\n" +
                "end if;\n" +
                "end if;\n" +
                "end;");
        decider = "updatePerformance";
        in1.setPromptText("enter student id");
        in2.setPromptText("topic (math/verbal) ");
        in3.setPromptText("correct/wrong");
    }

    public void performanceRecorsTriggerAction(ActionEvent actionEvent) {
        textar.setText("CREATE FUNCTION public.autoinsertintoperformancerecord()\n" +  "    RETURNS trigger\n" +
                "    LANGUAGE 'plpgsql'\n" + "    COST 100\n" + "    VOLATILE NOT LEAKPROOF \n" +
                "AS $BODY$\n" + "declare\n" + "uid integer;\n" + "begin\n" +
                "if (TG_TABLE_NAME = 'person') then\n" + "if(TG_OP = 'INSERT') then \n" + "uid = new.id;\n" +
                "end if;\n" + "insert into \"PerformanceRecord\" \n" + "values (uid, '0', '0', '0', '0');\n" +
                "return new;\n" + "end if;\n" + "end;\n" + "$BODY$;\n" + "\n" +
                "ALTER FUNCTION public.autoinsertintoperformancerecord()\n" + "    OWNER TO postgres;\n");
        in1.setPromptText("enter name");
        in2.setPromptText("enter age");
        in3.setPromptText("enter address");
        in4.setPromptText("enter password");
        decider = "signupstudent";

    }

    public void mathQuesAction(ActionEvent actionEvent) {
        textar.setText("select * from \"mathquestion\"\n" +
                "where testid = \n(select lasttest \nfrom student \nwhere id = studentid);");
        in1.setPromptText("enter student id");
        in2.setPromptText("no need");
        in3.setPromptText("no need");
        in4.setPromptText("no need");
        in5.setPromptText("no need");
        decider = "mathQuestion";
    }

    public void verbalQuesAction(ActionEvent actionEvent) {
        textar.setText("select * from \"mathquestion\"\n" +
                "where testid = \n(select lasttest \nfrom student \nwhere id = studentid);");
        in1.setPromptText("enter student id");
        in2.setPromptText("no need");
        in3.setPromptText("no need");
        in4.setPromptText("no need");
        in5.setPromptText("no need");
        decider = "verbalQuestion";
    }

    public void trigger2Action(ActionEvent actionEvent) {
        textar.setText("CREATE FUNCTION public.auto_update_into_performancerecord()\n" +
                "    RETURNS trigger\n" +
                "    LANGUAGE 'plpgsql'\n" +
                "    COST 100\n" +
                "    VOLATILE NOT LEAKPROOF \n" +
                "AS $BODY$declare\n" +
                "\tuid integer;\n" +
                "\tqid integer;\n" +
                "\tans character varying[];\n" +
                "\ta1 character varying[];\n" +
                "\ta2 character varying;\n" +
                "\tsub character varying;\n" +
                "BEGIN\n" +
                "IF (TG_TABLE_NAME = 'quizhistory' ) then\n" +
                "\tif (TG_OP = 'INSERT') then\n" +
                "\t\tuid = NEW.stdid;\n" +
                "\t\tqid = NEW.quizid;\n" +
                "\t\tans = NEW.givenans;\n" +
                "end if;\n" +
                "select correctans into a1 from \"questions\" where questionid = qid;\n" +
                "select topic into sub from \"questions\" where questionid = qid;\n" +
                "if ans = a1 then\n" +
                "\ta2:='correct';\n" +
                "else\n" +
                "\ta2:= 'wrong';\n" +
                "end if;\n" +
                "call updateperformance(uid,sub,a2); \n" +
                "RETURN NEW;\n" +
                "END if;\n" +
                "end;\n" +
                "$BODY$;\n" +
                "\n" +
                "ALTER FUNCTION public.auto_update_into_performancerecord()\n" +
                "    OWNER TO postgres;\n");
        in1.setPromptText("enter quiz id");
        in2.setPromptText("enter student id");
        in3.setPromptText("enter question id");
        in4.setPromptText("enter answer");
        decider = "trigger2";
    }

    public void GREScoreAction(ActionEvent actionEvent) {
        textar.setText("select gremscore,grevscore\n" +
                "from grescores\n" +
                "where stdid = 'studentid';");
        in1.setPromptText("enter the student id");
        decider = "gregre";
    }

    public void TopScorerAction(ActionEvent actionEvent) {
        textar.setText("select stdid , max(gremscore+grevscore) as \"score\"\n" +
                "from \"person\" , \"grescores\"\n" +
                "where \"person\".id = \"grescores\".stdid\n" +
                "group by \"grescores\".stdid;");
        decider = "topscorer";
    }

    public void teacherListAction(ActionEvent actionEvent) {
        textar.setText("select name,age \nfrom person\nwhere person.id \nin \n(select id \nfrom teacher);");
        decider = "teacherlist";
        in1.setPromptText("");
        in2.setPromptText("");
        in3.setPromptText("");
        in4.setPromptText("");
        in5.setPromptText("");
    }
}
