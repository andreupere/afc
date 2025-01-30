package cat.uib.secom.crypto.afc.store;

import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import junit.framework.TestCase;

public class SimpleTests extends TestCase {

	protected GroupTTPEntityManagerBean groupTTP;
	
	protected String curveFileName = "d840347-175-161.param";
	
	public SimpleTests() {
		groupTTP = new GroupTTPEntityManagerBean();
	}
	
	
	public ElementWrapper testRetrieveG1() {
		byte[] g1 = groupTTP.getManagerConfig("public_g1");
		BBSParameters bbsParameters = new BBSParameters(curveFileName);
		ElementWrapper g1ew = null;
		try {
			g1ew = PairingHelper.toElementWrapper(g1, bbsParameters.getPairing(), "G1");
			System.out.println("testRetrieveG1: " + g1ew.getElement() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return g1ew;
	}
	
	
	public EmbeddableBBSGroupPublicKey testRetrieveGroupPublicKey() {
		EmbeddableBBSGroupPublicKey gpk = groupTTP.retrieveGroupPublicKey(new Long(1)).getGroupPublicKey();
		System.out.println("gpk: " + gpk);
		return gpk;
	}
	
	public void testCompareG1() {
		ElementWrapper g1 = testRetrieveG1();
		EmbeddableBBSGroupPublicKey egpk = testRetrieveGroupPublicKey();
		
		BBSParameters bbsParameters = new BBSParameters(curveFileName);
		BBSGroupPublicKey gpk = egpk.restoreBBSGroupPublicKey(bbsParameters);
		
		System.out.println("v " + gpk.getV().getElement());
		
		System.out.println("g1 (g1): " + g1.getElement());
		System.out.println("g1 (gpk): " + gpk.getG1().getElement());
		
		
		assertTrue(  gpk.getG1().getElement().equals( g1.getElement() )  );
		

	}
	
}
