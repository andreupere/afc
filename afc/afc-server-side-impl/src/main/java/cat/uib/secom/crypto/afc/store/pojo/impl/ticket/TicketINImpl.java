package cat.uib.secom.crypto.afc.store.pojo.impl.ticket;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;


/**
 * Definition of Tin* = ( Tin, Sign<a>(Tin) )
 * */
@Embeddable
public class TicketINImpl implements TicketIN {

	
	@Column(name="TIN_SERIAL_NUMBER")
	private Integer serialNumber;
	@Transient
	private byte[] sn;
	
	
	@Column(name="TIN_SOURCE_STATION")
	private Integer srcProvider;
	@Transient
	private byte[] sp;
	
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="TIN_TAU_1")
	private Date timestamp1;
	
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="TIN_VALIDITY_TIME")
	private Date validityTime;
	
	@Embedded
	private SigmaSignedImpl sigmaSigned;
	
	
	



	
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


	public Integer getSrcProvider() {
		return srcProvider;
	}
	public void setSrcProvider(Integer srcProvider) {
		this.srcProvider = srcProvider;
	}


	public byte[] getSp() {
		return sp;
	}
	public void setSp(byte[] sp) {
		this.sp = sp;
	}


	public Date getTimestamp1() {
		return timestamp1;
	}
	public void setTimestamp1(Date timestamp1) {
		this.timestamp1 = timestamp1;
	}


	public Date getValidityTime() {
		return validityTime;
	}
	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}


	public SigmaSigned getSigmaSigned() {
		return sigmaSigned;
	}
	public void setSigmaSigned(SigmaSigned sigmaSigned) {
		this.sigmaSigned = (SigmaSignedImpl) sigmaSigned;
	}
	
	
	
	
	
	public TicketIN deserialize(String in) {
		String[] p = in.split(",");
		TicketINImpl t = new TicketINImpl();
		SigmaSignedImpl ssi = new SigmaSignedImpl();
		
		t.serialNumber = new Integer( p[0] );
		t.srcProvider = new Integer( p[1] );
		t.validityTime = new Date( new Long(p[2] ) );
		t.timestamp1 = new Date( new Long(p[3]));
		t.sigmaSigned = (SigmaSignedImpl) ssi.deserialize(p[4]);
		
		return t;
	}
	public String serialize() {
		String out = serialNumber + "," +
		 			 srcProvider + "," +
		 			 validityTime.getTime() + "," +
		 			 timestamp1.getTime() + "," +
		 			 sigmaSigned.serialize();
		return out;
	}
	
	
//	public void setR1(BigInteger r1) {
//		this.r1 = r1;
//	}
//	public BigInteger getR1() {
//		return r1;
//	}
//	public void set_r1(byte[] _r1) {
//		this._r1 = _r1;
//	}
//	public byte[] get_r1() {
//		return _r1;
//	}


	
//	public byte[] getSignature() {
//		return signature;
//	}
//	public void setSignature(byte[] signature) {
//		this.signature = signature;
//	}
	
}
