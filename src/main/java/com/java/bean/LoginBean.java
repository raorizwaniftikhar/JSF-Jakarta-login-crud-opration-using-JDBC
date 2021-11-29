package com.java.bean;

import com.java.database.DatabaseConnection;
import com.java.model.Login;
import com.java.model.Student;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Bean class for Login Screen
 */
@ManagedBean @SessionScoped
public class LoginBean {
	/**
	 * Database Query Variable
	 */
	public static PreparedStatement preparedStatement;
	public static Connection connection;
	public static ResultSet resultSet;
	/**
	* Create Login Model class Object because this object
	* use in  index.xhtml for put value in this object
	 * */
	Login login = new Login();

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	/**
	 *
	  *	Check user in database if user information exist in Database
	 *	then show Students details else go to error page
	 */
	public String validateUserLogin() {
		String navResult = "";
		System.out.println("login Username is= " + login.getUsername());
		Student student = null;
		try {
			connection = DatabaseConnection.getInstance().getMysqlConnection();
			preparedStatement = connection.prepareStatement("select * from student_record where student_email = ? and student_password = ? ");
			preparedStatement.setString(1, login.getUsername());
			preparedStatement.setString(2, login.getPassword());
			resultSet = preparedStatement.executeQuery();
			if(resultSet != null) {
				while(resultSet.next()){
				student = new Student();
				student.setId(resultSet.getInt("student_id"));
				student.setName(resultSet.getString("student_name"));
				student.setEmail(resultSet.getString("student_email"));
				student.setGender(resultSet.getString("student_gender"));
				student.setAddress(resultSet.getString("student_address"));
				student.setPassword(resultSet.getString("student_password"));
				}
			}
			connection.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		if (student != null) {
			navResult = "studentsList.xhtml?faces-redirect=true";
		} else {
			navResult = "failure.xhtml?faces-redirect=true";
		}
		return navResult;
	}
}