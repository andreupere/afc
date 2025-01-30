package cat.uib.secom.crypto.afc.bean;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;

@Root(name="ticket-in")
public class TicketINBean implements TicketIN {

	@Element(name="serial-number")
	protected Integer serialNumber;
	
	@Element(name="sigma-signed")
	protected SigmaSignedBean sigmaSigned;
	
	@Element(name="source-provider")
	protected Integer srcProvider;
	
	
	protected Date timestamp1;
	
	
	protected Date validityTime;
	
	@Element
	protected Long tau1;
	
	@Element
	protected Long validity;
	
	
	public Integer getSerialNumber() {
		return serialNumber;
	}

	public SigmaSigned getSigmaSigned() {
		return sigmaSigned;
	}

	public Integer getSrcProvider() {
		return srcProvider;
	}

	public Date getTimestamp1() {
		timestamp1 = new Date( tau1 );
		return timestamp1;
	}

	public Date getValidityTime() {
		validityTime = new Date( validity );
		return validityTime;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSigmaSigned(SigmaSigned sigmaSigned) {
		this.sigmaSigned = (SigmaSignedBean) sigmaSigned;
	}

	public void setSrcProvider(Integer srcProvider) {
		this.srcProvider = srcProvider;
	}

	public void setTimestamp1(Date timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	public Long getTau1() {
		return tau1;
	}

	public Long getValidity() {
		return validity;
	}

	public void setTau1(Long tau1) {
		this.tau1 = tau1;
	}

	public void setValidity(Long validity) {
		this.validity = validity;
	}

}
