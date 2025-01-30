package cat.uib.secom.crypto.afc.net.message;

public interface NetworkMessage {

	public String serialize();
	
	public NetworkMessage desearialize(String in);
	
}
