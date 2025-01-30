package cat.uib.secom.crypto.afc.integration.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.bean.TicketOUTSignedBean;
import cat.uib.secom.crypto.afc.integration.messages.EntranceRequestXML;
import cat.uib.secom.crypto.afc.integration.messages.EntranceResponseXML;
import cat.uib.secom.crypto.afc.integration.messages.ExitRequestXML;
import cat.uib.secom.crypto.afc.integration.messages.ExitResponseXML;
import cat.uib.secom.crypto.afc.model.router.AFCRouter;
import cat.uib.secom.crypto.afc.model.router.RouterHelper;
import cat.uib.secom.crypto.afc.net.message.xml.ErrorSignedXML;
import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.utils.net.socket.Client;

public class ServerVMThread extends Thread {
	
	private Socket _s;
	
	public ServerVMThread(Socket s, GeneralConfigXML gConfig) {
		super("Integration Server");
		_s = s;
	}

	public void run() {
		System.out.println("Running client...");
		
		Client client = new Client(_s);
		try {
			Reader r = client.prepareInput();
			Writer w = client.prepareOutput();
			Serializer s = new Persister();
			
			// receive message
			EntranceRequestXML enReq = null;
			ExitRequestXML exReq = null;
			String all = "";
			try {
				
				BufferedReader br = new BufferedReader( client.getReader() );
    			
    			String l = "";
    			while (  !( l = br.readLine() ).contains("\0") ) {
        			all += l;
        		}
        		all += l;
				
				enReq = s.read(EntranceRequestXML.class, all);
				// entrance message received
				TicketINSignedBean tins = AFCRouter.systemEntrance(enReq.getIdRobot(), enReq.getIdDoor());
				
				// sends response
				EntranceResponseXML enRes = new EntranceResponseXML();
				enRes.setTicketSerialNumber(tins.getTicketIN().getSerialNumber());
				s.write(enRes, w);
				
			} catch(org.simpleframework.xml.core.ElementException e) {
				System.out.println("abans de read servervmthread");
				exReq = s.read(ExitRequestXML.class, all);
				System.out.println("despres de read servervmthread");
				// exit message received
				RouterHelper rh = AFCRouter.systemExit(exReq.getIdRobot(), exReq.getIdDoor(), exReq.getTicketSerialNumber());
				
				ExitResponseXML exRes = new ExitResponseXML();
				exRes.setExit(rh.isResult());
				
				TicketOUTSignedBean touts = null;
				ErrorSignedXML error = null;
				if (rh.getResponse() instanceof TicketOUTSignedBean ) {
					touts = (TicketOUTSignedBean) rh.getResponse();
					exRes.setFare(touts.getTicketOUT().getFare());
					exRes.setMessage(touts.getTicketOUT().getStr());
				}
				else if (rh.getResponse() instanceof ErrorSignedXML ) {
					error = (ErrorSignedXML) rh.getResponse();
					exRes.setMessage(error.getError().getMessage());
				} 
				else
					return;
				
				// sends respònse
				s.write(exRes, w);
			}
			
			client.closeInput();
			client.closeOutput();
			client.closeSocket();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
