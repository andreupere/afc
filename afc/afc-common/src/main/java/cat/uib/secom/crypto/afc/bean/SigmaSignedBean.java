package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSSignature;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSSignatureBean;


@Root(name="sigma-signed")
public class SigmaSignedBean implements SigmaSigned {
	
	@Element(name="sigma")
	protected SigmaBean sigma;
	
	@Element
	protected BBSSignatureBean signature;
	
	@Element(name="group-public-key")
	protected BBSGroupPublicKeyBean groupPublicKey;
	
	

	public BBSGroupPublicKey getEmbeddedGroupPublicKey() {
		return groupPublicKey;
	}

	public Sigma getSigma() {
		return this.sigma;
	}

	public BBSSignature getSignature() {
		return this.signature;
	}

	public void setEmbeddedGroupPublicKey(BBSGroupPublicKey embeddedGroupPublicKey) {
		this.groupPublicKey = (BBSGroupPublicKeyBean) embeddedGroupPublicKey;
	}

	public void setSigma(Sigma sigma) {
		this.sigma = (SigmaBean) sigma;
	}

	public void setSignature(BBSSignature signature) {
		this.signature = (BBSSignatureBean) signature;
	}

	
	public void storeSignature(Signature signature) {
		BBSSignatureBean bbssb = new BBSSignatureBean();
		bbssb.setT1( signature.getT1().toHexString() );
		bbssb.setT2( signature.getT2().toHexString() );
		bbssb.setT3( signature.getT3().toHexString() );
		bbssb.setC( signature.getC().toHexString() );
		bbssb.setSx( signature.getSx().toHexString() );
		bbssb.setSalpha( signature.getSalpha().toHexString() );
		bbssb.setSbeta( signature.getSbeta().toHexString() );
		bbssb.setSdelta1( signature.getSdelta1().toHexString() );
		bbssb.setSdelta2( signature.getSdelta2().toHexString() );
		this.signature = bbssb;
		
	}

}
