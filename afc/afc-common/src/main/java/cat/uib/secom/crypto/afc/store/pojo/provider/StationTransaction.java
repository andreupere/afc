package cat.uib.secom.crypto.afc.store.pojo.provider;



import java.util.Date;


/**
 * 
 * TODO: docs
 * 
 * @author Andreu Pere
 * 
 */
public interface StationTransaction {
	
	
	
	public void setId(Long id);
	public Long getId();
	

	public void setWhenIssued(Date when);
	public Date getWhenIssued();
	
	
	public void setUsed(boolean used);
	public boolean isUsed();
	
	
	public void setStation(Station station);
	public Station getStation();
	
	
}