package cat.uib.secom.crypto.afc.store.jpa.business.payttp;


import it.unisa.dia.gas.jpbc.Pairing;

import java.io.StringWriter;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Query;


import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.uib.secom.crypto.afc.bean.GammaDestProviderBean;
import cat.uib.secom.crypto.afc.bean.GammaUserBean;
import cat.uib.secom.crypto.afc.net.message.xml.PayTTPResponseAfterChargeAttempt;
import cat.uib.secom.crypto.afc.net.message.xml.PayTTPSignedResponse;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitGammaUserXML;
import cat.uib.secom.crypto.afc.net.message.xml.UserChargedOKBeanXML;
import cat.uib.secom.crypto.afc.net.message.xml.UserNotChargedKOBeanXML;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.logic.DestinationProviderLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPAccountImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPTransactionImpl;
import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPAccount;
import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPTransaction;
import cat.uib.secom.crypto.afc.store.pojo.proofs.GammaDestProvider;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.AbstractEntityManagerBean;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.crypto.symmetrickey.AESCipherProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.strings.StringUtils;




/**
 * @author Andreu Pere
 * 
 * Manager Bean of PaymentTTPAccount and PaymentTTPTransaction
 * */
public class PaymentTTPEntityManagerBean extends AbstractEntityManagerBean {

	private static Logger _logger = LoggerFactory.getLogger(PaymentTTPEntityManagerBean.class);
	
	private GeneralConfigXML gConfig;
	
