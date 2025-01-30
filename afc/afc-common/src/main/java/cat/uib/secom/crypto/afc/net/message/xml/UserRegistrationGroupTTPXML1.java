package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="registration-request-groupttp")
public class UserRegistrationGroupTTPXML1 {

	@Element(name="user-identity")
	protected String userIdentity;
	
	@Element
	protected String certificate;
	
	@Element
	protected String signature;
	
	@Element
	protected String yu;
	
	
	
	public String getUserIdentity() {
		return userIdentity;
	}
	
	public String getCertificate() {
		return certificate;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public String getYu() {
		return yu;
	}
	
	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}
	
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public void setYu(String yu) {
		this.yu = yu;
	}
	
	
	
}
