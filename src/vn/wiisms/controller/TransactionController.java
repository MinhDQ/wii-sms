package vn.wiisms.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import vn.wiisms.enums.TransactionStatus;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
	
	@RequestMapping(value="/addTransactionPage", method = RequestMethod.GET)
	public String getAddCustomerPage(ModelMap model) {

		return "addTransaction";

	}
	
	@RequestMapping(value="/newTransaction", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, ModelMap model) {
		
		String 		senderNumber 	= request.getParameter("sender");
		String 		originalSMS 	= request.getParameter("sms");
		String[] 	smsMess 		= originalSMS.split("_",3);

		String 	receiverNumber 		= smsMess[0];
		int 	amount 				= Integer.parseInt(smsMess[1]);
		String 	description 		= smsMess[2];
		
		Date createDate = new Date();
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        Entity transaction = new Entity("Transaction");
        transaction.setProperty("sender_num", senderNumber);
        transaction.setProperty("receir_num", receiverNumber);
        transaction.setProperty("origin_sms", originalSMS);
        transaction.setProperty("amount", amount);
        transaction.setProperty("desc", description);
        transaction.setProperty("status", TransactionStatus.NEW.toString());
        transaction.setProperty("create_date", createDate);
        transaction.setProperty("modifi_date", createDate);		//new transaction
        transaction.setProperty("create_user", user);
        transaction.setProperty("modifi_user", user);			//new transaction

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(transaction);
        
        return new ModelAndView("redirect:listTransaction");
        
	}
		
	@RequestMapping(value="/updateTranaction/{trans_uid}", method = RequestMethod.GET)
	public String getUpdateCustomerPage(@PathVariable String transactionUID, 
			HttpServletRequest request, ModelMap model) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Transaction");
		query.addFilter("trans_uid", FilterOperator.EQUAL, transactionUID);
		PreparedQuery pq = datastore.prepare(query);
		
		Entity e = pq.asSingleEntity();
		model.addAttribute("Transaction",  e);
		
		return "update";

	}
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, ModelMap model) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		 
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String originalName =  request.getParameter("originalName");
		
		Query query = new Query("Customer");
		query.addFilter("name", FilterOperator.EQUAL, originalName);
		PreparedQuery pq = datastore.prepare(query);
		Entity customer = pq.asSingleEntity();
		
		customer.setProperty("name", name);
		customer.setProperty("email", email);
		customer.setProperty("date", new Date());

        datastore.put(customer);
        
        //return to list
        return new ModelAndView("redirect:list");
        
	}
		
	@RequestMapping(value="/deleteTransaction/{trans_uid}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String transactionUID,
			HttpServletRequest request, ModelMap model) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Query query = new Query("Transaction");
		query.addFilter("trans_uid", FilterOperator.EQUAL, transactionUID);
		PreparedQuery pq = datastore.prepare(query);
		Entity transaction = pq.asSingleEntity();
		
        datastore.delete(transaction.getKey());
        
        //return to list
        return new ModelAndView("redirect:../listTransaction");
        
	}
	
	
	//get all customers
	@RequestMapping(value="/listTransaction", method = RequestMethod.GET)
	public String listCustomer(ModelMap model) {

		if(!UserServiceFactory.getUserService().isUserLoggedIn())
			return "loginPage";
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Transaction").addSort("creat_date", Query.SortDirection.DESCENDING);
	    List<Entity> customers = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
	    
	    model.addAttribute("transactionList",  customers);
	    
		return "listTransaction";

	}
	
}