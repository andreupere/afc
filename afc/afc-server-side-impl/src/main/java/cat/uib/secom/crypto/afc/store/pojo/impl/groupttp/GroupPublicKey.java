package cat.uib.secom.crypto.afc.store.pojo.impl.groupttp;

import java.math.BigInteger;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cat.uib.secom.crypto.sig.bbs.store.jpa.entities.impl.GroupPublicKeyDB;




/**
 * @author Andreu Pere
 * 
 * Entity that represents the group public key structure in the database.
 * The group public key is stored as byte[] in the database.
 * It also defines get methods that returns BigInteger
 * 
 * @see UserPrivateKey
 * @see GroupManagerPrivateKey
 * */
@Entity
@Table(name="GROUP_TTP_PUBLIC_KEY")
public class GroupPublicKey extends GroupPublicKeyDB {
	
	
	
	
	

}
