package cat.uib.secom.crypto.acjt.android;

import java.security.NoSuchAlgorithmException;

import cat.uib.secom.crypto.sig.acjt.SchemeImplementation;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "acjt-android";

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        
        String results = "";
		try {
			// discarding first iteration
			SchemeImplementation.execute();
			int maxIt = 10;
			int it = 1;
			results = "Times\tin\t(ms)\r\n";
			results = results + "it\tsetup\tjoin\tsign\tverify\topen\r\n"; 
			while (it <= maxIt) {
				results = results + it + "\t";
				results += SchemeImplementation.execute();
				it++;
			}
			System.out.println(results);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }

}

