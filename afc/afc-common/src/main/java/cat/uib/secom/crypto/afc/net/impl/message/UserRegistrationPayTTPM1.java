package cat.uib.secom.crypto.afc.net.impl.message;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.strings.StringUtils;

public class UserRegistrationPayTTPM1 implements NetworkMessage {

	
	private byte[] _yu;
	private byte[] _s0;
	private byte[] _signature;
	
	
	public NetworkMessage desearialize(String in) {
		String[] p = in.split(" ");
		UserRegistrationPayTTPM1 urp1 = new UserRegistrationPayTTPM1();
		urp1._yu = StringUtils.hexStringToByteArray( p[0] );
		urp1._s0 = StringUtils.hexStringToByteArray( p[1] );
		urp1._signature = StringUtils.hexStringToByteArray( p[2] );
		return urp1;
	}

	public String serialize() {
		String out = StringUtils.readHexString( _yu ) + " " +
					 StringUtils.readHexString( _s0 ) + " " +
					 StringUtils.readHexString( _signature );
		return out;
	}

	
	
	public byte[] getYu() {
		return _yu;
	}

	public byte[] getS0() {
		return _s0;
	}

	public byte[] getSignature() {
		return _signature;
	}

	public void setYu(byte[] yu) {
		_yu = yu;
	}

	public void setS0(byte[] s0) {
		_s0 = s0;
	}

	public void setSignature(byte[] signature) {
		_signature = signature;
	}

}
