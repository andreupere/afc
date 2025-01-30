package cat.uib.secom.crypto.sig.acjt;

import java.math.BigInteger;

public class ACJTSecretKey {

	private BigInteger pPrima, qPrima, x;
	
	public ACJTSecretKey(BigInteger pPrima, BigInteger qPrima, BigInteger x) {
		this.pPrima = pPrima;
		this.qPrima = qPrima;
		this.x = x;
	}

	public BigInteger getpPrima() {
		return pPrima;
	}

	public void setpPrima(BigInteger pPrima) {
		this.pPrima = pPrima;
	}

	public BigInteger getqPrima() {
		return qPrima;
	}

	public void setqPrima(BigInteger qPrima) {
		this.qPrima = qPrima;
	}

	public BigInteger getX() {
		return x;
	}

	public void setX(BigInteger x) {
		this.x = x;
	}
	
	
	public Integer length() {
		Integer length = qPrima.bitLength() +
						 pPrima.bitLength() +
						 x.bitLength();
		return length;
	}
	
	
	
}
