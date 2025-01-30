package cat.uib.secom.crypto.afc.store.pojo.ticket;

import java.math.BigInteger;

public interface Sigma {

	public String getS1();

	public void setS1(String s1);

	

	/*public String getDeltau();

	public void setDeltau(String deltau);*/

	
	public String getDeltau();
	
	public void setDeltau(String deltau);

	
	public String getHk();

	public void setHk(String hk);
	
	
	public String serialize();

	


}