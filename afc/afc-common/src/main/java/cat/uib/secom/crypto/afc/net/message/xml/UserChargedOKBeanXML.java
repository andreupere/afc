package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="user-charged-ok")
public class UserChargedOKBeanXML implements PayTTPResponseAfterChargeAttempt {
	
	@Element(name="serial-number")
	protected Integer serialNumber;
	
	@Element 
	protected Double fare;
	
	@Element
	protected String message;

	
	
	
	public Integer getSerialNumber() {
		return serialNumber;
	}

	public Double getFare() {
		return fare;
	}

	public String getMessage() {
		return message;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
