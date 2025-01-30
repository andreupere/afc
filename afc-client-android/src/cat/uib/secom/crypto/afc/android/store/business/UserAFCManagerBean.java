package cat.uib.secom.crypto.afc.android.store.business;

import android.content.ContentValues;
import android.util.Log;
import cat.uib.secom.crypto.afc.android.store.entities.AFCClientManager;
import cat.uib.secom.crypto.afc.android.store.entities.UserAFC;
import cat.uib.secom.crypto.afc.android.store.entities.UserAFC.UserAFCColumns;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey;



public class UserAFCManagerBean {

	public static final String TAG = UserAFCManagerBean.class.getName();
	
	public void deployUser(byte[] xu, byte[] yu, String realIdentity) {
		ContentValues values = new ContentValues();
		
		values.put( UserAFCColumns.XU, xu );
		values.put( UserAFCColumns.YU, yu );
		values.put( UserAFCColumns.REAL_IDENTITY, realIdentity );
		
		AFCClientManager cm = new AFCClientManager();
		
		long idRow = cm.insert( AFCClientManager.USER_KEYS, values);
		Log.v(TAG, Long.toString(idRow));
		
	}
	
	public void storeGroupSignatureSchemeData(BBSGroupPublicKey gpk, BBSUserPrivateKey usk, Long userId) {
		ContentValues values = new ContentValues();
		
		// public key elements
		values.put( UserAFCColumns.G1, gpk.getG1().toByteArray() );
		values.put( UserAFCColumns.G2, gpk.getG2().toByteArray() );
		values.put( UserAFCColumns.H, gpk.getH().toByteArray() );
		values.put( UserAFCColumns.U, gpk.getU().toByteArray() );
		values.put( UserAFCColumns.V, gpk.getV().toByteArray() );
		values.put( UserAFCColumns.OMEGA, gpk.getOmega().toByteArray() );
		
		// private key elements
		values.put( UserAFCColumns.AI, usk.getA().toByteArray() );
		values.put( UserAFCColumns.XI, usk.getX().toByteArray() );
		
		
		AFCClientManager cm = new AFCClientManager();
		int count = cm.update( AFCClientManager.USER_KEYS, userId, values);
		Log.v(TAG, Integer.toString(count));
		
	}
	
}
