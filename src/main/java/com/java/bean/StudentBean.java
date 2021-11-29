package com.java.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.java.model.Student;
import com.java.database.DatabaseConnection;
import jakarta.faces.bean.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.bean.ManagedBean;


/**
 * Student Bean Class for Student CRUD operation
 * Database Query is written here
 */

@RequestScoped
@ManagedBean
public class StudentBean {


	public static Statement statement;
	public static Connection connection;
	public static ResultSet resultSet;
	public static PreparedStatement preparedStatement;

	Student student = new Student();
	List<Student> students;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	/* Method To Fetch The Student Records From Database */
	public List<Student> studentsList() {
		students = new ArrayList<Student>();
		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from student_record");
			while(resultSet.next()) {
				Student stuObj = new Student();
				stuObj.setId(resultSet.getInt("student_id"));
				stuObj.setName(resultSet.getString("student_name"));
				stuObj.setEmail(resultSet.getString("student_email"));
				stuObj.setPassword(resultSet.getString("student_password"));
				stuObj.setGender(resultSet.getString("student_gender"));
				stuObj.setAddress(resultSet.getString("student_address"));
				students.add(stuObj);
			}   
			System.out.println("Total Records Fetched: " + students.size());
			connection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		} 
		return students;
	}

	/* Method Used To Save New Student Record In Database */
	public String saveStudent() {
		int saveResult = 0;
		String navigationResult = "";
		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			preparedStatement = connection.prepareStatement("insert into student_record (student_name, student_email, student_password, student_gender, student_address) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, student.getName());
			preparedStatement.setString(2, student.getEmail());
			preparedStatement.setString(3, student.getPassword());
			preparedStatement.setString(4, student.getGender());
			preparedStatement.setString(5, student.getAddress());
			saveResult = preparedStatement.executeUpdate();
			connection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		if(saveResult !=0) {
			navigationResult = "studentsList.xhtml?faces-redirect=true";
		} else {
			navigationResult = "createStudent.xhtml?faces-redirect=true";
		}
		return navigationResult;
	}

	/* Method Used To Edit Student Record In Database */
	public  String getStudentById(int studentId) {
		Student editRecord = null;
		System.out.println("editStudentRecordInDB() : Student Id: " + studentId);

		/* Setting The Particular Student Details In Session */
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from student_record where student_id = "+studentId);
			if(resultSet != null) {
				resultSet.next();
				editRecord = new Student();
				editRecord.setId(resultSet.getInt("student_id"));
				editRecord.setName(resultSet.getString("student_name"));
				editRecord.setEmail(resultSet.getString("student_email"));
				editRecord.setGender(resultSet.getString("student_gender"));
				editRecord.setAddress(resultSet.getString("student_address"));
				editRecord.setPassword(resultSet.getString("student_password"));
			}
			sessionMapObj.put("editRecordObj", editRecord);
			connection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/editStudent.xhtml?faces-redirect=true";
	}

	/* Method Used To Update Student Record In Database */
	public  String updateStudent(Student student) {
		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			preparedStatement = connection.prepareStatement("update student_record set student_name=?, student_email=?, student_password=?, student_gender=?, student_address=? where student_id=?");
			preparedStatement.setString(1,student.getName());
			preparedStatement.setString(2,student.getEmail());
			preparedStatement.setString(3,student.getPassword());
			preparedStatement.setString(4,student.getGender());
			preparedStatement.setString(5,student.getAddress());
			preparedStatement.setInt(6,student.getId());
			preparedStatement.executeUpdate();
			connection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		return "/studentsList.xhtml?faces-redirect=true";
	}

	/* Method Used To Delete Student Record From Database */
	public  String deleteStudentById(int studentId){
		System.out.println("deleteStudent() : Student Id: " + studentId);
		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			preparedStatement = connection.prepareStatement("delete from student_record where student_id = "+studentId);
			preparedStatement.executeUpdate();
			connection.close();
		} catch(Exception sqlException){
			sqlException.printStackTrace();
		}
		return "/studentsList.xhtml?faces-redirect=true";
	}
}