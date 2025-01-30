package cat.uib.secom.crypto.afc.store.logic;

import java.util.Date;

import cat.uib.secom.crypto.afc.bean.GammaUserBean;
import cat.uib.secom.crypto.afc.net.message.xml.UserChargedOKBeanXML;
import cat.uib.secom.crypto.afc.net.message.xml.UserNotChargedKOBeanXML;

public class DestinationProviderLogic {

	public static Double computeFare(Integer srcStationId, Integer dstStationId, Date in, Date exit) {
		System.out.println("computing fare src=" + srcStationId + " dst=" + dstStationId);
		if (dstStationId.equals(1) )
			return new Double(5.3);
		else if (dstStationId.equals(2) )
			return new Double(7.2);
		else if (dstStationId.equals(3) )
			return new Double(6.7);
		else if (dstStationId.equals(4) )
			return new Double(8.1);
		else
			return new Double(3.6);
	}
	
	
	public static UserChargedOKBeanXML composeOK(Integer serialNumber, Double fare) {
		UserChargedOKBeanXML ok = new UserChargedOKBeanXML();
		ok.setFare(fare);
		ok.setSerialNumber(serialNumber);
		ok.setMessage("ok");
		
		return ok;
	}
	
	public static UserNotChargedKOBeanXML composeKO(GammaUserBean gammaUser, Double balance) {
		UserNotChargedKOBeanXML ko = new UserNotChargedKOBeanXML();
		ko.setGammaUser(gammaUser);
		ko.setMessage("error: balance=" + balance + "; fare=" + gammaUser.getFare());
		return ko;
	}
	
}
