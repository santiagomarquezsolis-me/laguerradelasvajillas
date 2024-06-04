package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.AdventureCodes;
import es.zgames.utils.GlobalInformation;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LaGuerradelasVajillasOptionsActivity extends LaGuerradelasVajillasActivity {
	 private GlobalInformation globalInformation = GlobalInformation.getInstance();
	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		 setContentView(R.layout.spanish_options_layout);
		 
		 CheckBox advanceIU_CheckBox = (CheckBox) findViewById(R.id.advanceIU_CheckBox);
		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 advanceIU_CheckBox.setChecked(true);
		 } else {
			 advanceIU_CheckBox.setChecked(false);
		 }
		 advanceIU_CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				GlobalInformation globalInformation = GlobalInformation.getInstance();
				if (arg0.isChecked()){
					 globalInformation.setAdvancedLocation(AdventureCodes.TRUE);
				 } else {
					 globalInformation.setAdvancedLocation(AdventureCodes.FALSE);
				 }
			}
		 });
//
		 
		 
		 CheckBox music_CheckBox = (CheckBox) findViewById(R.id.music_CheckBox);
		 if (globalInformation.getMusic().equals(AdventureCodes.TRUE)){
			 music_CheckBox.setChecked(true);
		 } else {
			 music_CheckBox.setChecked(false);
		 }
		 music_CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				GlobalInformation globalInformation = GlobalInformation.getInstance();
				if (arg0.isChecked()){
					 globalInformation.setMusic(AdventureCodes.TRUE);
				 } else {
					globalInformation.setMusic(AdventureCodes.FALSE);
				 }
			}
		 });
//
//		 
//		 CheckBox autosave_CheckBox = (CheckBox) findViewById(R.id.autosave_CheckBox);
//		 if (globalInformation.getAutoSave().equals(AdventureCodes.TRUE)){
//			 autosave_CheckBox.setChecked(true);
//		 } else {
//			 autosave_CheckBox.setChecked(false);
//		 }
//		 autosave_CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				GlobalInformation globalInformation = GlobalInformation.getInstance();
//				if (arg0.isChecked()){
//					 globalInformation.setAutoSave(AdventureCodes.TRUE);
//				 } else {
//					 globalInformation.setAutoSave(AdventureCodes.FALSE);
//				 }
//			}
//		 });

		 
		 CheckBox withoutGraphics_CheckBox = (CheckBox) findViewById(R.id.withoutGraphics_CheckBox);
		 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
			 withoutGraphics_CheckBox.setChecked(false);
		 } else {
			 withoutGraphics_CheckBox.setChecked(true);
		 }
		 withoutGraphics_CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				GlobalInformation globalInformation = GlobalInformation.getInstance();
				if (arg0.isChecked()){
					 globalInformation.setWithGraphics(AdventureCodes.FALSE);
				 } else {
					 globalInformation.setWithGraphics(AdventureCodes.TRUE);
				 }
			}
		 });

	 }
	 
	 
	 
}
