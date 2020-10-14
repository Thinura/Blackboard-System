package main.instructor;

/*
 * ADD PROFESSOR METHOD HAS NOT YET BEEN DEFINED. ASSIGNED PERSON PLEASE ADD.
 */

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jasypt.util.password.StrongPasswordEncryptor;

import main.professor.professor.Professor;
import main.student.studentdao.StudentDAO;

public class InstructorDAO {

    private Connection myCon;

    private String dbname;

    private String password;

    private String user;

    public InstructorDAO()throws Exception{

        Properties prop=new Properties();
        prop.load(InstructorDAO.class.getResourceAsStream("/details.properties"));
        dbname=prop.getProperty("dbName");
        user=prop.getProperty("user");
        password=prop.getProperty("password");
        try{System.out.println("InstructorDAO");
            myCon=DriverManager.getConnection(dbname, user, password);
        }catch(SQLException exc){
            System.out.println("Connection Problem::: Message ::");
            exc.printStackTrace();
        }
    }

    public int passwordChecker(Instructor prof)
    {
        PreparedStatement pstmt=null;

        String encryptedPassword=null;

        StrongPasswordEncryptor encryptor=new StrongPasswordEncryptor();

        try{

            pstmt=myCon.prepareStatement("select * from instructor where user_name=?");

            pstmt.setString(1, prof.getUsername());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){

                encryptedPassword=rs.getString("password");

                if(encryptor.checkPassword(prof.getPassword(), encryptedPassword)){
                    return 2;
                }
                else return 1;
            }
            else{
                /*
                 * Display error message in JOptionPane in LoginPortal Parent Interface
                 * 0-UserName Wrong
                 * 1-Password Wrong
                 * 2-Both Correct
                 */
                return 0;
            }
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            try{
                pstmt.close();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }


        return 0;
    }

    public Instructor getProfByUsername(String username)
    {
        PreparedStatement pstmt=null;
        Instructor prof=null;

        try{
            pstmt=myCon.prepareStatement("select * from instructor where user_name=?");

            pstmt.setString(1, username);
            ResultSet rs=pstmt.executeQuery();

            if(rs.next())
            {
                String courses=rs.getString("course_ids");
                String course[]=courses.split("_");
                prof=new Instructor(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7).charAt(0),rs.getString(8),rs.getString(9),rs.getString(10),course);
                System.out.println(rs.getString(2));
            }
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            try{
                pstmt.close();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
        return prof;
    }

    public List<Instructor> getAllInstructor(){
        PreparedStatement pstmt=null;
        ArrayList<Instructor> pr_list=new ArrayList<Instructor>();
        try{
			/*if(myCon==null)
				System.out.println("null");*/
            pstmt=myCon.prepareStatement("select * from instructor");
            ResultSet rs=pstmt.executeQuery();
            while(rs.next()){
                Instructor temp_pr=convertRowToInstructor(rs);
                pr_list.add(temp_pr);
            }
            return pr_list;
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
        return null;
    }

    public List<String> getAllUsernames(){
        PreparedStatement pstmt=null;
        ArrayList<String> pr_list=new ArrayList<String>();
        try{
            pstmt=myCon.prepareStatement("select * from instructor");
            ResultSet rs=pstmt.executeQuery();
            while(rs.next()){
                String pr_str=convertRowToInstructor(rs).getUsername();
                pr_list.add(pr_str);
            }
            return pr_list;
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
        return null;
    }

    private Instructor convertRowToInstructor(ResultSet rs) throws Exception{
        String courses=rs.getString("course_ids");
        String course[]=courses.split("_");
        return new Instructor(rs.getInt("s.no"), rs.getString("user_name"), rs.getString("first_name"), rs.getString("middle_name"),rs.getString("last_name"), rs.getString("email"), rs.getString("sex").charAt(0), rs.getString("password"), rs.getString("security_ques"), rs.getString("answer"), course);
    }

    public List<Instructor> searchInstructor(String toSearch) {
        // TODO Auto-generated method stub
        PreparedStatement pstmt=null;
        ArrayList<Instructor> pr_list=new ArrayList<Instructor>();
        try{
            pstmt=myCon.prepareStatement("select * from instructor where (user_name like ?) or (first_name like ?) or (middle_name like ?) or (last_name like ?) or (email like ?) or (sex like ?) or (course_ids like ?)");
            pstmt.setString(1, "%"+toSearch+"%");
            pstmt.setString(2, "%"+toSearch+"%");
            pstmt.setString(3, "%"+toSearch+"%");
            pstmt.setString(4, "%"+toSearch+"%");
            pstmt.setString(5, "%"+toSearch+"%");
            pstmt.setString(6, "%"+toSearch+"%");
            pstmt.setString(7, "%"+toSearch+"%");
            //pstmt.setString(12, toSearch);
            ResultSet rs=pstmt.executeQuery();
            while(rs.next()){
                Instructor pr_temp=convertRowToInstructor(rs);
                pr_list.add(pr_temp);
            }
            return pr_list;
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
        return null;
    }
    private String EncryptPassword(String password){
        StrongPasswordEncryptor encryptor=new StrongPasswordEncryptor();
        String encryptedPassword=encryptor.encryptPassword(password);
        return encryptedPassword;
    }


    public void addInstructor(Instructor prof)
    {
        PreparedStatement pstmt=null;
        try{
            String sql = "insert into Instructor"+
                    " (user_name, first_name, middle_name, last_name, sex, email, password, security_ques, answer,course_ids )"+
                    " values(?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            pstmt=myCon.prepareStatement(sql);
            pstmt.setString(1, prof.getUsername());
            pstmt.setString(2, prof.getFirstname());
            pstmt.setString(3, prof.getMiddlename());
            pstmt.setString(4, prof.getLastname());
            pstmt.setString(5, ""+prof.getSex());
            pstmt.setString(6, prof.getEmail());
            pstmt.setString(7, EncryptPassword(prof.getPassword()));
            pstmt.setString(8, prof.getSecurityques());
            pstmt.setString(9, prof.getAnswer());
            pstmt.setString(10, prof.getCourseString());
            pstmt.executeUpdate();
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){
                    exc.printStackTrace();
                }
            }

        }
    }

    public void modifyInstructor(Instructor prof,boolean encrypt)
    {
        System.out.println("inside method");
        PreparedStatement pstmt=null;
        try{
            System.out.println(prof.getMiddlename()+" "+prof.getUsername());
            pstmt=myCon.prepareStatement("update instructor set user_name=?,first_name = ?, middle_name= ?, last_name = ?, sex = ?,email = ?,password = ? ,security_ques = ?, answer = ?, course_ids = ? where user_name = ?");
            pstmt.setString(1, prof.getUsername());
            pstmt.setString(2, prof.getFirstname());
            pstmt.setString(3, prof.getMiddlename());
            pstmt.setString(4, prof.getLastname());
            pstmt.setString(5, ""+prof.getSex());
            pstmt.setString(6, prof.getEmail());
            if(encrypt)
                pstmt.setString(7, EncryptPassword(prof.getPassword()));
            else
                pstmt.setString(7, prof.getPassword());
            pstmt.setString(8, prof.getSecurityques());
            pstmt.setString(9, prof.getAnswer());
            //##############################################################################
            String courseIds[]=prof.getCourseids();
            String courses="";
            for(int i=0;i<courseIds.length-1;i++)
            {
                courses=courses+courseIds[i]+"_";
            }
            courses=courses+courseIds[courseIds.length-1];
            pstmt.setString(10, courses);
            pstmt.setString(11, prof.getUsername());
            pstmt.executeUpdate();
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){
                    exc.printStackTrace();
                }
            }

        }
    }

