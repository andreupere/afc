package cat.uib.secom.crypto.afc.resource;



/**
 * TODO: to XML config file
 * 
 * 
 * @author Andreu Pere
 * */
public class StaticParameters {

	/**
	 * The initial users' balance in their accounts (euros)
	 * */
	public final static Double INITIAL_BALANCE = new Double(100); 
	/**
	 * The ticket validity time between entrance and exit in number of milliseconds
	 * */
	public final static Integer VALIDITY_TIME = new Integer(1000*60*60);
	/**
	 * For random ticket serial number generation
	 * */
	public final static Integer SERIAL_NUMBER_MAX = new Integer(999999999);
	
	/**
	 * The name of curve settings file
	 * */
	public final static String CURVE_FILE = "d840347-175-161.param";
	
	public final static Integer NUMBER_OF_DEPLOY_KEYS = new Integer(10);
	
	public final static String CERT_PATH_NAME = "/home/apaspai/developing/keys_certs/";
	
}
