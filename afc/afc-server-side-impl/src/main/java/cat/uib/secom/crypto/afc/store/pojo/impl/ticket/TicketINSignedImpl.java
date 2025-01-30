package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;


@Embeddable
public class TicketINSignedImpl implements TicketINSigned {

	/**
	 * XMLdsig output
	 * */
	@Column(name="TIN_SIGNATURE", length=256)
	private String signature;
	
	
	@Embedded
	private TicketINImpl ticketIN;
	
	
	public String getSignature() {
		return this.signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	

	public void setTicketIN(TicketIN ticketIN) {
		this.ticketIN = (TicketINImpl) ticketIN;
	}

	public TicketIN getTicketIN() {
		return ticketIN;
	}
	
}