	private final static String PU_NAME = "paymentTTP-PU";
		

	
	public PaymentTTPEntityManagerBean() {
		super(PU_NAME);
		Serializer serializer = new Persister();
		try {
			gConfig = serializer.read(GeneralConfigXML.class, this.getClass().getResourceAsStream("/general-config.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public PaymentTTPEntityManagerBean(String persistenceUnitName) {
		super(persistenceUnitName);
	}
	
	// TODO: g1 com a parametre d'entrada esta aqui pq payTTP no hagi d'anar a cercar a groupTTP g1 (rompria DRY ara)
	// TODO: tb li pas pairing per el mateix...
	public PayTTPSignedResponse chargeUser(SystemExitGammaUserXML gammaUserEncrypted, GammaDestProvider gammaPd, ElementWrapper g1ew, Pairing pairing) {
		PayTTPResponseAfterChargeAttempt response = null;
		KeyPair keyPair = null;
		GammaUserBean gub = null;
		try {
			// get AES key and IV from gamma
			String keyIvAES = gammaUserEncrypted.getKeyIvEncrypted();
			_logger.debug("key and iv encrypted (hex): " + keyIvAES);
			// decrypt keyIvAES by RSA decryption
			CipherProcess cp = CipherProcess.getInstance("RSA");
			keyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + "pttp.pem", 
															gConfig.getPrivateKeyPassword().toCharArray());
			
			byte[] kiv = cp.decrypt(keyPair.getPrivate(), 
									StringUtils.hexStringToByteArray( gammaUserEncrypted.getKeyIvEncrypted() ) );
			_logger.debug("kiv length: " + kiv.length*8 + "bits");
			// get key and iv from byte[]. I know the key and iv length in bytes, so, split
			byte[] k = Arrays.copyOfRange(kiv, 0, 16);
			byte[] v = Arrays.copyOfRange(kiv, 16, kiv.length);
			
			_logger.debug("key (hex): " + StringUtils.readHexString(k) + " " + k.length * 8 + "bits");
			_logger.debug("iv (hex): " + StringUtils.readHexString(v) + " " + v.length * 8 + "bits");
			// deserialize kiv contents to SymmetricKeyIvXML
			Serializer serializer = new Persister();
			
			IvParameterSpec iv = new IvParameterSpec(v);
			SecretKeySpec spec = new SecretKeySpec(k, "AES");
			SecretKey secretKey = (SecretKey) spec;
			_logger.debug("rebuild key: " + secretKey.getEncoded() + " " + secretKey.getEncoded().length*8 + "bits");
			_logger.debug("rebuild key: " + StringUtils.readHexString( secretKey.getEncoded() ) + " " + StringUtils.readHexString(secretKey.getEncoded()).length()*8 + "bits");
			
			byte[] gammaUDecrypted = AESCipherProcess.decrypt( StringUtils.hexStringToByteArray( gammaUserEncrypted.getDataEncrypted() ) , 
									 secretKey, 
									 iv);
			_logger.debug("gu decrypted: " + new String(gammaUDecrypted) );
			// I need the gammaUser object, so deserialize from gu
			serializer = new Persister();
			gub = serializer.read(GammaUserBean.class, new String( gammaUDecrypted ) );
			// I have gammaUser deserialized into gub
			_logger.debug("GammaUserBean decrypted: " + gub.getOmega1() + " " + gub.getFare() + " " + gub.getSerialNumber());
			
			GammaDestProviderBean gdpb = (GammaDestProviderBean) gammaPd;
			
			// ara són Bean, puc extreure propietats
			// agafa omega1
			String omega1 = gub.getOmega1();
			// agafa deltaU
			String deltau = gammaPd.getSigma().getDeltau();
			
			
			
			
			
			cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
			String yu = cp.decrypt(keyPair.getPrivate(), deltau );
			// TODO: desxifra deltau -> yu (pseudonim)
			
			//String yu = decrypted;
			_logger.debug("decrypted yu: " + yu);
			// extreu s1
			String s1 = gdpb.getSigma().getS1();
			// extreu c1
			String c1 = gdpb.getC1();
			// extreu fare
			Double fare = gdpb.getFare();
			// extreu serialNumber
			Integer serialNumber = gub.getSerialNumber();
			
			
			// verifies ZKP
			
		
			ElementWrapper omega1ew = PairingHelper.toElementWrapper(omega1, pairing, "Zr");
			ElementWrapper s1ew = PairingHelper.toElementWrapper(s1, pairing, "G1");
			ElementWrapper yuew = PairingHelper.toElementWrapper(yu, pairing, "G1");
			ElementWrapper c1ew = PairingHelper.toElementWrapper(c1, pairing, "Zr");
			boolean verification = PairingHelper.schnorrZKPverify(g1ew, omega1ew, s1ew, yuew, c1ew);
			_logger.debug("PayTTP ZKP verification out.5: " + verification);
			
			// search account related to yu
			PaymentTTPAccount pa = this.selectUser(yu);
			PaymentTTPTransaction pt = chargeUser(pa, fare);
			response = DestinationProviderLogic.composeOK(serialNumber, fare);
		} catch (NotEnoughBalanceException e) {
			response = DestinationProviderLogic.composeKO(gub, e.getBalanceBefore());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// mount and sign response
		PayTTPSignedResponse signedResponse = new PayTTPSignedResponse();
		try {
			StringWriter sw = new StringWriter();
			Serializer serializer = new Persister();
			serializer.write(response, sw);
			// sign contents
			SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			System.out.println(sw.getBuffer().toString());
			//byte[] signatureOutput = sp.sign(keyPair, StringUtils.hexStringToByteArray( sw.getBuffer().toString() ));
			byte[] signatureOutput = sp.sign(keyPair, sw.getBuffer().toString().getBytes() );
			String signature = StringUtils.readHexString(signatureOutput);
			// mount message xml
			signedResponse.setResponse(response);
			signedResponse.setSignature(signature);
			if (response instanceof UserChargedOKBeanXML) 
				signedResponse.setOk(true);
			else if (response instanceof UserNotChargedKOBeanXML)
				signedResponse.setOk(false);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return signedResponse;
		
	}
	
	
	public PaymentTTPTransaction chargeUser(PaymentTTPAccount pa, Double fare) throws NotEnoughBalanceException {
		
		Double balanceBefore = pa.getBalance();
		Double balanceAfter = balanceBefore - fare;
		_logger.debug("User " + pa.getYu() + " data: balanceBefore=" + balanceBefore + ", balanceAfter=" + balanceAfter + ", fare=" + fare);
		if (balanceAfter < 0.0) {			
			throw new NotEnoughBalanceException("Balance not enough", balanceBefore, fare);
		}
		else { 
			pa.setBalance(balanceAfter);
		}
		
		super.preparePersistence();
		super.getEntityManager().merge(pa);
		
		PaymentTTPTransaction pt = new PaymentTTPTransactionImpl();
		pt.setYu( pa.getYu() );
		pt.setBalanceAfter(balanceAfter);
		pt.setBalanceBefore(balanceBefore);
		pt.setCost(fare);
		pt.setDate( new Date() );
		pt.setPaymentAccount(pa);
		
		
		
		super.getEntityManager().persist(pt);
		super.getEntityTransaction().commit();
		super.closePersistence();
		
		
		return pt;
		
	}
	
	
	/**
	 * @deprecated
	 * 
	 * Charge Fee to the user identified by pseudonym yu
	 * 
	 * @param yu BigInteger pseudonym
	 * @param fare the fare to be charged to the user
	 * 
	 * @return the new account balance
	 * @throws NotEnoughBalanceException 
	 * */
	@SuppressWarnings("unchecked")
	public Double chargeFeeToUser(byte[] yu, Double fare) throws UserNotRegisteredException, MultipleEqualPseudonymsException, NotEnoughBalanceException {
		
		
		PaymentTTPAccount pa = selectUser(yu);
		

		Double balanceBefore = pa.getBalance();
		Double balanceAfter = balanceBefore - fare;
		if (balanceAfter < 0.0) {
			
			throw new NotEnoughBalanceException("Balance not enough", balanceBefore, fare);
		}
		else { 
			pa.setBalance(balanceAfter);
		}
		
		getEntityManager().merge(pa);
		
		PaymentTTPTransaction pt = new PaymentTTPTransactionImpl();
		pt.setYu( StringUtils.readHexString( yu ) );
		pt.setBalanceAfter(balanceAfter);
		pt.setBalanceBefore(balanceBefore);
		pt.setCost(fare);
		pt.setDate( new Date() );
		pt.setPaymentAccount(pa);
		
		
		getEntityManager().persist(pt);
		
		getEntityTransaction().commit();
		
		
		
		return balanceAfter;
	}
	
	
	
	public PaymentTTPAccount registerUser(PaymentTTPAccount pa) {
		super.preparePersistence();
		super.getEntityManager().persist(pa);
		super.getEntityTransaction().commit();
		super.closePersistence();
		// ¿?¿?¿?¿?¿?¿?
		return pa;
	}
	
	/**
	 * @deprecated
	 * 
	 * Register a user identified by a pseudonym
	 * 
	 * @param yu as the user pseudonym
	 * */
	public PaymentTTPAccount registerUser(byte[] yu) {
		
		
		PaymentTTPAccount pa = new PaymentTTPAccountImpl();
		pa.setYu( StringUtils.readHexString( yu ) );
		pa.setBalance(gConfig.getInitialBalance());
//		Hash hash = new Hash(yu);
//		hash.generate();
//		pa.setHashedYu( hash.readHexString() );
		pa.setHashedYu( StringUtils.readHexString( yu ) );
		
		getEntityManager().persist(pa);
		
		getEntityTransaction().commit();
		
		return getEntityManager().merge(pa);
	}
	
	
	@SuppressWarnings("unchecked")
	public PaymentTTPAccount selectUser(String yu) throws UserNotRegisteredException, MultipleEqualPseudonymsException {
		Query q = getEntityManager().createNamedQuery("searchPseudonym");
		q.setParameter("pseudonym", yu );
		List<PaymentTTPAccount> list = q.getResultList();
		
		if (list.isEmpty())
			throw new UserNotRegisteredException("User not registered in the system");

		if (list.size() > 1)
			throw new MultipleEqualPseudonymsException("More than one user with the same pseudonym");
		
		PaymentTTPAccount pa = (PaymentTTPAccount) list.get(0);
		

		return pa;
	}
	
	
	@SuppressWarnings("unchecked")
	public PaymentTTPAccount selectUser(byte[] yu) throws UserNotRegisteredException, MultipleEqualPseudonymsException {
		return selectUser( StringUtils.readHexString( yu ) );
		
		
	}
	
	
}
