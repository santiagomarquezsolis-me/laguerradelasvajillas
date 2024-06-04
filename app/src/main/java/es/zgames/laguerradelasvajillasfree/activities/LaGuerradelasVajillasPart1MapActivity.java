package es.zgames.laguerradelasvajillasfree.activities;


import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.Adventure;
import es.zgames.textadventures.engine.AdventureCodes;
import es.zgames.utils.GlobalInformation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LaGuerradelasVajillasPart1MapActivity extends LaGuerradelasVajillasActivity {
	
	private Adventure<?, ?> part1_Adventure = null;
    private GlobalInformation globalInformation = GlobalInformation.getInstance();
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        setContentView(R.layout.spanish_part1_map_layout);        
        
        part1_Adventure = Adventure.getInstance();
        
        paintMapLayout1();
       	
       	startAnimating();

    }
    
    private void paintMapLayout1(){

    	// Imagenes de localidades
    	ImageView map_part1_location_001_ImageView = (ImageView)  findViewById(R.id.map_part1_location_001_ImageView);
    	map_part1_location_001_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_002_ImageView = (ImageView)  findViewById(R.id.map_part1_location_002_ImageView);
    	map_part1_location_002_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_003_ImageView = (ImageView)  findViewById(R.id.map_part1_location_003_ImageView);
    	map_part1_location_003_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_004_ImageView = (ImageView)  findViewById(R.id.map_part1_location_004_ImageView);
    	map_part1_location_004_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_005_ImageView = (ImageView)  findViewById(R.id.map_part1_location_005_ImageView);
    	map_part1_location_005_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_006_ImageView = (ImageView)  findViewById(R.id.map_part1_location_006_ImageView);
    	map_part1_location_006_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_007_ImageView = (ImageView)  findViewById(R.id.map_part1_location_007_ImageView);
    	map_part1_location_007_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_008_ImageView = (ImageView)  findViewById(R.id.map_part1_location_008_ImageView);
    	map_part1_location_008_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_009_ImageView = (ImageView)  findViewById(R.id.map_part1_location_009_ImageView);
    	map_part1_location_009_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_010_ImageView = (ImageView)  findViewById(R.id.map_part1_location_010_ImageView);
    	map_part1_location_010_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_011_ImageView = (ImageView)  findViewById(R.id.map_part1_location_011_ImageView);
    	map_part1_location_011_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_012_ImageView = (ImageView)  findViewById(R.id.map_part1_location_012_ImageView);
    	map_part1_location_012_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_013_ImageView = (ImageView)  findViewById(R.id.map_part1_location_013_ImageView);
    	map_part1_location_013_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_014_ImageView = (ImageView)  findViewById(R.id.map_part1_location_014_ImageView);
    	map_part1_location_014_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_015_ImageView	= (ImageView) findViewById(R.id.map_part1_location_015_ImageView);
    	map_part1_location_015_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_016_ImageView = (ImageView)  findViewById(R.id.map_part1_location_016_ImageView);
    	map_part1_location_016_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_017_ImageView = (ImageView)  findViewById(R.id.map_part1_location_017_ImageView);
    	map_part1_location_017_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_018_ImageView = (ImageView)  findViewById(R.id.map_part1_location_018_ImageView);
    	map_part1_location_018_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_019_ImageView = (ImageView)  findViewById(R.id.map_part1_location_019_ImageView);
    	map_part1_location_019_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_020_ImageView = (ImageView)  findViewById(R.id.map_part1_location_020_ImageView);
    	map_part1_location_020_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_021_ImageView = (ImageView)  findViewById(R.id.map_part1_location_021_ImageView);
    	map_part1_location_021_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_022_ImageView = (ImageView)  findViewById(R.id.map_part1_location_022_ImageView);
    	map_part1_location_022_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_023_ImageView = (ImageView)  findViewById(R.id.map_part1_location_023_ImageView);
    	map_part1_location_023_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_024_ImageView = (ImageView)  findViewById(R.id.map_part1_location_024_ImageView);
    	map_part1_location_024_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_025_ImageView = (ImageView)  findViewById(R.id.map_part1_location_025_ImageView);
    	map_part1_location_025_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView map_part1_location_026_ImageView = (ImageView)  findViewById(R.id.map_part1_location_026_ImageView);
    	map_part1_location_026_ImageView.setVisibility(View.INVISIBLE);

    	//
    	// Imagenes de Conectores    	
    	ImageView connector_01_ImageView = (ImageView)  findViewById(R.id.connector_01_ImageView);
    	connector_01_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_02_ImageView = (ImageView)  findViewById(R.id.connector_02_ImageView);
    	connector_02_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_03_ImageView = (ImageView)  findViewById(R.id.connector_03_ImageView);
    	connector_03_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_04_ImageView = (ImageView)  findViewById(R.id.connector_04_ImageView);
    	connector_04_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_05_ImageView = (ImageView)  findViewById(R.id.connector_05_ImageView);
    	connector_05_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_06_ImageView = (ImageView)  findViewById(R.id.connector_06_ImageView);
    	connector_06_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_07_ImageView = (ImageView)  findViewById(R.id.connector_07_ImageView);
    	connector_07_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_08_ImageView = (ImageView)  findViewById(R.id.connector_08_ImageView);
    	connector_08_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_09_ImageView = (ImageView)  findViewById(R.id.connector_09_ImageView);
    	connector_09_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_10_ImageView = (ImageView)  findViewById(R.id.connector_10_ImageView);
    	connector_10_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_11_ImageView = (ImageView)  findViewById(R.id.connector_11_ImageView);
    	connector_11_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_12_ImageView = (ImageView)  findViewById(R.id.connector_12_ImageView);
    	connector_12_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_13_ImageView = (ImageView)  findViewById(R.id.connector_13_ImageView);
    	connector_13_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_14_ImageView = (ImageView)  findViewById(R.id.connector_14_ImageView);
    	connector_14_ImageView.setVisibility(View.INVISIBLE); 
    	
    	ImageView connector_15_ImageView = (ImageView)  findViewById(R.id.connector_15_ImageView);
    	connector_15_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_16_ImageView = (ImageView)  findViewById(R.id.connector_16_ImageView);
    	connector_16_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_17_ImageView = (ImageView)  findViewById(R.id.connector_17_ImageView);
    	connector_17_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_18_ImageView = (ImageView)  findViewById(R.id.connector_18_ImageView);
    	connector_18_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_19_ImageView = (ImageView)  findViewById(R.id.connector_19_ImageView);
    	connector_19_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_20_ImageView = (ImageView)  findViewById(R.id.connector_20_ImageView);
    	connector_20_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_21_ImageView = (ImageView)  findViewById(R.id.connector_21_ImageView);
    	connector_21_ImageView.setVisibility(View.INVISIBLE);    	
    	
    	ImageView connector_22_ImageView = (ImageView)  findViewById(R.id.connector_22_ImageView);
    	connector_22_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_23_ImageView = (ImageView)  findViewById(R.id.connector_23_ImageView);
    	connector_23_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_24_ImageView = (ImageView)  findViewById(R.id.connector_24_ImageView);
    	connector_24_ImageView.setVisibility(View.INVISIBLE);
    	
    	ImageView connector_25_ImageView = (ImageView)  findViewById(R.id.connector_25_ImageView);
    	connector_25_ImageView.setVisibility(View.INVISIBLE);

     	if (part1_Adventure.isLocationVisited("001")){
    		map_part1_location_001_ImageView.setVisibility(View.VISIBLE);
    		connector_03_ImageView.setVisibility(View.VISIBLE);
    		connector_04_ImageView.setVisibility(View.VISIBLE);    		
    	}
    	
    	if (part1_Adventure.isLocationVisited("002")){
    		map_part1_location_002_ImageView.setVisibility(View.VISIBLE);
    		connector_01_ImageView.setVisibility(View.VISIBLE);
    		connector_03_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("003")){
    		map_part1_location_003_ImageView.setVisibility(View.VISIBLE);    		
    		connector_01_ImageView.setVisibility(View.VISIBLE);
    		connector_02_ImageView.setVisibility(View.VISIBLE);    		    		
    	}
    	
    	if (part1_Adventure.isLocationVisited("004")){
    		map_part1_location_004_ImageView.setVisibility(View.VISIBLE);
    		connector_02_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("005")){
    		map_part1_location_005_ImageView.setVisibility(View.VISIBLE);    		
    		connector_04_ImageView.setVisibility(View.VISIBLE);
    		connector_05_ImageView.setVisibility(View.VISIBLE);
    		connector_06_ImageView.setVisibility(View.VISIBLE);
    		connector_08_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("006")){
    		map_part1_location_006_ImageView.setVisibility(View.VISIBLE);
    		connector_06_ImageView.setVisibility(View.VISIBLE);
    		connector_07_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("007")){
    		map_part1_location_007_ImageView.setVisibility(View.VISIBLE);
    		connector_05_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("008")){
    		map_part1_location_008_ImageView.setVisibility(View.VISIBLE);
    		connector_07_ImageView.setVisibility(View.VISIBLE);    		
    	}
    	
    	if (part1_Adventure.isLocationVisited("009")){
    		map_part1_location_009_ImageView.setVisibility(View.VISIBLE);
    		connector_08_ImageView.setVisibility(View.VISIBLE);
    		connector_09_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("010")){
    		map_part1_location_010_ImageView.setVisibility(View.VISIBLE);
    		connector_09_ImageView.setVisibility(View.VISIBLE);
    		connector_10_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("011")){
    		map_part1_location_011_ImageView.setVisibility(View.VISIBLE);
    		connector_10_ImageView.setVisibility(View.VISIBLE);
    		connector_14_ImageView.setVisibility(View.VISIBLE);
    		connector_15_ImageView.setVisibility(View.VISIBLE);
    		connector_19_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("012")){
    		map_part1_location_012_ImageView.setVisibility(View.VISIBLE);
    		connector_11_ImageView.setVisibility(View.VISIBLE);
    		connector_15_ImageView.setVisibility(View.VISIBLE);
    		connector_16_ImageView.setVisibility(View.VISIBLE);
    		connector_20_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("013")){
    		map_part1_location_013_ImageView.setVisibility(View.VISIBLE);
    		connector_16_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    	if (part1_Adventure.isLocationVisited("014")){
    		map_part1_location_014_ImageView.setVisibility(View.VISIBLE);
    		connector_14_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("015")){
    		map_part1_location_015_ImageView.setVisibility(View.VISIBLE);
    		connector_19_ImageView.setVisibility(View.VISIBLE);
    		connector_25_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("016")){
    		map_part1_location_016_ImageView.setVisibility(View.VISIBLE);
    		connector_25_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("017")){
    		map_part1_location_017_ImageView.setVisibility(View.VISIBLE);
    		connector_11_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("018")){
    		map_part1_location_018_ImageView.setVisibility(View.VISIBLE);
    		connector_20_ImageView.setVisibility(View.VISIBLE);
    		connector_23_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("019")){
    		map_part1_location_019_ImageView.setVisibility(View.VISIBLE);
    		connector_23_ImageView.setVisibility(View.VISIBLE);
    		connector_24_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("020")){
    		map_part1_location_020_ImageView.setVisibility(View.VISIBLE);
    		connector_21_ImageView.setVisibility(View.VISIBLE);
    		connector_24_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("021")){
    		map_part1_location_021_ImageView.setVisibility(View.VISIBLE);
    		connector_12_ImageView.setVisibility(View.VISIBLE);
    		connector_17_ImageView.setVisibility(View.VISIBLE);
    		connector_21_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("022")){
    		map_part1_location_022_ImageView.setVisibility(View.VISIBLE);
    		connector_12_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("023")){
    		map_part1_location_023_ImageView.setVisibility(View.VISIBLE);
    		connector_13_ImageView.setVisibility(View.VISIBLE);
    		connector_17_ImageView.setVisibility(View.VISIBLE);
    		connector_18_ImageView.setVisibility(View.VISIBLE);
    		connector_22_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("024")){
    		map_part1_location_024_ImageView.setVisibility(View.VISIBLE);
    		connector_18_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("025")){
    		map_part1_location_025_ImageView.setVisibility(View.VISIBLE);
    		connector_22_ImageView.setVisibility(View.VISIBLE);
    	}

    	if (part1_Adventure.isLocationVisited("026")){
    		map_part1_location_026_ImageView.setVisibility(View.VISIBLE);
    		connector_13_ImageView.setVisibility(View.VISIBLE);
    	}
    	
    }
    

    private void startAnimating() {
		 Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		 
		 
		 ImageView current_location_ImageView = null;	 
		 		 
		 if (part1_Adventure.getCurrentLocation().getId().equals("001")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_001_ImageView);
			 
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("002")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_002_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("003")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_003_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("004")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_004_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("005")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_005_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("006")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_006_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("007")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_007_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("008")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_008_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("009")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_009_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("010")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_010_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("011")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_011_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("012")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_012_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("013")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_013_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("014")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_014_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("015")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_015_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("016")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_016_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("017")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_017_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("018")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_018_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("019")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_019_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("020")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_020_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("021")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_021_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("022")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_022_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("023")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_023_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("024")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_024_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("025")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_025_ImageView);
		 } else if (part1_Adventure.getCurrentLocation().getId().equals("026")) {
			 current_location_ImageView = (ImageView) findViewById(R.id.map_part1_location_026_ImageView);
		 }
		 		 
		 current_location_ImageView.startAnimation(fade_out);

		 
	 }
  
    
}
