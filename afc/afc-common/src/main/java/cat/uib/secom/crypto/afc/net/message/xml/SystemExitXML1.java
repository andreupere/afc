package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;


@Root(name="exit-request")
public class SystemExitXML1 {

	@Element(name="ticket-in-signed")
	protected TicketINSignedBean ticketINSignedBean;
	
	@Element(name="encrypted-k")
	protected String encryptedK;

	
	
	
	public TicketINSignedBean getTicketINSignedBean() {
		return ticketINSignedBean;
	}

	public String getEncryptedK() {
		return encryptedK;
	}

	public void setTicketINSignedBean(TicketINSignedBean ticketINSignedBean) {
		this.ticketINSignedBean = ticketINSignedBean;
	}

	public void setEncryptedK(String encryptedK) {
		this.encryptedK = encryptedK;
	}
	
	
	
}
