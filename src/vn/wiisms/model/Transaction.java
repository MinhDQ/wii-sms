package vn.wiisms.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

public class Transaction {

	long transUID;
	String senderNumber;
	String receiverNumber;
	String originalSMS;
	int amount;
	String description;
	String status;
	Date createDate;
	Date modifiDate;
	String createUserName;
	String modifiUserName;

	public long getTransUID() {
		return transUID;
	}

	public void setTransUID(long transUID) {
		this.transUID = transUID;
	}

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}

	public String getReceiverNumber() {
		return receiverNumber;
	}

	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}

	public String getOriginalSMS() {
		return originalSMS;
	}

	public void setOriginalSMS(String originalSMS) {
		this.originalSMS = originalSMS;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiDate() {
		return modifiDate;
	}

	public void setModifiDate(Date modifiDate) {
		this.modifiDate = modifiDate;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getModifiUserName() {
		return modifiUserName;
	}

	public void setModifiUserName(String modifiUserName) {
		this.modifiUserName = modifiUserName;
	}

	public static List<Transaction> convert(List<Entity> entities) {

		List<Transaction> transactions = new ArrayList<Transaction>(0);

		for (Entity entity : entities) {
			Transaction transaction = new Transaction();
			
			transaction.setTransUID(entity.getKey().getId());
			transaction.setSenderNumber(entity.getProperty("sender_num")
					.toString());
			transaction.setReceiverNumber(entity.getProperty("receir_num")
					.toString());
			transaction.setOriginalSMS(entity.getProperty("origin_sms")
					.toString());
			transaction.setAmount(Integer.parseInt(entity.getProperty("amount")
					.toString()));
			transaction.setDescription(entity.getProperty("desc").toString());
			transaction.setStatus(entity.getProperty("status").toString());
			transaction.setCreateDate((Date)entity.getProperty(
					"create_date"));
			transaction.setModifiDate((Date)entity.getProperty(
					"modifi_date"));
			transaction.setCreateUserName(entity.getProperty("create_user")
					.toString());
			transaction.setModifiUserName(entity.getProperty("modifi_user")
					.toString());

			transactions.add(transaction);
		}

		return transactions;
	}

}