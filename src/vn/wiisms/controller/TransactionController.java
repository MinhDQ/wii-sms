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
	
	@RequestMapping(value="/newTransaction", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, ModelMap model) {
		
		String transactionUID = request.getParameter("id");
		String name = request.getParameter("sender");
		String email = request.getParameter("sms");
		
	    Key transactionKey = KeyFactory.createKey("Transaction", name);
	        
		Date createDate = new Date();
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        Entity transaction = new Entity("Transaction", transactionKey);
        transaction.setProperty("trans_uid", transactionUID);
        transaction.setProperty("name", name);
        transaction.setProperty("email", email);
        transaction.setProperty("creat_date", createDate);
        transaction.setProperty("modif_date", createDate);
        transaction.setProperty("creat_user", user);
        transaction.setProperty("modif_user", user);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(transaction);
        
        return new ModelAndView("redirect:list");
        
	}
		
	@RequestMapping(value="/update/{name}", method = RequestMethod.GET)
	public String getUpdateCustomerPage(@PathVariable String name, 
			HttpServletRequest request, ModelMap model) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Customer");
		query.addFilter("name", FilterOperator.EQUAL, name);
		PreparedQuery pq = datastore.prepare(query);
		
		Entity e = pq.asSingleEntity();
		model.addAttribute("customer",  e);
		
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
		
	@RequestMapping(value="/delete/{name}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String name,
			HttpServletRequest request, ModelMap model) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Query query = new Query("Customer");
		query.addFilter("name", FilterOperator.EQUAL, name);
		PreparedQuery pq = datastore.prepare(query);
		Entity customer = pq.asSingleEntity();
		
        datastore.delete(customer.getKey());
        
        //return to list
        return new ModelAndView("redirect:../list");
        
	}
	
	
	//get all customers
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String listCustomer(ModelMap model) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Customer").addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> customers = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
	    
	    model.addAttribute("customerList",  customers);
	    
		return "list";

	}
	
}