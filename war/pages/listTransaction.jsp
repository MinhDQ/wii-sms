<jsp:root version="1.2" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:display="urn:jsptld:http://displaytag.sf.net">
  <jsp:directive.page contentType="text/html; charset=UTF-8" />
  <jsp:directive.page import="java.util.List" />
  <jsp:directive.page import="com.google.appengine.api.datastore.Entity" />
  <jsp:directive.page import="vn.wiisms.model.*" />

	<jsp:include page="header.jsp" flush="true"/>
    
	<h2>All Transactions</h2>
	
  <display:table name="transactionList" class="simple" pagesize="10">
  	<display:setProperty name="paging.banner.placement" value="bottom"/>
  	 <display:column property="transUID" title="ID" class="idcol"/>
  	 <display:column property="senderNumber" title="Sender"/>
  	 <display:column property="receiverNumber" title="Receiver"/>
  	 <display:column property="originalSMS" title="Original SMS"/>
  	 <display:column property="amount" title="Amount"/>
  	 <display:column property="description" title="Description"/>
  	 <display:column property="status" title="Status"/>
  	 <!--display:column property="createDate" title="Created Date"/>
  	 <display:column property="modifiDate" title="Modified Date"/>
  	 <display:column property="createUserName" title="Created User"/-->
  	 <display:column property="modifiUserName" title="Modified User"/>
  	   
  </display:table>
  
    <jsp:include page="footer.jsp" flush="true"/>
</jsp:root>
    