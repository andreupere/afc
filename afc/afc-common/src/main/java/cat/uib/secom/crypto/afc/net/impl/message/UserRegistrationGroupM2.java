package cat.uib.secom.crypto.afc.net.impl.message;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.strings.StringUtils;

public class UserRegistrationGroupM2 implements NetworkMessage {

	private byte[] _g1;
	private byte[] _g2;
	private byte[] _h;
	private byte[] _u;
	private byte[] _v;
	private byte[] _omega;
	
	private byte[] _x;
	private byte[] _a;
	
	
	
	
	public NetworkMessage desearialize(String in) {
		String[] p = in.split(" ");
		UserRegistrationGroupM2 urm2 = new UserRegistrationGroupM2();
		urm2._g1 = StringUtils.hexStringToByteArray( p[0] );
		urm2._g2 = StringUtils.hexStringToByteArray( p[1] );
		urm2._h = StringUtils.hexStringToByteArray( p[2] );
		urm2._u = StringUtils.hexStringToByteArray( p[3] );
		urm2._v = StringUtils.hexStringToByteArray( p[4] );
		urm2._omega = StringUtils.hexStringToByteArray( p[5] );
		
		urm2._x = StringUtils.hexStringToByteArray( p[6] );
		urm2._a = StringUtils.hexStringToByteArray( p[7] );
		return urm2;
	}

	
	
	public String serialize() {
		String out = StringUtils.readHexString( _g1 ) + " " +
					 StringUtils.readHexString( _g2 ) + " " +
					 StringUtils.readHexString( _h ) + " " +
					 StringUtils.readHexString( _u ) + " " +
					 StringUtils.readHexString( _v ) + " " +
					 StringUtils.readHexString( _omega ) + " " +
					 StringUtils.readHexString( _x ) + " " +
					 StringUtils.readHexString( _a );
		return out;
	}



	public byte[] getG1() {
		return _g1;
	}



	public byte[] getG2() {
		return _g2;
	}



	public byte[] getH() {
		return _h;
	}



	public byte[] getU() {
		return _u;
	}



	public byte[] getV() {
		return _v;
	}



	public byte[] getOmega() {
		return _omega;
	}



	public void setG1(byte[] g1) {
		_g1 = g1;
	}



	public void setG2(byte[] g2) {
		_g2 = g2;
	}



	public void setH(byte[] h) {
		_h = h;
	}



	public void setU(byte[] u) {
		_u = u;
	}



	public void setV(byte[] v) {
		_v = v;
	}



	public void setOmega(byte[] omega) {
		_omega = omega;
	}
	
	
	public byte[] getX() {
		return _x;
	}
	public byte[] getA() {
		return _a;
	}
	
	public void setX(byte[] x) {
		_x = x;
	}
	public void setA(byte[] a) {
		_a = a;
	}

	
}
