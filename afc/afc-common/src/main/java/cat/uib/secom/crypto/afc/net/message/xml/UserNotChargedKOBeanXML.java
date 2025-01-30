package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.bean.GammaUserBean;

@Root(name="user-not-charged-ko")
public class UserNotChargedKOBeanXML implements PayTTPResponseAfterChargeAttempt {

	@Element
	protected GammaUserBean gammaUser;
	
	@Element
	protected String message;

	
	
	public GammaUserBean getGammaUser() {
		return gammaUser;
	}

	public String getMessage() {
		return message;
	}

	public void setGammaUser(GammaUserBean gammaUser) {
		this.gammaUser = gammaUser;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