    public void deleteProf(String user) {
        // TODO Auto-generated method stub
        PreparedStatement pstmt=null;
        try{
            //System.out.println("Course"+id);
            pstmt=myCon.prepareStatement("delete from instructor where user_name = ?");
            pstmt.setString(1, user);
            pstmt.executeUpdate();
        }catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
    }

    public List<String> getAllCourses(String user){
        ArrayList<String> course_taken = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs=null;
        try{
            pstmt=myCon.prepareStatement("select * from instructor where user_name=?");
            pstmt.setString(1, user);
            rs=pstmt.executeQuery();
            while(rs.next()){
                String c_name = rs.getString("course_ids");
                String str[]=c_name.split("_");
                for(int i=0;i<str.length;i++)
                    course_taken.add(str[i]);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
            if(rs!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
        System.out.println(course_taken.size());
        return course_taken;
    }

    public void updateCourseIds(String user,String course_id){
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        try{
            pstmt=myCon.prepareStatement("select course_ids from instructor where user_name = ?");
            pstmt.setString(1, user);
            rs=pstmt.executeQuery();
            String cid="";
            while(rs.next())
                cid=rs.getString("course_ids");
            //System.out.println("cid id ##### Hre = "+cid);
            if(cid==null||cid=="")
                cid+=course_id;
            else cid+="_"+course_id;

            //System.out.println("cid id ##### Hre After= "+cid);
            pstmt=myCon.prepareStatement("update instructor set course_ids= ? where user_name = ?");
            pstmt.setString(1, cid);
            pstmt.setString(2, user);
            pstmt.executeUpdate();
        }catch(Exception exc){exc.printStackTrace();}
        finally{
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(Exception exc){}
            }
        }
    }


	/*public void daleteCourseIds(String user,String course_id){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			pstmt=myCon.prepareStatement("select course_ids from professor where user_name = ?");
			pstmt.setString(1, user);
			rs=pstmt.executeQuery();
			String cid="";
			while(rs.next())
				cid=rs.getString("course_ids");
			//System.out.println("cid id ##### Hre = "+cid);
			int index=cid.indexOf(course_id);
			if(course_id.length()==cid.length()){
					cid="";
			}else cid=cid.substring(0, index-1)+cid.substring(index+course_id.length());
			System.out.println("cid id ##### Hre After= "+cid);
			pstmt=myCon.prepareStatement("update professor set course_ids= ? where user_name = ?");
			pstmt.setString(1, cid);
			pstmt.setString(2, user);
			pstmt.executeUpdate();
		}catch(Exception exc){exc.printStackTrace();}
		finally{
			if(pstmt!=null){
				try{
					pstmt.close();
				}catch(Exception exc){}
			}
		}
	}*/


}
