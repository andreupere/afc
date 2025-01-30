package cat.uib.secom.crypto.sig.acjt;

import java.math.BigInteger;

public class ACJTSignature {

	private BigInteger c, s1, s2, s3, s4, t1, t2, t3;
	
	public ACJTSignature(BigInteger c, BigInteger s1, BigInteger s2, BigInteger s3, BigInteger s4,
						 BigInteger t1, BigInteger t2, BigInteger t3) {
		this.c = c;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}

	public BigInteger getC() {
		return c;
	}

	public void setC(BigInteger c) {
		this.c = c;
	}

	public BigInteger getS1() {
		return s1;
	}

	public void setS1(BigInteger s1) {
		this.s1 = s1;
	}

	public BigInteger getS2() {
		return s2;
	}

	public void setS2(BigInteger s2) {
		this.s2 = s2;
	}

	public BigInteger getS3() {
		return s3;
	}

	public void setS3(BigInteger s3) {
		this.s3 = s3;
	}

	public BigInteger getS4() {
		return s4;
	}

	public void setS4(BigInteger s4) {
		this.s4 = s4;
	}

	public BigInteger getT1() {
		return t1;
	}

	public void setT1(BigInteger t1) {
		this.t1 = t1;
	}

	public BigInteger getT2() {
		return t2;
	}

	public void setT2(BigInteger t2) {
		this.t2 = t2;
	}

	public BigInteger getT3() {
		return t3;
	}

	public void setT3(BigInteger t3) {
		this.t3 = t3;
	}
	
	public Integer length() {
		Integer length = c.bitLength() + 
						 s1.bitLength() + 
						 s2.bitLength() + 
						 s3.bitLength() + 
						 s4.bitLength() + 
						 t1.bitLength() + 
						 t2.bitLength() + 
						 t3.bitLength();
		return length;
	}
	
}
