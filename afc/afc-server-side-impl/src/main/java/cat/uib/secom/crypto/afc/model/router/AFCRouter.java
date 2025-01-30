package cat.uib.secom.crypto.afc.model.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.net.message.xml.ErrorSignedXML;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.user.standalone.UserAFCClientStandalone;

/**
 * This class routes messages received from infrastructure and manages the corresponding response to the infrastructure
 * 
 * @author Andreu Pere
 * 
 * */
public class AFCRouter {

	private static Logger _logger = LoggerFactory.getLogger(AFCRouter.class);
	
	
	public AFCRouter() {}
	
	
	
	/**
	 * It calls the underlying system registration (group TTP and payment TTP) flow
	 * It obtains a new user if it is not deployed yet and a pair of BBS keys
	 * */
	public static void userSystemRegistration(String idRobot) {
		UserAFCClientStandalone uAFCClient = new UserAFCClientStandalone(idRobot);
		
		UserAFC uAFC = uAFCClient.userRegistrationGroupTTP(idRobot);  // returns an instance to the deployed user. uAFC contains all the data about current user
		uAFCClient.userRegistrationPayTTP(idRobot);  // returns nothing
		
		_logger.info("[idRobot=" + idRobot + "] successfully registered to the AFC system. Assigned yu=" + uAFC.getYu());
	}
	
	
	/**
	 * It calls the system entrance flow
	 * It obtains an entrance ticket (ticketIN)
	 * */
	public static TicketINSignedBean systemEntrance(String idRobot, String idDoor) {
		UserAFCClientStandalone uAFCClient = new UserAFCClientStandalone(idRobot);
		boolean timeBased = true;
		TicketINSignedBean ticketIN = uAFCClient.systemEntrance(idRobot, idDoor, timeBased);
		
		_logger.info("[idRobot=" + idRobot + "] has obtained the entrance ticket (sn=" + ticketIN.getTicketIN().getSerialNumber() + 
					  "; entranceId=" + ticketIN.getTicketIN().getSrcProvider() + ")");
		return ticketIN;
	}
	
	
	/**
	 * It calls the system exit flow
	 * It obtains an exit ticket (ticketOUT)
	 * */
	public static RouterHelper systemExit(String idRobot, String idDoor, Integer ticketSerialNumber) { // potser que ticketSerialNumber no sigui necessari
		UserAFCClientStandalone uAFCClient = new UserAFCClientStandalone(idRobot);
		boolean timeBased = true;
		RouterHelper response = uAFCClient.systemExit(idRobot, idDoor, ticketSerialNumber, timeBased);
		_logger.info("[idRobot=" + idRobot + "] Can she exit the system? " + response.isResult());
		
		if (response.isResult()) {
			TicketOUTSignedBean ticketOUT = (TicketOUTSignedBean) response.getResponse();
			_logger.info("[idRobot=" + idRobot + "] has obtained the exit ticket and she can exit system (sn=" + ticketOUT.getTicketOUT().getSerialNumber() + "; " + 
						 "fare=" + ticketOUT.getTicketOUT().getFare() + "; " +
						 "exitId=" + ticketOUT.getTicketOUT().getDstProvider() + ")");
		}
		else {
			ErrorSignedXML error = (ErrorSignedXML) response.getResponse();
			_logger.info("[idRobot=" + idRobot + "] cannot exit the system due: " + error.getError().getMessage());
		}
		
		return response;
	
	}
		
		
	
	
	
	
	
	
}
