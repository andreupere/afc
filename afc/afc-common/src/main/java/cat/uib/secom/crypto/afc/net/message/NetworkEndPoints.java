package cat.uib.secom.crypto.afc.net.message;

public class NetworkEndPoints {

	// GROUP TTP REGISTRATION MESSAGES
	public static final String USER_TO_GROUP_TTP_REQUEST_G1 = "USER_REGISTRATION_M1";
	
	public static final String USER_TO_GROUP_TTP_REQUEST_KEYPAIR = "USER_REGISTRATION_M2";
	
	public static final String USER_REGISTRATION_M3 = "USER_REGISTRATION_M3";
	
	// PAYMENT TTP REGISTRATION MESSAGES
	public static final String USER_REGISTRATION_PAY_TTP_1 = "UPM1";
	
	public static final String USER_REGISTRATION_PAY_TTP_2 = "UPM2";
	
	public static final String USER_REGISTRATION_PAY_TTP_3 = "UPM3";
	
	
	// SYSTEM ENTRANCE MESSAGES
	public static final String SYSTEM_ENTRANCE_1 = "SYSTEM_ENTRANCE_1";
	
	public static final String SYSTEM_ENTRANCE_2 = "SYSTEM_ENTRANCE_2";
	
	
	// SYSTEM EXIT MESSAGES
	public static final String SYSTEM_EXIT_1 = "SYSTEM_EXIT_1"; // U sends (tin*, PK(k)) to Pd
	
	public static final String SYSTEM_EXIT_2 = "SYSTEM_EXIT_2"; // Pd sends beta* to U
	
	public static final String SYSTEM_EXIT_3 = "SYSTEM_EXIT_3";	// U sends gammaU to Pd
	
	public static final String SYSTEM_EXIt_4 = "SYSTEM_EXIT_4";	// Pd sends tout* to U
	
	
}
