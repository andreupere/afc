package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUT;


@Root(name="ticket-out")
public class TicketOUTBean implements TicketOUT {

	@Element(name="destination-provider")
	protected Integer destProvider;
	
	@Element
	protected Double fare;
	
	@Element(name="serial-number")
	protected Integer serialNumber;
	
	@Element
	protected String str;
	
	
	
	public Integer getDstProvider() {
		return destProvider;
	}

	public Double getFare() {
		return fare;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public String getStr() {
		return str;
	}

	public void setDstProvider(Integer dstProvider) {
		this.destProvider = dstProvider;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
