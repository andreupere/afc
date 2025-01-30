package cat.uib.secom.crypto.afc.android.store.entities;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserAFCJourney {

	public static final String AUTHORITY = "cat.uib.secom.afc.android";
	
	private UserAFCJourney() {}
	
	public static final class UserAFCJourneyColumns implements BaseColumns {
		private UserAFCJourneyColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user_afc_journey");
		
		public static final String DEFAULT_SORT_ORDER  = "modified DESC";
		
		
		
		
		
		public static final String TIN_K = "TIN_SECRET_K";
		
		public static final String TIN_R1 = "TIN_SECRET_R1";
		
		
		
		public static final String TIN_SIGNATURE = "TIN_SIGNATURE";
		
		public static final String TIN_SERIAL_NUMBER = "TIN_SERIAL_NUMBER";
		
		public static final String TIN_SOURCE_STATION = "TIN_SOURCE_STATION";
		
		public static final String TIN_TAU_1 = "TIN_TAU_1";
		
		public static final String TIN_VALIDITY_TIME = "TIN_VALIDITY_TIME";
		
		public static final String TIN_OMEGA_S1 = "TIN_OMEGA_S1";  // SIGMA
		
		public static final String TIN_OMEGA_DELTAU = "TIN_OMEGA_DELTAU";  // SIGMA
		
		public static final String TIN_OMEGA_HK = "TIN_OMEGA_HK";  // SIGMA
		
		public static final String TIN_OMEGA_SIGN_T1 = "TIN_OMEGA_SIGN_T1";
		
		public static final String TIN_OMEGA_SIGN_T2 = "TIN_OMEGA_SIGN_T2";
		
		public static final String TIN_OMEGA_SIGN_T3 = "TIN_OMEGA_SIGN_T3";
		
		public static final String TIN_OMEGA_SIGN_C = "TIN_OMEGA_SIGN_C";
		
		public static final String TIN_OMEGA_SIGN_SALPHA = "TIN_OMEGA_SIGN_SALPHA";
		
		public static final String TIN_OMEGA_SIGN_SBETA = "TIN_OMEGA_SIGN_SBETA";
		
		public static final String TIN_OMEGA_SIGN_SX = "TIN_OMEGA_SIGN_SX";
		
		public static final String TIN_OMEGA_SIGN_SDELTA1 = "TIN_OMEGA_SIGN_SDELTA1";
		
		public static final String TIN_OMEGA_SIGN_DELTA2 = "TIN_OMEGA_SIGN_DELTA2";
		
		
		
		
		
		public static final String TOUT_SIGNATURE = "TOUT_SIGNATURE";
		
		public static final String TOUT_SERIAL_NUMBER = "TOUT_SERIAL_NUMBER";
		
		public static final String TOUT_DESTINATION_STATION = "TOUT_DESTINATION_STATION";
		
		public static final String TOUT_FARE = "TOUT_FARE";
		
		public static final String TOUT_MESSAGE = "TOUT_MESSAGE";
		
		
		
		
	}
	
}
