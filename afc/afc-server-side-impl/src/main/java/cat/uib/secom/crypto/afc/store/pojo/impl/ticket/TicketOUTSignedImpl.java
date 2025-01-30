package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUT;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;


@Embeddable
public class TicketOUTSignedImpl implements TicketOUTSigned {

	/**
	 * XMLdsig output
	 * */
	@Column(name="TOUT_SIGNATURE", length=512)
	private String signatureOUT;
	
	@Embedded
	private TicketOUTImpl ticketOUT;

	
	
	public void setSignatureOUT(String signatureOUT) {
		this.signatureOUT = signatureOUT;
	}

	public String getSignatureOUT() {
		return signatureOUT;
	}

	public void setTicketOUT(TicketOUT ticketOUT) {
		this.ticketOUT = (TicketOUTImpl) ticketOUT;
	}

	public TicketOUT getTicketOUT() {
		return ticketOUT;
	}
	
	
	
}
