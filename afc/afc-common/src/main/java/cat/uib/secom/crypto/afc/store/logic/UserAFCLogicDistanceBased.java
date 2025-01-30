package cat.uib.secom.crypto.afc.store.logic;


import java.security.PublicKey;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unisa.dia.gas.jpbc.Pairing;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;
import cat.uib.secom.crypto.sig.bbs.core.engines.AbstractBBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;


/**
 * Logic of an user
 * It is independent from data store implementation (persistence agnostic)
 * 
 * @author Andreu Per
 * */
public class UserAFCLogicDistanceBased extends UserAFCLogic {
	
	private static Logger _logger = LoggerFactory.getLogger(UserAFCLogicDistanceBased.class);

	
	public static long signatureTime = 0;
	public static long encryptYuTime = 0;
	

	
	
	
	
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
		
		
		
		sigma = UserAFCLogicDistanceBased.composesSigma(sigma, s1.toHexString(), deltau, "-");
		_logger.debug("sigma s1: " + sigma.getS1());
		
		sigmaSigned = UserAFCLogicDistanceBased.signSigma(sigma, sigmaSigned, engine);
		
		
		journey.setR1( r1.toHexString() );
		
		journey.setK("-");
		
		return sigmaSigned;
		
	}
	
	
}
