package cat.uib.secom.crypto.afc.android.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cat.uib.secom.crypto.afc.android.AFCMainActivity;
import cat.uib.secom.crypto.afc.android.benchmark.AndroidLog;
import dalvik.system.VMRuntime;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

public class AFCMainActivityTest extends ActivityInstrumentationTestCase2<AFCMainActivity> {

	public AFCMainActivityTest() {
		super("cat.uib.secom.crypto.afc.android", AFCMainActivity.class);
	}
	
	
	public void test() {
		// benchmark file
    	Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
    	File sdCardPath = Environment.getExternalStorageDirectory();
    	String path = sdCardPath.getAbsolutePath() + "/afc/benchmark/" + sdf.format(date) + "/";
    	String logFileName = "wildfire_with_precomputation_traceable_version.csv";
    	AndroidLog log = new AndroidLog(path, logFileName);
    	
		try {
			log.init();
			log.writeComments("Comment1: times in ms");
			log.writeHeader("Load Pairing \t Prepare RSA \t Prepare Storage \t Get G1 from TTP \t Reg. Group TTP \t Reg. Pay TTP \t Prepare entrance \t Entrance \t Exit \t Group Signature \t Total \t Precomputation on?");
		
		
			int iterations = 20;
			int i = 1;
			
			while (i <= iterations) {
				System.out.println("Test number " + i);
				// do test
				String logLine = doTask(i);
				log.write(logLine);
				i++;
			}
			log.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public String doTask(int iteration) {
		AFCMainActivity ma = getActivity();
		String logLine = ma.getLog();
		try {
			tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logLine;
	}
	
	public void tearDown() {
		try {
			super.tearDown();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
