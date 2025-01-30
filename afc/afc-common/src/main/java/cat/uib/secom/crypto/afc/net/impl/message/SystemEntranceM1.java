package cat.uib.secom.crypto.afc.net.impl.message;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;
import cat.uib.secom.utils.strings.StringUtils;

public class SystemEntranceM1 implements NetworkMessage {
	
	// sigma
	private byte[] deltau;
	private byte[] hk;
	private byte[] s1;
	
	
	// bbs gpk
	private byte[] g1;
	private byte[] g2;
	private byte[] h;
	private byte[] u;
	private byte[] v;
	private byte[] omega;
	
	
	// bbs signature
	private byte[] t1;
	private byte[] t2;
	private byte[] t3;
	private byte[] c;
	private byte[] sx;
	private byte[] salpha;
	private byte[] sbeta;
	private byte[] sdelta1;
	private byte[] sdelta2;
	
	
	
	public SystemEntranceM1() {}
	
	
	//////////// from NetworkMessage
	public NetworkMessage desearialize(String in) {
		String[] p = in.split(";");
		SystemEntranceM1 se1 = new SystemEntranceM1();
		
		se1.deltau = StringUtils.hexStringToByteArray( p[0] );
		se1.hk = StringUtils.hexStringToByteArray( p[1] );
		se1.s1 = StringUtils.hexStringToByteArray( p[2] );
		
		se1.g1 = StringUtils.hexStringToByteArray( p[3] );
		se1.g2 = StringUtils.hexStringToByteArray( p[4] );
		se1.h = StringUtils.hexStringToByteArray( p[5] );
		se1.u = StringUtils.hexStringToByteArray( p[6] );
		se1.v = StringUtils.hexStringToByteArray( p[7] );
		se1.omega = StringUtils.hexStringToByteArray( p[8] );
		
		se1.t1 = StringUtils.hexStringToByteArray( p[9] );
		se1.t2 = StringUtils.hexStringToByteArray( p[10] );
		se1.t3 = StringUtils.hexStringToByteArray( p[11] );
		se1.c = StringUtils.hexStringToByteArray( p[12] );
		se1.sx = StringUtils.hexStringToByteArray( p[13] );
		se1.salpha = StringUtils.hexStringToByteArray( p[14] );
		se1.sbeta = StringUtils.hexStringToByteArray( p[15] );
		se1.sdelta1 = StringUtils.hexStringToByteArray( p[16] );
		se1.sdelta2 = StringUtils.hexStringToByteArray( p[17] );
		
		return se1;
	}

	public String serialize() {
		String out = StringUtils.readHexString( deltau ) + " " + 
					 StringUtils.readHexString( hk ) + " " +
					 StringUtils.readHexString( s1 ) + ";" +
					 
					 StringUtils.readHexString( g1 ) + " " +
					 StringUtils.readHexString( g2 ) + " " +
					 StringUtils.readHexString( h ) + " " +
					 StringUtils.readHexString( u ) + " " +
					 StringUtils.readHexString( v ) + " " +
					 StringUtils.readHexString( omega) + ";" +
					 
					 StringUtils.readHexString( t1 ) + " " +
					 StringUtils.readHexString( t2 ) + " " +
					 StringUtils.readHexString( t3 ) + " " +
					 StringUtils.readHexString( c ) + " " +
					 StringUtils.readHexString( sx ) + " " +
					 StringUtils.readHexString( salpha ) + " " +
					 StringUtils.readHexString( sbeta ) + " " +
					 StringUtils.readHexString( sdelta1 ) + " " +
					 StringUtils.readHexString( sdelta2 );
		return out;
	}


	
	//////////// from Sigma
	public byte[] getDeltau() {
		return this.deltau;
	}


	public byte[] getHk() {
		return this.hk;
	}


	public byte[] getS1() {
		return this.s1;
	}


	public String serialized() {
		return "";
	}


	public void setDeltau(byte[] deltau) {
		this.deltau = deltau;
	}


	public void setHk(byte[] hk) {
		this.hk = hk;
	}


	public void setS1(byte[] s1) {
		this.s1 = s1;
	}


	
	/////////// from BBSGroupPublicKey (store)
	public byte[] getG1() {
		return g1;
	}


	public byte[] getG2() {
		return g2;
	}


	public byte[] getH() {
		return h;
	}


	public byte[] getOmega() {
		return omega;
	}


	public byte[] getU() {
		return u;
	}


	public byte[] getV() {
		return v;
	}


	public void setG1(byte[] arg0) {
		this.g1 = arg0;
	}


	public void setG2(byte[] arg0) {
		this.g2 = arg0;
	}


	public void setH(byte[] arg0) {
		this.h = arg0;
	}


	public void setOmega(byte[] arg0) {
		this.omega = arg0;
	}


	public void setU(byte[] arg0) {
		this.u = arg0;
	}


	public void setV(byte[] arg0) {
		this.v = arg0;
	}


	
	
	
	
	///////////// from BBSSignature
	public byte[] getC() {
		return this.c;
	}


	public byte[] getSalpha() {
		return this.salpha;
	}


	public byte[] getSbeta() {
		return this.sbeta;
	}


	public byte[] getSdelta1() {
		return this.sdelta1;
	}


	public byte[] getSdelta2() {
		return this.sdelta2;
	}


	public byte[] getSx() {
		return this.sx;
	}


	public byte[] getT1() {
		return this.t1;
	}


	public byte[] getT2() {
		return this.t2;
	}


	public byte[] getT3() {
		return this.t3;
	}


	
	
	public void setT1(byte[] arg0) {
		this.t1 = arg0;
	}


	public void setT2(byte[] arg0) {
		this.t2 = arg0;
	}


	public void setT3(byte[] arg0) {
		this.t3 = arg0;
	}
	
	
	public void setC(byte[] arg0) {
		this.c = arg0;
	}


	public void setSalpha(byte[] arg0) {
		this.salpha = arg0;
	}
	
	public void setSbeta(byte[] arg0) {
		this.sbeta = arg0;
	}
	
	public void setSx(byte[] arg0) {
		this.sx = arg0;
	}


	public void setSdelta1(byte[] arg0) {
		this.sdelta1 = arg0;
	}


	public void setSdelta2(byte[] arg0) {
		this.sdelta2 = arg0;
	}


	


	





	


}
