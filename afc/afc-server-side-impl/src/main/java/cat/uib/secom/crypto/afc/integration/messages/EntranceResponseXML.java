package cat.uib.secom.crypto.afc.integration.messages;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class EntranceResponseXML {
	
	@Element
	private Integer ticketSerialNumber;
	
	public EntranceResponseXML() {}

	
	
	public void setTicketSerialNumber(Integer ticketSerialNumber) {
		this.ticketSerialNumber = ticketSerialNumber;
	}

	public Integer getTicketSerialNumber() {
		return ticketSerialNumber;
	}
	
	
	
	
	

}
