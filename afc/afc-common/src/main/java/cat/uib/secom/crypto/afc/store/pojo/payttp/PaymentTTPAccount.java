package cat.uib.secom.crypto.afc.store.pojo.payttp;


import java.util.Set;



public interface PaymentTTPAccount {


	
	
	public Long getId();
	
	public void setId(Long id);

	
	
	public String getYu();
	
	public void setYu(String yu);
	
	/**
	 * @deprecated 
	 * */
	public void setHashedYu(String hashedYu);
	
	/**
	 * @deprecated 
	 * */
	public String getHashedYu();
	
	

	public Double getBalance();
	
	public void setBalance(Double balance);
	
	
	/*public void setPayTransactions(Set<PaymentTTPTransaction> payTransactions);
	 * 
	public Set<PaymentTTPTransaction> getPayTransactions();*/
	
	
	
	
	
	
	
}
