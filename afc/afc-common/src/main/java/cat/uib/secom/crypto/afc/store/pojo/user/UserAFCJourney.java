package cat.uib.secom.crypto.afc.store.pojo.user;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;





/**
 * 
 * TODO: doc
 * 
 * @author Andreu Pere
 * */
public interface UserAFCJourney {
	
	
	
	
	public Long getId();
	
	public void setId(Long id);
	
	
	
	
	public TicketINSigned getTicketINSigned();
	
	public void setTicketINSigned(TicketINSigned ticketINSigned);
	
	
	
	
	public TicketOUTSigned getTicketOUTSigned();
	
	public void setTicketOUTSigned(TicketOUTSigned ticketOUTSigned);
	
	
	
	
	public void setYu(String yu);
	
	public String getYu();
	
	


	public void setK(String k);
	
	public String getK();
	
	

	
	public void setR1(String r1);
	
	public String getR1();
	
	
	

	
}
