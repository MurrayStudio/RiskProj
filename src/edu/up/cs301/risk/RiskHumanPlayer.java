package edu.up.cs301.risk;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
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
	private Button attack;
	private Button move;
	private Button place;
	private Button surrender;
	private Button endTurn;
	private Button deselect;
	private Button deselect2;
	
	String tf;

	private int[] countryIds = new int[17];
	private int[] countryCountIds = new int[17];
	private Button[] countries = new Button[17];
	private TextView[] countryCount = new TextView[17];

	private int countrySelectedID;
	private int countrySelectedID2;
	private int countrySelectedIndexID;
	private int countrySelectedIndexID2;
	private String countrySelectedName;
	private String countrySelectedName2;

	private boolean countryPressed;
	private boolean country2Pressed;
	private boolean country2CanBeSelected;
	private boolean attackEnabled;
	private boolean moveEnabled;
	private boolean placeEnabled;
	private boolean deselectEnabled;
	private boolean deselect2Enabled;
	private boolean isPlace;

	private GameAction currentAction;

	private int playerID = RiskState.PLAYER_ONE; // default ID is player 1

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
	public RiskHumanPlayer(String name, int playerID) {
		super(name);
		this.playerID = playerID;
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

		int i;
		for (i = 1; i < 17; i++) {

			if (this.state.playerInControl(i) == 100) {
				countryCount[i].setTextColor(Color.BLACK);
				String temp = Integer.toString(this.state
						.getPlayerTroopsInCountry(100, i));
				countryCount[i].setText(temp);
			} else {
				countryCount[i].setTextColor(Color.YELLOW);
				String temp = Integer.toString(this.state
						.getPlayerTroopsInCountry(200, i));
				countryCount[i].setText(temp);
			}
		}

		if (this.state.getHaveTroopBeenPlaced() == true) {
			tf = "t";
		} else {
			tf = "f";
		}

		Log.i("troop placed before?", tf);
		
		if (state.getHaveTroopBeenPlaced() == true) {
			place.setBackgroundColor(deselect2.getContext().getResources()
					.getColor(R.color.Yellow));
			placeEnabled = false;
		}

		if (placeEnabled == false) {
			place.setBackgroundColor(place.getContext().getResources()
					.getColor(R.color.Yellow));
		}
		if (moveEnabled == false) {
			move.setBackgroundColor(move.getContext().getResources()
					.getColor(R.color.Yellow));
		}
		if (attackEnabled == false) {
			attack.setBackgroundColor(attack.getContext().getResources()
					.getColor(R.color.Yellow));
		}

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

		// check if you hit disabled button
		if (button.getId() == R.id.Attack && attackEnabled == false) {
			flash(Color.RED, 200);
		}
		if (button.getId() == R.id.Move && moveEnabled == false) {
			flash(Color.RED, 200);
		}
		if (button.getId() == R.id.Place && placeEnabled == false) {
			flash(Color.RED, 200);
		}
		if (button.getId() == R.id.Deselect && deselectEnabled == false) {
			flash(Color.RED, 200);
		}
		if (button.getId() == R.id.Deselect2 && deselect2Enabled == false) {
			flash(Color.RED, 200);
		}

		// check if deselect country was hit
		if (button.getId() == R.id.Deselect && deselectEnabled == true) {
			// country is no longer pressed
			countryPressed = false;
			deselectEnabled = false;
			country2CanBeSelected = false;

			deselect.setBackgroundColor(deselect.getContext().getResources()
					.getColor(R.color.Yellow));
			deselect2.setBackgroundColor(deselect2.getContext().getResources()
					.getColor(R.color.Yellow));
			deselect.setText("Country 1 Not Selected");
			deselect2.setText("Country 2 Not Selected");
		}

		// check if deselect country was hit
		if (button.getId() == R.id.Deselect2 && deselect2Enabled == true) {
			// country is no longer pressed
			country2Pressed = false;
			deselect2Enabled = false;
			deselect2.setBackgroundColor(deselect.getContext().getResources()
					.getColor(R.color.Yellow));
			deselect2.setText("Country 2 Not Selected");
		}

		int y;
		for (y = 1; y < 17; y++) {
			if (button.getId() == countryIds[y]) {
				switch (button.getId()) {
				case R.id.russiaButton:
					countrySelectedName = "Russia";
					break;
				case R.id.icelandButton:
					countrySelectedName = "Iceland";
					break;
				case R.id.italyButton:
					countrySelectedName = "Italy";
					break;
				case R.id.swedenButton:
					countrySelectedName = "Sweden";
					break;
				case R.id.atlantisButton:
					countrySelectedName = "Atlantis";
					break;
				case R.id.hogwartsButton:
					countrySelectedName = "Hogwarts";
					break;
				case R.id.narniaButton:
					countrySelectedName = "Narnia";
					break;
				case R.id.germanyButton:
					countrySelectedName = "Germany";
					break;
				case R.id.mordorButton:
					countrySelectedName = "Mordor";
					break;
				case R.id.gondorButton:
					countrySelectedName = "Gondor";
					break;
				case R.id.shireButton:
					countrySelectedName = "The Shire";
					break;
				case R.id.rohanButton:
					countrySelectedName = "Rohan";
					break;
				case R.id.bulgariaButton:
					countrySelectedName = "Bulgaria";
					break;
				case R.id.israelButton:
					countrySelectedName = "Israel";
					break;
				case R.id.switzerlandButton:
					countrySelectedName = "Switzerland";
					break;
				case R.id.ukraineButton:
					countrySelectedName = "Ukraine";
					break;
				}

				// you can turn on deselect 2
				if (country2CanBeSelected == true) {
					country2Pressed = true;
					deselect2Enabled = true;

					countrySelectedID2 = button.getId();
					countrySelectedIndexID2 = y;

					deselect2.setBackground(deselect2.getContext()
							.getResources()
							.getDrawable(R.drawable.custombuttonshapewhite));
					deselect2.setText("Deselect: " + countrySelectedName);

				} else {
					// only turn on deselect 1
					countryPressed = true;
					deselectEnabled = true;

					countrySelectedID = button.getId();
					countrySelectedIndexID = y;

					deselect.setBackground(deselect.getContext().getResources()
							.getDrawable(R.drawable.custombuttonshapewhite));
					deselect.setText("Deselect: " + countrySelectedName);
				}
			}
		}

		if (state.getHaveTroopBeenPlaced() == false && countryPressed == true) {

			attackEnabled = false;
			moveEnabled = false;
			placeEnabled = true;

			attack.setBackgroundColor(attack.getContext().getResources()
					.getColor(R.color.Yellow));
			move.setBackgroundColor(move.getContext().getResources()
					.getColor(R.color.Yellow));
			place.setBackground(place.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));

		} else if (state.getHaveTroopBeenPlaced() == true
				&& countryPressed == true) {

			attackEnabled = true;
			moveEnabled = true;

			attack.setBackground(attack.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
			move.setBackground(move.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));
		} else if (state.getHaveTroopBeenPlaced() == true
				&& countryPressed == false) {

			attackEnabled = false;
			moveEnabled = false;

			attack.setBackgroundColor(attack.getContext().getResources()
					.getColor(R.color.Yellow));
			move.setBackgroundColor(move.getContext().getResources()
					.getColor(R.color.Yellow));
		}

		// if attack is pressed
		if (button.getId() == R.id.Attack && attackEnabled == true) {
			deselect2Enabled = true;
			country2CanBeSelected = true;

			deselect2.setBackground(deselect2.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));

			// GameAction attackAction = new RiskAttackAction(this, true,
			// countrySelectedIndexID, );
		}

		// if move is pressed
		if (button.getId() == R.id.Move && moveEnabled == true) {
			deselect2Enabled = true;
			country2CanBeSelected = true;

			deselect2.setBackground(deselect2.getContext().getResources()
					.getDrawable(R.drawable.custombuttonshapewhite));

		}

		// if place is pressed
		if (button.getId() == R.id.Place && placeEnabled == true) {
			Log.i("selected index", Integer.toString(countrySelectedIndexID));
			Log.i("selected index", Integer.toString(playerID));
			Log.i("before troop",
					Integer.toString(this.state.getPlayerTroopsInCountry(
							playerID, countrySelectedIndexID)));

			RiskPlaceTroopAction action = new RiskPlaceTroopAction(this, true,
					countrySelectedIndexID, playerID);
			createAlertBox("Place troops in " + countrySelectedName + "?",
					action);

			Log.i("after troop",
					Integer.toString(this.state.getPlayerTroopsInCountry(
							playerID, countrySelectedIndexID)));

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
		// ignore the message if it's not a RiskState message
		if (!(info instanceof RiskState))
			return;

		// update our state; then update the display
		this.state = (RiskState) info;
		updateDisplay();
	}

	/**
	 * creates dialog box confirming action
	 * 
	 * @param String
	 *            question to be asked
	 * @param String
	 *            action to be passed if action confirmed
	 * @return returns if yes was hit or not
	 */
	private void createAlertBox(String question, GameAction action) {
		AlertDialog.Builder alert = new AlertDialog.Builder(myActivity);
		alert.setTitle(question);
		currentAction = action;
		if (currentAction instanceof RiskPlaceTroopAction) {
			isPlace = true;
		}

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				game.sendAction(currentAction);
				if (isPlace == true) {
					//this.state.setHaveTroopBeenPlacedToTrue();
					deselectEnabled = false;
					placeEnabled = false;
					countryPressed = false;
					country2CanBeSelected = false;
				}
			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();

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

		countryIds[0] = 0;
		countryCountIds[1] = R.id.countryCount1;
		countryCountIds[2] = R.id.countryCount2;
		countryCountIds[3] = R.id.countryCount3;
		countryCountIds[4] = R.id.countryCount4;
		countryCountIds[5] = R.id.countryCount5;
		countryCountIds[6] = R.id.countryCount6;
		countryCountIds[7] = R.id.countryCount7;
		countryCountIds[8] = R.id.countryCount8;
		countryCountIds[9] = R.id.countryCount9;
		countryCountIds[10] = R.id.countryCount10;
		countryCountIds[11] = R.id.countryCount11;
		countryCountIds[12] = R.id.countryCount12;
		countryCountIds[13] = R.id.countryCount13;
		countryCountIds[14] = R.id.countryCount14;
		countryCountIds[15] = R.id.countryCount15;
		countryCountIds[16] = R.id.countryCount16;

		countries[0] = null;

		int i;
		for (i = 1; i < 17; i++) {

			countries[i] = (Button) myActivity.findViewById(countryIds[i]);
			countries[i].setOnClickListener(this);

			countryCount[i] = (TextView) myActivity
					.findViewById(countryCountIds[i]);
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
		deselect2 = (Button) myActivity.findViewById(R.id.Deselect2);
		deselect2.setOnClickListener(this);

		attack.setBackgroundColor(attack.getContext().getResources()
				.getColor(R.color.Yellow));
		move.setBackgroundColor(move.getContext().getResources()
				.getColor(R.color.Yellow));
		place.setBackgroundColor(place.getContext().getResources()
				.getColor(R.color.Yellow));
		deselect.setBackgroundColor(deselect.getContext().getResources()
				.getColor(R.color.Yellow));
		deselect.setText("Country 1 Not Selected");
		deselect2.setBackgroundColor(deselect2.getContext().getResources()
				.getColor(R.color.Yellow));
		deselect2.setText("Country 2 Not Selected");

		// if we have a game state, "simulate" that we have just received
		// the state from the game so that the GUI values are updated
		if (state != null) {
			receiveInfo(state);
		}
	}

}// class RiskHumanPlayer

