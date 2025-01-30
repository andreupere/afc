package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;

@Root(name="sigma")
public class SigmaBean implements Sigma {

	@Element
	protected String deltau;
	
	@Element
	protected String hk;
	
	@Element
	protected String s1;
	
	
	
	public String getDeltau() {
		return deltau;
	}

	public String getHk() {
		return hk;
	}

	public String getS1() {
		return s1;
	}

	public void setDeltau(String deltau) {
		this.deltau = deltau;
	}

	public void setHk(String hk) {
		this.hk = hk;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String serialize() {
		return this.s1 + " " + this.deltau + " " + this.hk;
	}

}
