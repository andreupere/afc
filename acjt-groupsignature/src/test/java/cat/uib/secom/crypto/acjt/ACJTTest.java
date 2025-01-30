package cat.uib.secom.crypto.acjt;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import cat.uib.secom.crypto.sig.acjt.SchemeImplementation;

public class ACJTTest {

	@Test
	public void ACJTTestScheme() {
		String results = "";
		try {
			// discarding first iteration
			SchemeImplementation.execute();
			int maxIt = 20;
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
