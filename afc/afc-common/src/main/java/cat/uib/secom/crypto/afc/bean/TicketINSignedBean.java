package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;


@Root(name="ticket-in-signed")
public class TicketINSignedBean implements TicketINSigned {

	@Element
	protected String signature;
	
	@Element(name="ticket-in")
	protected TicketINBean ticketINBean;
	
	
	public String getSignature() {
		return signature;
	}

	public TicketIN getTicketIN() {
		return ticketINBean;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setTicketIN(TicketIN ticketIN) {
		this.ticketINBean = (TicketINBean) ticketIN;
	}

}
