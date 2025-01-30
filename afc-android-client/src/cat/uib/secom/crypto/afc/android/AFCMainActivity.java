package cat.uib.secom.crypto.afc.android;

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

import org.bouncycastle.util.encoders.Base64;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


import cat.uib.secom.crypto.afc.android.pojo.UserAFCAndroid;
import cat.uib.secom.crypto.afc.android.pojo.UserAFCJourneyAndroid;
import cat.uib.secom.crypto.afc.bean.BetaSignedBean;
import cat.uib.secom.crypto.afc.bean.GammaUserBean;
import cat.uib.secom.crypto.afc.bean.SigmaBean;
import cat.uib.secom.crypto.afc.bean.SigmaSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketINBean;
import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.net.message.NetworkEndPoints;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitGammaUserXML;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML2;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML2;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML3;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML0DistanceBased;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML1DistanceBased;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogic;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogicDistanceBased;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;
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
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.crypto.symmetrickey.AESCipherProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.NetworkClient;
import cat.uib.secom.utils.strings.StringUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class AFCMainActivity extends Activity {
	
	private BBSGroupPublicKey gpk;
	private BBSUserPrivateKey usk;
	
	
	public static final String TAG = "AFCMainActivity";
	protected static final String curveFileName = "d840347-175-161.param";
	
	protected static final String HOST = "10.174.245.210";
	protected static final Integer PORT = new Integer(20000); 
	protected static final Integer PAY_PORT = new Integer(30000);
	protected static final Integer GROUP_PORT = new Integer(20000);
	
	protected static final Integer SOURCE_PORT = new Integer(40000);
	protected static final Integer DESTINATION_PORT = new Integer(50000);
	
	
	protected BBSParameters bbsParameters = null;
	protected ElementWrapper g1;
	protected UserAFC me;
	protected UserAFCJourney myJourney;
	
	protected ElementWrapper s0;
	protected ElementWrapper r0;
	
	
	protected String myCertificate = "my_certificate";
	protected String myRealIdentity = "andreupere_android_" + Math.random();
	
	protected TicketINSignedBean ticketINSignedBean;
	protected TicketOUTSignedBean ticketOUTSignedBean;
	
	
	
	protected File dir; // where are stored tickets and info
	protected String folder = "/afc/"; // folder name where are stored tickets and info
	
	protected static String KEYPAIR_FILE_NAME = "keypair.xml";
	protected static String KEYPAIR_REQUEST_FILE_NAME = "request_keypair.xml";
	protected static String TICKETIN_REQUEST_FILE_NAME = "entrance_request.xml";
	protected static String TICKETIN_FILE_NAME = "entrance_ticketin.xml";
	protected static String EXIT_RQ1_FILE_NAME = "exit_req1.xml";
	protected static String EXIT_BETAS_FILE_NAME = "exit_beta_signed.xml";
	protected static String EXIT_GAMMAU_FILE_NAME = "exit_gammau.xml";
	protected static String TICKETOUT_FILE_NAME = "exit_ticketout.xml";
	
	
	protected KeyPair rsaKeyPair;
	protected String certFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + folder + "android_pk.pem";
	protected String passwordRSAPrivateKey = "pere";
	
	protected AbstractBBSEngine engine;
	protected static boolean PRECOMPUTATION = true;
	
	protected boolean debug = true;
	

	protected String slog;
	
	
	protected long timeGroupSignature = 0;
	protected static boolean TIMEBASED = false;
	
	
	public String getLog() {
		return slog;
	}
	
	
	
	
	private void storeInFile(Object obj, File folder, String fileName) throws Exception {
		Serializer s = new Persister();
		File f = new File(folder, fileName);
		s.write(obj, f);
	}
	
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //VMRuntime.getRuntime().setMinimumHeapSize(10000000);
        
        setContentView(R.layout.main);
        
        
        
        me = new UserAFCAndroid();
        myJourney = new UserAFCJourneyAndroid();
        
        if ( !debug ) {
        	bbsParameters = new BBSParameters(curveFileName);
            bbsParameters.getPairing();
        	prepareRSAKeyPair();
        	prepareStorage();
        	getG1FromGroupTTP();
        	userRegistrationGroupTTP();
        	userRegistrationPayTTP();
        	prepareEntrance();
        	systemEntrance(TIMEBASED);
        	systemExit(TIMEBASED);
        } else {
        	long total = System.currentTimeMillis();
        	
        	long initLoadPairing = System.currentTimeMillis();
        	bbsParameters = new BBSParameters(curveFileName);
            bbsParameters.getPairing();
            long timeLoadPairing = System.currentTimeMillis() - initLoadPairing;
            
            
            long initPrepareRSAKeyPair = System.currentTimeMillis();
        	prepareRSAKeyPair();
        	long timePrepareRSAKeyPair = System.currentTimeMillis() - initPrepareRSAKeyPair;
        	
        	long initPrepareStorage = System.currentTimeMillis();
        	prepareStorage();
        	long timePrepareStorage = System.currentTimeMillis() - initPrepareStorage;
        	
        	long initGetG1FromGroupTTP = System.currentTimeMillis();
        	getG1FromGroupTTP();
        	long timeGetG1FromGroupTTP = System.currentTimeMillis() - initGetG1FromGroupTTP;
        	
        	long initUserRegistrationGroupTTP = System.currentTimeMillis();
        	userRegistrationGroupTTP();
        	long timeUserRegistrationGroupTTP = System.currentTimeMillis() - initUserRegistrationGroupTTP;
        	
        	long initUserRegistrationPayTTP = System.currentTimeMillis();
        	userRegistrationPayTTP();
        	long timeUserRegistrationPayTTP = System.currentTimeMillis() - initUserRegistrationPayTTP;
        	
        	long initPrepareEntrance = System.currentTimeMillis();
        	prepareEntrance();
        	Log.i(TAG, "prepareEntrance: (precomputation=" + PRECOMPUTATION + "): " + (System.currentTimeMillis() - initPrepareEntrance) + "ms");
        	long timePrepareEntrance = System.currentTimeMillis() - initPrepareEntrance;
        	
        	long initEntrance = System.currentTimeMillis();
        	systemEntrance(TIMEBASED);
        	long timeEntrance = System.currentTimeMillis() - initEntrance;
        	
        	long initExit = System.currentTimeMillis();
        	systemExit(TIMEBASED);
        	long timeExit = System.currentTimeMillis() - initExit;
        	
        	long timeTotal = System.currentTimeMillis() - total;
        	
//        	try{
    		slog = timeLoadPairing + "\t" + 
			  	   timePrepareRSAKeyPair + "\t" + 
			  	   timePrepareStorage + "\t" + 
			  	   timeGetG1FromGroupTTP + "\t" + 
			  	   timeUserRegistrationGroupTTP + "\t" + 
			  	   timeUserRegistrationPayTTP + "\t" + 
			  	   timePrepareEntrance + "\t" + 
			  	   timeEntrance + "\t" + 
			  	   timeExit + "\t" +
			  	   timeGroupSignature + "\t" +
			  	   timeTotal + "\t" +
			  	   PRECOMPUTATION;
        	Log.i(TAG, slog);	
//				log.write(timeLoadPairing + "\t" + 
//						  timePrepareRSAKeyPair + "\t" + 
//						  timePrepareStorage + "\t" + 
//						  timeGetG1FromGroupTTP + "\t" + 
//						  timeUserRegistrationGroupTTP + "\t" + 
//						  timeUserRegistrationPayTTP + "\t" + 
//						  timePrepareEntrance + "\t" + 
//						  timeEntrance + "\t" + 
//						  timeExit + "\t" + 
//						  timeTotal + "\t" +
//						  PRECOMPUTATION);
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
        	
        }
        
        bbsParameters = null;
        
        
    }
    
    protected void prepareRSAKeyPair() {
    	try {
			rsaKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, passwordRSAPrivateKey.toCharArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    protected void prepareStorage() {
    	File sdCardPath = Environment.getExternalStorageDirectory();
		String path = sdCardPath.getAbsolutePath() + folder;
		dir = new File(path);
		if ( !dir.exists() ) 
			dir.mkdirs();
    }
    
    
    protected void getG1FromGroupTTP() {
    	Log.v(TAG, "requesting G1");
    	/***************************************************************
    	 **************** NETWORKING CODE STARTS ***********************
    	 ***************************************************************/
    	String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_G1, null, GROUP_PORT);  // send request G1 and receive G1
    	/***************************************************************
    	 **************** NETWORKING CODE ENDS *************************
    	 ***************************************************************/
    	
    	
    	Log.v(TAG, "received: " + received);
    	byte[] g1 = StringUtils.hexStringToByteArray(received);
    	try {
	    	this.g1 = PairingHelper.toElementWrapper(g1, bbsParameters.getPairing(), "G1");
	        this.g1 = new ElementWrapper( this.g1.getElement().getImmutable() );  // Assure immutable element
	        Log.v(TAG, "g1 is here: " + this.g1.getElement());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    
    protected void userRegistrationGroupTTP() {
    	Log.v(TAG, "creating new User AFC");
    	me = UserAFCLogic.newUserAFC(me, g1, bbsParameters.getPairing(), myCertificate, myRealIdentity);
    	Log.v(TAG, "new user created");
    	
    	SignatureProcess sp;
    	String signature = "";
		try {
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			byte[] signatureOutput = sp.sign(rsaKeyPair, StringUtils.hexStringToByteArray( me.getYu()));
			signature = StringUtils.readHexString(signatureOutput);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		Log.v(TAG, "request signed");
    	

		
		Log.v(TAG, "request keypair");
		UserRegistrationGroupTTPXML1 xml1 = new UserRegistrationGroupTTPXML1();
		xml1.setCertificate(myCertificate);
		xml1.setSignature(signature);
		xml1.setUserIdentity(me.getRealIdentity());
		xml1.setYu(me.getYu());
		
		NetworkClient client = new NetworkClient(HOST, GROUP_PORT);
		//File fxml1 = new File("m1.xml");
		Serializer serializer = new Persister();
		Reader reader = null;
		Writer writer = null;
		UserRegistrationGroupTTPXML2 xml2 = null;
		
		try {
			/***************************************************************
	    	 **************** NETWORKING CODE STARTS ***********************
	    	 ***************************************************************/
			client.createSocket();
			reader = client.prepareInput();
			writer = client.prepareOutput();
			client.write(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR);
			serializer.write(xml1, writer);
			
			
			// store request to file
			this.storeInFile(xml1, dir, KEYPAIR_REQUEST_FILE_NAME);
			
			
			xml2 = serializer.read(UserRegistrationGroupTTPXML2.class, reader);
			
			// store in file the keypair
			this.storeInFile(xml2, dir, KEYPAIR_FILE_NAME);
			
			
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
			
		
		
		
		//String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR, m1.serialize(), GROUP_PORT);
		Log.v(TAG, "received key pair " + xml2.getGroupPublicKey().getG1());
		
//		UserRegistrationGroupM2 m2 = new UserRegistrationGroupM2();
//		m2 = (UserRegistrationGroupM2) m2.desearialize(received);
		
		long init = System.currentTimeMillis();
		gpk = 
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( xml2.getGroupPublicKey().getG1(),
																			   xml2.getGroupPublicKey().getG2(),
																			   xml2.getGroupPublicKey().getH(),
																			   xml2.getGroupPublicKey().getU(),
																			   xml2.getGroupPublicKey().getV(),
																			   xml2.getGroupPublicKey().getOmega(),
																			   bbsParameters.getPairing() );
		
		usk =
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( xml2.getUserPrivateKey().getAi(),
																			   xml2.getUserPrivateKey().getXi(),
																			   bbsParameters.getPairing() );
		
		Log.i(TAG, "- - - Rebuild group key pair: " + (System.currentTimeMillis() - init) + "ms");
		
		Log.v(TAG, "retrieved and rebuilt gpk: " + gpk);
		Log.v(TAG, "retrieved and rebuilt usk: " + usk);
		

    }
    
    
    
    protected void userRegistrationPayTTP() {
    	
    	ElementWrapper r0 = PairingHelper.random( bbsParameters.getPairing().getZr() );
		ElementWrapper s0 = PairingHelper.powZn( gpk.getG1() , r0);
		
		SignatureProcess sp;
    	String signature = "";
		try {
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			byte[] signatureOutput = sp.sign(rsaKeyPair, me.getYu().getBytes());
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
		
		NetworkClient client = new NetworkClient(HOST, PAY_PORT);
		Serializer serializer = new Persister();
		Reader reader = null;
		Writer writer = null;
		UserRegistrationPayTTPXML2 xml2 = null;
		try {
			/***************************************************************
	    	 **************** NETWORKING CODE STARTS ***********************
	    	 ***************************************************************/
			client.createSocket();
			reader = client.prepareInput();
			writer = client.prepareOutput();
			//client.write(NetworkEndPoints.USER_REGISTRATION_PAY_TTP_1);
			
			serializer.write(xml1, writer);
			writer.flush();
			serializer = new Persister();
			xml2 = serializer.read(UserRegistrationPayTTPXML2.class, reader);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Log.v(TAG, "xml2 received: " + xml2.getC0());
		UserRegistrationPayTTPXML3 xml3 = null;
		try {
			ElementWrapper c0 = PairingHelper.toElementWrapper(xml2.getC0(), bbsParameters.getPairing(), "Zr");
			ElementWrapper xuew = PairingHelper.toElementWrapper(me.getXu(), bbsParameters.getPairing(), "Zr");
			ElementWrapper omegaew = PairingHelper.schnorrZKPline(r0, c0, xuew );
			xml3 = new UserRegistrationPayTTPXML3();
			xml3.setOmega0(omegaew.toHexString());
			System.out.println("xml3.omega " + xml3.getOmega0() );
			//client.write( NetworkEndPoints.USER_REGISTRATION_PAY_TTP_3 );
			//writer = client.prepareOutput();
			

			serializer.write(xml3, writer);
			writer.flush();
			
			// AQUI ES PODRIA AFEGIR REBRE UN 'OK' DEL SERVIDOR PER A DIR QUE ESTÀ REGISTRAT, QUIN SALDO TÉ, ETC.
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
    	
    }
    
    
    protected void prepareEntrance() {
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
    
    
    protected void systemEntrance(boolean timeBased) {
    	
    	Sigma sigma = new SigmaBean();
    	SigmaSigned ssb = new SigmaSignedBean();
    	SigmaSigned ss;
		try {
			long init = System.currentTimeMillis();
			if (timeBased) {
				ss = UserAFCLogic.openAFCJourney(myJourney, 
											 	 me, 
											 	 sigma, 
											 	 ssb, 
											 	 (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey) gpk, 
											 	 (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey) usk, 
											 	 bbsParameters.getPairing(),
											 	 rsaKeyPair.getPublic(),  // TODO: public key from Mc
											 	 engine); 
			} else {
				ss = UserAFCLogicDistanceBased.openAFCJourney(myJourney, 
			 			  									  me, 
			 			  									  sigma, 
			 			  									  ssb, 
			 			  									  (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey) gpk, 
			 			  									  (cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey) usk, 
			 			  									  bbsParameters.getPairing(),
			 			  									  rsaKeyPair.getPublic(),  // TODO: public key from Mc
			 			  									  engine); 
			}
			Log.i(TAG, "- - - encrypt yu, compose sigma and group sign: " + (System.currentTimeMillis() - init) + "ms");
			timeGroupSignature = UserAFCLogic.signatureTime;
			
			ssb = new SigmaSignedBean();
			ssb.setSigma(ss.getSigma());
			ssb.setSignature(ss.getSignature());
	    	ssb.setEmbeddedGroupPublicKey(ss.getEmbeddedGroupPublicKey());
	    	Serializer serializer = new Persister();
	    	
	    	
	    	/***************************************************************
	    	 **************** NETWORKING CODE STARTS ***********************
	    	 ***************************************************************/
	    	NetworkClient client = new NetworkClient(HOST, SOURCE_PORT);
	    	//File entrance1 = new File("entrance1.xml");
	    	client.createSocket();
	    	Reader reader = client.prepareInput();
	    	Writer writer = client.prepareOutput();
	    	serializer.write(ssb, writer);
	    	
	    	// store ticketin request
	    	this.storeInFile(ssb, dir, TICKETIN_REQUEST_FILE_NAME);
	    	
	    	
	    	
	    	ticketINSignedBean = serializer.read(TicketINSignedBean.class, reader);
	    	TicketINBean tis = (TicketINBean) ticketINSignedBean.getTicketIN();
	    	
	    	// store ticketin
	    	this.storeInFile(ticketINSignedBean, dir, TICKETIN_FILE_NAME);
	    	

	    	
	    	Log.v(TAG, "ticket in received: " + ticketINSignedBean);
	    	
	    	
	    	// verify signature
	    	// extracts signature string and convert to byte[]
	    	byte[] rsaSignature = StringUtils.hexStringToByteArray( ticketINSignedBean.getSignature() );
	    	// extracts ticketin contents and serialize to xml to verify signature made by provider
	    	StringWriter sw = new StringWriter();
	    	serializer.write(tis, sw);
	    	StringBuffer buffer = sw.getBuffer();
	    	Log.v(TAG, "for verification: " + buffer.toString());
	    	Hash hash = new Hash(buffer.toString().getBytes());
	    	hash.generate();
	    	Log.v(TAG, "hash: " + hash.readHexString());
	    	SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
	    	init = System.currentTimeMillis();
	    	boolean verification = sp.verify(rsaKeyPair, 
	    									 hash.generate(), 
	    									 rsaSignature);
	    	Log.i(TAG, "- - - ticketIn RSA signature verification: " + (System.currentTimeMillis() - init) + "ms");
			
	    	Log.v(TAG, "ticketin verification: " + verification);
	    	
	    	client.closeInput();
	    	client.closeOutput();
	    	client.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
    }
    
    
    
    protected void systemExit(boolean timeBased) {
    	
    	
    	Serializer serializer = new Persister();
    	Writer writer = null;
    	Reader reader = null;
    	NetworkClient client = new NetworkClient(HOST, DESTINATION_PORT);
	
		
    	try {
    		client.createSocket();
    		writer = client.prepareOutput();
    		reader = client.prepareInput();
    		
    		CipherProcess cp;
    		long init = 0;
    		if (timeBased){
	    		// recuperam k
		    	String k = myJourney.getK();
		    	Log.v(TAG, "decrypted k:" + k);
		    	// xifram k PK[Pd](k)
		    	cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
		    	
		    	init = System.currentTimeMillis();
				String encryptedK = cp.encrypt(rsaKeyPair.getPublic(), k );
				Log.i(TAG, "- - - encrypting k with RSA: " + (System.currentTimeMillis() - init) + "ms");
		    	
		    	// enviam missatge xml
				SystemExitXML1 xml1 = new SystemExitXML1();
		    	xml1.setEncryptedK( encryptedK );
		    	xml1.setTicketINSignedBean(ticketINSignedBean);
		    	
		    	/***************************************************************
		    	 **************** NETWORKING CODE STARTS ***********************
		    	 ***************************************************************/
		    	serializer.write(xml1, ( BufferedWriter )client.getWriter());
		    	// store request
		    	this.storeInFile(xml1, dir, EXIT_RQ1_FILE_NAME);
	    		
    		}
    		else {
    			
    			/***************************************************************
    	    	 **************** NETWORKING CODE STARTS ***********************
    	    	 ***************************************************************/
    			client.write("EXIT_REQUEST_PLEASE");
    			
    			String s;
        		String all = "";
        		BufferedReader br = new BufferedReader( client.getReader() );
        		
        		while ( !( s = br.readLine() ).contains("\0") ) {
        			all += s;
        			System.out.println("rebut: " + s);
        		}
        		all += s;
        		
    			SystemExitXML0DistanceBased xml0 = serializer.read(SystemExitXML0DistanceBased.class, all);
    			String value = xml0.getValue();
    			Log.i(TAG, "cast engine");
    			BBSEngineTraceable engineTraceable = (BBSEngineTraceable) engine;
    			Log.i(TAG, "init signTraceable");
    			Signature signatureTraceable = engineTraceable.signTraceable(value);
    			Log.i(TAG, "end signTraceable");
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
    			
    			
    			/***************************************************************
    	    	 **************** NETWORKING CODE STARTS ***********************
    	    	 ***************************************************************/
    			serializer.write(xml1, ( BufferedWriter )client.getWriter());
    		}
	    	
    		
    		client.flush();
    		
    		
    		
    		/***************************************************************
        	 **************** NETWORKING CODE STARTS ***********************
        	 ***************************************************************/
    		String s;
    		String all = "";
    		BufferedReader br = new BufferedReader( client.getReader() );
    		
    		while ( !( s = br.readLine() ).contains("\0") ) {
    			all += s;
    			System.out.println("rebut: " + s);
    			//fbw.write(s);
    		}
    		all += s;

    		
    		
    		BetaSignedBean xml2 = serializer.read(BetaSignedBean.class, all );
    		
    		// store betasigned
    		this.storeInFile(xml2, dir, EXIT_BETAS_FILE_NAME);
    		


    		Log.v(TAG, "betaSigned received..." + xml2);
    		
    		
    		
    		// agafa fare des de betaSignedBean
    		Double fare = xml2.getBeta().getFare();
    		// agafa serial number
    		Integer serialNumber = ticketINSignedBean.getTicketIN().getSerialNumber();
    		// calcula omega1
    		

    		ElementWrapper r1ew = PairingHelper.toElementWrapper( myJourney.getR1() , 
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
    		
    		// composar gammaUserBean
    		GammaUserBean gammaUserBean = new GammaUserBean();
    		gammaUserBean.setFare(fare);
    		gammaUserBean.setOmega1(omega1ew.toHexString());
    		gammaUserBean.setSerialNumber(serialNumber);
    		Log.v(TAG, "GammaUserBean: " + gammaUserBean.getOmega1() + " " + gammaUserBean.getFare() + " " + gammaUserBean.getSerialNumber());
    		
    		// extreure i xifrar gammaUserBean (xml)
    		StringWriter sw = new StringWriter();
    		serializer.write(gammaUserBean, sw);
    		cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
    		Log.v(TAG, "gammaUser content length (bytes): " + sw.getBuffer().toString().getBytes().length);
    		
    		// build IV and secret key for AES cipher
    		init = System.currentTimeMillis();
    		IvParameterSpec ivAES = AESCipherProcess.createIv();
    		SecretKey secretKeyAES = AESCipherProcess.createSymmetricKey(128);
    		// encrypt with AES gammaUserBean xml serialization
    		byte[] gammaUserEncrypted = AESCipherProcess.encrypt(sw.getBuffer().toString().getBytes(), secretKeyAES, ivAES);
    		Log.i(TAG, "- - - generate key/iv and encrypt gammaU with AES: " + (System.currentTimeMillis() - init) + "ms");
    		
    		Log.v(TAG, "gammaUser to encrypt: " + sw.getBuffer().toString());
    		Log.v(TAG, "gammaUser encrypted: " + StringUtils.readHexString( gammaUserEncrypted ) );
    		


    		// encrypt key and IV with RSA
    		System.out.println("keylength:" + secretKeyAES.getEncoded().length * 8);
    		System.out.println("key: " + new String( secretKeyAES.getEncoded() ) + " " + secretKeyAES.getEncoded().length*8 + "bits");
    		System.out.println("iv: " + new String( ivAES.getIV() ) + ivAES.getIV().length*8 + "bits" );
    		System.out.println("key (hex): " + StringUtils.readHexString( secretKeyAES.getEncoded() ) + " " + StringUtils.readHexString( secretKeyAES.getEncoded()).getBytes().length * 8 + "bits" );
    		System.out.println("iv (hex): " + StringUtils.readHexString( ivAES.getIV() ) + " " + StringUtils.readHexString( ivAES.getIV() ).getBytes().length*8 + "bits" );
    		
    		byte[] join = new byte[secretKeyAES.getEncoded().length + ivAES.getIV().length];
			System.arraycopy(secretKeyAES.getEncoded(), 0, join, 0, secretKeyAES.getEncoded().length);
			System.arraycopy(ivAES.getIV(), 0, join, secretKeyAES.getEncoded().length, ivAES.getIV().length);
			

			cp = CipherProcess.getInstance("RSA");
			init = System.currentTimeMillis();
			byte[] encryptedKeyIV = cp.encrypt(rsaKeyPair.getPublic(), join);
			Log.i(TAG, "- - - encrypt AES key/iv with RSA: " + (System.currentTimeMillis() - init) + "ms");
    		
    		SystemExitGammaUserXML seguxml = new SystemExitGammaUserXML();
    		seguxml.setDataEncrypted( StringUtils.readHexString(gammaUserEncrypted) ); 
    		seguxml.setKeyIvEncrypted( StringUtils.readHexString( encryptedKeyIV ) );
    		

    		Log.v(TAG, "sending gammaUser encrypted...");
    		
    		/***************************************************************
        	 **************** NETWORKING CODE STARTS ***********************
        	 ***************************************************************/
    		serializer.write(seguxml, (BufferedWriter) client.getWriter());
    		client.flush();
    		Log.v(TAG, "gammaUser sent...");
    		
    		// store gammauser
    		this.storeInFile(gammaUserBean, dir, EXIT_GAMMAU_FILE_NAME);
    		
    		
    		
    		
    		// rebre ticket de sortida
    		Log.v(TAG, "ticketOUTSigned not received yet...");
    		
    		
    		/***************************************************************
        	 **************** NETWORKING CODE STARTS ***********************
        	 ***************************************************************/
    		ticketOUTSignedBean = serializer.read(TicketOUTSignedBean.class, (BufferedReader) client.getReader());
    		Log.v(TAG, "ticketOUTSigned received...");
    		
    		// store ticketout
    		this.storeInFile(ticketOUTSignedBean, dir, TICKETOUT_FILE_NAME);
    		
    		
    		
    		
    		// verify signature
	    	// extracts signature string and convert to byte[]
	    	byte[] rsaSignature = StringUtils.hexStringToByteArray( ticketOUTSignedBean.getSignatureOUT() );
	    	rsaSignature = Base64.decode(rsaSignature);
	    	// extracts ticketin contents and serialize to xml to verify signature made by provider
	    	sw = new StringWriter();
	    	serializer.write(ticketOUTSignedBean.getTicketOUT(), sw);
	    	StringBuffer buffer = sw.getBuffer();
	    	Log.v(TAG, "for verification: " + buffer.toString());
	    	Hash hash = new Hash(buffer.toString().getBytes());
	    	hash.generate();
	    	Log.v(TAG, "hash: " + hash.readHexString());
	    	SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
	    	init = System.currentTimeMillis();
	    	boolean verification = sp.verify(rsaKeyPair, 
	    									 hash.generate(),  
	    									 rsaSignature);
			Log.i(TAG, "- - - ticketout verification: " + (System.currentTimeMillis() - init) + "ms");
			
	    	Log.v(TAG, "ticketout verification: " + verification);
    		
    		
    		// fi
    		client.closeInput();
    		client.closeOutput();
    		client.closeSocket();
    		
    	} catch(IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	
    }
    
    
    
    
    protected String sendAndWait(String endPoint, String toBeSend, Integer port) {
    	String received = "";
    	NetworkClient client = new NetworkClient(HOST, port);
    	try {
        	Log.v(TAG, "create socket...");
	        client.createSocket();
	        client.prepareInput();
	        client.prepareOutput();
	        Log.v(TAG, "input output...");
	        client.write(endPoint);
	        if (toBeSend != null) {
	        	client.write(toBeSend);
	        }
	        received = client.read();
	        Log.v(TAG, "received: " + received);
	        client.closeInput();
	        client.closeOutput();
	        client.closeSocket();
	        
    	} catch(IOException e) {}
    	return received;
    }
    
    
    
    
    
    
}