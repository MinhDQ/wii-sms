<jsp:root version="1.2" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:display="urn:jsptld:http://displaytag.sf.net">
  <jsp:directive.page contentType="text/html; charset=UTF-8" />
  <jsp:directive.page import="vn.wiisms.model.*" />

	<link href="/pages/css/maven-base.css" rel="stylesheet" type="text/css"/>
	<link href="/pages/css/maven-theme.css" rel="stylesheet" type="text/css"/>
	<link href="/pages/css/site.css" rel="stylesheet" type="text/css"/>
	<link href="/pages/css/screen.css" rel="stylesheet" type="text/css"/>

 

  <p>The simplest possible usage of the table tag is to point the table tag at a java.util.List implementation and do
  nothing else. The table tag will iterate through the list and display a column for each property contained in the
  objects.</p>

  <p>Typically, the only time that you would want to use the tag in this simple way would be during development as a
  sanity check. For production, you should always define at least a single column.</p>

</jsp:root>