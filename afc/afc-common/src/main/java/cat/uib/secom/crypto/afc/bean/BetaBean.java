package cat.uib.secom.crypto.afc.bean;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Beta;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;

@Root(name="beta")
public class BetaBean implements Beta {
	
	@Element(name="ticket-in-signed")
	protected TicketINSignedBean ticketINSigned;
	
	@Element
	protected String c1;
	
	@Element(name="destination-provider")
	protected Integer destProvider;
	
	@Element
	protected Double fare;
	
	@Element
	protected String k;
	
	@Element
	protected Long tau2;
	
	
	protected Date timestamp1;
	
	
	

	public String getC1() {
		return c1;
	}

	public Integer getDestProvider() {
		return destProvider;
	}

	public Double getFare() {
		return fare;
	}

	public String getK() {
		return k;
	}

	public Date getTimestamp1() {
		this.timestamp1 = new Date( this.tau2 );
		return timestamp1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public void setDestProvider(Integer destProvider) {
		this.destProvider = destProvider;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public void setK(String k) {
		this.k = k;
	}

	public void setTimestamp1(Date timestamp1) {
		this.tau2 = timestamp1.getTime();
		this.timestamp1 = timestamp1;
	}

	public TicketINSigned getTicketINSigned() {
		return ticketINSigned;
	}

	public void setTicketINSigned(TicketINSigned tinSigned) {
		this.ticketINSigned = (TicketINSignedBean) tinSigned;
	}

	
	
	public Long getTau2() {
		return tau2;
	}

	public void setTau2(Long tau2) {
		this.tau2 = tau2;
	}

}
