package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import it.unisa.dia.gas.jpbc.Pairing;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSSignature;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableSignature;
import cat.uib.secom.utils.strings.StringUtils;



/**
 * @author Andreu Pere
 * 
 * Definition of SIGMA* element: SIGMA* = ( SIGMA, Sign<a>(SIGMA) )
 * @see Signature
 * @see Sigma
 * */
@Embeddable
public class SigmaSignedImpl implements SigmaSigned {
	
	@Embedded
	private SigmaImpl sigma;
	
	/**
	 * BBS signature object
	 * */
	@Embedded
	private EmbeddableSignature signature;
	
	@Transient
	@Embedded
	private EmbeddableBBSGroupPublicKey embeddedGroupPublicKey;
	
	

	public void setSigma(Sigma sigma) {
		this.sigma = (SigmaImpl) sigma;
	}

	public Sigma getSigma() {
		return sigma;
	}

	public void setSignature(BBSSignature signature) {
		this.signature = (EmbeddableSignature) signature;
	}

	public BBSSignature getSignature() {
		return signature;
	}
	
	public void storeSignature(Signature signature) {
		EmbeddableSignature s = new EmbeddableSignature();
		s.setT1( signature.getT1().toHexString() );
		s.setT2( signature.getT2().toHexString() );
		s.setT3( signature.getT3().toHexString() );
		s.setC( signature.getC().toHexString() );
		s.setSx( signature.getSx().toHexString() );
		s.setSalpha( signature.getSalpha().toHexString() );
		s.setSbeta( signature.getSbeta().toHexString() );
		s.setSdelta1( signature.getSdelta1().toHexString() );
		s.setSdelta2( signature.getSdelta2().toHexString() );
		this.signature = s;
		
	}
	public Signature restoreSignature(Pairing pairing) {
		
		Signature s = new cat.uib.secom.crypto.sig.bbs.core.impl.signature.Signature(StringUtils.hexStringToByteArray( this.getSignature().getT1() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getT2() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getT3() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getC() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getSalpha() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getSbeta() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getSx() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getSdelta1() ),
																					 StringUtils.hexStringToByteArray( this.getSignature().getSdelta2() ),
																					 pairing
																					);
		
		return s;
	}

	public void setEmbeddedGroupPublicKey(BBSGroupPublicKey embeddedGroupPublicKey) {
		this.embeddedGroupPublicKey = (EmbeddableBBSGroupPublicKey) embeddedGroupPublicKey;
	}

	public BBSGroupPublicKey getEmbeddedGroupPublicKey() {
		return embeddedGroupPublicKey;
	}

	
	
	
	public SigmaSigned deserialize(String in) {
		String[] p = in.split(";");
		
		SigmaSignedImpl ssi = new SigmaSignedImpl();
		
		SigmaImpl s = new SigmaImpl();
		EmbeddableSignature signature = new EmbeddableSignature();
		EmbeddableBBSGroupPublicKey gpk = new EmbeddableBBSGroupPublicKey();
		
		ssi.sigma = (SigmaImpl) s.deserialize(p[0]);
		ssi.signature = (EmbeddableSignature) signature.deserialize(p[1]);
		ssi.embeddedGroupPublicKey = (EmbeddableBBSGroupPublicKey) gpk.deserialize(p[2]);
		
		return ssi;
	}

	public String serialize() {
//		String out = this.getSigma().serialize() + ";" +
//					 this.getSignature().serialize() + ";" +
//					 this.getEmbeddedGroupPublicKey().serialize();
		return null;
	}

}
