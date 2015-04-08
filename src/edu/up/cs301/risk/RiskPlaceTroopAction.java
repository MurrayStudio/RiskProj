package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskPlaceTroopAction is an action to place a given amount of troops on the
 * board
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Bryant
 * @version March 2015
 */
public class RiskPlaceTroopAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	// whether this move is a plus (true) or minus (false)
	private boolean placeTroopTrue;
	private int countryID;
	private int playerID;

	/**
	 * Constructor for the RiskPlaceTroopAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskPlaceTroopAction(GamePlayer player, boolean placeTroopTrue,
			int countryID, int playerID) {
		super(player);
		this.placeTroopTrue = placeTroopTrue;
		this.countryID = countryID;
		this.playerID = playerID;
	}

	public void setcountrySelect(int countryID) {
		this.countryID = countryID;
	}

	// returns that countries ID which the user selected to place troops
	public int getCountryID() {
		return this.countryID;
	}
	
	// returns that player ID who used action
	public int getPlayerID() {
		return this.playerID;
	}

	public boolean placeTroopTrue() {
		return placeTroopTrue;
	}

}
// class RiskMoveAction
