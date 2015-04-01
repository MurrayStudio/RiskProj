package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * This action ends the turn of the current player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will Bryant
 * @version March 2015
 */
public class RiskEndTurnAction extends GameAction {
	
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	//whether this move is a plus (true) or minus (false)
	private boolean endTurnTrue;
	
	/**
	 * Constructor for the RiskEndTurnAction class.
	 * 
	 * @param source
	 *            the player making the move
	
	 */
	public RiskEndTurnAction(GamePlayer player, boolean endTurnTrue) {
		super(player);
		this.endTurnTrue = endTurnTrue;
		
	}
	
	public boolean endTurnTrue(){
		return endTurnTrue;
	}
	
	
	
	
	}
