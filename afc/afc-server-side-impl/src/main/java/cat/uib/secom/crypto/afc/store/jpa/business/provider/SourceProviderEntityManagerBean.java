package cat.uib.secom.crypto.afc.store.jpa.business.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unisa.dia.gas.jpbc.Pairing;




import cat.uib.secom.crypto.afc.store.logic.SourceProviderLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.provider.StationTransactionImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.TicketINImpl;
import cat.uib.secom.crypto.afc.store.pojo.provider.Station;
import cat.uib.secom.crypto.afc.store.pojo.provider.StationTransaction;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngine;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.AbstractEntityManagerBean;

import cat.uib.secom.utils.strings.StringUtils;






public class SourceProviderEntityManagerBean extends AbstractEntityManagerBean {

	private static Logger _logger = LoggerFactory.getLogger(SourceProviderEntityManagerBean.class);

	
	private final static String PU_NAME = "providers-PU";
	
	
	private Station station;
	private String xmlConfig;
	private Long stationId;
	
	
	public SourceProviderEntityManagerBean(Long stationId) {
		super(PU_NAME);
		this.stationId = stationId;
	}
	
	
	public boolean verifySigmaSigned(SigmaSigned sigmaSigned, Pairing pairing) throws Exception {
		boolean result = false;
		_logger.debug("verifySigmaSigned1 " + sigmaSigned.getEmbeddedGroupPublicKey());
		_logger.debug("verifySigmaSigned2 " + sigmaSigned.getEmbeddedGroupPublicKey().getG1());
		cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey coreBBSgpk = 
			new cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey( StringUtils.hexStringToByteArray( sigmaSigned.getEmbeddedGroupPublicKey().getG1() ),
																			   StringUtils.hexStringToByteArray( sigmaSigned.getEmbeddedGroupPublicKey().getG2() ),
																			   StringUtils.hexStringToByteArray( sigmaSigned.getEmbeddedGroupPublicKey().getH() ),
																			   StringUtils.hexStringToByteArray( sigmaSigned.getEmbeddedGroupPublicKey().getU() ),
																			   StringUtils.hexStringToByteArray( sigmaSigned.getEmbeddedGroupPublicKey().getV() ),
																			   StringUtils.hexStringToByteArray(sigmaSigned.getEmbeddedGroupPublicKey().getOmega() ),
																			   pairing);
		cat.uib.secom.crypto.sig.bbs.core.signature.Signature coreSignature =
			new cat.uib.secom.crypto.sig.bbs.core.impl.signature.Signature( StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getT1() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getT2() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getT3() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getC() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getSalpha() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getSbeta() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getSx() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getSdelta1() ),
																			StringUtils.hexStringToByteArray( sigmaSigned.getSignature().getSdelta2() ),
																			pairing);
		
		SourceProviderLogic.verifySigmaSigned(sigmaSigned.getSigma(), 
											  coreSignature, 
											  new BBSEngine(coreBBSgpk));
		
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public TicketIN composeTicketIN(SigmaSigned sigmaSigned, Integer validityTime) {
		

		// since the serialNumber must be unique, and it is defined in the database as Primary Key (unique)
		// we can store a StationTransaction and use the generated ID as the ticket serial number
		StationTransaction stx = new StationTransactionImpl();
		station = iam( stationId );
		SourceProviderLogic.newStationTrasaction(stx, station);
		
		super.preparePersistence();
		super.getEntityManager().persist(stx);
		super.getEntityTransaction().commit();
		
		TicketIN t = new TicketINImpl();
		SourceProviderLogic.createTicketIN(t, sigmaSigned, station, new Integer( stx.getId().intValue() ), validityTime );
		
		super.closePersistence();
	
		return t;
	}
	
	 
	
	// NOT USED FOR PROVIDERS NOW
	public void storeTicketINSigned(TicketINSigned ticket, String signature) throws Exception {
		throw new Exception("Not implemented yet...!");
		
		/*ticket.setSignature(signature);
		getEntityManager().persist(ticket);
		getEntityTransaction().commit();*/
		
	}
	
	
	
	
	public Station iam(Long id) {
		if (station == null)
			station = super.getEntityManager().find(StationImpl.class, id);
		return station;
	}
	
	public Station iam() {
		if (station == null) 
			station = iam(new Long(1));
		return station;
	}


	
	public void setXmlConfig(String xmlConfig) {
		this.xmlConfig = xmlConfig;
	}


	public String getXmlConfig() {
		return xmlConfig;
	}
	

}
