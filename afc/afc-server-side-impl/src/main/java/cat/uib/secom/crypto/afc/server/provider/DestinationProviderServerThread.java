package cat.uib.secom.crypto.afc.server.provider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Date;

import org.bouncycastle.util.encoders.Base64;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import cat.uib.secom.crypto.afc.bean.BetaBean;
import cat.uib.secom.crypto.afc.bean.BetaSignedBean;
import cat.uib.secom.crypto.afc.bean.GammaDestProviderBean;
import cat.uib.secom.crypto.afc.bean.GammaUserBean;
import cat.uib.secom.crypto.afc.bean.SigmaSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.net.message.xml.ErrorSignedXML;
import cat.uib.secom.crypto.afc.net.message.xml.ErrorXML;
import cat.uib.secom.crypto.afc.net.message.xml.PayTTPResponseAfterChargeAttempt;
import cat.uib.secom.crypto.afc.net.message.xml.PayTTPSignedResponse;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitGammaUserXML;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserChargedOKBeanXML;
import cat.uib.secom.crypto.afc.net.message.xml.UserNotChargedKOBeanXML;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.payttp.PaymentTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.provider.DestinationProviderEntityManagerBean;
import cat.uib.secom.crypto.afc.store.logic.DestinationProviderLogic;
import cat.uib.secom.crypto.afc.store.logic.SourceProviderLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationTransactionImpl;
import cat.uib.secom.crypto.afc.user.standalone.UserAFCClientStandalone;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;

import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableSignature;
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

public class DestinationProviderServerThread extends Thread {

	
	private GeneralConfigXML gConfig;
	
	private Socket _s;
	
	protected BBSParameters bbsParameters;
	
	protected DestinationProviderEntityManagerBean destinationProviderEMB;
	protected PaymentTTPEntityManagerBean payTTPEMB;
	protected GroupTTPEntityManagerBean groupTTP;
	protected ElementWrapper g1;
	
	protected Long stationId;
	protected File folderOutputXML;
	
