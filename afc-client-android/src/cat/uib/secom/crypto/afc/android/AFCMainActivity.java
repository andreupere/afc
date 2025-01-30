package cat.uib.secom.crypto.afc.android;

import java.io.IOException;

import it.unisa.dia.gas.jpbc.Element;
import cat.uib.secom.crypto.afc.R;
import cat.uib.secom.crypto.afc.android.pojo.UserAFCAndroid;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationGroupM1;
import cat.uib.secom.crypto.afc.net.impl.message.UserRegistrationGroupM2;
import cat.uib.secom.crypto.afc.net.message.NetworkEndPoints;
import cat.uib.secom.crypto.afc.store.logic.UserAFCLogic;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.utils.math.pairing.ElementWrapper;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AFCMainActivity extends Activity {
	
	private BBSGroupPublicKey gpk;
	private BBSUserPrivateKey usk;
	
	
	public static final String TAG = "AFCMainActivity";
	private static final String curveFileName = "d840347-175-161.param";
	
	private static final String HOST = "10.174.239.254";
	private static final Integer PORT = new Integer(10000);
	
	
	protected BBSParameters bbsParameters;
	protected ElementWrapper g1;
	protected UserAFC me;
	protected Client client;
	
	protected byte[] myCertificate = "my_certificate".getBytes();
	protected String myRealIdentity = "andreupere";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bbsParameters = new BBSParameters(curveFileName);
        
        me = new UserAFCAndroid();
        
        client = new Client(HOST, PORT);
        
        getG1FromGroupTTP();
        
        userRegistrationGroupTTP();
        
        userRegistrationPayTTP();
        
        
    }
    
    
    protected void getG1FromGroupTTP() {
    	
    	String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_G1, null);  // send request G1 and receive G1
    	byte[] g1 = StringUtils.hexStringToByteArray(received);
    	try {
	    	this.g1 = PairingHelper.toElementWrapper(g1, bbsParameters.getPairing(), "G1");
	        this.g1 = new ElementWrapper( this.g1.getElement().getImmutable() );  // Assure immutable element
    	} catch(Exception e) {}
    	
    	
    }
    
    protected void userRegistrationGroupTTP() {
    	
    	me = UserAFCLogic.newUserAFC(me, g1, bbsParameters.getPairing(), myCertificate, myRealIdentity);
    	
    	String signature = "yu_signed";
    	
    	UserRegistrationGroupM1 m1 = new UserRegistrationGroupM1();
		m1.setCertificate(me.getCertificate());
		m1.setSignature(signature);
		m1.setUserIdentity(me.getRealIdentity());
		m1.setYu(me.getYu());
		
		String received = sendAndWait(NetworkEndPoints.USER_TO_GROUP_TTP_REQUEST_KEYPAIR, m1.serialize());
		
		UserRegistrationGroupM2 m2 = new UserRegistrationGroupM2();
		m2.desearialize(received);
		
		cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey gpk = 
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( m2.getG1(),
																			   m2.getG2(),
																			   m2.getH(),
																			   m2.getU(),
																			   m2.getV(),
																			   m2.getOmega(),
																			   bbsParameters.getPairing() );
		
		cat.uib.secom.crypto.sig.bbs.core.keys.BBSUserPrivateKey usk =
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey( m2.getA(),
																			   m2.getX(),
																			   bbsParameters.getPairing() );
		
		
		System.out.println("retrieved and rebuilt gpk: " + gpk);
		System.out.println("retrieved and rebuilt usk: " + usk);
		
    	
    }
    
    
    
    protected void userRegistrationPayTTP() {}
    
    
    
    
    protected String sendAndWait(String endPoint, String toBeSend) {
    	String received = "";
    	try {
        	Log.v(TAG, "create socket...");
	        client.createSocket();
	        client.prepareInput();
	        client.prepareOutput();
	        Log.v(TAG, "input output...");
	        client.write(endPoint);
	        if (toBeSend != null) {
	        	client.write(toBeSend);
	        }
	        received = client.read();
	        client.closeInput();
	        client.closeOutput();
	        client.closeSocket();
	        
    	} catch(IOException e) {}
    	return received;
    }
    
    
}