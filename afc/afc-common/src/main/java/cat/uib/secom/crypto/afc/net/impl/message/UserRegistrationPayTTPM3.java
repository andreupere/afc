package cat.uib.secom.crypto.afc.net.impl.message;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.strings.StringUtils;

public class UserRegistrationPayTTPM3 implements NetworkMessage {

	

	private byte[] _omega0;

	
	
	public NetworkMessage desearialize(String in) {
		UserRegistrationPayTTPM3 urp3 = new UserRegistrationPayTTPM3();
		urp3._omega0 = StringUtils.hexStringToByteArray( in );
		return urp3;
	}

	public String serialize() {
		String out = StringUtils.readHexString( _omega0 );
		return out;
	}

	


	public byte[] getOmega0() {
		return _omega0;
	}


	public void setOmega0(byte[] omega0) {
		_omega0 = omega0;
	}



}
