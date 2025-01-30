package cat.uib.secom.crypto.afc.android.pojo;

import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;

public class UserAFCAndroid implements UserAFC {
	
	private String certificate;
	private BBSGroupPublicKey gpk;
	private BBSUserPrivateKey usk;
	private Long id;
	private String realIdentity;
	private String xu;
	private String yu;

	
	public UserAFCAndroid() {}
	
	
	public String getCertificate() {
		return certificate;
	}

	public BBSGroupPublicKey getGroupPublicKey() {
		return gpk;
	}

	public Long getId() {
		return id;
	}

	public String getRealIdentity() {
		return realIdentity;
	}

	public BBSUserPrivateKey getUserPrivateKey() {
		return usk;
	}

	public String getXu() {
		return xu;
	}

	public String getYu() {
		return yu;
	}

	public void setCertificate(String arg0) {
		this.certificate = arg0;
	}

	public void setGroupPublicKey(BBSGroupPublicKey arg0) {
		this.gpk = arg0;
	}

	public void setId(Long arg0) {
		this.id = arg0;
	}

	public void setRealIdentity(String arg0) {
		this.realIdentity = arg0;
	}

	public void setUserPrivateKey(BBSUserPrivateKey arg0) {
		this.usk = arg0;
	}

	public void setXu(String arg0) {
		this.xu = arg0;
	}

	public void setYu(String arg0) {
		this.yu = arg0;
	}

}
