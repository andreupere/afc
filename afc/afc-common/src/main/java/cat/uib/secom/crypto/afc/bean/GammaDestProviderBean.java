package cat.uib.secom.crypto.afc.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.proofs.GammaDestProvider;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;

@Root(name="gamma-destination-provider")
public class GammaDestProviderBean implements GammaDestProvider {

	@Element
	protected String c1;
	
	@Element
	protected Double fare;
	
	@Element(name="serial-number")
	protected Integer serialNumber;
	
	@Element
	protected SigmaBean sigma;
	
	
	
	
	public String getC1() {
		return c1;
	}

	public Double getFare() {
		return fare;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public Sigma getSigma() {
		return sigma;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSigma(Sigma sigma) {
		this.sigma = (SigmaBean) sigma;
	}

}
