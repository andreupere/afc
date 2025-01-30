package cat.uib.secom.crypto.afc.store.jpa.business.payttp;

public class NotEnoughBalanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8724505675516661886L;
	
	
	public Double balanceBefore;
	public Double fare;
	
	public NotEnoughBalanceException(String str) {
		super(str);
	}
	
	public NotEnoughBalanceException(String str, Double balanceBefore, Double fare) {
		super(str);
		this.fare = fare;
		this.balanceBefore = balanceBefore;
	}

	
	
	public Double getBalanceBefore() {
		return balanceBefore;
	}

	public Double getFare() {
		return fare;
	}

	public void setBalanceBefore(Double balanceBefore) {
		this.balanceBefore = balanceBefore;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}
}
