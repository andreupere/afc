package cat.uib.secom.crypto.afc.android.pojo;

import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;

public class UserAFCJourneyAndroid implements UserAFCJourney {

	private Long id;
	private String k;
	private String r1;
	private TicketINSigned tinSigned;
	private TicketOUTSigned toutSigned;
	private String yu;
	
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getK() {
		return k;
	}

	@Override
	public String getR1() {
		return r1;
	}

	@Override
	public TicketINSigned getTicketINSigned() {
		return tinSigned;
	}

	@Override
	public TicketOUTSigned getTicketOUTSigned() {
		return toutSigned;
	}

	@Override
	public String getYu() {
		return yu;
	}

	@Override
	public void setId(Long arg0) {
		this.id = arg0;
	}

	@Override
	public void setK(String arg0) {
		this.k = arg0;
	}

	@Override
	public void setR1(String arg0) {
		this.r1 = arg0;
	}

	@Override
	public void setTicketINSigned(TicketINSigned arg0) {
		this.tinSigned = arg0;
	}

	@Override
	public void setTicketOUTSigned(TicketOUTSigned arg0) {
		this.toutSigned = arg0;
	}

	@Override
	public void setYu(String arg0) {
		this.yu = arg0;
	}

	 
	
}
