package cat.uib.secom.crypto.afc.store.pojo.impl.provider;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cat.uib.secom.crypto.afc.store.pojo.provider.Station;
import cat.uib.secom.crypto.afc.store.pojo.provider.StationTransaction;


/**
 * @author Andreu Pere
 * 
 * This is the representation of each station data. It could also contains keys for testing purposes
 * if the java keystore or other method to store cryptographic keys is not used.
 * 
 * */
@Entity
@Table(name="PROVIDER_STATION")
public class StationImpl implements Station {

	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	
	/**
	 * This is the field that contains the Pd or Ps ID
	 * */
	@Column(name="PROVIDER_ID")
	private Integer identification;
	
	/**
	 * This station is source station (it should be true) or destination station (it should be false)
	 * */
	@Column(name="PROVIDER_IS_SOURCE")
	private boolean source;
	
	/**
	 * Spatial position of each station
	 * */
	@Column(name="PROVIDER_LATITUDE")
	private Double latitude;
	
	@Column(name="PROVIDER_LONGITUDE")
	private Double longitude;
	
	/**
	 * A short description can be filled here
	 * */
	@Column(name="PROVIDER_DESCRIPTION")
	private String description;
	
	
	@OneToMany(targetEntity=cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationTransactionImpl.class,
			cascade=javax.persistence.CascadeType.ALL,
			mappedBy="station")
	private Set<StationTransaction> stationTransactions;
	
	
	
	

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setIdentification(Integer identification) {
		this.identification = identification;
	}

	public Integer getIdentification() {
		return identification;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public boolean isSource() {
		return source;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	
	
	
	public void setStationTransactions(Set<StationTransaction> stationTransactions) {
		this.stationTransactions = stationTransactions;
	}

	public Set<StationTransaction> getStationTransactions() {
		return stationTransactions;
	}
	
}
