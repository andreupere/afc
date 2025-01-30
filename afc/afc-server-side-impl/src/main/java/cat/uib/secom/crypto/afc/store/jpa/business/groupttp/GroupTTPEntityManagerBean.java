package cat.uib.secom.crypto.afc.store.jpa.business.groupttp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import cat.uib.secom.crypto.afc.store.pojo.groupttp.GroupManagerConfig;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.GroupManagerConfigImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.GroupManagerPrivateKey;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.GroupPublicKey;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.KeyPair;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.UserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.accessors.GroupManagerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupManagerPrivateElements;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupManagerPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.NoMoreAvailableKeysException;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.UserAlreadyServedException;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.GroupManagerEntityManagerBean;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupManagerPrivateKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSUserPrivateKey;




public class GroupTTPEntityManagerBean extends GroupManagerEntityManagerBean {

	private final static String PU_NAME = "groupManager-PU";
	
	
	public GroupTTPEntityManagerBean(String persistenceUnitName) {
		super(persistenceUnitName);
	}
	
	public GroupTTPEntityManagerBean() {
		super(PU_NAME);
	}
	
	
	/**
	 * Initialize public g1 (as 'alpha' in the AFC protocol) and other public constant parameters
	 * for all the users in the system. This method stores them in the database.
	 * 
	 * @param curveFileName
	 * */
	public void initTTP(String curveFileName) {
				
		BBSParameters bbsParameters = new BBSParameters(curveFileName);
		byte[] g1 = bbsParameters.generateG1bytes();
		
		GroupManagerConfig gmc = new GroupManagerConfigImpl();
		gmc.setProperty("public_g1");
		gmc.setValue(g1);
		
		super.preparePersistence();
		super.getEntityManager().persist(gmc);
		super.getEntityTransaction().commit();
		super.closePersistence();
		
	}
	
	
	/*public GroupPublicKey retrieveGroupPublicKey(Long id) {
		super.preparePersistence();
		GroupPublicKey gpk = super.getEntityManager().find(GroupPublicKey.class, id);
		System.out.println(gpk);
		super.closePersistence();
		return gpk;
	}*/
	
	public void revokeAnonymity() throws Exception {
		throw new Exception("Not implemented yet...");
	}
	
	
	/**
	 * Load from data store a property
	 * 
	 * @param property the name of the desired property to be loaded from data store
	 * */
	public byte[] getManagerConfig(String property) {
		
		
		Query q = super.getEntityManager().createNamedQuery("getProperty");
		q.setParameter("property", "public_g1");
		GroupManagerConfig prop = (GroupManagerConfig) q.getSingleResult();
		
		return prop.getValue();
		
	}
	
	
	public void storeGroupData(GroupManagerAccessor gma) {
		this.storeGroupUserData(gma.getUserPrivateKeys(),
						   gma.getGroupPublicKey(),
						   gma.getGroupManagerPrivateKey(),
						   gma.getGroupManagerPrivateElements());
	}
	
	protected void storeGroupUserData(HashMap<Integer, BBSUserPrivateKey> uskMap, 
								BBSGroupPublicKey gpk, 
								BBSGroupManagerPrivateKey gmsk, 
								BBSGroupManagerPrivateElements gmse) {
		
		// we build the entity GrouPublicKeyDB
		GroupPublicKey gpkDB = new GroupPublicKey();
		
		// we build the entity GroupManagerPrivateKeyDB
		GroupManagerPrivateKey gmskDB = new GroupManagerPrivateKey();
		
		
		// we set gpkDB groupPublicKey
		EmbeddableBBSGroupPublicKey groupPublicKey = new EmbeddableBBSGroupPublicKey();
		
		groupPublicKey.setG1( gpk.getG1().toHexString() );
		groupPublicKey.setG2( gpk.getG2().toHexString() );
		groupPublicKey.setH( gpk.getH().toHexString() );
		groupPublicKey.setU( gpk.getU().toHexString() );
		groupPublicKey.setV( gpk.getV().toHexString() );
		groupPublicKey.setOmega( gpk.getOmega().toHexString() );
		
		gpkDB.setGroupPublicKey(groupPublicKey);
		

		super.preparePersistence();
		
		// persist instance
		super.getEntityManager().persist( gpkDB );
		
		
		// we set gmskDB fields
		EmbeddableBBSGroupManagerPrivateKey groupManagerPrivateKey = new EmbeddableBBSGroupManagerPrivateKey();
		groupManagerPrivateKey.setDelta1( gmsk.getDelta1().toHexString() );
		groupManagerPrivateKey.setDelta2( gmsk.getDelta2().toHexString() );
		groupManagerPrivateKey.setGamma( gmse.getGamma().toHexString() );
		gmskDB.setGroupManagerPrivateKey(groupManagerPrivateKey);
		gmskDB.setGpkDB(gpkDB);
		

		// persist instance
		super.getEntityManager().persist(gmskDB);
		
		
		// we set upkDB fields for all users
		Set<Integer> s = uskMap.keySet();
		Iterator<Integer> it = s.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			BBSUserPrivateKey singleBBSupk = uskMap.get(key);
			// we build the entity UserPrivateKeyDB
			UserPrivateKey upkDB = new UserPrivateKey();
			
			EmbeddableBBSUserPrivateKey userPrivateKey = new EmbeddableBBSUserPrivateKey();
			userPrivateKey.setAi( singleBBSupk.getA().toHexString() );
			userPrivateKey.setXi( singleBBSupk.getX().toHexString() );
			
			upkDB.setUserPrivateKey(userPrivateKey);

			upkDB.setDateIssued(new Date());
			upkDB.setIssued(false);
			upkDB.setRevoked(false);
			upkDB.setGroupManager(gpkDB);
			upkDB.setYu("");
			// persist instance

			getEntityManager().persist((UserPrivateKey) upkDB);
		}
		
		getEntityTransaction().commit();
		
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public KeyPair issueKeyPair(String uIdentity, String yu) throws UserAlreadyServedException, NoMoreAvailableKeysException {
		
		super.preparePersistence();
		
		Query q = getEntityManager().createNamedQuery("isIssuedAFC");
		q.setParameter(1, uIdentity );
		List<UserPrivateKey> lu = q.getResultList();
		
		// if any result, then throw new UserAlreadyServedException (the user is already registered with a PK/SK key pair)
		if ( !lu.isEmpty() ) {
			throw new UserAlreadyServedException(); 
		}
		
		//Query qq = getEntityManager().createNamedQuery("all");

		
		
		// looking for a key not issued
		q = getEntityManager().createNamedQuery("issueKeyPairAFC");
		// if no results, no more available keys, so throw new NoMoreAvailableKeysException
		lu = q.getResultList(); 
		if ( lu.isEmpty() ) {
			throw new NoMoreAvailableKeysException();
		}
		
		// if results, get the first result and issue the key to user uIdentity
		UserPrivateKey u = lu.get(0);
		u.setDateIssued(new Date());
		u.setIssued(true);
		u.setuIdentity(uIdentity);
		u.setYu(yu);
		
		
		// update user
		getEntityManager().merge(u);
		// commit changes
		getEntityTransaction().commit();
		
		
		// get public key
		GroupPublicKey gpk = (GroupPublicKey) u.getGroupPublicKeyDB();

		super.closePersistence();
		
		return new KeyPair(gpk, u);
	}
	
	
	

}
