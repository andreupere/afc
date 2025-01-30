package cat.uib.secom.crypto.afc.store.pojo.impl.groupttp;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cat.uib.secom.crypto.sig.bbs.store.jpa.entities.impl.UserPrivateKeyDB;





/**
 * @author Andreu Pere
 * 
 * Entity object that represents the user private key structure in the database.
 * The user private key definition is stored as byte[] (Ai and xi, where i is a user number).
 * It also has get methods returning BigInteger for Ai and xi parameters
 * 
 * It contains NamedQueries to query the database
 * 
 * @see GroupPublicKey
 * */
@NamedQueries({
	@NamedQuery(name="isIssuedAFC",
			query="SELECT u FROM UserPrivateKey u WHERE u.uIdentity = ?1"),
	@NamedQuery(name="issueKeyPairAFC",
			query="SELECT u FROM UserPrivateKey u WHERE u.issued = false"),
	@NamedQuery(name="openAFC",
			query="SELECT u FROM UserPrivateKey u WHERE u.userPrivateKey.ai = ?1"),
	@NamedQuery(name="all",
			query="SELECT u FROM UserPrivateKey u")
	})
@Entity
@Table(name="GROUP_TTP_USER_PRIVATE_KEY")
@DiscriminatorValue("USER_PRIVATE_KEY")
public class UserPrivateKey extends UserPrivateKeyDB {

	/**
	 * User pseudonym
	 * */
	@Column(name="AFC_USER_YU")
	private String yu;

	
	
	public void setYu(String yu) {
		this.yu = yu;
	}
	public String getYu() {
		return yu;
	}
	
}
