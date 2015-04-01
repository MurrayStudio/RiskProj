package edu.up.cs301.risk;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * A GUI of a risk-player. 
 * The GUI displays the current number of units on a specific territory 
 * and send moves to the game.
 * 
 * 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan Mealy, Garrett Becker, Lucas Burns, Shamus Murray, John Will Bryant
 * @version July 2013
 */
public class RiskHumanPlayer extends GameHumanPlayer implements RiskPlayer, OnClickListener {

	/* instance variables */
	
	
	// the most recent game state, as given to us by the RiskLocalGame
	private RiskState state;
	
	// the android activity that we are running
	private GameMainActivity myActivity;
	
	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public RiskHumanPlayer(String name) {
		super(name);
	}

	/**
	 * Returns the GUI's top view object
	 * 
	 * @return
	 * 		the top object in the GUI's view heirarchy
	 */
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}
	
	/**
	 * sets the troop value in the text view 
	 * depending on the country selected
	 */
	protected void updateDisplay() {
		
	}

	/**
	 * this method gets called when the user clicks on an attack, defend, place troops, or surrender button.
	 *  It creates a new RiskMoveAction to return to the parent activity.
	 * 
	 * @param button
	 * 		the button that was clicked
	 */
	public void onClick(View button) {
		// if we are not yet connected to a game, ignore
		if (game == null) return;

		
		
		
	}// onClick
	
	/**
	 * callback method when we get a message (e.g., from the game)
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		// ignore the message if it's not a CounterState message
		if (!(info instanceof RiskState)) return;
		
		// update our state; then update the display
		this.state = (RiskState)info;
		updateDisplay();
	}
	
	/**
	 * callback method--our game has been chosen/rechosen to be the GUI,
	 * called from the GUI thread
	 * 
	 * @param activity
	 * 		the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {
		
		// remember the activity
		myActivity = activity;
		
	    // Load the layout resource for our GUI
		activity.setContentView(R.layout.activity_main);
		
		//makes surfaceView transparent
		//stackOverflow TantanQi
		//SurfaceView sfvTrack = (SurfaceView) myActivity.findViewById(R.id.countriesMapDraw);
		//sfvTrack.setZOrderOnTop(true); // necessary
		//SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
		//sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
		
		Button country1 = (Button) myActivity.findViewById(R.id.russiaButton);
		final Button attack = (Button) myActivity.findViewById(R.id.Attack);
		
		country1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attack.setText("attack disabled");
			}
		});
		

		
		// if we have a game state, "simulate" that we have just received
		// the state from the game so that the GUI values are updated
		if (state != null) {
			receiveInfo(state);
		}
	}

}// class RiskHumanPlayer

