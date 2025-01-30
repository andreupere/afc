package cat.uib.secom.crypto.afc.net.impl.message;

import it.unisa.dia.gas.jpbc.Pairing;
import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.strings.StringUtils;


/**
 * 
 * User registration to group TTP. Message from User to Group TTP
 * 
 * @author Andreu Pere
 * 
 * */
public class UserRegistrationGroupM1 implements NetworkMessage {

	private String _userIdentity;
	private byte[] _certificate;
	private String _signature;
	private byte[] _yu;
	
	
	
	
	public NetworkMessage desearialize(String in) {
		String[] p = in.split(" ");
		UserRegistrationGroupM1 urm1 = new UserRegistrationGroupM1();
		urm1._userIdentity = p[0];
		urm1._yu = StringUtils.hexStringToByteArray( p[1] );
		urm1._signature = p[2];
		urm1._certificate = StringUtils.hexStringToByteArray( p[3] );
		
		return urm1;
	}

	public String serialize() {
		String out = _userIdentity + " " +
					 StringUtils.readHexString( _yu ) + " " +
					 _signature + " " +
					 StringUtils.readHexString( _certificate ) + " ";
		return out;
	}

	public String getUserIdentity() {
		return _userIdentity;
	}

	public byte[] getCertificate() {
		return _certificate;
	}

	public String getSignature() {
		return _signature;
	}

	public byte[] getYu() {
		return _yu;
	}

	public void setUserIdentity(String userIdentity) {
		_userIdentity = userIdentity;
	}

	public void setCertificate(byte[] certificate) {
		_certificate = certificate;
	}

	public void setSignature(String signature) {
		_signature = signature;
	}

	public void setYu(byte[] yu) {
		_yu = yu;
	}
	
	
	

	
	
	
}
