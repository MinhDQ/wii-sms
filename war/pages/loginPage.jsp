<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
<body>
	<h1>Wii SMS</h1>

	<hr />

	<h2>Login</h2>
	<%
		String loginURL = UserServiceFactory.getUserService().createLoginURL("/");
		String logoutURL = UserServiceFactory.getUserService().createLogoutURL("/");
		     
		%>
	Login? <a href="<%=loginURL%>">Click here</a>
	Logout? <a href="<%=logoutURL%>">Click here</a>

</body>
</html>