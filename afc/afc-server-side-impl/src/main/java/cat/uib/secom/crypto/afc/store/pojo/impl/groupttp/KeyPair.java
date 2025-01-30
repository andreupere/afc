package cat.uib.secom.crypto.afc.store.pojo.impl.groupttp;


/**
 * @author Andreu Pere
 * 
 * This class is a POJO with gets and sets
 * It stores a public/private key pair as stored in the data store
 * 
 * @see GroupPublicKey
 * @see UserPrivateKey
 * */
public class KeyPair {

	/**
	 * Group public key as stored in the database
	 * */
	private GroupPublicKey groupPublicKey;
	/**
	 * User private key as stored in the database
	 * */
	private UserPrivateKey userPrivateKey;
	
	
	public KeyPair(GroupPublicKey gpk, UserPrivateKey usk) {
		this.setGroupPublicKey(gpk);
		this.setUserPrivateKey(usk);
	}

	public void setGroupPublicKey(GroupPublicKey groupPublicKey) {
		this.groupPublicKey = groupPublicKey;
	}

	public GroupPublicKey getGroupPublicKey() {
		return groupPublicKey;
	}

	public void setUserPrivateKey(UserPrivateKey userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}

	public UserPrivateKey getUserPrivateKey() {
		return userPrivateKey;
	}
}
