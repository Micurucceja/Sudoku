package gui.model;

public enum DIFFICULTY implements java.io.Serializable {
	
	EASY("/gui/resources/easyButton.png"),
	NORMAL("/gui/resources/normalButton.png"),
	HARD("/gui/resources/hardButton.png");
	
	private String urlImageButton;

	
	private DIFFICULTY(String urlString) {
		this.urlImageButton = urlString;
	}
	
	public String getUrlImageButton() {
		return urlImageButton;
	}
	
}
