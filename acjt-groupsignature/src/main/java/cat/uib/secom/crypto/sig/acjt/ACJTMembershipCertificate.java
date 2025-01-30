package cat.uib.secom.crypto.sig.acjt;

import java.math.BigInteger;

public class ACJTMembershipCertificate {

	private BigInteger Ai, ei;

	public ACJTMembershipCertificate(BigInteger Ai, BigInteger ei) {
		this.Ai = Ai;
		this.ei = ei;
	}
	
	
	
	public BigInteger getAi() {
		return Ai;
	}

	public void setAi(BigInteger ai) {
		Ai = ai;
	}

	public BigInteger getEi() {
		return ei;
	}

	public void setEi(BigInteger ei) {
		this.ei = ei;
	}
	
	public Integer length() {
		Integer length = Ai.bitLength() +
						 ei.bitLength();
		return length;
	}
}
