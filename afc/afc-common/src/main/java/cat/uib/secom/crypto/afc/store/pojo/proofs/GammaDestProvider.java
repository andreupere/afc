package cat.uib.secom.crypto.afc.store.pojo.proofs;

import java.math.BigInteger;

import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;




public interface GammaDestProvider {

	
	
	
	
	public Double getFare();
	
	public void setFare(Double fare);
	
	
	public Integer getSerialNumber() ;
	
	public void setSerialNumber(Integer serialNumber);
	

	
	
	public Sigma getSigma();
	
	public void setSigma(Sigma sigma);
	
	
	public String getC1();
	
	public void setC1(String c1);
	
	
}
