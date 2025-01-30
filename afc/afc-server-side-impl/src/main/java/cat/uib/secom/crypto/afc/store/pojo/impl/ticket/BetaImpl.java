package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import java.math.BigInteger;
import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Beta;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;



@Embeddable
public class BetaImpl implements Beta {
	
	@Embedded
	private TicketINSignedImpl ticketINSigned;

	@Column(name="BETA_K")
	private String k;
	
	@Column(name="BETA_FARE")
	private Double fare;
	
	@Column(name="BETA_C1")
	private String c1;
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="BETA_TAU_2")
	private Date timestamp1;
	
	@Column(name="BETA_DESTINATION_STATION_ID")
	private Integer destProvider;
	


	
	
	
	public TicketINSigned getTicketINSigned() {
		return ticketINSigned;
	}
	public void setTicketINSigned(TicketINSigned ticketINSigned) {
		this.ticketINSigned = (TicketINSignedImpl) ticketINSigned;
	}
	
	
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}


	
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}

	
	public String getC1() {
		return c1;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}


	
	public Date getTimestamp1() {
		return timestamp1;
	}
	public void setTimestamp1(Date timestamp1) {
		this.timestamp1 = timestamp1;
	}

	
	public Integer getDestProvider() {
		return destProvider;
	}
	public void setDestProvider(Integer destProvider) {
		this.destProvider = destProvider;
	}

	
}
