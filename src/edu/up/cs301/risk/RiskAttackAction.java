package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskAttackAction is an action to involving troops of one player attacking a
 * country of another player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Bryant
 * @version March 2015
 */
public class RiskAttackAction extends GameAction {

	private int attackcountryIndexID;
	private int defendcountryIndexID;

	/**
	 * Constructor for the RiskAttackAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskAttackAction(GamePlayer player, int attackcountryIndexID,
			int defendcountryIndexID) {
		super(player);
		this.attackcountryIndexID = attackcountryIndexID;
		this.defendcountryIndexID = defendcountryIndexID;
	}

	public int getAttackCountryID() {
		return attackcountryIndexID;
	}

	public int getDefendCountryID() {
		return defendcountryIndexID;
	}
}
// class RiskMoveAction
