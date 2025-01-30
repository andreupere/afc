package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SymmetricKeyIvXML {

	@Element
	protected byte[] key;
	
	@Element
	protected byte[] iv;

	public byte[] getKey() {
		return key;
	}

	
	public byte[] getIv() {
		return iv;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
	
	
	
}
