<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>

	<jsp:include page="header.jsp"/>
    
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
				<td><a href="updateTransaction/<%=e.getKey().getId()%>">Update</a> | <a href="deleteTransaction/<%=e.getKey().getId()%>">Delete</a></td>
			</tr>
		<%
			}
		%>
	</table>
	
    <jsp:include page="footer.jsp"/>