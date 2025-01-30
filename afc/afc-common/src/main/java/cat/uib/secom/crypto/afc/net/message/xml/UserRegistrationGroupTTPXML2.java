package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSUserPrivateKeyBean;

@Root(name="registration-group-keypair")
public class UserRegistrationGroupTTPXML2 {
	
	@Element(name="group-public-key")
	protected BBSGroupPublicKeyBean groupPublicKey;
	
	@Element(name="private-key")
	protected BBSUserPrivateKeyBean userPrivateKey;

	
	
	
	public BBSGroupPublicKeyBean getGroupPublicKey() {
		return groupPublicKey;
	}

	public BBSUserPrivateKeyBean getUserPrivateKey() {
		return userPrivateKey;
	}

	public void setGroupPublicKey(BBSGroupPublicKeyBean groupPublicKey) {
		this.groupPublicKey = groupPublicKey;
	}

	public void setUserPrivateKey(BBSUserPrivateKeyBean userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}

}
