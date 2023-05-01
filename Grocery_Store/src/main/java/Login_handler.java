

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login_handler
 */
@WebServlet("/userSignin")
public class Login_handler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static boolean checkString(String argument) {
		if(argument == null || argument.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			Connection con = CreateConnection.create();
			
			String form = request.getParameter("formName");
			
			if(form.equals("login")) {
				RequestDispatcher add = request.getRequestDispatcher("/LoginPage.html");
				Statement check = con.createStatement();
				String username = request.getParameter("Username");
				if(checkString(username)) {
					out.println("<p>Enter Username</p>");
					add.include(request, response);
				}
				String password = request.getParameter("Password");
				if(checkString(password)) {
					out.println("<p>Enter password</p>");
					add.include(request, response);
				}
				ResultSet rs = check.executeQuery("SELECT * FROM USERS");
				String flag = "none";
				while(rs.next()) {
					if(username.equals(rs.getString("Username"))) {
						if(password.equals(rs.getString("Password"))) {
							if(rs.getString("Category").equals("Users")) {
								flag = "Users";
							}
							else if(rs.getString("Category").equals("Shopkeeper")) {
								flag = "Shopkeeper";
							}
							else if(rs.getString("Category").equals("Admin")) {
								flag = "Admin";
							}
						}
						else {
							flag = "ip";
						}
					}
				}
				if(flag.equals("Users")) {
					HttpSession session = request.getSession();
					session.setMaxInactiveInterval(30*60);
					session.setAttribute("category", "Users");
					session.setAttribute("Username", username);
					response.sendRedirect("User_startPage.html");					
				}
				else if(flag.equals("Shopkeeper")) {
					HttpSession session = request.getSession();
					session.setMaxInactiveInterval(30*60);
					session.setAttribute("category", "Shopkeeper");
					session.setAttribute("Username", username);
					response.sendRedirect("Shopkeeper_page.html");
				}
				else if(flag.equals("Admin")) {
					HttpSession session = request.getSession();
					session.setMaxInactiveInterval(30*60);
					session.setAttribute("category", "Admin");
					response.sendRedirect("Admin_page.html");					
				}
				else if(flag.equals("ip")) {
					out.println("Incorrect password");
					add.include(request, response);
				}
				else if(flag.equals("none")) {
					out.println("User not found. Please register first.");
					response.sendRedirect("RegisterPage.html");					
				}
			}
			
			if(form.equals("register")) {
				con.setAutoCommit(false);
				RequestDispatcher add = request.getRequestDispatcher("/RegisterPage.html");
				HttpSession session = request.getSession();
				if(session.getAttribute("category").equals("Admin")) {
					add = request.getRequestDispatcher("/Admin_page.html");
				}
				try(PreparedStatement insert = con.prepareStatement("INSERT INTO USERS VALUES(?,?,?,?,?,?,?,?,?,?)")){
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT MAX(ID) FROM USERS");
					int ID = 0;
					while(rs.next()) {
						ID = 1 + rs.getInt("MAX(ID)");
					}
					insert.setInt(1, ID);
					st.close();
					
					String name = request.getParameter("name");
					if(checkString(name)) {
						out.println("<p>Enter name</p>");
						add.include(request, response);
					}
					insert.setString(2, name);
					
					String num = request.getParameter("ph_no");
					if(checkString(num)) {
						out.println("<p>Enter phone no</p>");
						add.include(request, response);
					}
					insert.setLong(3, Long.parseLong(num));
					
					String mail = request.getParameter("email");
					if(checkString(mail)) {
						out.println("<p>Enter emailid</p>");
						add.include(request, response);
					}
					insert.setString(4, mail);
					
					String age = request.getParameter("age");
					if(checkString(age)) {
						out.println("<p>Enter age</p>");
						add.include(request, response);
					}
					insert.setInt(5, Integer.parseInt(age));
					
					String address = request.getParameter("address");
					if(checkString(address)) {
						out.println("<p>Select address</p>");
						add.include(request, response);
					}
					insert.setString(6, address);
					
					String gender = request.getParameter("gender");
					if(checkString(gender)) {
						out.println("<p>Select gender</p>");
						add.include(request, response);
					}
					insert.setString(7, gender);
					
					String category = request.getParameter("category");
					insert.setString(8, category);
					
					String passwd = request.getParameter("Password");
					if(checkString(passwd)) {
						out.println("<p>Enter password</p>");
						add.include(request, response);
					}
					insert.setString(9, passwd);
					
					String usrname = request.getParameter("username");
					if(checkString(usrname)) {
						out.println("<p>Enter username</p>");
						add.include(request, response);
					}
					Statement check = con.createStatement();
					ResultSet check1 = check.executeQuery("SELECT ID FROM USERS WHERE Username = '" + usrname + "'");
					if(check1.next()) {
						out.println("<p>This username is already taken. Please try a different one</p>");
						add.include(request, response);
					}
					insert.setString(10, usrname);
					check.close();
					insert.executeUpdate();
					con.commit();
					if(category.equals("Users")) {
						response.sendRedirect("User_startPage.html");
					}
					else if(category.equals("Shopkeeper")) {
						add.include(request, response);
						out.println("<p>Entry successful!</p>");
					}
				}
				catch(SQLException e) {
					try {
						con.rollback();
						add.include(request, response);
						out.println("<p>Please try again, server was busy</p>");
					}
					catch(SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
