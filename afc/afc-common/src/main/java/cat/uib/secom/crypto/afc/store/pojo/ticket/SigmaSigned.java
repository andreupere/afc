package cat.uib.secom.crypto.afc.store.pojo.ticket;

import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.store.entities.BBSSignature;
import it.unisa.dia.gas.jpbc.Pairing;

public interface SigmaSigned {

	public void setSigma(Sigma sigma);

	public Sigma getSigma();

	
	
	public void setSignature(BBSSignature signature);

	public BBSSignature getSignature();
	
	
	public void storeSignature(Signature signature);
	
	
	public void setEmbeddedGroupPublicKey(BBSGroupPublicKey embeddedGroupPublicKey);

	public BBSGroupPublicKey getEmbeddedGroupPublicKey();



}