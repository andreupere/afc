package cat.uib.secom.crypto.afc.store.pojo.impl.proofs;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.proofs.GammaUser;



@Embeddable
public class GammaUserImpl implements GammaUser {


	
	@Column(name="GAMMA_USER_OMEGA1")
	private String omega1;
	
	@Column(name="GAMMA_USER_TIN_SERIAL_NUMBER")
	private Integer serialNumber;
	
	@Column(name="GAMMA_USER_FARE")
	private Double fare;
	
	
	
	public String getOmega1() {
		return omega1;
	}
	public void setOmega1(String omega1) {
		this.omega1 = omega1;
	}




	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}




	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}


	
}