	public DestinationProviderServerThread(Socket s, Long stationId, GeneralConfigXML gConfig) {
		super("SourceProvider");
		this.stationId = stationId;
		this.gConfig = gConfig;
		_s = s;
		folderOutputXML = new File( gConfig.getOutputXmlsPath() + "dprovider/" );
		bbsParameters = new BBSParameters(gConfig.getElipticCurveFileName());
		destinationProviderEMB = new DestinationProviderEntityManagerBean();
		payTTPEMB = new PaymentTTPEntityManagerBean();
		groupTTP = new GroupTTPEntityManagerBean();
		byte[] public_g1 = groupTTP.getManagerConfig("public_g1");
		try {
			g1 = PairingHelper.toElementWrapper(public_g1, bbsParameters.getPairing(), "G1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		Logger _logger = LoggerFactory.getLogger("Thread" + Thread.currentThread().getId());
		
		System.out.println("run client...");
		File f1 = new File(folderOutputXML, "f1.xml");
		File f2 = new File(folderOutputXML, "f2.xml");
		Serializer serializer = new Persister();
		
		Client client = new Client(_s);
		File toutFile = new File(folderOutputXML, "toutsigned.xml");
		
		try {
			client.createSocket();
			Reader reader = client.prepareInput();
			Writer writer = client.prepareOutput();
			
			SystemExitXML1 xml1 = serializer.read(SystemExitXML1.class, (BufferedReader)client.getReader());
			
			TicketINSignedBean tinSignedBean = xml1.getTicketINSignedBean();
			Integer serialNumber = tinSignedBean.getTicketIN().getSerialNumber();
			
			// verificar signatura ticketINSigned
			xml1.getTicketINSignedBean().getSignature();
			
			// recupera hk
			String hk = xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSigma().getHk();
			
			// agafa encrypted k
			String encryptedK = xml1.getEncryptedK();
			
			// desxifra encrypted k
			String certFileName = gConfig.getCertsPath() + "dprovider.pem";
			KeyPair keyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, 
																	gConfig.getPrivateKeyPassword().toCharArray());
			CipherProcess cp = CipherProcess.getInstance("RSA/ECB/PKCS1Padding");
			String k = cp.decrypt(keyPair.getPrivate(), encryptedK );
			
			
			// executa hash sobre k
			Hash hash = new Hash( StringUtils.hexStringToByteArray(k) );
			hash.generate();
			
			// compara
			if ( hash.readHexString().equals(hk)) {
				_logger.debug("hash(k) = sigma.hk");
			}
			else {
				_logger.debug("hash(k) not= sigma.hk");
			}
			
			// comprova que el serialNumber no estigui usat
			boolean used = destinationProviderEMB.isSerialNumberUsed( xml1.getTicketINSignedBean().getTicketIN().getSerialNumber() );
			_logger.debug("tin.sn used? " + used);
			if (used) {
				sendError("Sorry, ticket reused.", keyPair, client);
			}
			
			// extreu timestamp1
			Date timestamp1 = xml1.getTicketINSignedBean().getTicketIN().getTimestamp1();
			// comprova validity time not expired
			Date validityTime = xml1.getTicketINSignedBean().getTicketIN().getValidityTime();
			Date now = new Date();
			if (validityTime.before( now )) {
				_logger.debug("ticket expired. validity till: " + validityTime + " now: " + now);
				sendError("Sorry, ticket expired. Ticket valid till " + validityTime, keyPair, client);
				return;
			}
			
			_logger.debug("ticket not expired. validity till: " + validityTime);
			
			Date tau2 = new Date();
			Integer srcStationId = xml1.getTicketINSignedBean().getTicketIN().getSrcProvider();
			Integer dstStationId = destinationProviderEMB.iam( new Long(stationId) ).getIdentification();
			
			// calcular fare
			Double fare = DestinationProviderLogic.computeFare(srcStationId, dstStationId, timestamp1, tau2);
			// calcular c1
			ElementWrapper c1ew = PairingHelper.random( bbsParameters.getPairing().getZr() );
			// composes betaBean
			BetaBean betaBean = new BetaBean();
			betaBean.setTicketINSigned(tinSignedBean);
			betaBean.setK(k);
			betaBean.setFare(fare);
			betaBean.setC1(c1ew.toHexString());
			betaBean.setTimestamp1(tau2);
			betaBean.setDestProvider(dstStationId);
			
			

			// sign beta
			SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			
			StringWriter sw = new StringWriter();
			serializer.write(betaBean, sw);
			StringBuffer buffer = sw.getBuffer();
			byte[] signatureOutput = sp.sign(keyPair, 
					 						 buffer.toString().getBytes() );

			// representacio hexadecimal
			String signatureOutputHex = StringUtils.readHexString(signatureOutput);
			
		

			BetaSignedBean betaSignedBean = new BetaSignedBean();
			betaSignedBean.setBeta(betaBean);
			betaSignedBean.setSignature(signatureOutputHex);
			
			// enviar a U
			serializer.write(betaSignedBean, f1);
			// llegir f1 i enviar per socket

			
			serializer.write(betaSignedBean, (BufferedWriter)client.getWriter());
			client.write("\0");
			client.flush();
			

			
			
			
			_logger.debug("betaSigned sent...");

			
			
			
			// composes gammaPd
			GammaDestProviderBean gammaPd = new GammaDestProviderBean();
			gammaPd.setFare(fare);
			gammaPd.setSerialNumber(serialNumber);
			gammaPd.setSigma(xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSigma());
			gammaPd.setC1( c1ew.toHexString() );
			
			
			// rebre gammaU
			_logger.debug("Waiting for GammaUser...");
			SystemExitGammaUserXML systemExitGammaUserXML = serializer.read(SystemExitGammaUserXML.class, (BufferedReader)client.getReader());
			_logger.debug("GammaUser received...");
			_s.shutdownInput();
			
			// sent gammaUserBean and gammaPd to payTTP (no envia, directament crida a les funcions de persistencia i lògica de payTTP)
			// obtains ok* or ko* from payTTP
			PayTTPSignedResponse response = payTTPEMB.chargeUser(systemExitGammaUserXML, gammaPd, g1, bbsParameters.getPairing());
			
			
			
			certFileName = gConfig.getCertsPath() + "dprovider.pem";
			keyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, "pere".toCharArray());
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			
			if (response.isOk()) {
				_logger.debug("Client charged OK. Response from pTTP: " + response.getResponse().getMessage());
				// composes tout
				TicketOUTBean toutBean = new TicketOUTBean();
				toutBean.setSerialNumber(serialNumber);
				toutBean.setDstProvider(dstStationId);
				toutBean.setFare(fare);
				toutBean.setStr("leave taking at " + tau2);
				
				sw = new StringWriter();
				serializer.write(toutBean, sw);
				buffer = sw.getBuffer();
				Hash h2 = new Hash(buffer.toString().getBytes());
		    	h2.generate();
		    	_logger.debug("hash: " + h2.readHexString());
		    	
				signatureOutput = sp.sign(keyPair, 
						 				  h2.generate() );

				// representacio hexadecimal
				signatureOutput = Base64.encode(signatureOutput);
				signatureOutputHex = StringUtils.readHexString(signatureOutput);
				
				
				TicketOUTSignedBean toutSignedBean = new TicketOUTSignedBean();
				toutSignedBean.setSignatureOUT(signatureOutputHex);
				toutSignedBean.setTicketOUT(toutBean);
				
				// sent to U toutSigned
				client.prepareOutput();
				serializer.write(toutSignedBean, (BufferedWriter)client.getWriter());
				serializer.write(toutSignedBean, toutFile);
				client.write("\0");
				client.flush();
				_logger.debug("The destination provider has sent the ticketOUT to the client");
				
				// Annotate transaction
				destinationProviderEMB.newTransaction(new StationTransactionImpl(), new StationImpl(), dstStationId);
				
			} else {
				_logger.debug("Client not charged. Response from pTTP: " + response.getResponse().getMessage());
				
				// composes error message
//				ErrorXML error = new ErrorXML();
//				error.setMessage(response.getResponse().getMessage());
//				
//				sw = new StringWriter();
//				serializer.write(error, sw);
//				buffer = sw.getBuffer();
//				Hash h2 = new Hash(buffer.toString().getBytes());
//		    	h2.generate();
//		    	_logger.debug("hash: " + h2.readHexString());
//		    	
//				signatureOutput = sp.sign(keyPair, 
//						 				  h2.generate() );
//				
//				// representacio hexadecimal
//				signatureOutput = Base64.encode(signatureOutput);
//				signatureOutputHex = StringUtils.readHexString(signatureOutput);
//				
//				ErrorSignedXML errorSigned = new ErrorSignedXML();
//				errorSigned.setSignature(signatureOutputHex);
//				errorSigned.setError(error);
//				client.prepareOutput();
//				serializer.write(errorSigned, (BufferedWriter)client.getWriter());
//				client.write("\0");
//				client.flush();
				sendError(response.getResponse().getMessage(), keyPair, client);
				_logger.debug("The system sends an error message to the client");
				
				
			}
			
			
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

	
	private void sendError(String message, KeyPair keyPair, Client client) {
		try {
			ErrorXML error = new ErrorXML();
			error.setMessage(message);
			
			StringWriter sw = new StringWriter();
			Serializer serializer = new Persister();
			serializer.write(error, sw);
			StringBuffer buffer = sw.getBuffer();
			Hash h2 = new Hash(buffer.toString().getBytes());
	    	h2.generate();
	    	
	    	SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			byte[] signatureOutput = sp.sign(keyPair, 
					 				  h2.generate() );
			
			// representacio hexadecimal
			signatureOutput = Base64.encode(signatureOutput);
			String signatureOutputHex = StringUtils.readHexString(signatureOutput);
			
			ErrorSignedXML errorSigned = new ErrorSignedXML();
			errorSigned.setSignature(signatureOutputHex);
			errorSigned.setError(error);
			client.prepareOutput();
			serializer.write(errorSigned, (BufferedWriter)client.getWriter());
			client.write("\0");
			client.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
