<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@ page import="java.sql.*, javax.servlet.*, javax.servlet.http.*"%>
	<%
	try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GROCERY_STORE", "root", "password");) {
		session = request.getSession();
		Statement st = con.createStatement();
		String pageName = request.getParameter("page");
		String query = "SELECT * FROM ITEMS WHERE Vendor = '" + session.getAttribute("Username") + "'";
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
	%>
	<div class="flex itemrows">
		<div class="items">
			<img style="margin: 10px; width: 300px; height: 300px;"
				src=<%=rs.getString("Image")%>>
			<div class="flex"
				style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%=rs.getString("Name")%></p>
				<p style="font-size: large;"><%=rs.getInt("Price")%></p>
				<p style="font-size: large;"><%=rs.getInt("Quantity") %></p>
				<button onclick="deleteProduct('<%= rs.getInt("ID") %>')">Delete</button>
				Update quantity:<input type="number" ondblclick="updateProduct(this.value, '<%= rs.getInt("ID") %>')">
				Update price:<input type="number" ondblclick="updatePrice(this.value, '<%= rs.getInt("ID") %>')"> *double click to update
				
			</div>
			<div style="width: 200px;"></div>
		</div>
		<%
		if (rs.next()) {
		%>
		<div class="items">
			<img style="margin: 10px; width: 300px; height: 300px;"
				src=<%=rs.getString("Image")%>>
			<div class="flex"
				style="position: relative; bottom: 15px; justify-content: center; align-items: center;">
				<p style="font-size: large;"><%=rs.getString("Name")%></p>
				<p style="font-size: large;"><%=rs.getInt("Price")%></p>
				<p style="font-size: large;"><%=rs.getInt("Quantity") %></p>
				<button onclick="deleteProduct('<%= rs.getInt("ID") %>')">Delete</button>
				Update quantity:<input type="number" ondblclick="updateProduct(this.value, '<%= rs.getInt("ID") %>')">
				Update price:<input type="number" ondblclick="updatePrice(this.value, '<%= rs.getInt("ID") %>')"> *double click to update
			</div>
			<div style="width: 200px;"></div>
		</div>
		<%
		}
		%>
	</div>
	<%
	}
	}
	catch (SQLException e) {
	e.printStackTrace();
	}
	%>
</body>
</html>