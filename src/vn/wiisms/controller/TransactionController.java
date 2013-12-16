package vn.wiisms.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import vn.wiisms.enums.TransactionStatus;
import vn.wiisms.model.Transaction;

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

	@RequestMapping(value = "/addTransactionPage", method = RequestMethod.GET)
	public String getAddCustomerPage(ModelMap model) {

		return "addTransaction";

	}

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String error(ModelMap model) {

		return "error";

	}

	@RequestMapping(value = "/newTransaction", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, ModelMap model) {

		String result = addTransaction(request);
		model.addAttribute("error", result);

		if (result.contains("ERROR"))
			return new ModelAndView("redirect:error");
		else
			return new ModelAndView("redirect:listTransaction");
	}

	@RequestMapping(value = "/newTransaction8x00", method = RequestMethod.POST)
	@ResponseBody
	public String add8x00(HttpServletRequest request, ModelMap model) {

		return addTransaction(request);

	}

	private String addTransaction(HttpServletRequest request) {
		try {

			String senderNumber = request.getParameter("sender");
			// Validation
			if (!senderNumber.matches("[0-9]+") || senderNumber.length() < 8
					|| senderNumber.length() > 12)
				return "ERROR: senderNumber is not well-formed";

			String originalSMS = request.getParameter("sms");
			// Validation
			if (StringUtils.countOccurrencesOf(originalSMS, "_") < 1
					|| StringUtils.countOccurrencesOf(originalSMS, "_") > 2)
				return "ERROR: SMS is not well-formed. \"_\" character should appears 1 to 2 times";

			String[] smsMess = originalSMS.split("_", 3);

			String receiverNumber = smsMess[0];
			// Validation
			if (!receiverNumber.matches("[0-9]+")
					|| receiverNumber.length() < 8
					|| receiverNumber.length() > 12)
				return "ERROR: receiverNumber is not well-formed";

			// Validation
			if (!smsMess[1].matches("[0-9]+") || smsMess[1].length() < 5
					|| smsMess[1].length() > 7)
				return "ERROR: money amount must be in threshold (10000 - 9999000)";

			int amount = Integer.parseInt(smsMess[1]);

			String description = "";
			if (StringUtils.countOccurrencesOf(originalSMS, "_") == 2)
				description = smsMess[2];

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
			transaction.setProperty("modifi_date", createDate); // new
																// transaction
			transaction.setProperty("create_user", user);
			transaction.setProperty("modifi_user", user); // new transaction

			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			datastore.put(transaction);
			
			return receiverNumber + ":" + senderNumber + "_" + amount + "_"
					+ description;
		} catch (Exception e) {
			return "ERROR: Unknow exception";
		}

	}

	@RequestMapping(value = "/updateTranaction/{transactionUID}", method = RequestMethod.GET)
	public String getUpdateCustomerPage(@PathVariable String transactionUID,
			HttpServletRequest request, ModelMap model) {

		Key key = KeyFactory.createKey("Transaction",
				Long.valueOf(transactionUID));

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query query = new Query("Transaction");
		query.setFilter(FilterOperator.EQUAL.of("__key__", key));
		PreparedQuery pq = datastore.prepare(query);

		Entity e = pq.asSingleEntity();
		model.addAttribute("Transaction", e);

		return "update";

	}

	@RequestMapping(value = "/updateTransaction", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, ModelMap model) {

		// DatastoreService datastore =
		// DatastoreServiceFactory.getDatastoreService();

		// return to list
		return new ModelAndView("redirect:list");

	}

	@RequestMapping(value = "/deleteTransaction/{transactionUID}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String transactionUID,
			HttpServletRequest request, ModelMap model) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Key key = KeyFactory.createKey("Transaction",
				Long.valueOf(transactionUID));

		Query query = new Query("Transaction");
		query.setFilter(FilterOperator.EQUAL.of("__key__", key));

		PreparedQuery pq = datastore.prepare(query);
		Entity transaction = pq.asSingleEntity();

		datastore.delete(transaction.getKey());

		// return to list
		return new ModelAndView("redirect:../listTransaction");

	}

	// get all customers
	@RequestMapping(value = "/listTransaction", method = RequestMethod.GET)
	public String listCustomer(ModelMap model) {

		if (!UserServiceFactory.getUserService().isUserLoggedIn())
			return "loginPage";

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query query = new Query("Transaction").addSort("create_date",
				Query.SortDirection.DESCENDING);
		List<Entity> transactions = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(10));

		model.addAttribute("transactionList", Transaction.convert(transactions));

		return "listTransaction";

	}

}