
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/cartHandler")
public class cartHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection con = CreateConnection.create()) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String method = request.getParameter("method");
			if (method.equals("add")) {
				try (PreparedStatement add = con.prepareStatement("INSERT INTO CART VALUES (?,?,?)");) {
					con.setAutoCommit(false);
					Statement st = con.createStatement();
					HttpSession session = request.getSession();
					ResultSet rs = st.executeQuery(
							"SELECT ID FROM USERS WHERE Username = '" + session.getAttribute("Username") + "'");
					int ID = 0;
					while (rs.next()) {
						ID = rs.getInt("ID");
					}
					add.setInt(1, ID);

					int itemID = Integer.parseInt(request.getHeader("itemID"));
					add.setInt(2, itemID);

					String quantity = request.getHeader("quantity");
					if (quantity == null || quantity.trim().equals("")) {
						System.out.println("Quantity: " + quantity);
					}
					int q = Integer.parseInt(quantity);
					add.setInt(3, q);
					add.executeUpdate();
					con.commit();
					response.sendRedirect("User_cart.java");
				} catch (SQLException e) {
					try {
						con.rollback();
						out.println("Something went wrong please try again");
					} catch (SQLException x) {
						x.printStackTrace();
					}
				}
			} else if (method.equals("checkout")) {
				try (Statement st = con.createStatement()) {
					con.setAutoCommit(false);
					HttpSession session = request.getSession();
					ResultSet rs = st.executeQuery(
							"SELECT ID FROM USERS WHERE Username = '" + session.getAttribute("Username") + "'");
					int ID = 0;
					while (rs.next()) {
						ID = rs.getInt("ID");
					}
					rs = st.executeQuery("SELECT * FROM CART, ITEMS WHERE Item_id = ITEMS.ID AND User_id = " + ID);
					try {
						while (rs.next()) {
							PreparedStatement add = con.prepareStatement("INSERT INTO TRANSACTIONS VALUES(?,?,?,?,?)");
							con.setAutoCommit(false);
							add.setInt(1, ID);
							add.setInt(2, rs.getInt("Item_id"));
							add.setInt(3, rs.getInt("Item_quantity"));
							LocalDate date = LocalDate.now();
							String trans_date = date.getYear() + "-" + date.getMonthValue() + "-"
									+ date.getDayOfMonth();
							int cost = rs.getInt("Price") * rs.getInt("Item_quantity");
							add.setInt(4, cost);
							add.setString(5, trans_date);
							add.executeUpdate();
							con.commit();
							System.out.println("Transaction successful!");
							try(Statement update = con.createStatement()){
								con.setAutoCommit(false);
								ResultSet r = update.executeQuery("SELECT * FROM ITEMS WHERE ID = " + rs.getInt("Item_id"));
								r.next();
								int newQuantity = r.getInt("Quantity") - rs.getInt("Item_quantity");
								update.executeUpdate("UPDATE ITEMS SET Quantity = " + newQuantity + " WHERE ID = " + rs.getInt("Item_id"));
								con.commit();
							}catch (SQLException e) {
								try {
									con.rollback();
									e.printStackTrace();
									out.println("Something went wrong please try again");
								} catch (SQLException x) {
									x.printStackTrace();
								}
							}
						}
						try (Statement delete = con.createStatement()) {
							con.setAutoCommit(false);
							String query = "DELETE FROM CART WHERE User_id = " + ID;
							delete.executeUpdate(query);
							con.commit();
							System.out.println("Delete cart successful!");
						} catch (SQLException e) {
							try {
								con.rollback();
								e.printStackTrace();
								out.println("Something went wrong please try again");
							} catch (SQLException x) {
								x.printStackTrace();
							}
						}

					} catch (SQLException e) {
						try {
							con.rollback();
							e.printStackTrace();
							out.println("Something went wrong please try again");
						} catch (SQLException x) {
							x.printStackTrace();
						}
					}
					con.commit();
					response.sendRedirect("User_startPage.html");
				} catch (SQLException e) {
					try {
						con.rollback();
						out.println("Something went wrong please try again");
						e.printStackTrace();
					} catch (SQLException x) {
						x.printStackTrace();
					}
				}
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

}
