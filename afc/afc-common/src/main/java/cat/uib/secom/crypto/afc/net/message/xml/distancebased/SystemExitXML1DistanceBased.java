package cat.uib.secom.crypto.afc.net.message.xml.distancebased;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSGroupPublicKeyBean;
import cat.uib.secom.crypto.sig.bbs.store.entities.bean.BBSSignatureBean;

@Root
public class SystemExitXML1DistanceBased {

	@Element(name="ticket-in-signed")
	protected TicketINSignedBean ticketINSignedBean;
	
	@Element
	protected BBSSignatureBean signature;
	
	@Element(name="group-public-key")
	protected BBSGroupPublicKeyBean groupPublicKey;

	
	
	
	
	public TicketINSignedBean getTicketINSignedBean() {
		return ticketINSignedBean;
	}

	public BBSSignatureBean getSignature() {
		return signature;
	}

	public BBSGroupPublicKeyBean getGroupPublicKey() {
		return groupPublicKey;
	}

	public void setTicketINSignedBean(TicketINSignedBean ticketINSignedBean) {
		this.ticketINSignedBean = ticketINSignedBean;
	}

	public void setSignature(BBSSignatureBean signature) {
		this.signature = signature;
	}

	public void setGroupPublicKey(BBSGroupPublicKeyBean groupPublicKey) {
		this.groupPublicKey = groupPublicKey;
	}
	
	
	
	
}
