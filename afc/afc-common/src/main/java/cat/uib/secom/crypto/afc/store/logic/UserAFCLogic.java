package cat.uib.secom.crypto.afc.store.logic;


import java.security.KeyPair;
import java.security.PublicKey;

import javassist.bytecode.ByteArray;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;
import cat.uib.secom.crypto.sig.bbs.core.accessors.SignerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.engines.AbstractBBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
//import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
//import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.strings.StringUtils;


/**
 * Logic of an user
 * It is independent from data store implementation (persistence agnostic)
 * 
 * @author Andreu Per
 * */
public class UserAFCLogic {
	
	private static Logger _logger = LoggerFactory.getLogger(UserAFCLogic.class);
	
	public static long signatureTime;
	public static long encryptYuTime;
	

	public static UserAFC newUserAFC(UserAFC u, ElementWrapper g1, Pairing pairing, String certificate, String realIdentity) {
		Element g1e = g1.getElement();
		ElementWrapper xuw = PairingHelper.random( pairing.getZr() );
		ElementWrapper yuw = PairingHelper.powZn( g1, xuw );
		u.setXu( xuw.toHexString() );
		u.setYu( yuw.toHexString() );
		u.setCertificate( certificate );
		u.setRealIdentity( realIdentity );
		return u;
	}
	
	
	/**
	 * After user creates xu and yu.
	 * 
	 * */
	public static UserAFC newUserAFC(UserAFC u, String xu, String yu, String certificate, String realIdentity) {
		
		u.setXu(xu);
		u.setYu(yu);
		u.setCertificate(certificate);
		u.setRealIdentity(realIdentity);
		
		return u;
		
	}
	
	public static void storeGroupData() {
		
		
		
	}
	
	
	
	
	
	
	
	/**
	 * This is the first step that user executes when he wants to enter to system
	 * 
	 * All the input parameters are interfaces. These parameters MUST be instantiated before call method. 
	 * This strategy is based in polymorphism to be independent from the underlining data store implementation.
	 * 
	 * @return group signature
	 * @throws Exception 
	 * */
	public static SigmaSigned openAFCJourney(UserAFCJourney journey, 
										     UserAFC u, 
										     Sigma sigma, 
										     SigmaSigned sigmaSigned,
										     BBSGroupPublicKey gpk, 
										     BBSUserPrivateKey usk, 
										     Pairing pairing,
										     PublicKey rsaPublicKey,
										     AbstractBBSEngine engine) 
											 throws Exception {
		
		ElementWrapper r1 = PairingHelper.random( pairing.getZr() );
		r1.getElement().getImmutable();
		
		
		ElementWrapper s1 = PairingHelper.powZn( gpk.getG1() , r1);
		s1.getElement().getImmutable();
		
		// encryption of yu
		_logger.debug("user yu: " + u.getYu());
		long init = System.currentTimeMillis();
		CipherProcess cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
		String deltau = cp.encrypt(rsaPublicKey, u.getYu() );
		encryptYuTime = System.currentTimeMillis() - init;
		
		//deltau = encrypted;
		_logger.debug("deltau: " + deltau);
		
		ElementWrapper k = PairingHelper.random( pairing.getZr() );
		
		
		/*getEntityManager().persist( getCurrentJourney() );
		getEntityTransaction().commit();*/
		

		Hash hash = new Hash( k.toByteArray() );
		hash.generate();
		String hk = hash.readHexString();
		
		sigma = UserAFCLogic.composesSigma(sigma, s1.toHexString(), deltau, hk);
		_logger.debug("sigma s1: " + sigma.getS1());
		
		sigmaSigned = UserAFCLogic.signSigma(sigma, sigmaSigned, engine);
		
		
		journey.setR1( r1.toHexString() );
		
		journey.setK(k.toHexString());
		
		return sigmaSigned;
		
	}
	
	
	protected static Sigma composesSigma(Sigma s, String s1, String deltau, String hk) {
		s.setS1(s1);
		s.setDeltau(deltau);
		s.setHk(hk);
		
		return s;
	}
	
	protected static SigmaSigned signSigma(Sigma sigma, SigmaSigned sigmaSigned, AbstractBBSEngine engine) {
		
		

		String toBeSigned = sigma.serialize();
		_logger.debug("toBeSigned: " + sigma.serialize());
		

		
//		SignerAccessor sa = new SignerAccessor();
//		sa.setBBSKeyPair(gpk, usk);
//		
//
//		sa.setMessage(toBeSigned);
		long init = System.currentTimeMillis();
		Signature signature = engine.sign(toBeSigned);
		signatureTime =  System.currentTimeMillis() - init;

		sigmaSigned.setSigma(sigma);
		sigmaSigned.storeSignature(signature);
		//cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey sgpk = new BBSGroupPublicKeyBean();
		BBSGroupPublicKeyBean gpkb = new BBSGroupPublicKeyBean();
		gpkb.setG1( engine.getBBSGroupPublicKey().getG1().toHexString() );
		gpkb.setG2( engine.getBBSGroupPublicKey().getG2().toHexString() );
		gpkb.setH( engine.getBBSGroupPublicKey().getH().toHexString() );
		gpkb.setU( engine.getBBSGroupPublicKey().getU().toHexString() );
		gpkb.setV( engine.getBBSGroupPublicKey().getV().toHexString() );
		gpkb.setOmega( engine.getBBSGroupPublicKey().getOmega().toHexString() );
		sigmaSigned.setEmbeddedGroupPublicKey(gpkb);

		// TODO: add group public key to sigmaSigned
		//sigmaSigned.setEmbeddedGroupPublicKey(gpk);
		
		
		
		return sigmaSigned;
	}


	
	
	
	
	public long getSignatureTime() {
		return signatureTime;
	}
	public long getEncryptYuTime() {
		return encryptYuTime;
	}

	
	
	
	
	
	
	
}
