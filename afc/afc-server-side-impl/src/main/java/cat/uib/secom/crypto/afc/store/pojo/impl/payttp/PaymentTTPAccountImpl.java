package cat.uib.secom.crypto.afc.store.pojo.impl.payttp;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPAccount;
import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPTransaction;


@Entity
@Table(name="PAY_TTP_ACCOUNT")
@NamedQueries({
@NamedQuery(name="searchPseudonym",
		query="SELECT a FROM PaymentTTPAccountImpl a WHERE a.hashedYu = :pseudonym")
})
public class PaymentTTPAccountImpl implements PaymentTTPAccount {

	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	

	
	@Column(name="USER_YU")
	private String yu;
	
	/**
	 * In order to search Yu in the data base, we build a hash of original yu
	 * */
	@Column(name="USER_YU_HASH")
	private String hashedYu;
	
	@Column(name="CURRENT_BALANCE")
	private Double balance;
	
	
	@OneToMany(targetEntity=cat.uib.secom.crypto.afc.store.pojo.impl.payttp.PaymentTTPTransactionImpl.class,
			mappedBy="paymentAccount")
	private Set<PaymentTTPTransaction> payTransactions;

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public String getYu() {
		return yu;
	}
	public void setYu(String yu) {
		this.yu = yu;
	}
	
	
	

	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public void setPayTransactions(Set<PaymentTTPTransaction> payTransactions) {
		this.payTransactions = payTransactions;
	}
	public Set<PaymentTTPTransaction> getPayTransactions() {
		return payTransactions;
	}
	public void setHashedYu(String hashedYu) {
		this.hashedYu = hashedYu;
	}
	public String getHashedYu() {
		return hashedYu;
	}
	
	
	public String toString() {
		return "PaymentTTPAccountImpl: { id=" + this.getId() + "; yu(hex)=" + this.getHashedYu() + "; balance(after)=" + this.getBalance() + " € }";
	}
	
	
	
}
