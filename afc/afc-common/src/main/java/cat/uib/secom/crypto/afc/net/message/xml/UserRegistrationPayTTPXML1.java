package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="registration-payttp-m1")
public class UserRegistrationPayTTPXML1 {
	
	@Element
	private String yu;
	
	@Element
	private String s0;
	
	@Element
	private String signature;

	
	
	
	public String getYu() {
		return yu;
	}

	public String getS0() {
		return s0;
	}

	public String getSignature() {
		return signature;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

	public void setS0(String s0) {
		this.s0 = s0;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
