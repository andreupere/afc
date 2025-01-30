package cat.uib.secom.crypto.afc.store.pojo.user;

import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;


/**
 * TODO: doc
 * 
 * @author Andreu Pere
 * 
 * */
public interface UserAFC  {
	
	
	public Long getId();
	
	public void setId(Long id);

	
	
	public String getXu();
	
	public void setXu(String xu);
	
	
	
	

	public String getYu();
	
	public void setYu(String yu);
	

	
	
	public String getRealIdentity();
	
	public void setRealIdentity(String realIdentity);
	
	
	
	
	public String getCertificate();
	
	public void setCertificate(String certificate);
	
	
	
	public void setUserPrivateKey(BBSUserPrivateKey userPrivateKey);
	
	public BBSUserPrivateKey getUserPrivateKey();
	
	
	
	public void setGroupPublicKey(BBSGroupPublicKey groupPublicKey);
	
	public BBSGroupPublicKey getGroupPublicKey();
		

}
