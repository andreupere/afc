package cat.uib.secom.crypto.afc.store.pojo.payttp;


import java.util.Date;


public interface PaymentTTPTransaction {

	
	public Long getId();
	
	public void setId(Long id);

	
	
	
	public String getYu();
	
	public void setYu(String yu);

	
	
	
	public Date getDate();
	
	public void setDate(Date date);


	
	
	public Double getCost();
	
	public void setCost(Double cost);

	
	
	public Double getBalanceBefore();
	
	public void setBalanceBefore(Double balanceBefore);

	
	
	public Double getBalanceAfter();
	
	public void setBalanceAfter(Double balanceAfter);
	
	
	
	public void setPaymentAccount(PaymentTTPAccount paymentAccount);
	
	public PaymentTTPAccount getPaymentAccount();
	
	
	
}
