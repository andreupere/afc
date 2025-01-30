package cat.uib.secom.crypto.afc.server.payttp;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML2;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationPayTTPXML3;
import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.jpa.business.payttp.PaymentTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPAccountImpl;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;



public class PaymentTTPServerThread extends Thread {

	
	private static Logger _logger = LoggerFactory.getLogger(PaymentTTPServerThread.class);

	private GeneralConfigXML gConfig;
	
	private Socket _s;
	private File folderOutputXML;
	
	private String yu;
	private String s0;
	private String signature;
	private BBSParameters bbsParameters;
	private ElementWrapper c0 ;
	private PaymentTTPEntityManagerBean payTTPEMB;
	private GroupTTPEntityManagerBean groupTTPEMB;
	
	private byte[] g1;
	
	public PaymentTTPServerThread(Socket s, GeneralConfigXML gConfig) {
		super("PaymentTTP");
		_s = s;
		this.gConfig = gConfig;
		folderOutputXML = new File( gConfig.getOutputXmlsPath() + "pttp/" );
		bbsParameters = new BBSParameters("d840347-175-161.param");
		payTTPEMB = new PaymentTTPEntityManagerBean();
		groupTTPEMB = new GroupTTPEntityManagerBean();
		
		this.g1 = groupTTPEMB.getManagerConfig("public_g1");
	}
	
	public void run() {
		System.out.println("run client...");
		Client c = new Client(_s);

		try {
			Reader reader = c.prepareInput();
			Writer writer = c.prepareOutput();
			Serializer serializer = new Persister();
			UserRegistrationPayTTPXML1 xml1 = serializer.read(UserRegistrationPayTTPXML1.class, reader);
			xml1.getS0();
			xml1.getSignature();
			_logger.debug("xml1.yu: " + xml1.getYu() );
			this.yu = xml1.getYu();
			this.s0 = xml1.getS0();
			this.signature = xml1.getSignature();
			
			ElementWrapper c0 = PairingHelper.random( bbsParameters.getPairing().getZr() );
			this.c0 = c0;
			UserRegistrationPayTTPXML2 xml2 = new UserRegistrationPayTTPXML2 ();
			xml2.setC0(c0.toHexString());
			

			serializer.write(xml2, writer);
			writer.flush();
			_s.shutdownOutput();
			

			

			UserRegistrationPayTTPXML3 xml3 = serializer.read(UserRegistrationPayTTPXML3.class, reader);
			xml3.getOmega0();
			doRegistration(StringUtils.hexStringToByteArray( xml3.getOmega0() ) );
			
			// AQUI ES PODRIA ENVIAR UN 'OK' AL CLIENT PER INDICAR-LI QUE ESTÀ REGISTRAT, QUIN SALDO TE...
		
			
			c.closeInput();
			c.closeOutput();
			c.closeSocket();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	protected void doRegistration(byte[] omega0) {
		

		
		ElementWrapper yuew;
		ElementWrapper omega0ew;
		try {
			yuew = PairingHelper.toElementWrapper(yu, bbsParameters.getPairing(), "G1");
			omega0ew = PairingHelper.toElementWrapper(omega0, bbsParameters.getPairing(), "Zr");
			ElementWrapper g1ew = PairingHelper.toElementWrapper(g1, bbsParameters.getPairing(), "G1");
			ElementWrapper s0ew = PairingHelper.toElementWrapper(this.s0, bbsParameters.getPairing(), "G1");
			boolean result = PairingHelper.schnorrZKPverify(g1ew, omega0ew, s0ew, yuew, c0 );
			_logger.debug("verification result ZKP: " + result);
			
			PaymentTTPAccountImpl pa = new PaymentTTPAccountImpl();
			pa.setBalance(gConfig.getInitialBalance());
			pa.setHashedYu( yuew.toHexString() );
			pa.setYu( yuew.toHexString() );
			pa = (PaymentTTPAccountImpl) payTTPEMB.registerUser(pa);
			_logger.debug("PayTTP: user registered " + pa);
			
			
			//PaymentTTPTransactionImpl pt = (PaymentTTPTransactionImpl) payTTPEMB.chargeUser(pa, new Double(10.22));
			//System.out.println( "PayTTP: user charged " + pt );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
