package cat.uib.secom.crypto.afc.net.message.xml.distancebased;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SystemExitXML0DistanceBased {

	@Element
	protected String value;

	
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
	
}
