package es.zgames.textadventures.engine;

import java.util.Vector;

//import android.util.Log;


// 
//
// Location - represents a gaming location 
//  
// 

public class AdventureLocation {
	
	private String id;
	private String type; 
	private String title;
	private String image;
	private String retroImage;
	private String help;
	private String description;	
	private Vector<Exit> exits;
	private Vector<String> messages;
	private String hasFluid;
	private String fluidType;
	private String needLight;
	
	
	
	
	public String getFluidType() {
		return fluidType;
	}

	public void setFluidType(String fluidType) {
		this.fluidType = fluidType;
	}

	public String getRetroImage() {
		return retroImage;
	}

	public void setRetroImage(String retroImage) {
		this.retroImage = retroImage;
	}

	public String getNeedLight() {
		return needLight;
	}

	public void setNeedLight(String needLight) {
		this.needLight = needLight;
	}

	public String getHasFluid() {
		return hasFluid;
	}

	public void setHasFluid(String hasFluid) {
		this.hasFluid = hasFluid;
	}

	// Blank constructor 
	public AdventureLocation() {
		// Blank title + description 
		id = new String (); 
		type = new String();
		title = new String();
		image = new String();
		help = new String();
		description = new String();
		exits = new Vector<Exit>(); 
		messages = new Vector<String>();
	} 
	
	// Returns location id 
	public String getId(){ 
		return id; 
	} 

	// Returns location type 
	public String getType(){ 
		return type; 
	} 

	// Returns location title 
	public String getTitle(){ 
		return title; 
	} 

	// Returns location image 
	public String getImage(){ 
		return image; 
	}
	
	// Returns location help 
	public String getHelp(){ 
		return help; 
	} 

	// Returns location description
	public String getDescription(){ 
		return description; 
	}

	// Set location id 
	public void setId(String id){ 
		this.id = id; 
	} 

	// Returns location type 
	public void setType(String type){ 
		this.type = type; 
	} 

	// Returns location title 
	public void setTitle(String title){ 
		this.title = title; 
	} 

	// Returns location image 
	public void setImage(String image){ 
		this.image = image; 
	}
	
	// Returns location help 
	public void setHelp(String help){ 
		this.help = help; 
	} 

	public void setDescription(String description){
		this.description = description; 
	}

	// Adds an exit to this location 
	public void addExit (Exit exit) { 
		exits.addElement (exit); 
	} 
	
	// Removes an exit from this location 
	public void removeExit (Exit exit ) { 
		if (exits.contains (exit)) { 
			exits.removeElement (exit); 
		} 
	} 
	
	// Returns a vector of exits 
	public Vector<Exit> getExits () { 
		// Return a clone, as we don't want an external 
		// object to modify our original vector 
		return	(Vector<Exit>) exits.clone(); 
	} 
	
	public String getConnectId(String direction){
		boolean aux = false;
		int index = 0;
		while ((aux == false) && (index < exits.size())){
			Exit exit = exits.get(index);
			if ((exit.getDirectionName().equals(direction)) ||
				(exit.getShortDirectionName().equals(direction))) {
				return exit.getLeadsTo();
			} else {
				index++;
			}
		}
		return "";		
	}
	
	public Vector<String> getMessages() {
		return this.messages;
	}

	public void addMessage(String message) {
		this.messages.addElement(message);
	}
//	public void print(){
//		Log.d("AVENTURA_ORIGINAL", "ID LOCALIDAD: " + id);
//		Log.d("AVENTURA_ORIGINAL", "TIPO		: " + type);
//		Log.d("AVENTURA_ORIGINAL", "TITULO		: " + title);
//		Log.d("AVENTURA_ORIGINAL", "IMAGEN		: " + image);
//		Log.d("AVENTURA_ORIGINAL", "AYUDA		: " + help);
//		
//		for (int i=0; i < exits.size(); i++) {
//			Log.d("AVENTURA_ORIGINAL", "SALIDA		: " + exits.get(i).getDirectionName());
//		}	
//	}
}
