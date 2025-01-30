package cat.uib.secom.crypto.afc.android;

public class AndroidAFC {
	
	
}

/*import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import it.unisa.dia.gas.jpbc.Element;

import cat.uib.secom.crypto.afc.R;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.core.parameters.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.keys.BBSUserPrivateKey;
import cat.uib.secom.utils.crypto.RandomGenerator;
import cat.uib.secom.utils.math.pairing.PairingHelper;
import cat.uib.secom.utils.net.socket.Client;
import cat.uib.secom.utils.strings.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AndroidAFC extends Activity {
	
	public static final String TAG = "AndroidAFC";
	private static final String curveFileName = "d840347-175-161.param";
	
	private static final String HOST = "10.174.239.254";
	private static final Integer PORT = new Integer(10000);
	
	private BBSGroupPublicKey _bbsgpk;
	private BBSUserPrivateKey _bbsusk;
	
    *//** Called when the activity is first created. *//*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        
        // start the registration to Group TTP
        userRegistrationGroupTTP();
        
        userRegistrationPayTTP();
        
        
		
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    protected void userRegistrationGroupTTP() {
    	// get G1 from Group TTP
        byte[] g1 = null;
        Log.v(TAG,"connecting...");        
        
        Client client = new Client(HOST, PORT);
        try {
        	Log.v(TAG, "create socket...");
	        client.createSocket();
	        client.prepareInput();
	        client.prepareOutput();
	        Log.v(TAG, "input output...");
	        client.write(NetworkMessages.USER_TO_GROUP_TTP_REQUEST_G1);
	        String _g1 = client.read();
	        client.closeInput();
	        client.closeOutput();
	        client.closeSocket();
	        Log.v(TAG, "g1: " + _g1);
	        g1 = StringUtils.hexStringToByteArray(_g1);
        } catch(IOException e) {}
        
        
        // the user generates xu, yu and sends it to groupTTP
        Log.v(TAG, "Start bbsparameters");
		BBSParameters bbsParameters = new BBSParameters(curveFileName);
		bbsParameters.getPairing();
		
		Log.v(TAG, "bbsParameters end");

		Log.v(TAG, "from byteArray");
		Element g1e = null;
		try {
			g1e = PairingHelper.fromByteArray(g1, bbsParameters.getPairing(), "G1");
			g1e.getImmutable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v(TAG, "end from byteArray");
		
		Element xu = PairingHelper.randomValue(bbsParameters.getPairing().getZr());
		Log.v(TAG, "end random");
		Element yu = PairingHelper.pow(g1e, xu);
		Log.v(TAG, "end pow");
		String uIdentity = RandomGenerator.generate(10000).toString();
		Log.v(TAG, "end uIdentity");
		
		
		// user sends ( yu, uIdentity ) to Group TTP
		String signature = "signature";
		String cert = "user_certificate";
		
		String toBeSend = StringUtils.readHexString(yu.toBytes()) + " " +
						  uIdentity + " " +
						  signature + " " +
						  cert;
		
		Log.v(TAG, "toBeSend: " + toBeSend);
		String response = "";
		try {
			client = new Client("10.174.239.254", 10000);
			client.createSocket();
			client.prepareInput();
			client.prepareOutput();
			client.write(NetworkMessages.USER_TO_GROUP_TTP_REQUEST_KEYPAIR);
			client.write(toBeSend);
			
			response = client.read();
			Log.v(TAG, "response 2n message: " + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] kp = response.split(" ");
		Log.v(TAG, "g1: " + kp[0]);
		Log.v(TAG, "g2: " + kp[1]);
		Log.v(TAG, "h: " + kp[2]);
		Log.v(TAG, "u: " + kp[3]);
		Log.v(TAG, "v: " + kp[4]);
		Log.v(TAG, "omega: " + kp[5]);
		
		Log.v(TAG, "xi: " + kp[6]);
		Log.v(TAG, "ai: " + kp[7]);
        
		_bbsgpk = new BBSGroupPublicKey(StringUtils.hexStringToByteArray(kp[0]),
														 StringUtils.hexStringToByteArray(kp[1]),
														 StringUtils.hexStringToByteArray(kp[2]),
														 StringUtils.hexStringToByteArray(kp[3]),
														 StringUtils.hexStringToByteArray(kp[4]),
														 StringUtils.hexStringToByteArray(kp[5]),
														 bbsParameters.getPairing() );
		
		_bbsusk = new BBSUserPrivateKey(StringUtils.hexStringToByteArray( kp[7] ),
														 StringUtils.hexStringToByteArray( kp[6] ),
														 bbsParameters.getPairing());
		
		Log.v(TAG, "I have the keypair. Register finished successfully...");
    }
    
    
    
    
    protected void userRegistrationPayTTP() {
    	
    }
    
    
    
    
}*/