package es.zgames.textadventures.engine;

//// // Exit - represents an exit to a location 
// // Last modification date : October 07, 1997 // 
public class Exit { 
	// Numerical codes 
	public static final String UNDEFINED = "UNDEFINED"; 
	public static final String NORTH = "NORTE"; 
	public static final String SOUTH = "SUR"; 
	public static final String EAST = "ESTE"; 
	public static final String WEST = "OESTE"; 
	public static final String UP = "ARRIBA"; 
	public static final String DOWN = "ABAJO"; 
	public static final String NORTHEAST = "NORESTE"; 
	public static final String NORTHWEST = "NOROESTE"; 
	public static final String SOUTHEAST = "SURESTE"; 
	public static final String SOUTHWEST = "SUROESTE"; 
	public static final String IN = "ENTRAR"; 
	public static final String OUT = "SALIR"; 
	
	// Member variables 
	private String m_leadsTo = null;	
	
	// Full name of direction eg SOUTHEAST 
	private String m_directionName; 
	
	// Shortened version of direction eg SE 
	private String m_shortDirectionName; 
	// Default constructor 
	
	public Exit() {	 
		m_leadsTo = null; 
		m_directionName = UNDEFINED; 
		m_shortDirectionName = UNDEFINED; 
	} 
	

	
	// toString method 
	public String toString() { return m_directionName; } 
	
	// Assigns direction name 
	public void setDirectionName( String dirname ) { m_directionName = dirname; } 
	
	// Returns direction name 
	public String getDirectionName() { return m_directionName; } 
	
	// Assigns short direction name 
	public void setShortDirectionName ( String shortName ) { m_shortDirectionName = shortName; } 
	
	// Returns short direction name 
	public String getShortDirectionName () { return m_shortDirectionName; } 
	
	// Assigns location 
	public void setLeadsTo ( String leadsTo ) { m_leadsTo = leadsTo; } 
	
	// Returns location 
	public String getLeadsTo ( ) { return m_leadsTo; } 

} 
