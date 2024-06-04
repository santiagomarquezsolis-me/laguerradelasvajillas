package es.zgames.laguerradelasvajillasfree.activities;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.Adventure;
import es.zgames.textadventures.engine.AdventureObject;
import es.zgames.textadventures.engine.AdventureCodes;
import es.zgames.utils.GlobalInformation;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.TextView;

public class LaGuerradelasVajillasInventoryActivity<K, V> extends LaGuerradelasVajillasActivity  {
	
	private Adventure<?, ?> adventure = null;
	private GlobalInformation globalInformation = GlobalInformation.getInstance();
	private final int IMAGE_POSITION01 = 1;
	private final int IMAGE_POSITION02 = 2;
	private final int IMAGE_POSITION03 = 3;
	private final int IMAGE_POSITION04 = 4;
	private final int IMAGE_POSITION05 = 5;
	private final int IMAGE_POSITION06 = 6;
	private final int IMAGE_POSITION07 = 7;
	private final int IMAGE_POSITION08 = 8;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // part1_Adventure.isObjectTaken()
        
        setContentView(R.layout.spanish_inventary_layout);
        
        ImageView inventary01_ImageView = (ImageView) findViewById(R.id.inventary01_ImageView);
        ImageView inventary02_ImageView = (ImageView) findViewById(R.id.inventary02_ImageView);
        ImageView inventary03_ImageView = (ImageView) findViewById(R.id.inventary03_ImageView);
        ImageView inventary04_ImageView = (ImageView) findViewById(R.id.inventary04_ImageView);
        ImageView inventary05_ImageView = (ImageView) findViewById(R.id.inventary05_ImageView);
        ImageView inventary06_ImageView = (ImageView) findViewById(R.id.inventary06_ImageView);
        ImageView inventary07_ImageView = (ImageView) findViewById(R.id.inventary07_ImageView);
        ImageView inventary08_ImageView = (ImageView) findViewById(R.id.inventary08_ImageView);
        
        
        adventure = Adventure.getInstance();
        HashMap<String, Object> objects = adventure.getObjects();
        Iterator<?> it = objects.entrySet().iterator();
    	while (it.hasNext()){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				int resId = getResources().getIdentifier(object.getImage(), "drawable", getPackageName());
				if (object.getImagePosition()==1){
					inventary01_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==2){				
					inventary02_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==3){
					inventary03_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==4){
					inventary04_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==5){
					inventary05_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==6){
					inventary06_ImageView.setImageResource(resId);
				} else if (object.getImagePosition()==7){
					inventary07_ImageView.setImageResource(resId);
		        } else if (object.getImagePosition()==8){
					inventary08_ImageView.setImageResource(resId);
		        } 			
			}
    	}
    	
    	if (globalInformation.isImage01_free()){
    		inventary01_ImageView.setImageResource(R.drawable.inventary_empty);
    	} if (globalInformation.isImage02_free()){
    		inventary02_ImageView.setImageResource(R.drawable.inventary_empty);    	
    	} if (globalInformation.isImage03_free()){
    		inventary03_ImageView.setImageResource(R.drawable.inventary_empty);    	
    	} if (globalInformation.isImage04_free()){
    		inventary04_ImageView.setImageResource(R.drawable.inventary_empty);
    	} if (globalInformation.isImage05_free()){
    		inventary05_ImageView.setImageResource(R.drawable.inventary_empty);
    	} if (globalInformation.isImage06_free()){
    		inventary06_ImageView.setImageResource(R.drawable.inventary_empty);
    	} if (globalInformation.isImage07_free()){
    		inventary07_ImageView.setImageResource(R.drawable.inventary_empty);
    	} if (globalInformation.isImage08_free()){
    		inventary08_ImageView.setImageResource(R.drawable.inventary_empty);
    	}
     
    	inventary01_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION01);			    	
    		}
    	});
    	
    	inventary02_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION02);			    	
    		}
    	});
    	
    	inventary03_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION03);			    	
    		}
    	});
    	
    	inventary04_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION04);			    	
    		}
    	});
    	
    	inventary05_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION05);			    	
    		}
    	});
    	
    	inventary06_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION06);			    	
    		}
    	});
    	
    	inventary07_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION07);			    	
    		}
    	});
    	
    	inventary08_ImageView.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			writeObjectDescriptionText(IMAGE_POSITION08);			    	
    		}
    	});
    }

    private void writeObjectDescriptionText(int objectPosition){
    	TextView description_TextView = (TextView) findViewById(R.id.description_TextView);
    	
    	HashMap<String, Object> objects = adventure.getObjects();
        Iterator<?> it = objects.entrySet().iterator();
        boolean end = false;
    	while ((it.hasNext() && (end ==false))){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if (object.getImagePosition() == objectPosition){	
				if (object.getStatus().equals("NONE")){
					description_TextView.setText(object.getLongName() + ": " + object.getDescription());
				} else {
					description_TextView.setText(object.getLongName() + " (" + object.getStatus() + ") : " + object.getDescription());
				}
				end = true;
			} else {
				description_TextView.setText("");
			}
    	}
    	
    }
}
