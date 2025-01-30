package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Beta;
import cat.uib.secom.crypto.afc.store.pojo.ticket.BetaSigned;



/**
 * Definition of BETA* element: BETA* = ( BETA, Sign<a>(BETA) )
 * */
@Embeddable
public class BetaSignedImpl implements BetaSigned {

	@Embedded
	private BetaImpl beta;
	
	@Column(name="BETA_SIGNATURE")
	private String signature;

	
	public void setBeta(Beta beta) {
		this.beta = (BetaImpl) beta;
	}

	public Beta getBeta() {
		return beta;
	}
	

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	
	
}
