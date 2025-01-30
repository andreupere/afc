package cat.uib.secom.crypto.afc.store.pojo.impl.groupttp;




import javax.persistence.Entity;

import javax.persistence.Table;

import cat.uib.secom.crypto.sig.bbs.store.jpa.entities.impl.GroupManagerPrivateKeyDB;




/**
 * @author Andreu Pere
 * 
 * Entity object that represents the group private key and group manager private element
 * structure in the database.
 * The group manager private parameters are stored as byte[]
 * It also defines get methods returning BigInteger for parameters
 * 
 * @see GroupPublicKey
 * */
@Entity
@Table(name="GROUP_TTP_PRIVATE_KEY")
public class GroupManagerPrivateKey extends GroupManagerPrivateKeyDB {

	
}
