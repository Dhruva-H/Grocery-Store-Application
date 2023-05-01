<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cart</title>
<link rel="Stylesheet" href="Stylesheet1.css">
</head>
<body>
		<%@ page import = "java.sql.*, javax.servlet.*, javax.servlet.http.*" %>
		<%
		try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GROCERY_STORE", "root", "password");){
			Statement st = con.createStatement();
			session = request.getSession();
			ResultSet rs = st.executeQuery("SELECT ID FROM USERS WHERE Username = '" + session.getAttribute("Username") + "'");
			int ID = 0;
			while(rs.next()) {
				ID = rs.getInt("ID");
			}
			String query = "SELECT * FROM USER_CART WHERE User_id = " + ID;
			rs = st.executeQuery(query);
			while(rs.next()){
		%>
		<div class="flex itemrows">
			<div class="items">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Price") %>/-</p>
				<p style="font-size: large;"><%= rs.getInt("Item_quantity") %></p>
				</div>
				<div style="width: 200px;"></div>
			</div>
		<% if(rs.next()){ %>
			<div class="items">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Price") %>/-</p>
				<p style="font-size: large;"><%= rs.getInt("Item_quantity") %></p>
				</div>
				<div style="width: 200px;"></div>
			</div>
		<% } %>
		</div>
		<%
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		%>
</body>
</html>