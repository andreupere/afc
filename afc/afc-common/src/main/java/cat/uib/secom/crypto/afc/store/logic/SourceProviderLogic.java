package cat.uib.secom.crypto.afc.store.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



import cat.uib.secom.crypto.afc.resource.StaticParameters;
import cat.uib.secom.crypto.afc.store.pojo.provider.Station;
import cat.uib.secom.crypto.afc.store.pojo.provider.StationTransaction;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketIN;
import cat.uib.secom.crypto.sig.bbs.core.accessors.VerifierAccessor;
import cat.uib.secom.crypto.sig.bbs.core.engines.AbstractBBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.signature.Signature;

public class SourceProviderLogic {

	public static boolean verifySigmaSigned(Sigma sigma, Signature signature, AbstractBBSEngine engine) {		
		
		boolean result = engine.verify(signature, sigma.serialize());
		
//		VerifierAccessor v = new VerifierAccessor();
//		
//		v.setMessage(sigma.serialize());
//		v.setSignature(signature);
//		boolean result = v.verify(gpk);
		return result;
	}
	
	
	
	
	public static TicketIN createTicketIN(TicketIN ticketIN, 
										  SigmaSigned sigmaSigned, 
										  Station station, 
										  Integer newGeneratedSerialNumber, 
										  Integer validityTime) {
		
		
		ticketIN.setSerialNumber( newGeneratedSerialNumber );
		ticketIN.setSrcProvider( station.getIdentification() );
		Date d = new Date(); //now
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		ticketIN.setTimestamp1(c.getTime());
		
		
		//Long now = new Long( d.getTime() ); //now in milliseconds
		//d.setTime( d.getTime() + StaticParameters.VALIDITY_TIME); // now + VALIDITY_TIME
		
		
		c.add(Calendar.MILLISECOND, validityTime);
		ticketIN.setValidityTime(c.getTime());
		ticketIN.setSigmaSigned(sigmaSigned);
		
		return ticketIN;
	
	}
	
	public static StationTransaction newStationTrasaction(StationTransaction stx, Station station) {
		stx.setStation(station);
		stx.setUsed(false);
		stx.setWhenIssued(new Date());
		return stx;
	}
	
	
	
	
}
