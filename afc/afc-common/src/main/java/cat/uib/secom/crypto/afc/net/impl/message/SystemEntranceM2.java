package cat.uib.secom.crypto.afc.net.impl.message;


import java.util.Date;

import cat.uib.secom.crypto.afc.net.message.NetworkMessage;


public class SystemEntranceM2 implements NetworkMessage {

	private Integer serialNumber;
	private Integer srcProvider;
	private Date validityTime;
	private Date timestamp1;
	
	private SystemEntranceM1 sigmaSigned;
	
	
	public NetworkMessage desearialize(String in) {
		String[] p = in.split(",");
		SystemEntranceM2 se2 = new SystemEntranceM2();
		se2.serialNumber = new Integer( p[0] );
		se2.srcProvider = new Integer( p[1] );
		se2.validityTime = new Date( new Long( p[2]) );
		se2.timestamp1 = new Date( new Long(p[3]) );
		SystemEntranceM1 se1 = new SystemEntranceM1();
		se2.sigmaSigned = (SystemEntranceM1) se1.desearialize(p[4] + " " +
															  p[5] + " " +
															  p[6] + " " +
															  p[7] + " " +
															  p[8] + " " +
															  p[9] + " " +
															  p[10] + " " +
															  p[11] + " " +
															  p[12] + " " +
															  p[13] + " " +
															  p[14] + " " +
															  p[15] + " " +
															  p[16] + " " +
															  p[17] + " " +
															  p[18] + " " +
															  p[19] + " " +
															  p[20] + " " +
															  p[21]);
		
		return se2;
	}

	public String serialize() {
		String out = serialNumber + "," +
					 srcProvider + "," +
					 validityTime.getTime() + "," +
					 timestamp1.getTime() + "," +
					 sigmaSigned.serialize();
		return out;
	}

	
	
	
	
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSrcProvider(Integer srcProvider) {
		this.srcProvider = srcProvider;
	}

	public Integer getSrcProvider() {
		return srcProvider;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	public Date getValidityTime() {
		return validityTime;
	}

	public void setTimestamp1(Date timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public Date getTimestamp1() {
		return timestamp1;
	}

	public void setSigmaSigned(SystemEntranceM1 sigmaSigned) {
		this.sigmaSigned = sigmaSigned;
	}

	public SystemEntranceM1 getSigmaSigned() {
		return sigmaSigned;
	}

	
	
	
	
	

}
