package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;


import javax.persistence.Column;
import javax.persistence.Embeddable;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;




@Embeddable
public class SigmaImpl implements Sigma {


	
	@Column(name="TIN_OMEGA_S1")
	private String s1;
	
	
	// XMLenc
	@Column(name="TIN_OMEGA_DELTAU", length=512)
	private String deltau;
	
	
	// Hash
	@Column(name="TIN_OMEGA_HK", length=512)
	private String hk;
	

	

	

	

	
	
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	


	
	public String getDeltau() {
		return deltau;
	}
	public void setDeltau(String deltau) {
		this.deltau = deltau;
	}
	
	
	public String getHk() {
		return hk;
	}
	public void setHk(String hk) {
		this.hk = hk;
	}
	


	
	
	
	
	public String serialize() {
		return this.getS1() + " " + 
			   this.getDeltau() + " " + 
			   this.getHk();
	}
	
	public Sigma deserialize(String in) {
		String[] p = in.split(" ");
		SigmaImpl s = new SigmaImpl();
		s.deltau =  p[0];
		s.hk = p[1];
		s.s1 = p[2];
		return s;
	}
	


	
}
