package cat.uib.secom.crypto.afc.model.router;

public class RouterHelper {

	private boolean result;
	
	private Object response;

	
	public RouterHelper(boolean result, Object response) {
		this.result = result;
		this.response = response;
	}
	
	
	public boolean isResult() {
		return result;
	}

	public Object getResponse() {
		return response;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
}
