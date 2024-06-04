package es.zgames.textadventures.engine;

import java.util.HashMap;

public class Behaviour {
	private String behaviourId;
	private String name;
	private String moveActorTo;
	private String movePlayerTo;
	private String isRandomizeBehaviour;
	private String active;
	private String type;
	private int randomizeMaxNumber;
	private HashMap<String, String> objectsNeeded;
	private HashMap<String, String> objectsReturned;
	private String description;
	
	public Behaviour(){
		objectsNeeded = new HashMap<String, String>();
		objectsReturned = new HashMap<String, String>(); 
	}
	
	
	
	
	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public String getActive() {
		return active;
	}



	public void setActive(String active) {
		this.active = active;
	}



	public String getObjectNeededMessage(String objectName){
		return objectsNeeded.get(objectName);
	}
	
	public boolean isObjectNeeded(String objectName){				
		return objectsNeeded.containsKey(objectName);
	}
	
	public String getBehaviourId() {
		return behaviourId;
	}
	
	public void setBehaviourId(String behaviourId) {
		this.behaviourId = behaviourId;
	}
	
	public void addObjectsNeeded(String objectName, String message){
		objectsNeeded.put(objectName, message);
	}
	
	public void addObjectsReturned(String objectName, String message){
		objectsReturned.put(objectName, message);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMoveActorTo() {
		return moveActorTo;
	}
	
	public void setMoveActorTo(String moveActorTo) {
		this.moveActorTo = moveActorTo;
	}
	
	public String getMovePlayerTo() {
		return movePlayerTo;
	}
	
	public void setMovePlayerTo(String movePlayerTo) {
		this.movePlayerTo = movePlayerTo;
	}
	
	public HashMap<String, String> getObjectsNeeded() {
		return objectsNeeded;
	}
	
	public void setObjectsNeeded(HashMap<String, String> objectsNeeded) {
		this.objectsNeeded = objectsNeeded;
	}
	
	public HashMap<String, String> getObjectsReturned() {
		return objectsReturned;
	}
	
	public void setObjectsReturned(HashMap<String, String> objectsReturned) {
		this.objectsReturned = objectsReturned;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsRandomizeBehaviour() {
		return isRandomizeBehaviour;
	}

	public void setIsRandomizeBehaviour(String isRandomizeBehaviour) {
		this.isRandomizeBehaviour = isRandomizeBehaviour;
	}

	public int getRandomizeMaxNumber() {
		return randomizeMaxNumber;
	}

	public void setRandomizeMaxNumber(String randomizeMaxNumber) {
		this.randomizeMaxNumber = Integer.valueOf(randomizeMaxNumber);
	}
}
