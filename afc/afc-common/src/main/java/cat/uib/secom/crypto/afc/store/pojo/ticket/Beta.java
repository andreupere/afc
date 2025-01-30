package cat.uib.secom.crypto.afc.store.pojo.ticket;

import java.math.BigInteger;
import java.util.Date;

public interface Beta {
	
	public TicketINSigned getTicketINSigned();
	
	public void setTicketINSigned(TicketINSigned tinSigned);
	

	public String getK();

	public void setK(String k);


	public Double getFare();

	public void setFare(Double fare);
	

	
	public String getC1();

	public void setC1(String c1);
	

	
	public Date getTimestamp1();

	public void setTimestamp1(Date timestamp1);

	
	
	public Integer getDestProvider();

	public void setDestProvider(Integer destProvider);
	
	


}