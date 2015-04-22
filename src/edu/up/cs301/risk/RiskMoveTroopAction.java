package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskMoveTroopAction is an action that moves any number of troops from one
 * country to another
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Brian
 * @version April 2015
 */
public class RiskMoveTroopAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	private int countryID;
	private int countryID2;
	private int playerID;

	/**
	 * Constructor for the RiskMoveTroopAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskMoveTroopAction(GamePlayer player, int playerID, int countryID, int countryID2) {
		super(player);
		this.countryID = countryID;
		this.countryID2 = countryID2;
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}


	public int getCountry1() {
		return countryID;
	}

	public int getCountry2() {
		return countryID2;
	}

}
// class RiskMoveAction
