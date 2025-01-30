package cat.uib.secom.crypto.afc.integration.messages;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ExitResponseXML {
	
	@Element
	private boolean exit;
	
	@Element
	private String message;
	
	@Element(required=false)
	private Double fare;

	
	public ExitResponseXML() {}
	
	
	public boolean isExit() {
		return exit;
	}

	public String getMessage() {
		return message;
	}

	public Double getFare() {
		return fare;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}
	
	
	
	
	

}
