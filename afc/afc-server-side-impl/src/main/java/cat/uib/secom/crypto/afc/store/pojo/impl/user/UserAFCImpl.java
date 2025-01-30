package cat.uib.secom.crypto.afc.store.pojo.impl.user;



import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSUserPrivateKey;




/**
 * 
 * This entity contains the user credentials: identity and pseudonym.
 * It also contains the group public key and the user private key reference
 * 
 * TODO: Link to the group public key and the user private key
 * 
 * @author Andreu Pere
 * */
@Entity
@Table(name="USER_AFC")
@NamedQueries({
@NamedQuery(name="searchIdRobot",
		query="SELECT u FROM UserAFCImpl u WHERE u.realIdentity = :idRobot")
})
public class UserAFCImpl implements UserAFC {
	
	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	

	
	@Column(name="USER_SECRET_XU")
	private String xu;
	

	
	@Column(name="USER_YU")
	private String yu;
	
	
	
	@Column(name="USER_REAL_IDENTITY")
	private String realIdentity;
	
	@Transient
	private String ri;
	
	
	/**
	 * TODO: Goes to Java KEYSTORE !! ( PK / SK )
	 * */
	@Column(name="USER_CERTIFICATE")
	private String certificate;
	
	
	
	
	// TODO: new embeddable entities in the ShortGroupSignature project
	@Embedded
	private EmbeddableBBSUserPrivateKey userPrivateKey;
	@Embedded
	private EmbeddableBBSGroupPublicKey groupPublicKey;

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public String getXu() {
		return xu;
	}
	public void setXu(String xu) {
		this.xu = xu;
	}


	
	

	public String getYu() {
		return yu;
	}
	public void setYu(String yu) {
		this.yu = yu;
	}



	
	
	public String getRealIdentity() {
		return realIdentity;
	}
	public void setRealIdentity(String realIdentity) {
		this.realIdentity = realIdentity;
	}
	
	
	
	public String getRi() {
		return ri;
	}
	public void setRi(String ri) {
		this.ri = ri;
	}
	
	
	
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
	
	
	public void setUserPrivateKey(BBSUserPrivateKey userPrivateKey) {
		this.userPrivateKey = (EmbeddableBBSUserPrivateKey) userPrivateKey;
	}
	public BBSUserPrivateKey getUserPrivateKey() {
		return userPrivateKey;
	}
	
	
	
	public void setGroupPublicKey(BBSGroupPublicKey groupPublicKey) {
		this.groupPublicKey = (EmbeddableBBSGroupPublicKey) groupPublicKey;
	}
	public BBSGroupPublicKey getGroupPublicKey() {
		return groupPublicKey;
	}
		

}
