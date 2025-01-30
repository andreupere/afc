package cat.uib.secom.crypto.afc.server.groupttp;



import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;



import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cat.uib.secom.crypto.afc.net.message.NetworkEndPoints;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML1;
import cat.uib.secom.crypto.afc.net.message.xml.UserRegistrationGroupTTPXML2;
import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.pojo.impl.groupttp.KeyPair;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.NoMoreAvailableKeysException;
import cat.uib.secom.crypto.sig.bbs.store.exceptions.UserAlreadyServedException;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSUserPrivateKey;
import cat.uib.secom.utils.crypto.pkc.ExtractKeyPairFromPEMFile;
import cat.uib.secom.utils.crypto.pkc.SignatureProcess;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

public class GroupTTPServerThread extends Thread {

	private static Logger _logger = LoggerFactory.getLogger(GroupTTPServerThread.class);

	private GeneralConfigXML gConfig;
	
	private File folderOutputXML;
	
	private Socket _s;
	
	public GroupTTPServerThread(Socket s, GeneralConfigXML gConfig) {
		super("GroupTTP");
		_s = s;
		this.gConfig = gConfig;
		folderOutputXML = new File( gConfig.getOutputXmlsPath() + "sprovider/" );
	}
	
	public void run() {
		_logger.debug("run client...");
		Client c = new Client(_s);

		try {
			Reader reader = c.prepareInput();
			Writer writer = c.prepareOutput();
			
			String request = c.read();
			_logger.debug("request from client: " + request);
			
			String response = "";
			if (request.equals( NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_G1 )) {
				_logger.debug("request G1");
				response = GroupTTPServerThread.sendG1();
				_logger.debug("response: " + response);
				c.write(response);
			}
			else if (request.equals( NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR )) {
				_logger.debug("request keypair");
				Serializer serializer = new Persister();
				UserRegistrationGroupTTPXML1 xml1 = serializer.read(UserRegistrationGroupTTPXML1.class, reader);
				_logger.debug("message for keypair request received");
				
				// verify signature
				String certFileName = gConfig.getCertsPath() + "gttp.pem";
				java.security.KeyPair rsaKeyPair = ExtractKeyPairFromPEMFile.readKeyPair(certFileName, 
																						 "pere".toCharArray());
				SignatureProcess sp = SignatureProcess.getInstance("SHA256WithRSAEncryption");
				
				boolean verification = sp.verify(rsaKeyPair, 
												 StringUtils.hexStringToByteArray( xml1.getYu() ), 
												 StringUtils.hexStringToByteArray( xml1.getSignature() ) );
				
				_logger.debug("signature verification: " + verification);
				
				_logger.debug("received xml1: " + xml1.getUserIdentity());
				
				UserRegistrationGroupTTPXML2 xml2 = GroupTTPServerThread.sendKeyPair(xml1);
				serializer.write(xml2, writer);
				

			}
			else {
				
			}
			
			c.closeInput();
			c.closeOutput();
			c.closeSocket();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected static String sendG1() {
		GroupTTPEntityManagerBean groupTTP = new GroupTTPEntityManagerBean();	
		
		byte[] g1 = groupTTP.getManagerConfig("public_g1");
		
		return StringUtils.readHexString(g1);
		
	}
	
	
	
	
	protected static UserRegistrationGroupTTPXML2 sendKeyPair(UserRegistrationGroupTTPXML1 xml1 ) throws UserAlreadyServedException, NoMoreAvailableKeysException {
		GroupTTPEntityManagerBean groupTTP = new GroupTTPEntityManagerBean();
		KeyPair kp = groupTTP.issueKeyPair(xml1.getUserIdentity(), xml1.getYu() );
		
		EmbeddableBBSGroupPublicKey gpk = kp.getGroupPublicKey().getGroupPublicKey();
		EmbeddableBBSUserPrivateKey usk = kp.getUserPrivateKey().getUserPrivateKey();
		
		UserRegistrationGroupTTPXML2 xml2 = new UserRegistrationGroupTTPXML2();
		
		xml2.setGroupPublicKey(gpk.toBean());
		xml2.setUserPrivateKey(usk.toBean());
		
		return xml2;
	}
		
		
		
		
		
	}