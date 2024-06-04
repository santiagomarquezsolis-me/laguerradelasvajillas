package es.zgames.textadventures.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdventureObject<V, K> {

	private String locationId;
	private String name;
	private String longName;
	private String description;
	private String locationText;
	private String distance; // Distancia a la que est� el objeto
	private String article; // Articulo que acompa�a al objeto (la,los,las,el, etc.)
	private String status; // Estado del objeto
	private String containsFluid; // El objeto contiene un fluido (agua, vino, etc.)
	private String fluidType;
	

	private String lightSource; // El objeto es emisor de luz?
	private String lightLink; // Se usa para objetos que necesitan otro objeto para producir luz, 
							  // por ejemplo la linterna con su pila. Es un aspecto a mejorar si
							  // me pongo a hacer un editor en serio.
	private String isContainer;
	private String isFood;
	

	private HashMap<String, Object> containerObjects;
	private int size;
	private String show;
	private String image;
	private int chargeLevel;
	private String isTreasure;
	private int imagePosition;
	
	public String getIsFood() {
		return isFood;
	}

	public void setIsFood(String isFood) {
		this.isFood = isFood;
	}
	
	public String getIsTreasure() {
		return isTreasure;
	}

	public void setIsTreasure(String isTreasure) {
		this.isTreasure = isTreasure;
	}

	public String getFluidType() {
		return fluidType;
	}

	public void setFluidType(String fluidType) {
		this.fluidType = fluidType;
	}
		
	public int getChargeLevel() {
		return chargeLevel;
	}

	public void setChargeLevel(int chargeLevel) {
		this.chargeLevel = chargeLevel;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLightLink(){
		return this.lightLink;
	}
	
	public void setLightLink(String objectName){
		this.lightLink = objectName;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = Integer.valueOf(size);
	}

	public String getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(String isContainer) {
		if (isContainer.equals(AdventureCodes.TRUE)){
			this.containerObjects = new HashMap<String, Object>();
		}
		this.isContainer = isContainer;			
	}
	
	public void addContainerObject(AdventureObject<?, ?> object){
		this.containerObjects.put(object.getName(), object);
	}
	
	public AdventureObject<?, ?> getContainerObject(String objectName){
		return (AdventureObject<?, ?>) this.containerObjects.get(objectName);
	}
	
	public AdventureObject<?, ?> getContainerObjectByPosition(String objectName){
		return (AdventureObject<?, ?>) this.containerObjects.get(objectName);
	}
	
	public int getContainerObjectSize(){
		return this.containerObjects.size();
	}
	
	public boolean isContainerObjectEmpty(){
		return this.containerObjects.isEmpty();
	}

	public String getIsLightSource() {
		return lightSource;
	}

	public void setIsLightSource(String lightSource) {
		this.lightSource = lightSource;
	}

	public String getContainsFluid() {
		return containsFluid;
	}
	
	public void setContainsFluid(String containsFluid) {
		this.containsFluid = containsFluid;
	}
		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	public String getNameWithStatus() {
		if (status.equals(AdventureObjectCodes.STATUS_NONE)) {
			return longName;
		} else {
			return longName + " (" + status + ")";
		}
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
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}		
	
	public String getContainerObjectAsString(){
		String aux = SystemMessage.MESSAGE_HAS_IN;
		if (getIsContainer().equals(AdventureCodes.TRUE)){
			if (containerObjects.size()>0){
				Iterator<?> it = containerObjects.entrySet().iterator();
				AdventureObject<?, ?> object;
				while (it.hasNext()){
					Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					object = (AdventureObject<?, ?>)e.getValue();
					aux = aux + " " + object.getArticle().toLowerCase() + " " + object.getName().toLowerCase() + SystemMessage.MESSAGE_DOT;				
				}
			} else {
				aux = aux + " " + SystemMessage.MESSAGE_NOTHING;
			}
		}
		return aux;
	}
	
	public void removeObjectFromContainer(String objectName){
		if (containerObjects.containsKey(objectName)){
			containerObjects.remove(objectName);
		}
	}

	public int getImagePosition() {
		return imagePosition;
	}

	public void setImagePosition(int imagePosition) {
		this.imagePosition = imagePosition;
	}
	
	
}
