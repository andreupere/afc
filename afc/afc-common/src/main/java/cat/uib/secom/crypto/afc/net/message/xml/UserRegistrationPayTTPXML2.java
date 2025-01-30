package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="registration-payttp-m2")
public class UserRegistrationPayTTPXML2 {

	@Element
	protected String c0;

	
	
	public String getC0() {
		return c0;
	}

	public void setC0(String c0) {
		this.c0 = c0;
	}
	
}
