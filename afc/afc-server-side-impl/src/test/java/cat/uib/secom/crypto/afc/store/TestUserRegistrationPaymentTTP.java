package cat.uib.secom.crypto.afc.store;

import it.unisa.dia.gas.jpbc.Element;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationPayTTPM1;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationPayTTPM2;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationPayTTPM3;
import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.jpa.business.payttp.PaymentTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.user.UserAFCEntityManagerBean;
import cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPAccountImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPTransactionImpl;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import junit.framework.TestCase;

public class TestUserRegistrationPaymentTTP extends TestCase {

	protected PaymentTTPEntityManagerBean payTTPEMB;
	protected UserAFCEntityManagerBean userEMB;
	
	protected BBSParameters bbsParameters;
	protected String curveFileName = "d840347-175-161.param";
	protected cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey gpk;
	protected cat.uib.secom.crypto.sig.bbs.core.keys.BBSUserPrivateKey usk;
	protected UserAFC u;
	
	
	protected ElementWrapper c0;
	protected ElementWrapper r0;
	protected ElementWrapper omega0;
	protected ElementWrapper s0;
	
	
	public TestUserRegistrationPaymentTTP() {
		payTTPEMB = new PaymentTTPEntityManagerBean();
		userEMB = new UserAFCEntityManagerBean();
		
		bbsParameters = new BBSParameters(curveFileName);
		u = userEMB.iam(new Long( 1 ));
		BBSGroupPublicKey gpkDB = u.getGroupPublicKey();
		BBSUserPrivateKey uskDB = u.getUserPrivateKey();
		
		gpk = new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( gpkDB.getG1(),
																			   	 gpkDB.getG2(),
																			   	 gpkDB.getH(),
																			   	 gpkDB.getU(),
																			   	 gpkDB.getV(),
																			   	 gpkDB.getOmega(),
																			   	 bbsParameters.getPairing() );
		
		usk = new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( uskDB.getAi(),
																			   	 uskDB.getXi(),
																			   	 bbsParameters.getPairing() );
		
	}
	
	
	/*public void testUserRegistrationPaymentTTP() {
		NetworkMessage m1 = step1();
		NetworkMessage m2 = step2(m1);
		NetworkMessage m3 = step3(m2);
		step4(m3);
	}
	
	
	public NetworkMessage step1(){
		
		ElementWrapper r0 = PairingHelper.random( bbsParameters.getPairing().getZr() );
		ElementWrapper s0 = PairingHelper.powZn( gpk.getG1() , r0);
		
		UserRegistrationPayTTPM1 m1 = new UserRegistrationPayTTPM1();
		m1.setS0(s0.toByteArray());
		m1.setYu(u.getYu());
		// TODO
		m1.setSignature("".getBytes());
		
		// for testing
		this.s0 = s0;
		this.r0 = r0;
		
		return m1;
	}
	
	public NetworkMessage step2(NetworkMessage m1){
		ElementWrapper c0 = PairingHelper.random( bbsParameters.getPairing().getZr() );
		
		UserRegistrationPayTTPM2 m2 = new UserRegistrationPayTTPM2();
		m2.setC0(c0.toByteArray());
		
		// for testing
		this.c0 = c0;
		
		return m2;
	}
	
	
	public NetworkMessage step3(NetworkMessage m2){
		// rebuild elementwrapper xu
		UserRegistrationPayTTPM3 m3 = null;
		try {
			ElementWrapper xuew = PairingHelper.toElementWrapper(u.getXu(), bbsParameters.getPairing(), "Zr");
			ElementWrapper omegaew = PairingHelper.schnorrZKPline(this.r0, this.c0, xuew );
			
			m3 = new UserRegistrationPayTTPM3();
			m3.setOmega0(omegaew.toByteArray());
			
			// for testing
			this.omega0 = omegaew;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return m3;
	}
	
	
	public void step4(NetworkMessage m3) {
		UserRegistrationPayTTPM3 message3 = (UserRegistrationPayTTPM3) m3;
		message3.getOmega0();
		
		ElementWrapper yuew;
		ElementWrapper omega0ew;
		try {
			yuew = PairingHelper.toElementWrapper(u.getYu(), bbsParameters.getPairing(), "G1");
			omega0ew = PairingHelper.toElementWrapper(message3.getOmega0(), bbsParameters.getPairing(), "Zr");
			boolean result = PairingHelper.schnorrZKPverify(gpk.getG1(), omega0ew, this.s0, yuew, this.c0);
			assertTrue(result);
			
			PaymentTTPAccountImpl pa = new PaymentTTPAccountImpl();
			pa.setBalance(StaticParameters.INITIAL_BALANCE);
			pa.setHashedYu( yuew.toHexString() );
			pa.setYu( yuew.toByteArray() );
			pa = (PaymentTTPAccountImpl) payTTPEMB.registerUser(pa);
			System.out.println( "before: " + pa.getBalance() );
			
			PaymentTTPTransactionImpl pt = (PaymentTTPTransactionImpl) payTTPEMB.chargeUser(pa, new Double(10.22));
			System.out.println( "after: " + pt.getBalanceAfter() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}*/
	
	
}
