package cat.uib.secom.crypto.afc.store.pojo.provider;

import java.util.Set;





/**
 * 
 * TODO: docs
 * 
 * @author Andreu Pere
 * 
 * */
public interface Station {
	
	

	public void setId(Long id);

	public Long getId();

	
	
	
	public void setIdentification(Integer identification);

	public Integer getIdentification();

	
	
	
	public void setSource(boolean source);

	public boolean isSource();
	
	

	public void setLatitude(Double latitude);
	
	public Double getLatitude();
	

	public void setLongitude(Double longitude);
	
	public Double getLongitude();

	
	
	public void setDescription(String description);

	public String getDescription();

	
	
	
	/*public void setStationTransactions(Set<StationTransaction> stationTransactions)

	public Set<StationTransaction> getStationTransactions()*/
	
}
