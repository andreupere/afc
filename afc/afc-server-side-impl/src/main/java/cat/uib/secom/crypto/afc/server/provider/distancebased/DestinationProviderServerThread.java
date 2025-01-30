package cat.uib.secom.crypto.afc.server.provider.distancebased;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.net.message.xml.PayTTPSignedResponse;
import cat.uib.secom.crypto.afc.net.message.xml.SystemExitGammaUserXML;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML1DistanceBased;
import cat.uib.secom.crypto.afc.net.message.xml.distancebased.SystemExitXML0DistanceBased;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.payttp.PaymentTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.provider.DestinationProviderEntityManagerBean;
import cat.uib.secom.crypto.afc.store.logic.DestinationProviderLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationTransactionImpl;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngineTraceable;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSSignatureBean;
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.CipherProcess;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

public class DestinationProviderServerThread extends Thread {

	private static Logger _logger = LoggerFactory.getLogger(DestinationProviderServerThread.class);

	
	private Socket _s;
	
	protected BBSParameters bbsParameters;
	
	protected DestinationProviderEntityManagerBean destinationProviderEMB;
	protected PaymentTTPEntityManagerBean payTTPEMB;
	protected GroupTTPEntityManagerBean groupTTP;
	protected ElementWrapper g1;
	
	protected Long stationId;
	
	public DestinationProviderServerThread(Socket s, Long stationId) {
		super("SourceProvider");
		this.stationId = stationId;
		_s = s;
		bbsParameters = new BBSParameters("d840347-175-161.param");
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
		System.out.println("run client...");
		File f1 = new File("f1.xml");
		File f2 = new File("f2.xml");
		Serializer serializer = new Persister();
		
		Client client = new Client(_s);
		File toutFile = new File("toutsigned.xml");
		
		try {
			client.createSocket();
			Reader reader = client.prepareInput();
			Writer writer = client.prepareOutput();
			

			client.read();

			
			// client wants to exit, I send XML with a bigsigma
			ElementWrapper bigSigma = PairingHelper.random(bbsParameters.getPairing().getZr());
			SystemExitXML0DistanceBased xml0 = new SystemExitXML0DistanceBased();
			xml0.setValue(bigSigma.toHexString());
			
			serializer.write(xml0, (BufferedWriter) client.getWriter() );
			client.write("\0");
			client.flush();

			
			
			
			SystemExitXML1DistanceBased xml1 = serializer.read(SystemExitXML1DistanceBased.class, (BufferedReader)client.getReader());
			
			TicketINSignedBean tinSignedBean = xml1.getTicketINSignedBean();
			BBSSignatureBean signatureBean = xml1.getSignature();
			BBSGroupPublicKeyBean groupPublicKeyBean = xml1.getGroupPublicKey();
			
			Integer serialNumber = tinSignedBean.getTicketIN().getSerialNumber();
			
			// verificar signatura ticketINSigned
			xml1.getTicketINSignedBean().getSignature();
			
			// rebuild BBSGroupPublicKey
			BBSGroupPublicKey bbsgpk = new BBSGroupPublicKey( xml1.getGroupPublicKey().getG1(),
															  xml1.getGroupPublicKey().getG2(),
															  xml1.getGroupPublicKey().getH(),
															  xml1.getGroupPublicKey().getU(),
															  xml1.getGroupPublicKey().getV(),
															  xml1.getGroupPublicKey().getOmega(),
															  bbsParameters.getPairing());
			// rebuild BBSSignature
			Signature bbss = new Signature( StringUtils.hexStringToByteArray( xml1.getSignature().getT1() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getT2() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getT3() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getC() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getSalpha() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getSbeta() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getSx() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getSdelta1() ),
											StringUtils.hexStringToByteArray( xml1.getSignature().getSdelta2() ),
											bbsParameters.getPairing());
			
			BBSEngineTraceable engine = new BBSEngineTraceable(bbsgpk);
			boolean verification = engine.verify(bbss, bigSigma.toHexString());
			_logger.debug("Group signature over bigSigma verification: " + verification);
			Signature bbss2 = new Signature(  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getT1() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getT2() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getT3() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getC() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getSalpha() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getSbeta() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getSx() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getSdelta1() ),
											  StringUtils.hexStringToByteArray( xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSignature().getSdelta2() ),
											  bbsParameters.getPairing());
			boolean verifySameSigner = engine.verifySameSigner(bbss, bbss2);
			_logger.debug("Group signature verify same signer: " + verification);
			
			
			// comprova que el serialNumber no estigui usat
			boolean used = destinationProviderEMB.isSerialNumberUsed( xml1.getTicketINSignedBean().getTicketIN().getSerialNumber() );
			_logger.debug("tin.sn used? " + used);
			
			// extreu timestamp1
			Date timestamp1 = xml1.getTicketINSignedBean().getTicketIN().getTimestamp1();
			// comprova validity time not expired
			Date validityTime = xml1.getTicketINSignedBean().getTicketIN().getValidityTime();
			Date now = new Date();
			if (validityTime.before( now )) {
				_logger.debug("ticket expired. validity till: " + validityTime + " now: " + now);
			}
			else {
				_logger.debug("ticket not expired. validity till: " + validityTime);
			}
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
			betaBean.setK("-");
			betaBean.setFare(fare);
			betaBean.setC1(c1ew.toHexString());
			betaBean.setTimestamp1(tau2);
			betaBean.setDestProvider(dstStationId);
			
			

			// sign beta
			String certFileName = StaticParameters.CERT_PATH_NAME + stationId + "_pk.pem";
			KeyPair keyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, 
																	"pere".toCharArray());
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
//			BufferedReader fbr = new BufferedReader( new FileReader( f1 ) );
//			BufferedWriter fbw = (BufferedWriter) client.getWriter();
//			String str;
//			while ( (str = fbr.readLine()) != null ) {
//				fbw.write(str);
//			}
//			fbw.write("\0u00");
//			fbw.flush();
			
