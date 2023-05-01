

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
 * Servlet implementation class Item_handler
 */
@WebServlet("/ProductsModification")
public class Item_handler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static boolean checkString(String arg) {
		if(arg == null || arg.trim().equals("")) {
			return true;
		}
		return false;
	}
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Started");
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			Connection con = CreateConnection.create();
			String method = request.getParameter("method");
			String form = request.getParameter("formType");
			
			if(!checkString(form) && form.equals("insert")) {
				con.setAutoCommit(false);
				RequestDispatcher add = request.getRequestDispatcher("/Shopkeeper_page.html");
				try(PreparedStatement insert = con.prepareStatement("INSERT INTO ITEMS VALUES(?,?,?,?,?,?,?)");){
					con.setAutoCommit(false);
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT MAX(ID) FROM ITEMS");
					int ID = 0;
					while(rs.next()) {
						ID = 1 + rs.getInt("MAX(ID)");
						if(ID == 1) {
							st.executeUpdate("DELETE FROM ITEMS WHERE ID = 0");
						}
					}
					insert.setInt(1, ID);
					st.close();
					
					String name = request.getParameter("name");
					name += request.getParameter("description");
					if(checkString(name)) {
						out.println("Enter product name");
						add.include(request, response);
					}
					insert.setString(2, name);
					
					HttpSession session = request.getSession();
					String vendor = (String)session.getAttribute("Username");
					if(checkString(vendor)) {
						out.println("Username not found");
						add.include(request, response);
					}
					insert.setString(3, vendor);
					
					String quantity = request.getParameter("quantity");
					if(checkString(quantity)) {
						out.println("Enter product quantity");
						add.include(request, response);
					}
					insert.setInt(4, Integer.parseInt(quantity));
					
					String price = request.getParameter("price");
					if(checkString(price)) {
						out.println("Enter product price");
						add.include(request, response);
					}
					insert.setInt(5, Integer.parseInt(price));
					
					String img = request.getParameter("image");
					if(checkString(img)) {
						out.println("Provide product image URL");
						add.include(request, response);
					}
					insert.setString(6, img);
					
					String cat = request.getParameter("category");
					if(checkString(cat)) {
						out.println("Select category");
						add.include(request, response);
					}
					insert.setString(7, cat);
					insert.executeUpdate();
					con.commit();
					insert.close();
					out.println("Entry successful!");
					add.include(request, response);
				}
				catch(SQLException x) {
					try {
						con.rollback();
						x.printStackTrace();
						out.println("<p>Try again server was busy</p>");
						add.include(request, response);
					}
					catch(SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			else if(!checkString(method) && method.equals("update")) {
				try(Statement update = con.createStatement();){
					con.setAutoCommit(false);
					int quantity = Integer.parseInt(request.getHeader("quantity"));
					int ID = Integer.parseInt(request.getHeader("itemID"));
					update.executeUpdate("UPDATE ITEMS SET Quantity = " + quantity + " WHERE ID = " + ID);
					con.commit();
					response.sendRedirect("Shopkeeper_page.html");
				} catch(SQLException x) {
					try {
						con.rollback();
						out.println("Something went wrong please try again");
						response.sendRedirect("itemList.jsp");
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
			else if(!checkString(method) && method.equals("updatePrice")) {
				try(Statement update = con.createStatement();){
					con.setAutoCommit(false);
					int price = Integer.parseInt(request.getHeader("price"));
					int ID = Integer.parseInt(request.getHeader("itemID"));
					update.executeUpdate("UPDATE ITEMS SET Price = " + price + " WHERE ID = " + ID);
					con.commit();
					response.sendRedirect("Shopkeeper_page.html");
				} catch(SQLException x) {
					try {
						con.rollback();
						out.println("Something went wrong please try again");
						response.sendRedirect("itemList.jsp");
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
			else if(!checkString(method) && method.equals("delete")) {
				try(Statement delete = con.createStatement();){
					con.setAutoCommit(false);
					int ID = Integer.parseInt(request.getHeader("itemID"));
					delete.executeUpdate("DELETE FROM ITEMS WHERE ID = " + ID);
					con.commit();
					response.sendRedirect("Shopkeeper_page.html");
				}
				catch(SQLException e) {
					try {
						con.rollback();
						out.println("Something went wrong please try again");
						response.sendRedirect("itemList.jsp");
					} catch(SQLException x) {
						x.printStackTrace();
					}
				}
			}
			else {
				System.out.println("In else condition, method: " + method);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
