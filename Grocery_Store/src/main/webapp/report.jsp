<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Report</title>
<link rel="Stylesheet" href="Stylesheet1.css">
</head>
<body>
		<%@ page import = "java.sql.*, javax.servlet.*, javax.servlet.http.*, java.time.LocalDate" %>
		<%
		try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GROCERY_STORE", "root", "password");){
			Statement st = con.createStatement();
			String input = request.getHeader("sDate");
			LocalDate sDate = LocalDate.parse(input);
			input = request.getHeader("eDate");
			LocalDate eDate = LocalDate.parse(input);
			String start = sDate.getYear() + "-" + sDate.getMonthValue() + "-" + sDate.getDayOfMonth();
			String end = eDate.getYear() + "-" + eDate.getMonthValue() + "-" + eDate.getDayOfMonth();
			session = request.getSession();
			String query = "SELECT * FROM TRANSACTIONS, ITEMS WHERE Item_id = ID AND Vendor = '" + session.getAttribute("Username") + "' AND Date BETWEEN '" + start + "' AND '" + end + "'";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
		%>
		<div class="flex itemrows">
			<div class="items" style="width: auto;">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: space-between; align-items: center; height: 50%;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Cost") %>/-</p>
				<p style="font-size: large;"><%= rs.getInt("TRANSACTIONS.Quantity") %></p>
				<p style="font-size: large;"><%= rs.getString("Date") %></p>
				</div>
			</div>
		<% if(rs.next()) { %>
			<div class="items" style="width: auto;">
				<img style="margin: 10px; width: 300px; height: 300px;" src=<%= rs.getString("Image") %>>
				<div class="flex" style="position: relative; bottom: 15px; justify-content: space-between; align-items: center; height: 50%;">
				<p style="font-size: large;"><%= rs.getString("Name") %></p>
				<p style="font-size: large;"><%= rs.getInt("Cost") %>/-</p>
				<p style="font-size: large;"><%= rs.getInt("TRANSACTIONS.Quantity") %></p>
				<p style="font-size: large;"><%= rs.getString("Date") %></p>
				</div>
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