			serializer.write(betaSignedBean, (BufferedWriter)client.getWriter());
			client.write("\0");
			client.flush();
			
//			client.flush();
//			PrintWriter pw = (PrintWriter) client.getWriter();
//			pw.println("\r\n\r\n");
//			pw.flush();
			
			
			
			_logger.debug("betaSigned sent...");
			///////////_s.shutdownOutput();
			
			
			
			// composes gammaPd
			GammaDestProviderBean gammaPd = new GammaDestProviderBean();
			gammaPd.setFare(fare);
			gammaPd.setSerialNumber(serialNumber);
			gammaPd.setSigma(xml1.getTicketINSignedBean().getTicketIN().getSigmaSigned().getSigma());
			gammaPd.setC1( c1ew.toHexString() );
			
			
			// rebre gammaU
			//TODO: aixo no sera GammaUser sino SystemExitGammaUserXML
			_logger.debug("GammaUser encrypted not received yet...");
			SystemExitGammaUserXML systemExitGammaUserXML = serializer.read(SystemExitGammaUserXML.class, (BufferedReader)client.getReader());
			_logger.debug("GammaUser encrypted received...");
			_s.shutdownInput();
			
			// sent gammaUserBean and gammaPd to payTTP (no envia, directament crida a les funcions de persistencia i lògica de payTTP)
			// obtains ok* or ko* from payTTP
			PayTTPSignedResponse response = payTTPEMB.chargeUser(systemExitGammaUserXML, gammaPd, g1, bbsParameters.getPairing());
			
			if (response.isOk()) {
				_logger.debug("user charged OK: " + response.getResponse().getMessage());
			} else {
				_logger.debug("user not charged: " + response.getResponse().getMessage());
			}
			
			
			// composes tout
			TicketOUTBean toutBean = new TicketOUTBean();
			toutBean.setSerialNumber(serialNumber);
			toutBean.setDstProvider(dstStationId);
			toutBean.setFare(fare);
			toutBean.setStr("leave taking at " + tau2);
			
			// sign toutBean
			certFileName = StaticParameters.CERT_PATH_NAME + stationId + "_pk.pem";
			keyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, "pere".toCharArray());
			sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			
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
			_logger.debug("The system has sent the ticketOUT");
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
			// Annotate transaction
			destinationProviderEMB.newTransaction(new StationTransactionImpl(), new StationImpl(), dstStationId);
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
//	
//	protected void issueTicketIN(SigmaSigned sigmaSigned) {
//		
//
//		
//	}
	
	
	
	
	
}
