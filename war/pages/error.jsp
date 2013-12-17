<jsp:root version="1.2" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:display="urn:jsptld:http://displaytag.sf.net">
  <jsp:directive.page contentType="text/html; charset=UTF-8" />
  <jsp:directive.page import="java.util.List" />
  <jsp:directive.page import="com.google.appengine.api.datastore.Entity" />
  <jsp:directive.page import="vn.wiisms.model.*" />

	<jsp:include page="header.jsp" flush="true"/>
    
	<h2>Error!</h2>
	<p>${param.error}.</p>
	
    <jsp:include page="footer.jsp" flush="true"/>
</jsp:root>
    