package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUT;



/**
 * Definition of Tout* = ( Tout, Sign<a>(Tout) )
 * */
@Embeddable
public class TicketOUTImpl implements TicketOUT {

	@Column(name="TOUT_SERIAL_NUMBER")
	private Integer serialNumber;
	
	@Transient
	private byte[] sn;
	
	
	
	@Column(name="TOUT_DESTINATION_STATION")
	private Integer dstProvider;
	
	@Transient
	private byte[] dp;
	
	
	
	@Column(name="TOUT_FARE")
	private Double fare;
	
	@Column(name="TOUT_MESSAGE")
	private String str;
	


	
	
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public byte[] getSn() {
		return sn;
	}
	public void setSn(byte[] sn) {
		this.sn = sn;
	}

	
	
	public Integer getDstProvider() {
		return dstProvider;
	}
	public void setDstProvider(Integer dstProvider) {
		this.dstProvider = dstProvider;
	}

	
	
	public byte[] getDp() {
		return dp;
	}
	public void setDp(byte[] dp) {
		this.dp = dp;
	}

	
	
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}

	
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
	
	
	
}
