package ohm.quickdice.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import ohm.quickdice.QuickDiceApp;
import ohm.quickdice.R;
import ohm.quickdice.control.GraphicManager;
import ohm.quickdice.control.PreferenceManager;
import ohm.quickdice.entity.RollResult;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RollDiceToast {
	
	private static final String TAG = "RollDiceToast";
	
	Context context;
	PreferenceManager pref;
	GraphicManager graphicManager;
	
	//Cache for effects
	Animation rollDiceAnimation = null;
	private static final int ROLL_DICE_PLAYER_COUNT = 3; //Max number of overlapped sounds
	private int rollDicePlayerIndex = 0; //Used to cycle through sound players
	PlayerRunnable[] rollDicePlayer = null;
	PlayerRunnable[] rollDiceCriticalPlayer = null;
	PlayerRunnable[] rollDiceFumblePlayer = null;
	private ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();

	//Cache for "rollDiceToast"
	Toast rollDiceToast = null;
	View rollDiceView = null;
	ImageView rollDiceImage = null;
	TextView rollDiceText = null;
	
	private boolean showToast;
	private boolean animateToast;
	private boolean soundEnabled;
	private boolean soundExtEnabled;

	public RollDiceToast(Context context, PreferenceManager preferenceManager, GraphicManager graphicManager) {
		this.context = context;
		this.pref = preferenceManager;
		this.graphicManager = graphicManager;
		refreshConfig();
	}
	
	public void refreshConfig() {
		setShowToast(pref.getShowToast());
		setAnimateToast(pref.getShowAnimation());
		setSoundEnabled(pref.getSoundEnabled());
		setSoundExtEnabled(pref.getExtSoundEnabled());
	}
	
	private void setShowToast(boolean enabled) {
		showToast = enabled;
		if (! showToast) {
			//Free cache for "rollDiceToast"
			rollDiceToast = null;
			//rollDiceLayout = null;
			rollDiceView = null;
			rollDiceImage = null;
			rollDiceText = null;
		}
	}
	
	private void setAnimateToast(boolean enabled) {
		animateToast = enabled;
		//Roll Animation
		if (! showToast || ! animateToast) {
			//Free resources
			rollDiceAnimation = null;
		}
	}
	
	private void setSoundEnabled(boolean enabled) {
		soundEnabled = enabled;
		if (! soundEnabled) {
			//Free resources
			if (rollDicePlayer != null) {
				PlayerRunnable.disposePlayers(rollDicePlayer);
				rollDicePlayer = null;
			}
		}
	}

	private void setSoundExtEnabled(boolean enabled) {
		soundExtEnabled = enabled;
		if (! soundEnabled || ! soundExtEnabled) {
			//Free resources
			if (rollDiceCriticalPlayer != null) {
				PlayerRunnable.disposePlayers(rollDiceCriticalPlayer);
				rollDiceCriticalPlayer = null;
			}
			if (rollDiceFumblePlayer != null) {
				PlayerRunnable.disposePlayers(rollDiceFumblePlayer);
				rollDiceFumblePlayer = null;
			}
		}
	}
	
	public void performRoll(RollResult res) {

		if (showToast) {
			performRollPopup(res);
		
			if (animateToast)
				performRollAnimation(res);
		}
		
		if (soundEnabled)
			performRollSound(res);
	}

	private void performRollPopup(RollResult res) {
		//Create the references to the roll toast if not exist
		if (rollDiceToast == null) {
//			View rollDiceLayout = getLayoutInflater().inflate(
//					R.layout.dice_roll_toast,
//					null);
			View rollDiceLayout = LayoutInflater.from(context).inflate(
				R.layout.dice_roll_toast,
				null);

			rollDiceView = (View)rollDiceLayout.findViewById(R.id.drtRolling);
			rollDiceImage = (ImageView)rollDiceLayout.findViewById(R.id.drtImg);
			rollDiceText = (TextView)rollDiceLayout.findViewById(R.id.drtText);

			//rollDiceToast = new Toast(getApplicationContext());
			rollDiceToast = new Toast(context);
			rollDiceToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			rollDiceToast.setDuration(Toast.LENGTH_SHORT);
			rollDiceToast.setView(rollDiceLayout);
		}
		
		//#######################################################
		//Following block is needed only with rotating animations
		//#######################################################
//		//Check if the result is uniquely composed by 6 or 9 or 0
//		//and thus require a dot to tell if is upside down.
//		String result = Long.toString(res.getResultValue());
//		boolean dot = true;
//		for (int i = 0; i < result.length() && dot == true; i++) {
//			dot = result.charAt(i) == '6' || result.charAt(i) == '9' || result.charAt(i) == '0';
//		}
//		if (dot) {
//			rollDiceText.setText(result + ".");
//		} else {
//			rollDiceText.setText(result);
//		}
		//#######################################################
		rollDiceText.setText(Long.toString(res.getResultValue()));
		//#######################################################

		//Create the shape of the dice
		int resIndex = res.getResourceIndex();

		rollDiceImage.setImageDrawable(graphicManager.getDiceIconShape(resIndex, resIndex));

		rollDiceToast.show();
	}
	
	private void performRollAnimation(RollResult res) {
		if (rollDiceAnimation == null) {
			//rollDiceAnimation = AnimationUtils.loadAnimation(app, R.anim.dice_roll);
			rollDiceAnimation = AnimationUtils.loadAnimation(context, R.anim.dice_roll);
		}
		rollDiceView.startAnimation(rollDiceAnimation);
	}

	private void performRollSound(RollResult res) {
		if (res.isCritical() && soundExtEnabled) {
			if (rollDiceCriticalPlayer == null) {
				rollDiceCriticalPlayer = PlayerRunnable.initPlayers(R.raw.critical, ROLL_DICE_PLAYER_COUNT);
			}
			executor.execute(rollDiceCriticalPlayer[rollDicePlayerIndex]);
		} else if (res.isFumble() && soundExtEnabled) {
			if (rollDiceFumblePlayer == null) {
				rollDiceFumblePlayer = PlayerRunnable.initPlayers(R.raw.fumble, ROLL_DICE_PLAYER_COUNT);
			}
			executor.execute(rollDiceFumblePlayer[rollDicePlayerIndex]);
		} else {
			if (rollDicePlayer == null) {
				rollDicePlayer = PlayerRunnable.initPlayers(R.raw.roll, ROLL_DICE_PLAYER_COUNT);
			}
			executor.execute(rollDicePlayer[rollDicePlayerIndex]);
		}
		rollDicePlayerIndex = (rollDicePlayerIndex + 1) % 3;
	}

	private static class PlayerRunnable implements Runnable {
		
		int resId;
		MediaPlayer sound = null;
		
		public static PlayerRunnable[] initPlayers(int resId, int count) {
			PlayerRunnable[] retVal;
			
			retVal = new PlayerRunnable[count];
			for (int i = 0; i < count; i++) {
				retVal[i] = new PlayerRunnable(resId);
			}
			
			return retVal;
		}
		
		public static void disposePlayers(PlayerRunnable[] players) {
			if (players != null) {
				for (int i = 0; i < players.length; i++) {
					players[i].disposePlayer();
				}
			}
		}
		
		public PlayerRunnable(int resId) {
			this.resId = resId;
		}
		
		public void disposePlayer() {
			if (sound != null) {
				sound.release();
				sound = null;
			}
		}
		
		@Override
		public void run(){
			synchronized (this) {
				try {
					if (sound == null) {
						sound = MediaPlayer.create(QuickDiceApp.getInstance(), resId);
					}
					if (sound != null) {
						sound.start();
					}
				} catch (Exception ex) {
					Log.w(TAG, "PlayerRunnable # " + resId, ex);
				}
			}
		}
	};

}
