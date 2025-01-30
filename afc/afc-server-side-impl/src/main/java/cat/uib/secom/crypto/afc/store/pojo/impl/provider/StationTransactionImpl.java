package cat.uib.secom.crypto.afc.store.pojo.impl.provider;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import cat.uib.secom.crypto.afc.store.pojo.provider.Station;
import cat.uib.secom.crypto.afc.store.pojo.provider.StationTransaction;


/**
 * @author Andreu Pere
 * 
 * This class stores a transaction. At the source station side, the serialNumber will be filled
 * together with the date when it was issued. At the destination station side, the used field
 * will be filled with true. It also stores the station ID who makes this transaction.
 * 
 */
@Entity
@Table(name="PROVIDER_TRANSACTION")
@NamedQueries({
	@NamedQuery(name="isSerialNumberIssued",
				query="SELECT tx FROM StationTransactionImpl tx WHERE id = :serialNumber"),
	@NamedQuery(name="isSerialNumberIssuedAndNotUsed",
				query="SELECT tx FROM StationTransactionImpl tx WHERE id= :serialNumber AND used = false")
	})
public class StationTransactionImpl implements StationTransaction {

	/**
	 * All the issued serial numbers. This will be a PK field, so database lookups will be fast
	 * */
	@Id
	@GeneratedValue
	@Column(name="SERIAL_NUMBER")
	private Long id;
	
	/**
	 * When this serialNumber was issued
	 * */
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="DATE_ISSUED")
	private Date whenIssued;
	
	/**
	 * This field marks if a ticket serial number was used. This field should be true
	 * when destination provider sends the output ticket to the user. Otherwise, it should be false.
	 * It is, if will be false while the user is driving through the system and true when 
	 * the user leave the system.
	 * Will be marked as used (true) when the user exits the system. While the user drives through the system
	 * this variable will be false
	 * */
	@Column(name="IS_USED")
	private boolean used;
	
	
	/**
	 * The station relation to this row. It is, the station who made this transaction.
	 * */
	@ManyToOne
	@JoinColumn(name="PROVIDER_STATION_ID_FK")
	private StationImpl station;
	
	
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}

	
	public void setWhenIssued(Date when) {
		this.whenIssued = when;
	}
	public Date getWhenIssued() {
		return whenIssued;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public boolean isUsed() {
		return used;
	}
	public void setStation(Station station) {
		this.station = (StationImpl) station;
	}
	public Station getStation() {
		return station;
	}
	
}