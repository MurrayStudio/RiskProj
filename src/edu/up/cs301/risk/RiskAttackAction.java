package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskAttackAction is an action involving troops of one player attacking a
 * country of another player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Brian
 * @version April 2015
 */
public class RiskAttackAction extends GameAction {

	//index IDs for the attacking and defending country
	private int attackcountryIndexID;
	private int defendcountryIndexID;

	/**
	 * Constructor for the RiskAttackAction class.
	 * 
	 * @param player: the player making the move
	 * @param attackcountryIndexID: id of attacking country
	 * @param defendcountryIndexID: id of defending country
	 */
	public RiskAttackAction(GamePlayer player, int attackcountryIndexID,
			int defendcountryIndexID) {
		super(player);
		this.attackcountryIndexID = attackcountryIndexID;
		this.defendcountryIndexID = defendcountryIndexID;
	}

	/**
	 * getAttackCountryID
	 * 
	 * @return the index id (int) of the attacking country
	 * 
	 * 
	 */
	public int getAttackCountryID() {
		return attackcountryIndexID;
	}

	/**
	 * getDefendCountryID
	 * 
	 * @return the index id (int) of the defending country
	 * 
	 */
	public int getDefendCountryID() {
		return defendcountryIndexID;
	}
}// class RiskMoveAction
