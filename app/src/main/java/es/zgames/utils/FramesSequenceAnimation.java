package es.zgames.utils;

import java.lang.ref.SoftReference;

import android.os.Handler;
import android.widget.ImageView;

public class FramesSequenceAnimation {         
	public int FPS = 30;  // animation FPS      

	private int[] mFrames; // animation frames         
	private int mIndex;  // current frame         
	private boolean mShouldRun; // true if the animation should continue running. Used to stop the animation         
	private boolean mIsRunning; // true if the animation currently running. prevents starting the animation twice         
	private SoftReference<ImageView> mSoftReferenceImageView; // Used to prevent holding ImageView when it should be dead.         
	private Handler mHandler;         
	private int mDelayMillis;           
	private String animationElement;
	
	public FramesSequenceAnimation(ImageView imageView, int[] frames, String animationElement, int delay) {             
		mHandler = new Handler();         
		mFrames = frames;             
		mIndex = -1;             
		mSoftReferenceImageView = new SoftReference<ImageView>(imageView);             
		mShouldRun = false;             
		mIsRunning = false;             
		mDelayMillis = delay/FPS;        
		this.animationElement = animationElement;
	}          
	
	private int getNext() {             
		mIndex++;             
		if (mIndex >= mFrames.length) {       
			if (animationElement.equals("splash")) {
				mIndex =  mFrames.length - 1;
			} else {
				mIndex =  0;
			}
		}
		return mFrames[mIndex];         
	}          
	
	
	/**          
	 * * Starts the animation           
	 * */         
	
	public synchronized void start() {             
		mShouldRun = true;             
		if (mIsRunning)                 
			return;              
		Runnable runnable = new Runnable() {                 
			public void run() {                     
				ImageView imageView = mSoftReferenceImageView.get();                     
				if (!mShouldRun || imageView == null) {                         
					mIsRunning = false;                         
					return;                     
				}                      
				mIsRunning = true;                     
				if (imageView.isShown())                         
					imageView.setBackgroundResource(getNext());                     
				mHandler.postDelayed(this, mDelayMillis);                 
			}             
		};              
		
		mHandler.post(runnable);         
	}          
	
	/**          
	 * * Stops the animation          
	 * */         
	public synchronized void stop() {             
		mShouldRun = false;         
	}     
} 