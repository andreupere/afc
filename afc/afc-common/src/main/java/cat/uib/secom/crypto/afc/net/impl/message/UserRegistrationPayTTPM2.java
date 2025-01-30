package cat.uib.secom.crypto.afc.net.impl.message;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.strings.StringUtils;

public class UserRegistrationPayTTPM2 implements NetworkMessage {

	

	private byte[] _c0;

	
	
	public NetworkMessage desearialize(String in) {
		UserRegistrationPayTTPM2 urp2 = new UserRegistrationPayTTPM2();
		urp2._c0 = StringUtils.hexStringToByteArray( in );
		return urp2;
	}

	public String serialize() {
		String out = StringUtils.readHexString( _c0 );
		return out;
	}

	


	public byte[] getC0() {
		return _c0;
	}


	public void setC0(byte[] c0) {
		_c0 = c0;
	}



}
