package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.proofs.GammaUser;

@Root(name="gamma-user")
public class GammaUserBean implements GammaUser {

	@Element
	protected Double fare;
	
	@Element
	protected String omega1;
	
	@Element(name="serial-number")
	protected Integer serialNumber;
	
	
	
	
	
	public Double getFare() {
		return fare;
	}

	public String getOmega1() {
		return omega1;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public void setOmega1(String omega1) {
		this.omega1 = omega1;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

}
