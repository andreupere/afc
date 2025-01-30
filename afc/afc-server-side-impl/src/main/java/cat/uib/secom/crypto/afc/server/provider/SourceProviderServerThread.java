package cat.uib.secom.crypto.afc.server.provider;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.KeyPair;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import cat.uib.secom.crypto.afc.bean.SigmaSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketINBean;
import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.jpa.business.provider.SourceProviderEntityManagerBean;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableSignature;
import cat.uib.secom.utils.crypto.Hash;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

public class SourceProviderServerThread extends Thread {

	private static Logger _logger = LoggerFactory.getLogger(SourceProviderServerThread.class);

	private GeneralConfigXML gConfig;

	
	private Socket _s;
	
	protected BBSParameters bbsParameters;
	
	protected SourceProviderEntityManagerBean sourceProviderEMB;
	
	protected Long stationId;
	private File folderOutputXML;
	
	public SourceProviderServerThread(Socket s, Long stationId, GeneralConfigXML gConfig) {
		super("SourceProvider");
		this.stationId = stationId;
		this.gConfig = gConfig;
		_s = s;
		bbsParameters = new BBSParameters(gConfig.getElipticCurveFileName());
		sourceProviderEMB = new SourceProviderEntityManagerBean(this.stationId);
		folderOutputXML = new File( gConfig.getOutputXmlsPath() + "sprovider/" );
	}
	
	public void run() {
		System.out.println("run client...");
		Serializer serializer = new Persister();
		
		Client client = new Client(_s);
		// testing

		File entrance2 = new File(folderOutputXML, "entrance2.xml");
		try {
			client.createSocket();
			Reader reader = client.prepareInput();
			Writer writer = client.prepareOutput();
			SigmaSignedBean ssb = serializer.read(SigmaSignedBean.class, reader);
			
			SigmaSigned ss = new SigmaSignedImpl();
			EmbeddableBBSGroupPublicKey egpk = new EmbeddableBBSGroupPublicKey();
			egpk.setG1( ssb.getEmbeddedGroupPublicKey().getG1() );
			egpk.setG2( ssb.getEmbeddedGroupPublicKey().getG2() );
			egpk.setH( ssb.getEmbeddedGroupPublicKey().getH() );
			egpk.setU( ssb.getEmbeddedGroupPublicKey().getU() );
			egpk.setV( ssb.getEmbeddedGroupPublicKey().getV() );
			egpk.setOmega( ssb.getEmbeddedGroupPublicKey().getOmega() );

			
			
			Sigma s = new SigmaImpl();
			s.setDeltau(ssb.getSigma().getDeltau());
			s.setHk(ssb.getSigma().getHk());
			s.setS1(ssb.getSigma().getS1());
			
			
			EmbeddableSignature signature = new EmbeddableSignature();
			signature.setT1( ssb.getSignature().getT1() );
			signature.setT2( ssb.getSignature().getT2() );
			signature.setT3( ssb.getSignature().getT3() );
			signature.setC( ssb.getSignature().getC() );
			signature.setSx( ssb.getSignature().getSx() );
			signature.setSalpha( ssb.getSignature().getSalpha() );
			signature.setSbeta( ssb.getSignature().getSbeta() );
			signature.setSdelta1( ssb.getSignature().getSdelta1() );
			signature.setSdelta2( ssb.getSignature().getSdelta2() );
			
			ss.setSigma(s);
			ss.setSignature( signature );
			ss.setEmbeddedGroupPublicKey(egpk);
			
			boolean result = sourceProviderEMB.verifySigmaSigned(ss, bbsParameters.getPairing());
			_logger.debug("group signature verification: " + result);
			
			TicketIN ticketIN = sourceProviderEMB.composeTicketIN(ss, gConfig.getValidityTime());
			
			
			
			TicketINBean tinb = new TicketINBean();
			tinb.setSerialNumber(ticketIN.getSerialNumber());
			tinb.setSigmaSigned(ssb);
			tinb.setSrcProvider(ticketIN.getSrcProvider());
			tinb.setTau1(ticketIN.getTimestamp1().getTime());
			tinb.setValidity(ticketIN.getValidityTime().getTime());
			
			TicketINSignedBean tinsb = new TicketINSignedBean();
			
			tinsb.setTicketIN(tinb);
			// do signature process
			// locate corresponding certificate pem file
			String certFileName = gConfig.getCertsPath() + "sprovider.pem";
			KeyPair keyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, "pere".toCharArray());
			SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
			
			// serialitzar xml amb StringWriter
			StringWriter sw = new StringWriter();
			serializer.write(tinb, sw);
			// ara tenc el contingut xml dins sw
			StringBuffer buffer = sw.getBuffer();
			_logger.debug("signature rsa input: " + buffer.toString());
			Hash hash = new Hash(buffer.toString().getBytes());
	    	hash.generate();
	    	_logger.debug("hash: " + hash.readHexString());
			// pas byte[] a signar
			byte[] signatureOutput = sp.sign(keyPair, 
											 hash.generate() );
			
			// representacio hexadecimal
			String signatureOutputHex = StringUtils.readHexString(signatureOutput);
			_logger.debug("signature rsa output: " + signatureOutputHex);
			// afegir a ticket
			tinsb.setSignature(signatureOutputHex);
			
			serializer.write(tinsb, writer);
			serializer.write(tinsb, entrance2);
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	


	
	
	
	
	
}
