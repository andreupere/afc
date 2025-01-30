package cat.uib.secom.crypto.afc.store.pojo.impl.payttp;


import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPAccount;
import cat.uib.secom.crypto.afc.store.pojo.payttp.PaymentTTPTransaction;
import cat.uib.secom.utils.strings.StringUtils;


@Entity
@Table(name="PAY_TTP_TRANSACTION")
public class PaymentTTPTransactionImpl implements PaymentTTPTransaction {

	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	
	@Column(name="USER_YU")
	private String yu;
	

	
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="DATE")
	private Date date;

	@Column(name="COST")
	private Double cost;
	
	@Column(name="BALANCE_BEFORE")
	private Double balanceBefore;
	
	@Column(name="BALANCE_AFTER")
	private Double balanceAfter;

	@ManyToOne
	@JoinColumn(name="PAY_TTP_ACCOUNT_ID_FK", nullable=false)
	private PaymentTTPAccountImpl paymentAccount;
	
	
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

	


	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}


	
	
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}

	
	
	public Double getBalanceBefore() {
		return balanceBefore;
	}
	public void setBalanceBefore(Double balanceBefore) {
		this.balanceBefore = balanceBefore;
	}

	
	
	public Double getBalanceAfter() {
		return balanceAfter;
	}
	public void setBalanceAfter(Double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	public void setPaymentAccount(PaymentTTPAccount paymentAccount) {
		this.paymentAccount = (PaymentTTPAccountImpl) paymentAccount;
	}
	public PaymentTTPAccount getPaymentAccount() {
		return paymentAccount;
	}
	
	
	public String toString() {
		return "PaymentTTPTransactionImpl: { id=" + this.getId() + "; " +
				"yu(hex)=" + this.getYu() + "; " +
				this.getPaymentAccount() +  "; " +
				"balance(before)=" + this.getBalanceBefore() + "; " +
				"cost=" + this.getCost() + "; " +
				"balance(after)=" + this.getBalanceAfter() + "; " +
				"date=" + this.getDate() + "}";
	}
	
	
	
}
