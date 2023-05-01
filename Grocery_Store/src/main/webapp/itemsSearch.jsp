<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Start</title>
<link rel="Stylesheet" href="Stylesheet1.css">
</head>
<body>
		<%@ page import = "java.sql.*, javax.servlet.*, javax.servlet.http.*" %>
		<%
		try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GROCERY_STORE", "root", "password");){
			Statement st = con.createStatement();
			String query = "SELECT * FROM ITEMS WHERE Name LIKE '%" + request.getHeader("key") + "%'";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
		%>
		<div class="flex itemrows">
			<div class="items">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Price") %>/-</p>
				</div>
				<input type="number" ondblclick="addtoCart('<%= rs.getInt("ID") %>', this.value)">Add To Cart<br>*double click to enter
				<div style="width: 200px;"></div>
			</div>
		<% if(rs.next()){ %>
			<div class="items">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Price") %>/-</p>
				</div>
				<input type="number" ondblclick="addtoCart('<%= rs.getInt("ID") %>', this.value)">Add To Cart<br>*double click to enter
				<div style="width: 200px;"></div>
			</div>
		<% } %>
		</div>
		<%
			}%>
			<p>End Of List</p>
		<%	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		%>
</body>
</html>