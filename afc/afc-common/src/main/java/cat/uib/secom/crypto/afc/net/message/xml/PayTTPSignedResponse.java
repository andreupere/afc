package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class PayTTPSignedResponse  {

	@Element
	protected String signature;
	
	@Element
	protected PayTTPResponseAfterChargeAttempt response;

	protected boolean ok;
	
	
	public String getSignature() {
		return signature;
	}

	public PayTTPResponseAfterChargeAttempt getResponse() {
		return response;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setResponse(PayTTPResponseAfterChargeAttempt response) {
		this.response = response;
	}
	
	

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}
	

}
