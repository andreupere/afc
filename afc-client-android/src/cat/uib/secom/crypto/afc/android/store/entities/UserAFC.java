package cat.uib.secom.crypto.afc.android.store.entities;

import android.net.Uri;
import android.provider.BaseColumns;

public final class UserAFC {

	public static final String AUTHORITY = "cat.uib.secom.afc.android";
	private UserAFC() {}
	
	public static final class UserAFCColumns implements BaseColumns {
		
		private UserAFCColumns() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user_afc");
		
		public static final String DEFAULT_SORT_ORDER  = "modified DESC";
		
		/**
		 * Private Key AI parameter
		 * */
		public static final String AI  = "USER_PK_A";
		
		/**
		 * Private key XI parameter
		 * */
		public static final String XI = "USER_PK_X";
		
		/**
		 * Public key G1 parameter
		 * */
		public static final String G1 = "GROUP_PUBLIC_KEY_G1";
		
		/**
		 * Public key G2 parameter
		 * */
		public static final String G2 = "GROUP_PUBLIC_KEY_G2";
		
		/**
		 * Public key H parameter
		 * */
		public static final String H = "GROUP_PUBLIC_KEY_H";
		
		/**
		 * Public key U parameter
		 * */
		public static final String U = "GROUP_PUBLIC_KEY_U";
		
		/**
		 * Public key V parameter
		 * */
		public static final String V = "GROUP_PUBLIC_KEY_V";
		
		/**
		 * Public key OMEGA parameter
		 * */
		public static final String OMEGA = "GROUP_PUBLIC_KEY_OMEGA";
		
		/**
		 * User secret xu
		 * */
		public static final String XU = "USER_SECRET_XU";
		
		/**
		 * User pseudonim from xu
		 * */
		public static final String YU = "USER_YU";
		
		/**
		 * User certificate
		 * */
		public static final String CERTIFICATE = "USER_CERTIFICATE";
		
		/**
		 * User real identity
		 * */
		public static final String REAL_IDENTITY = "USER_REAL_IDENTITY";
		
	}
	
}
