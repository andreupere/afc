package cat.uib.secom.crypto.sig.acjt;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.bouncycastle.util.BigIntegers;

import cat.uib.secom.utils.crypto.Hash;

public class SchemeImplementation {

	
	
	public SchemeImplementation() {}
	
	public static String execute() throws NoSuchAlgorithmException {
		
		
		long setup = 0;
		long signing = 0;
		long join = 0;
		long verifying = 0;
		long open = 0;
		
		
		setup = System.currentTimeMillis();
		/***
		 * PARAMETERS
		 */
		// epsilon > 1 (controls the tightness of the statistical zero-knowledgeness)
		Integer epsilon_1 = 2;
		BigInteger epsion = new BigInteger(epsilon_1.toString());
		
		// k: output length of hash function 
		Integer k_1 = 160;
		BigInteger k = new BigInteger(k_1.toString());
		
		// l_p: security parameter (sets the size of the modulus to use)
		Integer lp_1 = 512; 
		BigInteger lp = new BigInteger(lp_1.toString());
		
		// lambda1, lambda2, sigma1 and sigma2 should be greater than this values (near byte)
		Integer lambda2_1 = 2056; // 4 * lp_1;
		Integer lambda1_1 = 4440; // epsilon_1 + (lambda2_1 + k_1) + 2;
		Integer sigma2_1 = 4448; // lambda1_1 + 2;
		Integer sigma1_1 = 9224; //epsilon_1 + (sigma2_1 + k_1)  + 2;
		
		BigInteger lambda1 = new BigInteger(lambda1_1.toString());
		BigInteger lambda2 = new BigInteger(lambda2_1.toString());
		BigInteger sigma1 = new BigInteger(sigma1_1.toString());
		BigInteger sigma2 = new BigInteger(sigma2_1.toString());
		
		// range 1
		
		// range 2
		
		// hash function
		
		/***
		 * SETUP
		 */
		// select random secret lp-bit primes p', q' such that p = 2p' +1 and q = 2q'+1 are prime (safe primes)
		BigInteger pPrima = new BigInteger("8120056C28EEF13C537BEB8EACCECF8E222545BEC47014EA574527A8FF84C821A5508E4CD7D9578024849D80F1335FC49E22F4772725F0283EAC02746114E833",16);							// ¿?¿?¿
		BigInteger qPrima = new BigInteger("E894E6ED699AFA9227B6BB49D7D5C4520D6A1032BB70D9035DEFE49EA76091EF6ED40B55A82E1F358B2C49DCF3430067C2DA49C0E99ABEB6734F9D0376E34F9B", 16);
		BigInteger p = pPrima.multiply(new BigInteger("2")).add(BigInteger.ONE);
		BigInteger q = qPrima.multiply(new BigInteger("2")).add(BigInteger.ONE);
		// set the modulus n = pq
		BigInteger n = p.multiply(q);
		
		
		// choose random a, a0, g, h in QR(n) (of order p'q')  ¿?¿?
		BigInteger a = new BigInteger("8120056C28EEF13C537BEB8EACCECF8E2221A4BEC47014EA574527A8FF84C821A5508E4CD7D9578024849D80F1335FC49E22F4772725F0283EAC02746114E833", 16);												// ¿?¿?¿
		BigInteger a0 = new BigInteger("8870056C28EEF13C537BEB8EACCECF8E222545BEC47014EA57452712FF84C821A5508E4CD7D9578024849D80F1335FC49E22F4772725F0283EAC02746114E833", 16);
		BigInteger g = new BigInteger("8120056C28EEF13C537BEB8EACCECF8E2285AA3EC47014EA574527A8FF84C821A5508E4CD7D9578024849D80F1335FC49E22F4772725F0283EAC02746114E833", 16);
		BigInteger h = new BigInteger("E894E6ED699AFA9227B6BB49D7D5C4520D6A1037AB70D9035DEFE49EA76091EF6ED40B55A82E17548B2C49DCF3430067C2DA49C0E99ABEBA734F9D0376E34F9B", 16);
		
		
		// chose random secret element x in Z*p'q'
		BigInteger x = new BigInteger(1024, new Random(n.longValue()));		// ¿?¿?¿
		// set y = g^x mod n
		BigInteger y = g.modPow(x, n);
		
		// the group public key is gpk=(n,a,a0,y,g,h)
		ACJTGroupPublicKey gpk = new ACJTGroupPublicKey(n, a, a0, y, g, h);
		// the corresponding secret key (known only to GM) is:
		ACJTSecretKey gmsk = new ACJTSecretKey(pPrima, qPrima, x);
		
		
		setup = System.currentTimeMillis() - setup;
		
		
		join = System.currentTimeMillis();
		/***
		 * JOIN
		 */
		// user Pi generates a secret exponent xiCapell in ]0, 2^lamba2[
		//BigInteger xiHat = new BigInteger(lambda2.bitLength(), new Random(n.longValue()));
		BigInteger xiHat = createRandomInRange(BigInteger.ONE, lambda2);
		// a random integer rCapell in ]0, n^2[
		//BigInteger rHat = new BigInteger((n.pow(2)).bitLength(), new Random(n.longValue()));
		BigInteger rHat = createRandomInRange(BigInteger.ONE, n.pow(2));
		// c1 = g^xi * h^r
		// not necessary to compute
		
		
		// selects alphai from ]0, 2^lambda2[
		//BigInteger alphai = new BigInteger(lambda2.bitLength(), new Random(n.longValue()));
		BigInteger alphai = createRandomInRange(BigInteger.ONE, lambda2);
		// selects betai from ]0, 2^lambda2[
		//BigInteger betai = new BigInteger(lambda2.bitLength(), new Random(n.longValue()));
		BigInteger betai = createRandomInRange(BigInteger.ONE, lambda2);
		
		// Pi computes xi
		BigInteger xi_1 = (new BigInteger("2")).modPow(lambda1, n);
		BigInteger xi_2 = (alphai.multiply(xiHat)).mod(n);
		xi_2 = (xi_2.add(betai)).mod( (new BigInteger("2").modPow(lambda2, n)) );
		BigInteger xi = xi_1.add(xi_2);
		xi = xi.mod(n);
		
		// Pi computes C2
		BigInteger c2 = gpk.getA().modPow(xi, n);
		
		
		// compute the range GAMMA
		BigInteger exponentSigma1 = (new BigInteger("2").pow(sigma1_1));
		BigInteger exponentSigma2 = (new BigInteger("2").pow(sigma2_1));
		BigInteger gammaRangeBottom = exponentSigma1.subtract(exponentSigma2);
		BigInteger gammaRangeUp = exponentSigma1.add(exponentSigma2);
		
		
		
		
		//Integer eiInt = gammaRangeBottom.intValue() + (int)(Math.random() * ((gammaRangeUp.intValue() - gammaRangeBottom.intValue()) + 1));
		//BigInteger ei = new BigInteger(eiInt.toString());
		// ei has 
		BigInteger ei = new BigInteger("10679483888479143284044034773992458612040696314991839478710301563678491575769216162281481940584974585806482221311333423742639008674802917342686392635605790806292907759422402634038808246300075316263990078412477408340876015685945972691566250804874655185572200663064734198930047127040940244773379894425867231280568491412534492464944261353326334711208071955707757383709332544098051290405282603345837624594846919386303382284941567814345353806688211876896148475278458207045147383242066519793488485638086635861757000455300792458963241205354565617684415160254511013724552671022208942572244043009610689146507400950288590206372615425642453615774973876737215534141049500303483257206612653034594765528941427795084498837840093106307681692844604291789005081522093133962038441515371916849310982018708947185885038504679444364780101002794398555078429944524319042841936880366206065104080591119820224679481684013571316113044919650775027799550730898275001692802802091834907136022185457005969009765307095110816737551773252870968211466845485292745688737528050754630117166859498368394249620354472599537947446531098816017483364446139238642801647430235659617738876363057090574843608857154719774712753838572530530133864440018014849705351994069132884244962480719580738068136450532301356975652033129499607258570103083770530157090902532649767500564662362370367021659730881856901164143222036189597423912812447471969335952512197232332624561562458536491143483003157426821992023912725647072379855211318527706596427394400484162584988843786908785568796444433872415662080786180886257894948998861628208867765466697995886982753990325418504612696931149703053241252173530793479620256187892540305790755307740984477635783905818553559433480783941715177239006917676017439772917720751865897946088251754472965957853180057324058789909723411995769371078415158980991861864095211371230556106132425799550789147239532914181135144325543304619000802187059992228955916396515416485643807711325631174021266120623447772084412797122753439023615907243050017854162030782745747070268581497695668570550895079185696798266369561639227002238434006438063483959828497900396931608121402938157165473659279227094402160556876160168878616193700755632114659286262913084725408588194729443796403088754525195779846144164501120305248061004477086009712757136129085072193794866652676112068991044155212380722396202117290313944894040818564397979176912741035932050688413667594644149553231379765962348348819860567147994609719468296353065064003153420115488921693520890298912272975413040173433455506121007831816668305480280577377021675486405472960792612312952229903690989839021303859425548308422630497819549549061164467737142810660366543487409327576699592755063366209583673861175723377777954212227549243063958018933316929402704055546226447211017096087209328101");
		
		System.out.println("ei number digits " + ei.toString().length());
		
		BigInteger Ai_1 = c2.multiply(gpk.getA0()).mod(n);
		BigInteger Ai_exp = ei.modInverse(n);
		BigInteger Ai = Ai_1.modPow(Ai_exp, n);
		
		ACJTMembershipCertificate certificate = new ACJTMembershipCertificate(Ai, ei);
		
		join = System.currentTimeMillis() - join;
		
		// without precomputation, remove comment from the next line
		//signing = System.currentTimeMillis();
		
		/***
		 * SIGNATURE
		 */
		// message to be signed
		String message = "Hola";
		
		// generate a random value w in {0,1}^2lp
		BigInteger w = new BigInteger(2*lp_1, new SecureRandom());
		
		// compute
		BigInteger t1 = (certificate.getAi().multiply( y.modPow(w, n) )).mod(n);
		
		BigInteger t2 = g.modPow(w, n);
		
		BigInteger t3_1 = g.modPow(certificate.getEi(), n);
		BigInteger t3_2 = h.modPow(w, n);
		BigInteger t3 = (t3_1.multiply(t3_2)).mod(n);
		
		
		// choose random r1
		Integer nBitsR1 = epsilon_1 * (sigma2_1 + k_1);
		BigInteger r1 = new BigInteger(nBitsR1, new SecureRandom());
		// choose random r2
		Integer nBitsR2 = epsilon_1 * (lambda2_1 + k_1);
		BigInteger r2 = new BigInteger(nBitsR2, new SecureRandom());
		// choose random r3
		Integer nBitsR3 = epsilon_1 * (sigma1_1 + (2*lp_1) + k_1 + 1);
		BigInteger r3 = new BigInteger(nBitsR3, new SecureRandom());
		// choose random r4
		Integer nBitsR4 = epsilon_1 * (2*lp_1 + k_1);
		BigInteger r4 = new BigInteger(nBitsR4, new SecureRandom());
		
		// compute a)
		BigInteger d1_1 = t1.modPow(r1, n);
		BigInteger d1_2 = gpk.getA().modPow(r2, n).multiply( gpk.getY().modPow(r3, n) ).mod(n); 
		BigInteger d1 = d1_1.divide(d1_2).mod(n);
		
		BigInteger d2_1 = t2.modPow(r1, n);
		BigInteger d2_2 = gpk.getG().modPow(r3, n);
		BigInteger d2 = d2_1.divide(d2_2).mod(n);
		
		BigInteger d3 = gpk.getG().modPow(r4, n);
		
		BigInteger d4_1 = gpk.getG().modPow(r1, n);
		BigInteger d4_2 = gpk.getH().modPow(r4, n);
		BigInteger d4 = d4_1.multiply(d4_2).mod(n); 
		
		// comment whether precomputation is not enabled
		signing = System.currentTimeMillis();
		
		// compute b)
		
		String data = gpk.getG().toString() + 
					  gpk.getH().toString() +
					  gpk.getY().toString() +
					  gpk.getA0().toString() +
					  gpk.getA().toString() +
					  t1.toString() +
					  t2.toString() +
					  t3.toString() +
					  d1.toString() +
					  d2.toString() +
					  d3.toString() +
					  d4.toString() +
					  message;
					  
		BigInteger c = Hash.getHash(data);
		
		// compute c) (all in Z) (without modules, right??)
		BigInteger two = new BigInteger("2");
		long precom = System.currentTimeMillis();
		BigInteger s1_1 = certificate.getEi().subtract( (two.pow(sigma1_1)) );
		precom = System.currentTimeMillis() - precom;
		BigInteger s1_2 = c.multiply(s1_1);
		BigInteger s1 = r1.subtract(s1_2);
		
		
		long precom2 = System.currentTimeMillis();
		BigInteger s2_1 = xi.subtract( (two.pow(lambda1_1)) );
		precom2 = System.currentTimeMillis() - precom2;
		BigInteger s2_2 = c.multiply(s2_1);
		BigInteger s2 = r2.subtract(s2_2);
		
		
		BigInteger s3 = r3.subtract( (c.multiply(certificate.getEi()).multiply(w)) );
		
		
		BigInteger s4 = r4.subtract( (c.multiply(w)) );
		
		ACJTSignature signature = new ACJTSignature(c, s1, s2, s3, s4, t1, t2, t3);
		
	
		signing = System.currentTimeMillis() - signing - precom - precom2;
		
		verifying = System.currentTimeMillis();
		
		/***
		 * VERIFICATION
		 */
		BigInteger v1_0 = gpk.getA0().modPow(signature.getC(), n);
		BigInteger v1_1_exp = signature.getS1().subtract( (signature.getC().multiply( two.modPow(sigma1, n) ).mod(n)).mod(n) ).mod(n);
		BigInteger v1_1 = signature.getT1().modPow(v1_1_exp, n);
		v1_1 = v1_0.multiply(v1_1).mod(n);
		BigInteger v1_2_exp = signature.getS2().subtract( (signature.getC().multiply( two.modPow(lambda1, n) ).mod(n)).mod(n) ).mod(n);
		BigInteger v1_2 = gpk.getA().modPow(v1_2_exp, n); 
		BigInteger v1_3 = gpk.getY().modPow(signature.getS3(), n);
		v1_2 = v1_2.multiply(v1_3).mod(n);
		BigInteger v1 = v1_1.divide(v1_2).mod(n);
		
		
		BigInteger v2_1_exp = signature.getS1().subtract( signature.getC().multiply( two.modPow(sigma1,n) ).mod(n) ).mod(n);
		BigInteger v2_1 = signature.getT2().modPow(v2_1_exp, n);
		BigInteger v2_2 = gpk.getG().modPow(signature.getS3(), n);
		BigInteger v2 = v2_1.divide(v2_2).mod(n);
		
		
		BigInteger v3_1 = signature.getT2().modPow(signature.getC(), n);
		BigInteger v3_2 = gpk.getG().modPow(signature.getS4(), n);
		BigInteger v3 = v3_1.multiply(v3_2).mod(n);
		
		
		BigInteger v4_0 = signature.getT3().modPow(signature.getC(), n);
		BigInteger v4_1_exp = signature.getS1().subtract( signature.getC().multiply( two.modPow(sigma1, n) ).mod(n) ).mod(n);
		BigInteger v4_1 = gpk.getG().modPow(v4_1_exp, n);
		v4_1 = v4_0.multiply(v4_1).mod(n); 
		BigInteger v4_2 = gpk.getH().modPow(signature.getS4(), n);
		BigInteger v4 = v4_1.multiply(v4_2).mod(n);
		
		String data2 = gpk.getG().toString() + 
				  	   gpk.getH().toString() +
				  	   gpk.getY().toString() +
				  	   gpk.getA0().toString() +
				  	   gpk.getA().toString() +
				  	   signature.getT1().toString() +
				  	   signature.getT2().toString() +
				  	   signature.getT3().toString() +
				  	   v1.toString() +
				  	   v2.toString() +
				  	   v3.toString() +
				  	   v4.toString() +
				  	   message;
				  	   
		BigInteger cPrima = Hash.getHash(data2);
		
		
		if (c.equals(cPrima))
			System.out.println("c=c' true");
		else
			System.out.println("c notequals c'");
		
		Integer nBitsS1 = nBitsR1 + 1;
		Integer nBitsS2 = nBitsR2 + 1;
		Integer nBitsS3 = nBitsR3 + 1;
		Integer nBitsS4 = nBitsR4 + 1;
		
		BigInteger s1Range = two.pow(nBitsS1);
		
		if ( s1Range.negate().compareTo(signature.getS1())==-1 & s1Range.compareTo(signature.getS1())==1 )
			System.out.println("s1 in range");
		
		BigInteger s2Range = two.pow(nBitsS2);
		if ( s2Range.negate().compareTo(signature.getS2())==-1 & s2Range.compareTo(signature.getS2())==1 )
			System.out.println("s2 in range");
		
		BigInteger s3Range = two.pow(nBitsS3);
		if ( s3Range.negate().compareTo(signature.getS3())==-1 & s3Range.compareTo(signature.getS3())==1 )
			System.out.println("s3 in range");
		
		BigInteger s4Range = two.pow(nBitsS4);
		if ( s4Range.negate().compareTo(signature.getS4())==-1 & s4Range.compareTo(signature.getS4())==1 )
			System.out.println("s4 in range");
		
		
		
		verifying = System.currentTimeMillis() - verifying;
		
		precom = System.currentTimeMillis();
		// time to precompute 2^{sigma_1} and 2^{lambda_1}
		two.modPow(sigma1, n);
		two.modPow(sigma1, n);
		two.modPow(lambda1, n);
		two.modPow(lambda1, n);
		precom = System.currentTimeMillis() - precom;
		
		verifying = verifying - precom;
		
		
		open = System.currentTimeMillis();
		
		// accept the signature iff c=cPrima and s1 in range1, s2 in range 2, s3 in range3 and s4 in range4
		
		/***
		 * OPEN
		 */
		// check the signature validity via VERIFY
		
		// recover Ai as
		BigInteger AiRecover = signature.getT1().divide(signature.getT2().modPow(x, n)).mod(n);
		// prove that loggy = logt2(T1/Ai mod n) (see definition (5))
		Double left = (Math.log(gpk.getY().doubleValue()));
		left = left / Math.log(gpk.getG().doubleValue());
		
		BigInteger right2 = signature.getT1().divide(certificate.getAi());
		right2 = right2.mod(n);
		Double right = Math.log( right2.doubleValue() );
		right = right / Math.log( signature.getT2().doubleValue() );
		
		// test equality
		if (left.equals(right)) 
			System.out.println("proved");
		
		open = System.currentTimeMillis() - open;
		
		
		System.out.println("s1range: "  + s1Range.toString(2).length());
		System.out.println("s1: " + signature.getS1().toString(2).length());
		System.out.println(cPrima.toString(2));
		
		length("n", n.bitLength());
		
		System.out.println("gammaRange: " + gammaRangeBottom.bitLength() + " " + gammaRangeUp.bitLength());
		
		length("ei", ei.bitLength());
		
		length("r1", r1.bitLength());
		length("r2", r2.bitLength());
		length("r3", r3.bitLength());
		length("r4", r4.bitLength());
		
		
		//System.out.println("two.pow(sigma1) length: " + two.pow(sigma1_1).bitLength() + " bits");
		//System.out.println("s1_1 length: " + s1_1.bitLength() + " bits");
		//System.out.println("s1_2 length: " + s1_2.bitLength() + " bits");
		
		length("s1", s1.bitLength());
		length("s2", s2.bitLength());
		length("s3", s3.bitLength());
		length("s4", s4.bitLength());
		length("signature", signature.length());

		length("c", c.bitLength());
		length("cPrima", cPrima.bitLength());
		
		
		
		length("signature", signature.length());
		length("group public key", gpk.length());
		length("secret gm key", gmsk.length());
		length("user certificate", certificate.length());
		

		return setup 		+ "\t" +
			   join 		+ "\t" +
			   signing 		+ "\t" +
			   verifying 	+ "\t" +
			   open 		+ "\r\n";

		
	}
	
	protected static void length(String paramName, Integer paramValue) {
		System.out.println(paramName + " length: " + paramValue + " bits");
	}
	
	
	protected static BigInteger createRandomInRange(BigInteger min, BigInteger max) {
		return new BigInteger(max.subtract(min).bitLength() - 1, new SecureRandom()).add(min);
	}
	
}
