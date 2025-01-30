package cat.uib.secom.crypto.afc.user.standalone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.Query;

import org.bouncycastle.util.encoders.Base64;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import cat.uib.secom.crypto.afc.bean.BetaSignedBean;
import cat.uib.secom.crypto.afc.bean.GammaUserBean;
import cat.uib.secom.crypto.afc.bean.SigmaBean;
import cat.uib.secom.crypto.afc.bean.SigmaSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketINBean;
import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.model.router.RouterHelper;
import cat.uib.secom.crypto.afc.net.message.NetworkEndPoints;
import cat.uib.secom.crypto.afc.net.message.xml.ErrorSignedXML;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitGammaUserXML;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML2;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML2;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML3;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML0DistanceBased;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML1DistanceBased;
import cat.uib.secom.crypto.afc.store.jpa.business.user.UserAFCEntityManagerBean;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogic;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogicDistanceBased;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketINImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketINSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketOUTImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketOUTSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.user.UserAFCImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.user.UserAFCJourneyImpl;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUT;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.crypto.sig.bbs.core.engines.AbstractBBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEnginePrecomputation;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngineTraceable;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSSignatureBean;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.AbstractEntityManagerBean;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableSignature;
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.crypto.symmetrickey.AESCipherProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

public class UserAFCClientStandalone extends AbstractEntityManagerBean { // it has database access capabilities since it extends AbstractEntityManagerBean class

	private static Logger _logger = LoggerFactory.getLogger(UserAFCClientStandalone.class);

	private final static String PU_NAME = "userAFC-PU";
	
	//protected static final String curveFileName = "d840347-175-161.param";
	
	protected static final String HOST = "localhost"; //"10.174.245.210";
	//protected static final Integer PORT = new Integer(20000); 
	//protected static final Integer PAY_PORT = new Integer(30000);
	//protected static final Integer GROUP_PORT = new Integer(20000);
	
	//protected static final Integer SOURCE_PORT = new Integer(40000);
	//protected static final Integer DESTINATION_PORT = new Integer(50000);
	
	protected BBSParameters bbsParameters = null;
	protected ElementWrapper g1;
	
	//protected String myCertificate = "my_certificate";  // TODO: to xml
	//protected String myRealIdentity = "andreupere_android_" + Math.random();  // TODO: to xml
	
	protected TicketINSignedBean ticketINSignedBean;
	protected TicketOUTSignedBean ticketOUTSignedBean;
	//protected String folder = "/home/apaspai/developing/keys_certs/"; // folder name where are stored tickets and info TODO: to xml
	
	protected KeyPair userRSAKeyPair, gttpRSAKeyPair, pttpRSAKeyPair, sProviderRSAKeyPair, dProviderRSAKeyPair;
	//protected String certFileName = folder + "1_pk.pem";  // TODO: to xml
	//protected String passwordRSAPrivateKey = "pere";  // TODO: to xml
	
	protected AbstractBBSEngine engine;
	protected static boolean PRECOMPUTATION = true;
	
	
	protected File folderOutputXML;
	
	protected static String KEYPAIR_FILE_NAME = "keypair.xml";
	protected static String KEYPAIR_REQUEST_FILE_NAME = "request_keypair.xml";
	protected static String TICKETIN_REQUEST_FILE_NAME = "entrance_request.xml";
	protected static String TICKETIN_FILE_NAME = "entrance_ticketin.xml";
	protected static String EXIT_RQ1_FILE_NAME = "exit_req1.xml";
	protected static String EXIT_BETAS_FILE_NAME = "exit_beta_signed.xml";
	protected static String EXIT_GAMMAU_FILE_NAME = "exit_gammau.xml";
	protected static String TICKETOUT_FILE_NAME = "exit_ticketout.xml";
	
	
	protected static boolean TIMEBASED = false;
	
	
	protected UserAFC me;
	protected UserAFCJourneyImpl myJourney;
	
	protected UserAFCEntityManagerBean userAFCEMB;

	
	
	
	private GeneralConfigXML gConfig;

	
	
	
	public UserAFCClientStandalone(String idRobot) {
		super(PU_NAME);
		// configurations from external xml (general-config.xml)
		
		// read general-config.xml
		//File fConfig = new File("general-config.xml");
		Serializer serializer = new Persister();
		
		try {
			gConfig = serializer.read(GeneralConfigXML.class, this.getClass().getResourceAsStream("/general-config.xml"));
			// override password for private key from certificate
			gConfig.setPrivateKeyPassword(idRobot);
		} catch (Exception e) {
			e.printStackTrace();
			_logger.warn(e.getMessage());
		}
		
		// init group signature parameters
		bbsParameters = new BBSParameters(gConfig.getElipticCurveFileName());
		// load RSA key pair for idRobot
		prepareRSAKeyPairs(idRobot);
		// folder where output xml files will be stored
		folderOutputXML = new File( gConfig.getOutputXmlsPath() + idRobot + "/" );		
		
	}
	
	
	
	
	public void prepareRSAKeyPairs(String idRobot) {
    	try {
			userRSAKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + idRobot + ".pem", 
																   gConfig.getPrivateKeyPassword().toCharArray());
			
			gttpRSAKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + "gttp.pem", 
					   											   gConfig.getPrivateKeyPassword().toCharArray());
					   											   
			pttpRSAKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + "pttp.pem", 
					   											   gConfig.getPrivateKeyPassword().toCharArray());
					   											   
			sProviderRSAKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + "sprovider.pem", 
					   													gConfig.getPrivateKeyPassword().toCharArray());
					   													
			dProviderRSAKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(gConfig.getCertsPath() + "dprovider.pem", 
					   													gConfig.getPrivateKeyPassword().toCharArray());
			
		} catch (IOException e) {
			e.printStackTrace();
			_logger.warn( e.getMessage() );
		}
    }

    

    
    
    protected ElementWrapper getG1FromGroupTTP() {
    	_logger.debug("Client requests G1 to the GroupTTP");
    	String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_G1, 
    								  null, 
    								  gConfig.getGttpPort());  // send request G1 and receive G1
    	byte[] g1 = StringUtils.hexStringToByteArray(received);
    	try {
	    	this.g1 = PairingHelper.toElementWrapper(g1, bbsParameters.getPairing(), "G1");
	        this.g1 = new ElementWrapper( this.g1.getElement().getImmutable() );  // Assure immutable element
	        _logger.debug("G1 is here: " + this.g1.getElement() );
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return this.g1;
    	
    }
    
    
    
    public UserAFC userRegistrationGroupTTP(String idRobot) {
    	_logger.debug("Let's go to register client " + idRobot + " to the GroupTTP");
    	// Extracts idRobot certificate from folder
    	String robotCertificate = "certificate_" + idRobot;
    	// test if g1 exist
    	if (g1 == null) {
    		g1 = getG1FromGroupTTP();
    	}
    	
    	_logger.debug("Creating a new AFC user...");
    	// create new user object
    	me = UserAFCLogic.newUserAFC(new UserAFCImpl(), g1, bbsParameters.getPairing(), robotCertificate, idRobot);
    	_logger.debug("AFC user created yu=" + me.getYu());
    	
    	
    	
    	
    	SignatureProcess sp;
    	String signature = "";
		try {
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			byte[] signatureOutput = sp.sign(userRSAKeyPair, StringUtils.hexStringToByteArray( me.getYu()));
			signature = StringUtils.readHexString(signatureOutput);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
    	

		UserRegistrationGroupTTPXML1 xml1 = new UserRegistrationGroupTTPXML1();
		xml1.setCertificate(me.getCertificate());
		xml1.setSignature(signature);
		xml1.setUserIdentity(me.getRealIdentity());
		xml1.setYu(me.getYu());
		
		Client client = new Client(HOST, gConfig.getGttpPort());
		//File fxml1 = new File("m1.xml");
		Serializer serializer = new Persister();
		Reader reader = null;
		Writer writer = null;
		UserRegistrationGroupTTPXML2 xml2 = null;
		
		try {
			client.createSocket();
			reader = client.prepareInput();
			writer = client.prepareOutput();
			client.write(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR);
			
			serializer.write(xml1, writer);
			// store request to file
			File f1 = new File(folderOutputXML, KEYPAIR_REQUEST_FILE_NAME);
			serializer.write(xml1, f1);
			
			xml2 = serializer.read(UserRegistrationGroupTTPXML2.class, reader);
			_logger.debug("keypair received... go to rebuild and store it");
			// store in file the keypair
			File f2 = new File(folderOutputXML, KEYPAIR_FILE_NAME);
			serializer.write(xml2, f2);
			
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
			
		
		
		
		//String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR, m1.serialize(), GROUP_PORT);
		//Log.v(TAG, "received key pair " + xml2.getGroupPublicKey().getG1());
		

		
		//long init = System.currentTimeMillis();
		BBSGroupPublicKey gpk = 
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( xml2.getGroupPublicKey().getG1(),
																			   xml2.getGroupPublicKey().getG2(),
																			   xml2.getGroupPublicKey().getH(),
																			   xml2.getGroupPublicKey().getU(),
																			   xml2.getGroupPublicKey().getV(),
																			   xml2.getGroupPublicKey().getOmega(),
																			   bbsParameters.getPairing() );
		
		BBSUserPrivateKey usk =
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( xml2.getUserPrivateKey().getAi(),
																			   xml2.getUserPrivateKey().getXi(),
																			   bbsParameters.getPairing() );
		
		
		_logger.debug("Group key pair received and rebuilt: (gpk=" + gpk + "; usk=" + usk + ")");
		
		me.setGroupPublicKey( toEmbeddableBBSGroupPublicKey(gpk) );
		me.setUserPrivateKey( toEmbeddableBBSUserPrivateKey(usk) );
		
		// save new user object (me) to database
		super.preparePersistence();
		super.getEntityManager().persist(me);
		super.closePersistence();
		
		_logger.info("Client registered successfully to the GroupTTP...");
		return me;
		

    }
    
    
    
    public void userRegistrationPayTTP(String idRobot) {
    	_logger.debug("Let's go to register client" + idRobot + " to the PaymentTTP");
    	// test if I know who I am. It I do not know who I am, extract from database
    	if (me == null) {
    		super.preparePersistence();
    		Query q = super.getEntityManager().createNamedQuery("searchIdRobot");
    		q.setParameter("idRobot", idRobot);
    		me = (UserAFC) q.getSingleResult();
    		super.closePersistence();
    	}
    	
    	BBSGroupPublicKey gpk = rebuildWorkingBBSGroupPublicKey(me, bbsParameters);
			
    	
    	ElementWrapper r0 = PairingHelper.random( bbsParameters.getPairing().getZr() );
		ElementWrapper s0 = PairingHelper.powZn(gpk.getG1() , r0);
		
		SignatureProcess sp;
    	String signature = "";
		try {
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			byte[] signatureOutput = sp.sign(userRSAKeyPair, me.getYu().getBytes());
			signature = StringUtils.readHexString(signatureOutput);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		UserRegistrationPayTTPXML1 xml1 = new UserRegistrationPayTTPXML1();
		xml1.setS0(s0.toHexString());
		xml1.setYu(me.getYu());
		xml1.setSignature(signature);
		
		Client client = new Client(HOST, gConfig.getPttpPort());
		Serializer serializer = new Persister();
		Reader reader = null;
		Writer writer = null;
		UserRegistrationPayTTPXML2 xml2 = null;
		try {
			client.createSocket();
			reader = client.prepareInput();
			writer = client.prepareOutput();
			
			serializer.write(xml1, writer);
			writer.flush();
			serializer = new Persister();
			xml2 = serializer.read(UserRegistrationPayTTPXML2.class, reader);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		

		UserRegistrationPayTTPXML3 xml3 = null;
		try {
			ElementWrapper c0 = PairingHelper.toElementWrapper(xml2.getC0(), bbsParameters.getPairing(), "Zr");
			ElementWrapper xuew = PairingHelper.toElementWrapper(me.getXu(), bbsParameters.getPairing(), "Zr");
			ElementWrapper omegaew = PairingHelper.schnorrZKPline(r0, c0, xuew );
			xml3 = new UserRegistrationPayTTPXML3();
			xml3.setOmega0(omegaew.toHexString());

			

			serializer.write(xml3, writer);
			writer.flush();
			
			// AQUI ES PODRIA AFEGIR REBRE UN 'OK' DEL SERVIDOR PER A DIR QUE ESTÀ REGISTRAT, QUIN SALDO TÉ, ETC.
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		_logger.debug("Client registered successfully to the PaymentTTP...");
    	
    }
    
    
    public void prepareEntrance(BBSGroupPublicKey gpk, BBSUserPrivateKey usk) {
    	if (PRECOMPUTATION) {
    		if (TIMEBASED)
    			engine = new BBSEnginePrecomputation(gpk, usk);
    		else
    			engine = new BBSEngineTraceable(gpk, usk);
    	}
    	else {
    		engine = new BBSEngine(gpk, usk);
    	}
    }
    
    
    public TicketINSignedBean systemEntrance(String idRobot, String idDoor, boolean timeBased) {
    	_logger.debug("Robot " + idRobot + " tries to enter through door " + idDoor);
    	// test if me is null. If it is, extract it from database
    	if (me == null) {
    		super.preparePersistence();
    		Query q = super.getEntityManager().createNamedQuery("searchIdRobot");
    		q.setParameter("idRobot", idRobot);
    		me = (UserAFC) q.getSingleResult();
    	}
    	
    	BBSGroupPublicKey gpk = rebuildWorkingBBSGroupPublicKey(me, bbsParameters);
    	BBSUserPrivateKey usk = rebuildWorkingBBSUserPrivateKey(me, bbsParameters);
    	
    	// now, I do not care about precomputation or not. PrepareEntrance runs here now
    	prepareEntrance(gpk, usk);
    	
    	UserAFCJourney uAFCJourney = new UserAFCJourneyImpl();
    	Sigma sigma = new SigmaBean();
    	SigmaSigned ssb = new SigmaSignedBean();
    	SigmaSigned ss;
		try {
			//long init = System.currentTimeMillis();
			if (timeBased) {
				ss = UserAFCLogic.openAFCJourney(uAFCJourney, 
											 	 me, 
											 	 sigma, 
											 	 ssb, 
											 	 (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey) gpk, 
											 	 (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey) usk, 
											 	 bbsParameters.getPairing(),
											 	 pttpRSAKeyPair.getPublic(),  // TODO: public key from Mc
											 	 engine); 
			} else {
				ss = UserAFCLogicDistanceBased.openAFCJourney(uAFCJourney, 
			 			  									  me, 
			 			  									  sigma, 
			 			  									  ssb, 
			 			  									  (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey) gpk, 
			 			  									  (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey) usk, 
			 			  									  bbsParameters.getPairing(),
			 			  									  pttpRSAKeyPair.getPublic(),  // TODO: public key from Mc
			 			  									  engine); 
			}
			//Log.i(TAG, "- - - encrypt yu, compose sigma and group sign: " + (System.currentTimeMillis() - init) + "ms");
			//timeGroupSignature = UserAFCLogic.signatureTime;
			
			ssb = new SigmaSignedBean();
			ssb.setSigma(ss.getSigma());
			ssb.setSignature(ss.getSignature());
	    	ssb.setEmbeddedGroupPublicKey(ss.getEmbeddedGroupPublicKey());
	    	Serializer serializer = new Persister();
	    	
	    	Client client = new Client(HOST, gConfig.getSourceProviderBasePort() + new Integer( idDoor ) );
	    	client.createSocket();
	    	Reader reader = client.prepareInput();
	    	Writer writer = client.prepareOutput();
	    	
	    	serializer.write(ssb, writer);
	    	
	    	// store ticketin request
	    	File f1 = new File(folderOutputXML, TICKETIN_REQUEST_FILE_NAME);
	    	serializer.write(ssb, f1);
	    	
	    	
	    	ticketINSignedBean = serializer.read(TicketINSignedBean.class, reader);
	    	TicketINBean tis = (TicketINBean) ticketINSignedBean.getTicketIN();
	    	
	    	// store ticketin
	    	File f2 = new File(folderOutputXML, TICKETIN_FILE_NAME);
	    	serializer.write(ticketINSignedBean, f2);
	    	
	    	
	    	// verify signature
	    	// extracts signature string and convert to byte[]
	    	byte[] rsaSignature = StringUtils.hexStringToByteArray( ticketINSignedBean.getSignature() );
	    	// extracts ticketin contents and serialize to xml to verify signature made by provider
	    	StringWriter sw = new StringWriter();
	    	serializer.write(tis, sw);
	    	StringBuffer buffer = sw.getBuffer();

	    	Hash hash = new Hash(buffer.toString().getBytes());
	    	hash.generate();

	    	SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
	    	//init = System.currentTimeMillis();
	    	boolean verification = sp.verify(sProviderRSAKeyPair, 
	    									 hash.generate(), 
	    									 rsaSignature);

			_logger.debug("Verification TicketIN: " + verification);

	    	
	    	client.closeInput();
	    	client.closeOutput();
	    	client.closeSocket();
	    	
	    	_logger.debug("The system goes to store the received ticketIN");
	    	// save ticket in the journey table in the user database
	    	uAFCJourney.setYu(me.getYu());
	    	uAFCJourney.setTicketINSigned(toTicketINSigned(ticketINSignedBean));
	    	// k and r already set in UserAFCLogic.openAFCJourney
	    	super.getEntityManager().persist(uAFCJourney);
	    	super.closePersistence();
	    	_logger.debug("The system has stored stored ticketIN successfully");
	    	
	    	
	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		
		return ticketINSignedBean;
    }
    
    
    
    public RouterHelper systemExit(String idRobot, String idDoor, Integer ticketSerialNumber, boolean timeBased) {
    	_logger.debug("Robot " + idRobot + " tries to exit through door " + idDoor);
    	
    	if (me == null) {
    		super.preparePersistence();
    		Query q = super.getEntityManager().createNamedQuery("searchIdRobot");
    		q.setParameter("idRobot", idRobot);
    		me = (UserAFC) q.getSingleResult();
    	}
    	
    	// search in the user database the previously opened journey identified by ticketSerialNumber
    	Query q = super.getEntityManager().createNamedQuery("searchTicketSerialNumber");
    	q.setParameter("serialNumber", ticketSerialNumber);
    	UserAFCJourney uAFCJourney = (UserAFCJourney) q.getSingleResult();
    	
    	
    	ticketINSignedBean = toTicketINSignedBean(uAFCJourney.getTicketINSigned(), me);
    	
    	
    	Serializer serializer = new Persister();
    	@SuppressWarnings("unused")
		Writer writer = null;
    	@SuppressWarnings("unused")
		Reader reader = null;
    	Client client = new Client(HOST, gConfig.getDestinationProviderBasePort() + new Integer(idDoor) );
	
		
    	try {
    		client.createSocket();
    		writer = client.prepareOutput();
    		reader = client.prepareInput();
    		
    		CipherProcess cp;
    		//long init = 0;
    		if (timeBased){
	    		// recuperam k
		    	String k = uAFCJourney.getK();

		    	// xifram k PK[Pd](k)
		    	cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
		    	
		    	//init = System.currentTimeMillis();
				String encryptedK = cp.encrypt(dProviderRSAKeyPair.getPublic(), k );

		    	
		    	// enviam missatge xml
				SystemExitXML1 xml1 = new SystemExitXML1();
		    	xml1.setEncryptedK( encryptedK );
		    	xml1.setTicketINSignedBean(ticketINSignedBean);
		    	serializer.write(xml1, ( BufferedWriter )client.getWriter());
		    	// store request
	    		File f1 = new File(folderOutputXML, EXIT_RQ1_FILE_NAME);
	    		serializer.write(xml1, f1);
    		}
    		else {
    			client.write("EXIT_REQUEST_PLEASE");
    			
    			String s;
        		String all = "";
        		BufferedReader br = new BufferedReader( client.getReader() );
        		
        		while ( !( s = br.readLine() ).contains("\0") ) {
        			all += s;
        		}
        		all += s;
        		
    			SystemExitXML0DistanceBased xml0 = serializer.read(SystemExitXML0DistanceBased.class, all);
    			String value = xml0.getValue();

    			BBSEngineTraceable engineTraceable = (BBSEngineTraceable) engine;

    			Signature signatureTraceable = engineTraceable.signTraceable(value);

    			SystemExitXML1DistanceBased xml1 = new SystemExitXML1DistanceBased();
    			xml1.setTicketINSignedBean(ticketINSignedBean);
    			
    			BBSSignatureBean stBean = new BBSSignatureBean();
    			stBean.setT1( signatureTraceable.getT1().toHexString() );
    			stBean.setT2( signatureTraceable.getT2().toHexString() );
    			stBean.setT3( signatureTraceable.getT3().toHexString() );
    			stBean.setC( signatureTraceable.getC().toHexString() );
    			stBean.setSalpha( signatureTraceable.getSalpha().toHexString() );
    			stBean.setSbeta( signatureTraceable.getSbeta().toHexString() );
    			stBean.setSx( signatureTraceable.getSx().toHexString() );
    			stBean.setSdelta1( signatureTraceable.getSdelta1().toHexString() );
    			stBean.setSdelta2( signatureTraceable.getSdelta2().toHexString() );
    			xml1.setSignature(stBean);
    			
    			BBSGroupPublicKeyBean gpkb = new BBSGroupPublicKeyBean();
    			gpkb.setG1( engine.getBBSGroupPublicKey().getG1().toHexString() );
    			gpkb.setG2( engine.getBBSGroupPublicKey().getG2().toHexString() );
    			gpkb.setH( engine.getBBSGroupPublicKey().getH().toHexString() );
    			gpkb.setU( engine.getBBSGroupPublicKey().getU().toHexString() );
    			gpkb.setV( engine.getBBSGroupPublicKey().getV().toHexString() );
    			gpkb.setOmega( engine.getBBSGroupPublicKey().getOmega().toHexString() );
    			xml1.setGroupPublicKey(gpkb);
    			
    			serializer.write(xml1, ( BufferedWriter )client.getWriter());
    		}
	    	
    		_logger.debug("First message is sent");
    		
    		client.flush();
    		
    		
    		
    		String s;
    		String all = "";
    		BufferedReader br = new BufferedReader( client.getReader() );
    		
    		while ( !( s = br.readLine() ).contains("\0") ) {
    			all += s;
    		}
    		all += s;

    		_logger.debug("second message is received and read");
    		
    		BetaSignedBean xml2 = null;
    		try {
    			xml2 = serializer.read(BetaSignedBean.class, all );
    		} catch(org.simpleframework.xml.core.ElementException errorMessage) {
    			ErrorSignedXML esXML = serializer.read(ErrorSignedXML.class, all);
    			_logger.debug("The system finds an error: " + esXML.getError().getMessage());
    			return new RouterHelper(false, esXML);
    		}
    		
    		// store betasigned
    		File f2 = new File(folderOutputXML, EXIT_BETAS_FILE_NAME);
    		serializer.write(xml2, f2);

    		_logger.debug("message stored in xml file");

    		// TODO: verificar signatura de Pd -> Ok
    		
    		
    		
    		// agafa fare des de betaSignedBean
    		Double fare = xml2.getBeta().getFare();
    		// agafa serial number
    		Integer serialNumber = ticketINSignedBean.getTicketIN().getSerialNumber();
    		// calcula omega1
    		

    		ElementWrapper r1ew = PairingHelper.toElementWrapper( uAFCJourney.getR1() , 
    															  bbsParameters.getPairing(), 
    															  "Zr");
    		ElementWrapper c1ew = PairingHelper.toElementWrapper(xml2.getBeta().getC1(), 
    															 bbsParameters.getPairing(), 
    															 "Zr");
    		ElementWrapper xuew = PairingHelper.toElementWrapper(me.getXu(), 
    															 bbsParameters.getPairing(), 
    															 "Zr");
    		ElementWrapper omega1ew = PairingHelper.schnorrZKPline(r1ew, 
    															   c1ew, 
    															   xuew);
    		_logger.debug("pairing calculation done");
    		
    		// composar gammaUserBean
    		GammaUserBean gammaUserBean = new GammaUserBean();
    		gammaUserBean.setFare(fare);
    		gammaUserBean.setOmega1(omega1ew.toHexString());
    		gammaUserBean.setSerialNumber(serialNumber);

    		
    		// extreure i xifrar gammaUserBean (xml)
    		StringWriter sw = new StringWriter();
    		serializer.write(gammaUserBean, sw);
    		cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");

    		_logger.debug("prepare AES");
    		// build IV and secret key for AES cipher
    		//init = System.currentTimeMillis();
    		IvParameterSpec ivAES = AESCipherProcess.createFastIv();
    		_logger.debug("IV ready");
    		SecretKey secretKeyAES = AESCipherProcess.createSymmetricKey(128);
    		_logger.debug("AES ready");
    		// encrypt with AES gammaUserBean xml serialization
    		byte[] gammaUserEncrypted = AESCipherProcess.encrypt(sw.getBuffer().toString().getBytes(), secretKeyAES, ivAES);
    		

    		_logger.debug("keylength:" + secretKeyAES.getEncoded().length * 8);
    		//_logger.debug("key: " + new String( secretKeyAES.getEncoded() ) + " " + secretKeyAES.getEncoded().length*8 + "bits");
    		//_logger.debug("iv: " + new String( ivAES.getIV() ) + ivAES.getIV().length*8 + "bits" );
    		_logger.debug("key (hex): " + StringUtils.readHexString( secretKeyAES.getEncoded() ) + " " + StringUtils.readHexString( secretKeyAES.getEncoded()).getBytes().length * 8 + "bits" );
    		_logger.debug("iv (hex): " + StringUtils.readHexString( ivAES.getIV() ) + " " + StringUtils.readHexString( ivAES.getIV() ).getBytes().length*8 + "bits" );
    		
    		byte[] join = new byte[secretKeyAES.getEncoded().length + ivAES.getIV().length];
			System.arraycopy(secretKeyAES.getEncoded(), 0, join, 0, secretKeyAES.getEncoded().length);
			System.arraycopy(ivAES.getIV(), 0, join, secretKeyAES.getEncoded().length, ivAES.getIV().length);
			

			cp = CipherProcess.getInstance("RSA");
			//init = System.currentTimeMillis();
			byte[] encryptedKeyIV = cp.encrypt(pttpRSAKeyPair.getPublic(), join);
    		
    		SystemExitGammaUserXML seguxml = new SystemExitGammaUserXML();
    		seguxml.setDataEncrypted( StringUtils.readHexString(gammaUserEncrypted) ); 
    		seguxml.setKeyIvEncrypted( StringUtils.readHexString( encryptedKeyIV ) );
    		
    		
    		serializer.write(seguxml, (BufferedWriter) client.getWriter());
    		client.flush();
    		
    		// store gammauser
    		File f3 = new File(folderOutputXML, EXIT_GAMMAU_FILE_NAME);
    		serializer.write(gammaUserBean, f3);
    		
    		
    		
    		// rebre ticket de sortida o error
    		try {
    			br = new BufferedReader( client.getReader() );
    			all = "";
    			s = "";
    			while ( !( s = br.readLine() ).contains("\0") ) {
        			all += s;
        		}
        		all += s;
    			
	    		//ticketOUTSignedBean = serializer.read(TicketOUTSignedBean.class, (BufferedReader) client.getReader());
	    		ticketOUTSignedBean = serializer.read(TicketOUTSignedBean.class, all );
	    		
	    		// store ticketout
	    		File f4 = new File(folderOutputXML, TICKETOUT_FILE_NAME);
	    		serializer.write(ticketOUTSignedBean, f4);
	    		
	    		
	    		
	    		// verify signature
		    	// extracts signature string and convert to byte[]
		    	byte[] rsaSignature = StringUtils.hexStringToByteArray( ticketOUTSignedBean.getSignatureOUT() );
		    	rsaSignature = Base64.decode(rsaSignature);
		    	// extracts ticketin contents and serialize to xml to verify signature made by provider
		    	sw = new StringWriter();
		    	serializer.write(ticketOUTSignedBean.getTicketOUT(), sw);
		    	StringBuffer buffer = sw.getBuffer();

		    	Hash hash = new Hash(buffer.toString().getBytes());
		    	hash.generate();

		    	SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
		    	//init = System.currentTimeMillis();
		    	boolean verification = sp.verify(dProviderRSAKeyPair, 
		    									 hash.generate(),  
		    									 rsaSignature);

		    	_logger.debug("Verification ticketOUT: " + verification);
	    		
	    		client.closeInput();
	    		client.closeOutput();
	    		client.closeSocket();
	    		
	    		
	    		_logger.debug("The system goes to store the received ticketOUT");
	    		uAFCJourney.setTicketOUTSigned( toTicketOUTSigned(ticketOUTSignedBean) );
	    		super.getEntityManager().persist(uAFCJourney);
	    		super.getEntityTransaction().commit();
	    		super.closePersistence();
	    		
	    		_logger.debug("The system has stored ticketOUT successfully");
	    		
	    		return new RouterHelper(true, ticketOUTSignedBean);
	    		
    		} catch(org.simpleframework.xml.core.ElementException errorMessage) {
    			ErrorSignedXML esXML = serializer.read(ErrorSignedXML.class, all);
    			_logger.debug("The system finds an error: " + esXML.getError().getMessage());
    			return new RouterHelper(false, esXML);
    		}
    		
    		
    		
    	} catch(IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return new RouterHelper(true, ticketOUTSignedBean);
    	
    	
    }
    
    
    
    
    protected String sendAndWait(String endPoint, String toBeSend, Integer port) {
    	String received = "";
    	Client client = new Client(HOST, port);
    	try {
	        client.createSocket();
	        client.prepareInput();
	        client.prepareOutput();
	        client.write(endPoint);
	        if (toBeSend != null) {
	        	client.write(toBeSend);
	        }
	        received = client.read();
	        client.closeInput();
	        client.closeOutput();
	        client.closeSocket();
	        
    	} catch(IOException e) {}
    	return received;
    }
	
    
    private BBSGroupPublicKey rebuildWorkingBBSGroupPublicKey(UserAFC uAFC, BBSParameters bbsParameters) {
    	return new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( uAFC.getGroupPublicKey().getG1(),
    																			  uAFC.getGroupPublicKey().getG2(),
    																			  uAFC.getGroupPublicKey().getH(),
    																			  uAFC.getGroupPublicKey().getU(),
    																			  uAFC.getGroupPublicKey().getV(),
    																			  uAFC.getGroupPublicKey().getOmega(),
				   																  bbsParameters.getPairing() );
    }
    
	private BBSUserPrivateKey rebuildWorkingBBSUserPrivateKey(UserAFC uAFC, BBSParameters bbsParameters) {
		return new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( uAFC.getUserPrivateKey().getAi(),
																				  uAFC.getUserPrivateKey().getXi(),
																				  bbsParameters.getPairing() );
	}
	
	private static EmbeddableBBSGroupPublicKey toEmbeddableBBSGroupPublicKey(BBSGroupPublicKey gpk) {
		EmbeddableBBSGroupPublicKey e = new EmbeddableBBSGroupPublicKey();
		e.setG1( gpk.getG1().toHexString() );
		e.setG2( gpk.getG2().toHexString() );
		e.setH( gpk.getH().toHexString() );
		e.setOmega( gpk.getOmega().toHexString() );
		e.setU( gpk.getU().toHexString() );
		e.setV( gpk.getU().toHexString() );
		return e;
	}
	
	private static EmbeddableBBSUserPrivateKey toEmbeddableBBSUserPrivateKey(BBSUserPrivateKey usk) {
		EmbeddableBBSUserPrivateKey e = new EmbeddableBBSUserPrivateKey();
		e.setXi( usk.getX().toHexString() );
		e.setAi( usk.getA().toHexString() );
		return e;
	}
	
	private static TicketINSigned toTicketINSigned(TicketINSignedBean tinBean) {
		TicketINSigned tins = new TicketINSignedImpl();
		
		TicketIN tin = new TicketINImpl();
		
		
		Sigma s = new SigmaImpl();
		s.setDeltau( tinBean.getTicketIN().getSigmaSigned().getSigma().getDeltau() );
		s.setHk( tinBean.getTicketIN().getSigmaSigned().getSigma().getHk() );
		s.setS1( tinBean.getTicketIN().getSigmaSigned().getSigma().getS1() );
		
		EmbeddableSignature signature = new EmbeddableSignature();
		signature.setT1( tinBean.getTicketIN().getSigmaSigned().getSignature().getT1() );
		signature.setT2( tinBean.getTicketIN().getSigmaSigned().getSignature().getT2() );
		signature.setT3( tinBean.getTicketIN().getSigmaSigned().getSignature().getT3() );
		signature.setC( tinBean.getTicketIN().getSigmaSigned().getSignature().getC() );
		signature.setSalpha( tinBean.getTicketIN().getSigmaSigned().getSignature().getSalpha() );
		signature.setSbeta( tinBean.getTicketIN().getSigmaSigned().getSignature().getSbeta() );
		signature.setSdelta1( tinBean.getTicketIN().getSigmaSigned().getSignature().getSdelta1() );
		signature.setSdelta2( tinBean.getTicketIN().getSigmaSigned().getSignature().getSdelta2() );
		signature.setSx( tinBean.getTicketIN().getSigmaSigned().getSignature().getSx() );
		
		
		SigmaSigned ss = new SigmaSignedImpl();
		ss.setSigma(s);
		ss.setSignature(signature);
		
		
		tin.setSerialNumber( tinBean.getTicketIN().getSerialNumber() );
		tin.setSrcProvider( tinBean.getTicketIN().getSrcProvider() );
		tin.setTimestamp1( tinBean.getTicketIN().getTimestamp1() );
		tin.setValidityTime( tinBean.getTicketIN().getValidityTime() );
		tin.setSigmaSigned( ss );
		
		tins.setTicketIN(tin);
		tins.setSignature(tinBean.getSignature());
		
		return tins;
	}
	
	private static TicketINSignedBean toTicketINSignedBean(TicketINSigned tins, UserAFC u) {
		TicketINSignedBean tinsBean = new TicketINSignedBean();
		
		TicketINBean tinBean = new TicketINBean();
		
		BBSGroupPublicKeyBean gpkBean = new BBSGroupPublicKeyBean();
		gpkBean.setG1( u.getGroupPublicKey().getG1() );
		gpkBean.setG2( u.getGroupPublicKey().getG2() );
		gpkBean.setH( u.getGroupPublicKey().getH() );
		gpkBean.setOmega( u.getGroupPublicKey().getOmega() );
		gpkBean.setU( u.getGroupPublicKey().getU() );
		gpkBean.setV( u.getGroupPublicKey().getV() );
		
		BBSSignatureBean sigBean = new BBSSignatureBean();
		sigBean.setT1( tins.getTicketIN().getSigmaSigned().getSignature().getT1() );
		sigBean.setT2( tins.getTicketIN().getSigmaSigned().getSignature().getT2() );
		sigBean.setT3( tins.getTicketIN().getSigmaSigned().getSignature().getT3() );
		sigBean.setC( tins.getTicketIN().getSigmaSigned().getSignature().getC() );
		sigBean.setSalpha( tins.getTicketIN().getSigmaSigned().getSignature().getSalpha() );
		sigBean.setSbeta( tins.getTicketIN().getSigmaSigned().getSignature().getSbeta() );
		sigBean.setSdelta1( tins.getTicketIN().getSigmaSigned().getSignature().getSdelta1() );
		sigBean.setSdelta2( tins.getTicketIN().getSigmaSigned().getSignature().getSdelta2() );
		sigBean.setSx( tins.getTicketIN().getSigmaSigned().getSignature().getSx() );
		
		SigmaBean sBean = new SigmaBean();
		sBean.setDeltau( tins.getTicketIN().getSigmaSigned().getSigma().getDeltau() );
		sBean.setHk( tins.getTicketIN().getSigmaSigned().getSigma().getHk() );
		sBean.setS1( tins.getTicketIN().getSigmaSigned().getSigma().getS1() );
		
		SigmaSignedBean ssBean = new SigmaSignedBean();
		ssBean.setSigma( sBean );
		ssBean.setSignature( sigBean );
		ssBean.setEmbeddedGroupPublicKey( gpkBean );
		
		tinBean.setSerialNumber( tins.getTicketIN().getSerialNumber() );
		tinBean.setSrcProvider( tins.getTicketIN().getSrcProvider() );
		tinBean.setTau1( tins.getTicketIN().getTimestamp1().getTime() );
		tinBean.setTimestamp1( tins.getTicketIN().getTimestamp1()  );
		tinBean.setValidity( tins.getTicketIN().getValidityTime().getTime() );
		tinBean.setValidityTime( tins.getTicketIN().getValidityTime() );
		tinBean.setSigmaSigned( ssBean );
		
		tinsBean.setSignature( tins.getSignature() );
		tinsBean.setTicketIN(tinBean);
		return tinsBean;
	}
	
	private static TicketOUTSigned toTicketOUTSigned(TicketOUTSignedBean toutsBean) {
		TicketOUTSigned touts = new TicketOUTSignedImpl();
		
		TicketOUT tout = new TicketOUTImpl();
		
		tout.setDstProvider( toutsBean.getTicketOUT().getDstProvider() );
		tout.setFare( toutsBean.getTicketOUT().getFare() );
		tout.setSerialNumber( toutsBean.getTicketOUT().getSerialNumber() );
		tout.setStr( toutsBean.getTicketOUT().getStr() );
		
		touts.setSignatureOUT(toutsBean.getSignatureOUT());
		touts.setTicketOUT(tout);
		return touts;
	}
	
}
