package es.zgames.textadventures.engine;

public class Description {
	private String locationId;
	private String name;
	private String description;
	private String auxiliaryDescription;
	private String returnObject;
	private String objectReturned;
	
	public Description(){
		this.locationId = "";
		this.name = "";
		this.description = "";
		this.auxiliaryDescription = "";
		this.returnObject = "";
		this.objectReturned = "";
	}
	
	public String getObjectReturned() {
		return objectReturned;
	}

	public void setObjectReturned(String objectReturned) {
		this.objectReturned = objectReturned;
	}

	public String getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(String returnObject) {
		this.returnObject = returnObject;
	}

	public String getLocationId() {
		return locationId;
	}
	
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAuxiliaryDescription() {
		return auxiliaryDescription;
	}
	public void setAuxiliaryDescription(String description) {
		this.auxiliaryDescription = description;
	}
	
}
