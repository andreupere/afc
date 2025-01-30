package cat.uib.secom.crypto.afc.store.pojo.impl.user;



import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketINSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketOUTSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;





/**
 * 
 * This entity contains the journey's (one way) definition. It contains a entry ticket and the output ticket.
 * It is not necessary to link to the UserAFC entity because this table will be owned only by one AFC user since
 * this table will be contained in the user's device together with the UserAFC table
 * 
 * @see TicketINSigned
 * @see TicketOUTSigned
 * 
 * 
 *  @author Andreu Pere
 * */
@Entity
@Table(name="USER_AFC_JOURNEY")
@NamedQueries({
	@NamedQuery(name="searchTicketSerialNumber",
			query="SELECT ua FROM UserAFCJourneyImpl ua WHERE ticketINSigned.ticketIN.serialNumber = :serialNumber")
})
public class UserAFCJourneyImpl implements UserAFCJourney {

	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	
	
	@Embedded
	private TicketINSignedImpl ticketINSigned;
	
	
	@Embedded
	private TicketOUTSignedImpl ticketOUTSigned;
	
	
	@Column(name="USER_YU")
	private String yu;
	
	
	@Column(name="TIN_SECRET_K")
	private String k;

	
	@Column(name="TIN_SECRET_R1")
	private String r1;
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public TicketINSigned getTicketINSigned() {
		return ticketINSigned;
	}
	public void setTicketINSigned(TicketINSigned ticketINSigned) {
		this.ticketINSigned = (TicketINSignedImpl) ticketINSigned;
	}
	
	
	
	public TicketOUTSigned getTicketOUTSigned() {
		return ticketOUTSigned;
	}
	public void setTicketOUTSigned(TicketOUTSigned ticketOUTSigned) {
		this.ticketOUTSigned = (TicketOUTSignedImpl) ticketOUTSigned;
	}
	
	
	public void setYu(String yu) {
		this.yu = yu;
	}
	public String getYu() {
		return yu;
	}
	
	
	
	public void setK(String k) {
		this.k = k;
	}
	public String getK() {
		return k;
	}
	
	
	
	
	public void setR1(String r1) {
		this.r1 = r1;
	}
	public String getR1() {
		return r1;
	}
	
	
	

	
}
