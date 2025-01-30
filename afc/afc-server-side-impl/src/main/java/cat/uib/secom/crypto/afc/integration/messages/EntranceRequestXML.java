package cat.uib.secom.crypto.afc.integration.messages;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class EntranceRequestXML {
	
	@Element
	private String idRobot;
	
	@Element
	private String idDoor;
	
	public EntranceRequestXML() {}
	

	public String getIdRobot() {
		return idRobot;
	}

	public String getIdDoor() {
		return idDoor;
	}

	public void setIdRobot(String idRobot) {
		this.idRobot = idRobot;
	}

	public void setIdDoor(String idDoor) {
		this.idDoor = idDoor;
	}
	
	

}
