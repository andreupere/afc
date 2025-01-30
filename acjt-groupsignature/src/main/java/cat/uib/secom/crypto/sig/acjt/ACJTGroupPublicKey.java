package cat.uib.secom.crypto.sig.acjt;

import java.math.BigInteger;

public class ACJTGroupPublicKey {

	private BigInteger n, a, a0, y, g, h;
	
	public ACJTGroupPublicKey(BigInteger n, BigInteger a, BigInteger a0, BigInteger y, BigInteger g, BigInteger h) {
		this.n = n;
		this.a = a;
		this.a0 = a0;
		this.y = y;
		this.g = g;
		this.h = h;
	}

	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getA() {
		return a;
	}

	public void setA(BigInteger a) {
		this.a = a;
	}

	public BigInteger getA0() {
		return a0;
	}

	public void setA0(BigInteger a0) {
		this.a0 = a0;
	}

	public BigInteger getY() {
		return y;
	}

	public void setY(BigInteger y) {
		this.y = y;
	}

	public BigInteger getG() {
		return g;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public BigInteger getH() {
		return h;
	}

	public void setH(BigInteger h) {
		this.h = h;
	}
	
	public Integer length() {
		Integer length = n.bitLength() +
						 a.bitLength() +
						 a0.bitLength() + 
						 y.bitLength() +
						 g.bitLength() +
						 h.bitLength();
		return length;
	}
	
	
}
