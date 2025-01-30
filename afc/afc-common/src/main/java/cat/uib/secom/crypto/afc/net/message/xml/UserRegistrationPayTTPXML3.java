package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="registration-payttp-m3")
public class UserRegistrationPayTTPXML3 {

	@Element
	protected String omega0;

	
	
	public String getOmega0() {
		return omega0;
	}

	public void setOmega0(String omega0) {
		this.omega0 = omega0;
	}
	
}
