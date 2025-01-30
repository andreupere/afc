package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Beta;
import cat.uib.secom.crypto.afc.store.pojo.ticket.BetaSigned;

@Root(name="beta-signed")
public class BetaSignedBean implements BetaSigned {

	@Element
	protected BetaBean beta;
	
	@Element
	protected String signature;
	
	
	
	public Beta getBeta() {
		return beta;
	}

	public String getSignature() {
		return signature;
	}

	public void setBeta(Beta beta) {
		this.beta = (BetaBean) beta;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
