package edu.up.cs301.risk;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * A GUI of a risk-player. The GUI displays the current number of units on a
 * specific territory and send moves to the game.
 * 
 * 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan Mealy, Garrett Becker, Lucas Burns, Shamus Murray, John Will
 *         Bryant
 * @version July 2013
 */
public class RiskHumanPlayer extends GameHumanPlayer implements RiskPlayer,
		OnClickListener {

	/* instance variables */
	Button attack;
	Button move;
	Button place;
	Button surrender;
	Button endTurn;
	Button deselect;

	int[] countryIds = new int[17];
	Button[] countries = new Button[17];

	int countrySelectedID = 0;

	boolean countryPressed = false;

	// the most recent game state, as given to us by the RiskLocalGame
	private RiskState state;

	// the android activity that we are running
	private GameMainActivity myActivity;

	/**
	 * constructor
	 * 
	 * @param name
	 *            the player's name
	 */
	public RiskHumanPlayer(String name) {
		super(name);
	}

	/**
	 * Returns the GUI's top view object
	 * 
	 * @return the top object in the GUI's view heirarchy
	 */
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/**
	 * sets the troop value in the text view depending on the country selected
	 */
	protected void updateDisplay() {

	}

	/**
	 * this method gets called when the user clicks on an attack, defend, place
	 * troops, or surrender button. It creates a new RiskMoveAction to return to
	 * the parent activity.
	 * 
	 * @param button
	 *            the button that was clicked
	 */
	public void onClick(View button) {
		// if we are not yet connected to a game, ignore
		if (game == null)
			return;
		
		if (button.getId() == R.id.Deselect) {
			countryPressed = false;
		}

		int y;
		for (y = 1; y < 17; y++) {
			if (button.getId() == countryIds[y]) {
				countrySelectedID = button.getId();
				countryPressed = true;
			}
		}

		if (state.getHaveTroopBeenPlayed() == false && countryPressed == true) {
			attack.setBackgroundColor(attack.getContext().getResources()
					.getColor(R.color.Yellow));
			move.setBackgroundColor(move.getContext().getResources()
					.getColor(R.color.Yellow));
			place.setBackground(place.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
			surrender.setBackground(surrender.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
		} else if(state.getHaveTroopBeenPlayed() == true && countryPressed == true) {
			attack.setBackground(attack.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
			move.setBackground(move.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
			place.setBackgroundColor(place.getContext().getResources()
					.getColor(R.color.Yellow));
			surrender.setBackground(surrender.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
		}else if(state.getHaveTroopBeenPlayed() == false && countryPressed == false){
			attack.setBackgroundColor(attack.getContext().getResources()
					.getColor(R.color.Yellow));
			move.setBackgroundColor(move.getContext().getResources()
					.getColor(R.color.Yellow));
			place.setBackgroundColor(place.getContext().getResources()
					.getColor(R.color.Yellow));
			surrender.setBackground(surrender.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
		}
		
		if (button.getId() == R.id.Attack) {
			attack.setText("you suck");
		}

		int i;
		for (i = 1; i < 17; i++) {
			if (button.getId() == countryIds[i]) {


			} else if (button.getId() == R.id.Attack) {
				// minus button: create "decrement" action
				// action = new CounterMoveAction(this, false);
			}
		}
		

	}// onClick

	/**
	 * callback method when we get a message (e.g., from the game)
	 * 
	 * @param info
	 *            the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		// ignore the message if it's not a CounterState message
		if (!(info instanceof RiskState))
			return;

		// update our state; then update the display
		this.state = (RiskState) info;
		updateDisplay();
	}

	/**
	 * callback method--our game has been chosen/rechosen to be the GUI, called
	 * from the GUI thread
	 * 
	 * @param activity
	 *            the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		myActivity = activity;

		// Load the layout resource for our GUI
		activity.setContentView(R.layout.activity_main);

		// makes surfaceView transparent
		// stackOverflow TantanQi
		// SurfaceView sfvTrack = (SurfaceView)
		// myActivity.findViewById(R.id.countriesMapDraw);
		// sfvTrack.setZOrderOnTop(true); // necessary
		// SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
		// sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

		countryIds[0] = 0;
		countryIds[1] = R.id.russiaButton;
		countryIds[2] = R.id.icelandButton;
		countryIds[3] = R.id.italyButton;
		countryIds[4] = R.id.swedenButton;
		countryIds[5] = R.id.atlantisButton;
		countryIds[6] = R.id.hogwartsButton;
		countryIds[7] = R.id.narniaButton;
		countryIds[8] = R.id.germanyButton;
		countryIds[9] = R.id.mordorButton;
		countryIds[10] = R.id.gondorButton;
		countryIds[11] = R.id.shireButton;
		countryIds[12] = R.id.rohanButton;
		countryIds[13] = R.id.bulgariaButton;
		countryIds[14] = R.id.israelButton;
		countryIds[15] = R.id.switzerlandButton;
		countryIds[16] = R.id.ukraineButton;

		countries[0] = null;

		int i;
		for (i = 1; i < 17; i++) {

			countries[i] = (Button) myActivity.findViewById(countryIds[i]);
			countries[i].setOnClickListener(this);
		}

		attack = (Button) myActivity.findViewById(R.id.Attack);
		attack.setOnClickListener(this);
		move = (Button) myActivity.findViewById(R.id.Move);
		move.setOnClickListener(this);
		place = (Button) myActivity.findViewById(R.id.Place);
		place.setOnClickListener(this);
		surrender = (Button) myActivity.findViewById(R.id.Surrender);
		surrender.setOnClickListener(this);
		endTurn = (Button) myActivity.findViewById(R.id.EndTurn);
		endTurn.setOnClickListener(this);
		deselect = (Button) myActivity.findViewById(R.id.Deselect);
		deselect.setOnClickListener(this);

		attack.setBackgroundColor(attack.getContext().getResources()
				.getColor(R.color.Yellow));
		move.setBackgroundColor(move.getContext().getResources()
				.getColor(R.color.Yellow));
		place.setBackgroundColor(place.getContext().getResources()
				.getColor(R.color.Yellow));

		// if we have a game state, "simulate" that we have just received
		// the state from the game so that the GUI values are updated
		if (state != null) {
			receiveInfo(state);
		}
	}

}// class RiskHumanPlayer

