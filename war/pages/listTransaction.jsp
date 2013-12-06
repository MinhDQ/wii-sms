<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<html>
<body>
	<h1>Wii SMS</h1>

	Function : <a href="addTransactionPage">Add Transaction</a>
	<hr />

	<h2>All Transactions</h2>
	<table border="1">
		<thead>
			<tr>
				<td>Sender</td>
				<td>Amount</td>
				<td>Created Date</td>
				<td>Action</td>
			</tr>
		</thead>
		<%
		
			List<Entity> transactions = (List<Entity>)request.getAttribute("transactionList");
		    for(Entity e : transactions){
		     
		%>
			<tr>
				<td><%=e.getProperty("sender_num") %></td>
				<td><%=e.getProperty("amount") %></td>
				<td><%=e.getProperty("create_date") %></td>
				<td><a href="update/<%=e.getProperty("name")%>">Update</a> | <a href="delete/<%=e.getProperty("name")%>">Delete</a></td>
			</tr>
		<%
			}
		%>
	</table>

</body>
</html>