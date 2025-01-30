package cat.uib.secom.crypto.afc.store.jpa.business.provider;

import java.util.Date;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import cat.uib.secom.crypto.afc.store.logic.SourceProviderLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationTransactionImpl;
import cat.uib.secom.crypto.afc.store.pojo.provider.Station;
import cat.uib.secom.crypto.afc.store.pojo.provider.StationTransaction;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.AbstractEntityManagerBean;



public class DestinationProviderEntityManagerBean extends AbstractEntityManagerBean {

	private final static String PU_NAME = "providers-PU";
	
	private Station station;
	
	public DestinationProviderEntityManagerBean() {
		super(PU_NAME);
	}
	
	
	public Station iam(Long id) {
		if (station == null)
			station = getEntityManager().find(StationImpl.class, id);
		return station;
	}
	
	public Station iam() {
		if (station == null) 
			station = iam(new Long(2));
		return station;
	}
	
	
	public boolean isSerialNumberUsed(Integer serialNumber) {
		
		Query q = getEntityManager().createNamedQuery("isSerialNumberIssuedAndNotUsed");
		q.setParameter("serialNumber", new Long(serialNumber.longValue()));
		
		try {
			q.getSingleResult();
			return false;
		} catch ( NonUniqueResultException e ) {
			return true;
		}
	}
	
	
	public void newTransaction(StationTransaction stx, Station station, Integer stationId) {
		
		station = iam( new Long( stationId.longValue() ) );
		
		SourceProviderLogic.newStationTrasaction(stx, station);
		
		super.preparePersistence();
		super.getEntityManager().persist(stx);
		super.getEntityTransaction().commit();
		super.closePersistence();
	}
	
	
}
