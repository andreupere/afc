package cat.uib.secom.crypto.afc.store;

import it.unisa.dia.gas.jpbc.Element;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationGroupM1;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationGroupM2;
import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.user.UserAFCEntityManagerBean;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.KeyPair;
import cat.uib.secom.crypto.afc.store.pojo.impl.user.UserAFCImpl;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.NoMoreAvailableKeysException;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.UserAlreadyServedException;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSUserPrivateKey;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import junit.framework.TestCase;

public class TestUserRegistrationGroupTTP extends TestCase {

	protected GroupTTPEntityManagerBean groupTTPEMB;
	protected UserAFCEntityManagerBean userEMB;
	
	protected BBSParameters bbsParameters;
	protected String curveFileName = "d840347-175-161.param";
	protected String certificate = "user_certificate";
	protected String realIdentity = "andreupere";
	
	
	public TestUserRegistrationGroupTTP() {
		groupTTPEMB = new GroupTTPEntityManagerBean();
		userEMB = new UserAFCEntityManagerBean();
		bbsParameters = new BBSParameters(curveFileName);
	}
	
	
	
	/*public void testUserRegistration() {
		byte[] g1 = step0();
		NetworkMessage m1 = step1(g1);
		NetworkMessage m2 = step2(m1);
		step3(m2);
		step3RestoreKeys(m2);
	}
	
	
	
	
	public byte[] step0() {
		byte[] g1 = groupTTPEMB.getManagerConfig("public_g1");
		return g1;
	}
	
	
	public NetworkMessage step1(byte[] g1) {
		Element g1e = bbsParameters.getPairing().getG1().newElement();
		g1e.setFromBytes(g1);
		g1e.getImmutable();
		ElementWrapper g1ew = new ElementWrapper( g1e );
		UserAFC u = new UserAFCImpl();
		UserAFCLogic.newUserAFC(u, g1ew, bbsParameters.getPairing(), certificate, realIdentity);
		userEMB.deployUser(u);
		
		String signature = "signature_yu";
		
		UserRegistrationGroupM1 m1 = new UserRegistrationGroupM1();
		m1.setCertificate(certificate);
		m1.setSignature(signature);
		m1.setUserIdentity(realIdentity);
		m1.setYu(u.getYu());
		
		return m1;
	}
	
	
	public NetworkMessage step2(NetworkMessage message) {
		UserRegistrationGroupM1 m = (UserRegistrationGroupM1) message;
		
		// TODO: verify signature
		m.getCertificate();
		m.getSignature();
		
		KeyPair kp = null;
		try {
			kp = groupTTPEMB.issueKeyPair(m.getUserIdentity(), m.getYu());
			System.out.println(kp);
		} catch (UserAlreadyServedException e) {
			e.printStackTrace();
		} catch (NoMoreAvailableKeysException e) {
			e.printStackTrace();
		}
		
		
		UserRegistrationGroupM2 m2 = new UserRegistrationGroupM2();
		m2.setG1( kp.getGroupPublicKey().getGroupPublicKey().getG1() );
		m2.setG2( kp.getGroupPublicKey().getGroupPublicKey().getG2() );
		m2.setH( kp.getGroupPublicKey().getGroupPublicKey().getH() );
		m2.setU( kp.getGroupPublicKey().getGroupPublicKey().getU() );
		m2.setV( kp.getGroupPublicKey().getGroupPublicKey().getV() );
		m2.setOmega( kp.getGroupPublicKey().getGroupPublicKey().getOmega() );
		
		m2.setA( kp.getUserPrivateKey().getUserPrivateKey().getAi() );
		m2.setX( kp.getUserPrivateKey().getUserPrivateKey().getXi() );
		
		return m2;
		
	}
	
	
	public void step3RestoreKeys(NetworkMessage message) {
		UserRegistrationGroupM2 m = (UserRegistrationGroupM2) message;
		
		cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey gpk = 
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( m.getG1(),
																			   m.getG2(),
																			   m.getH(),
																			   m.getU(),
																			   m.getV(),
																			   m.getOmega(),
																			   bbsParameters.getPairing() );
		
		cat.uib.secom.crypto.sig.bbs.core.keys.BBSUserPrivateKey usk =
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( m.getA(),
																			   m.getX(),
																			   bbsParameters.getPairing() );
		
		
		System.out.println("retrieved and rebuilt gpk: " + gpk);
		System.out.println("retrieved and rebuilt usk: " + usk);
		
	}
	
	
	public void step3(NetworkMessage message) {
		UserRegistrationGroupM2 m = (UserRegistrationGroupM2) message;
		
		BBSGroupPublicKey gpk = new EmbeddableBBSGroupPublicKey();
		gpk.setG1( m.getG1() );
		gpk.setG2( m.getG2() );
		gpk.setH( m.getH() );
		gpk.setU( m.getU() );
		gpk.setV( m.getV() );
		gpk.setOmega( m.getOmega() );
		
		BBSUserPrivateKey usk = new EmbeddableBBSUserPrivateKey();
		usk.setAi( m.getA() );
		usk.setXi( m.getX() );
		
		userEMB.storeGroupSignatureSchemeData(gpk, usk, new Long(1));
		
		
	}*/
	
	
	
}
