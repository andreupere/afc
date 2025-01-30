package cat.uib.secom.crypto.afc.integration.messages;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ExitRequestXML {

	@Element
	private String idRobot;
	
	@Element
	private String idDoor;
	
	@Element
	private Integer ticketSerialNumber;

	
	public ExitRequestXML() {}
	
	
	
	public String getIdRobot() {
		return idRobot;
	}

	public String getIdDoor() {
		return idDoor;
	}

	public Integer getTicketSerialNumber() {
		return ticketSerialNumber;
	}

	public void setIdRobot(String idRobot) {
		this.idRobot = idRobot;
	}

	public void setIdDoor(String idDoor) {
		this.idDoor = idDoor;
	}

	public void setTicketSerialNumber(Integer ticketSerialNumber) {
		this.ticketSerialNumber = ticketSerialNumber;
	}
	
	
	
	
	
}
