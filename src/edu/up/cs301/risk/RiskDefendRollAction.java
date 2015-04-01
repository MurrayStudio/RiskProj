package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskDefendRollAction is an action to roll the dice for the defending player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will Bryant
 * @version March 2015
 */
public class RiskDefendRollAction extends GameAction {
	
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	//whether this move is a plus (true) or minus (false)
	private boolean rollDefendTrue;
	
	/**
	 * Constructor for the RiskRollDefendAction class.
	 * 
	 * @param source
	 *            the player making the move
	
	 */
	public RiskDefendRollAction(GamePlayer player, boolean rollDefendTrue) {
		super(player);
		this.rollDefendTrue = rollDefendTrue;
		
	}
	
	public boolean rollDefendTrue(){
		return rollDefendTrue;
	}
	
	
	
	
	}
//class RiskMoveAction
