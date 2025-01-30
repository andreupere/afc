package cat.uib.secom.crypto.afc.store.pojo.ticket;


public interface TicketINSigned {

	
	public String getSignature();
	
	public void setSignature(String signature);
	

	public void setTicketIN(TicketIN ticketIN);

	public TicketIN getTicketIN();
	

	
}