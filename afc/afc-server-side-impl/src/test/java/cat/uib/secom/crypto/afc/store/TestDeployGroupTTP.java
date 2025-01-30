package cat.uib.secom.crypto.afc.store;


import cat.uib.secom.crypto.afc.store.jpa.business.groupttp.GroupTTPEntityManagerBean;
import cat.uib.secom.crypto.afc.store.logic.GroupTTPLogic;
import cat.uib.secom.crypto.sig.bbs.core.accessors.enhanced.GroupManagerAccessor;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.jpa.embeddable.impl.EmbeddableBBSGroupPublicKey;
import cat.uib.secom.utils.strings.StringUtils;
import junit.framework.TestCase;


/**
 * 
 * Group TTP deploys keys for users
 * 
 * */
public class TestDeployGroupTTP extends TestCase {

	protected GroupTTPEntityManagerBean groupTTP;
	
	// GroupTTP deploy information
	protected String curveFileName = "d840347-175-161.param";
	protected Integer numberUsers = new Integer(100);
	
	
	public TestDeployGroupTTP() {
		groupTTP = new GroupTTPEntityManagerBean();
	}
	
	
	/**
	 * Starts Group TTP. Creates g1 parameter
	 * 
	 * */
	public void testInitGroupManager() {
		
		groupTTP.initTTP(curveFileName);
		
	}
	
	
	/**
	 * 
	 * Initialize group keys and user private keys. 
	 * After the execution of this method, Group TTP is ready to issue keys and register users
	 * 
	 * */
	public void testInitGroup() {
		
		byte[] g1 = groupTTP.getManagerConfig("public_g1");
		System.out.println("After retrieve g1 " + StringUtils.readHexString(g1));
		
		GroupManagerAccessor gma = GroupTTPLogic.initGroup(numberUsers, curveFileName, g1);
		
		groupTTP.storeGroupData(gma);
		System.out.println( groupTTP.retrieveGroupPublicKey(new Long(1)) );
		EmbeddableBBSGroupPublicKey egpk = groupTTP.retrieveGroupPublicKey(new Long(1)).getGroupPublicKey();
		
		BBSParameters bbsParameters = new BBSParameters(curveFileName);
		BBSGroupPublicKey bbsgpk = egpk.restoreBBSGroupPublicKey(bbsParameters);
		
		String g1FromGroupPublicKey = bbsgpk.getG1().toHexString();
		
		System.out.println(bbsgpk.getG2().getElement());
		
		assertEquals(StringUtils.readHexString(g1), g1FromGroupPublicKey );
		
		
	}
	
}
