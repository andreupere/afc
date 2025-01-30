package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUT;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;


@Root(name="ticket-out-signed")
public class TicketOUTSignedBean implements TicketOUTSigned {

	@Element
	protected String signature;
	
	@Element(name="ticket-out")
	protected TicketOUTBean ticketOUT;
	
	
	
	public String getSignatureOUT() {
		return signature;
	}

	public TicketOUT getTicketOUT() {
		return ticketOUT;
	}

	public void setSignatureOUT(String signatureOUT) {
		this.signature = signatureOUT;
	}

	public void setTicketOUT(TicketOUT ticketOUT) {
		this.ticketOUT = (TicketOUTBean) ticketOUT;
	}

}
