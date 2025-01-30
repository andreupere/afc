package cat.uib.secom.crypto.afc.store.jpa.business.user;



import java.util.List;

import javax.persistence.Query;

import cat.uib.secom.crypto.afc.store.logic.UserAFCLogic;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.ticket.SigmaSignedImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.user.UserAFCImpl;
import cat.uib.secom.crypto.afc.store.pojo.impl.user.UserAFCJourneyImpl;
import cat.uib.secom.crypto.afc.store.pojo.ticket.Sigma;
import cat.uib.secom.crypto.afc.store.pojo.ticket.SigmaSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketINSigned;
import cat.uib.secom.crypto.afc.store.pojo.ticket.TicketOUTSigned;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFC;
import cat.uib.secom.crypto.afc.store.pojo.user.UserAFCJourney;
import cat.uib.secom.crypto.sig.bbs.core.engines.BBSEngine;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSGroupPublicKey;
import cat.uib.secom.crypto.sig.bbs.core.impl.keys.BBSUserPrivateKey;
import cat.uib.secom.crypto.sig.bbs.core.parameters.BBSParameters;
import cat.uib.secom.crypto.sig.bbs.store.jpa.business.AbstractEntityManagerBean;
import cat.uib.secom.utils.strings.StringUtils;



public class UserAFCEntityManagerBean extends AbstractEntityManagerBean {

	private final static String PU_NAME = "userAFC-PU";
	
	private UserAFC userAFC;
	private UserAFCJourney currentJourney;
	
	
	public UserAFCEntityManagerBean() {
		super(PU_NAME);
	}
	public UserAFCEntityManagerBean(String persistenceUnitName) {
		super(persistenceUnitName);
	}
	
	
	
	public void deployUser(UserAFC user) {
		
		super.preparePersistence();
		
		super.getEntityManager().persist(user);
		
		super.getEntityTransaction().commit();
		super.closePersistence();
		
	}	
	
	
	
	public SigmaSigned startAFCJourney() throws Exception {
		
		UserAFCJourney journey = new UserAFCJourneyImpl();
		UserAFCImpl user = new UserAFCImpl();
		Sigma sigma = new SigmaImpl();
		SigmaSigned sigmaSigned = new SigmaSignedImpl();
		
		iam(new Long(1)); // only one user
		
		BBSParameters bbsParameters = new BBSParameters(super.getCurveFileName());
		BBSGroupPublicKey bbsgpk = new BBSGroupPublicKey( StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getG1() ),
														  StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getG2() ),
														  StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getH() ),
														  StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getU() ),
														  StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getV() ),
														  StringUtils.hexStringToByteArray( userAFC.getGroupPublicKey().getOmega() ),
														  bbsParameters.getPairing());
		
		BBSUserPrivateKey bbsusk = new BBSUserPrivateKey( StringUtils.hexStringToByteArray( userAFC.getUserPrivateKey().getAi() ), 
														  StringUtils.hexStringToByteArray( userAFC.getUserPrivateKey().getXi() ),
														  bbsParameters.getPairing());
		
		sigmaSigned = UserAFCLogic.openAFCJourney(journey, user, sigma, sigmaSigned, bbsgpk, bbsusk, bbsParameters.getPairing(), null, new BBSEngine(bbsgpk, bbsusk) );
		
		// save current journey, saving r1
		super.preparePersistence();
		super.getEntityManager().persist(journey);		
		super.getEntityTransaction().commit();
		

		
		// save sigmasigned
		super.getEntityManager().persist(sigmaSigned);
		super.getEntityTransaction().commit();
		
		super.closePersistence();
		
		return sigmaSigned;
		
	}
	
	
	public void storeGroupSignatureSchemeData(cat.uib.secom.crypto.sig.bbs.store.entities.BBSGroupPublicKey gpk, 
											  cat.uib.secom.crypto.sig.bbs.store.entities.BBSUserPrivateKey usk, 
											  Long userId){
		
		super.preparePersistence();
		UserAFC user = super.getEntityManager().find(UserAFCImpl.class, userId);
		
		user.setGroupPublicKey(gpk);
		user.setUserPrivateKey(usk);
	
		
		super.getEntityManager().persist(user);
		super.getEntityTransaction().commit();
		
		super.closePersistence();
		
	}
	
	
	public UserAFCJourney storeTicketINSigned(TicketINSigned ticketINSigned) {
		
		getCurrentJourney().setYu(iam().getYu());
		getCurrentJourney().setTicketINSigned(ticketINSigned);
		getCurrentJourney().setTicketOUTSigned(null);
		
		currentJourney = super.getEntityManager().merge( getCurrentJourney() );
		
		super.getEntityTransaction().commit();
		
		// PROVAR SI AQUI VE AMB ID DE DATABASE !!!!!
		return getCurrentJourney();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public UserAFCJourney storeTicketOUTSigned(TicketOUTSigned ticketOUTSigned) throws Exception {
		
		UserAFCJourney journey = null;
		Integer ticketSN = ticketOUTSigned.getTicketOUT().getSerialNumber();
		
		Query q = getEntityManager().createNamedQuery("searchTicketSerialNumber");
		q.setParameter("serialNumber", ticketSN);
		List<UserAFCJourney> list = q.getResultList();
		if (list.size() == 1) {
			 journey = (UserAFCJourney) list.get(0);
		} else {
			throw new Exception("More than one journey with the same ticket Serial Number");
		}
		
		journey.setTicketOUTSigned(ticketOUTSigned);
		
		journey = getEntityManager().merge(journey);
		
		getEntityTransaction().commit();
		
		
		return journey;
	}
	
	
	
	
	
	
	public UserAFC iam(Long id) {
		UserAFCImpl u = getEntityManager().find(UserAFCImpl.class, id);
		return u;
	}
	
	
	public UserAFC iam() {
		if (userAFC == null) {
			userAFC = iam(new Long(1));
		}
		return userAFC;
	}


	public UserAFCJourney getCurrentJourney() {
		if (currentJourney == null)
			currentJourney = new UserAFCJourneyImpl();
		return currentJourney;
	}
	
	public void retrieveJourney(Integer ticketSerialNumber) {
		
		Query q = getEntityManager().createNamedQuery("searchTicketSerialNumber");
		q.setParameter("serialNumber", ticketSerialNumber);
		currentJourney = (UserAFCJourneyImpl) q.getSingleResult(); // throws an exception more than one result or something like this ????
	}
	
}
