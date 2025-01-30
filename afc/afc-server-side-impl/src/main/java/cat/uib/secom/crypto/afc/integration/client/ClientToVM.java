package cat.uib.secom.crypto.afc.integration.client;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cat.uib.secom.crypto.afc.integration.messages.EntranceRequestXML;
import cat.uib.secom.crypto.afc.integration.messages.EntranceResponseXML;
import cat.uib.secom.crypto.afc.integration.messages.ExitRequestXML;
import cat.uib.secom.crypto.afc.integration.messages.ExitResponseXML;
import cat.uib.secom.utils.net.socket.Client;

public class ClientToVM {
	
	private Client _client;
	
	// TODO: to xml config file
	private static String HOST = "localhost";
	private static Integer PORT = 60000;
	
	public ClientToVM() {
		_client = new Client(HOST, PORT);
	}
	public ClientToVM(String host, Integer port) {
		_client = new Client(host, port);
	}
	
	
	/**
	 * Entrance method. It sends request to ServerVM in order to start the entrance protocol
	 * 
	 * @param idRobot 
	 * @param idDoor
	 * 
	 * @return object containing the parsed xml received from ServerVM
	 * */
	public EntranceResponseXML entrance(String idRobot, String idDoor) {
		EntranceRequestXML eReq = new EntranceRequestXML();
		eReq.setIdDoor(idDoor);
		eReq.setIdRobot(idRobot);
		
		
		Serializer s = new Persister();
		try {
			_client.createSocket();
			
			// sends entrance request to ServerVM
			Reader r = _client.prepareInput();
			Writer w = _client.prepareOutput();
			s.write(eReq, w);
			_client.write("\0");
			
			// receive entrance response from ServerVM
			EntranceResponseXML eRes = s.read(EntranceResponseXML.class, r);
			
			_client.closeInput();
			_client.closeOutput();
			_client.closeSocket();
			
			return eRes;
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	/**
	 * Exit method. It sends request to ServerVM in order to start the exit protocol
	 * 
	 * @param idRobot 
	 * @param idDoor
	 * @param ticketSerialNumber
	 * 
	 * @return object containing the parsed xml received from ServerVM
	 * */
	public ExitResponseXML exit(String idRobot, String idDoor, Integer ticketSerialNumber) {
		ExitRequestXML eReq = new ExitRequestXML();
		eReq.setIdDoor(idDoor);
		eReq.setIdRobot(idRobot);
		eReq.setTicketSerialNumber(ticketSerialNumber);
		
		Serializer s = new Persister();
		try {
			_client.createSocket();
			
			// sends exit request to ServerVM
			Reader r = _client.prepareInput();
			Writer w = _client.prepareOutput();
			s.write(eReq, w);
			_client.write("\0");
			System.out.println("enviada peticio");
			// receive exit response from ServerVM
			ExitResponseXML eRes = s.read(ExitResponseXML.class, r);
			
			_client.closeInput();
			_client.closeOutput();
			_client.closeSocket();
			
			return eRes;
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Requires 2 arguments: HOST PORT");
			return;
		}
		String host = args[0];
		int port = new Integer( args[1] );
		ClientToVM c = new ClientToVM(host, port);
		String idRobot = "robot1";
		String idDoorEntrance = "1";
		String idDoorExit = "4";
		EntranceResponseXML enRes = c.entrance(idRobot, idDoorEntrance);
		System.out.println("System Entrance: (idRobot:" + idRobot + ", idDoorIn: " + idDoorEntrance + "); " +
						   "ticket: " + enRes.getTicketSerialNumber() );
		c = new ClientToVM(host, port);
		ExitResponseXML exRes = c.exit(idRobot, idDoorExit, enRes.getTicketSerialNumber());
		//ExitResponseXML exRes = c.exit("1", "4", 944);
		System.out.println("System Exit: (idRobot: " + idRobot + ", idDoorExit: " + idDoorExit + "); " +
						   "fare: " + exRes.getFare() + "; message: " + exRes.getMessage() + "; exit? " + exRes.isExit());

	}
	
	

}
