package cat.uib.secom.crypto.afc.store.pojo.impl.proofs;



import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;



import cat.uib.secom.crypto.afc.store.pojo.proofs.GammaDestProvider;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;


@Embeddable
public class GammaDestProviderImpl implements GammaDestProvider {

	@Column(name="GAMMA_PD_FARE")
	private Double fare;
	
	@Column(name="GAMMA_PD_TIN_SERIAL_NUMBER")
	private Integer serialNumber;
	

	
	/**
	 * WARN: here I set sigma as SIGMA*, in the protocol scheme is only SIGMA, without signature over it...
	 * */
	@Embedded
	private Sigma sigma;
	
	@Column(name="GAMMA_PD_C1")
	private String c1;
	

	
	
	
	
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}
	
	
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}


	
	
	public Sigma getSigma() {
		return sigma;
	}
	public void setSigma(Sigma sigma) {
		this.sigma = sigma;
	}
	
	
	public String getC1() {
		return c1;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}


	
}
