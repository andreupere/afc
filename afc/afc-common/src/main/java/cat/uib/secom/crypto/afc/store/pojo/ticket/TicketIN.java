package cat.uib.secom.crypto.afc.store.pojo.ticket;

import java.util.Date;

public interface TicketIN {

	
	public Integer getSerialNumber();

	public void setSerialNumber(Integer serialNumber);



	public Integer getSrcProvider();

	public void setSrcProvider(Integer srcProvider);



	public Date getTimestamp1();

	public void setTimestamp1(Date timestamp1);
	
	

	public Date getValidityTime();

	public void setValidityTime(Date validityTime);
	
	

	public SigmaSigned getSigmaSigned();

	public void setSigmaSigned(SigmaSigned sigmaSigned);
	


}