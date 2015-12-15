package com.alesegdia.virusex.entities.organism;

import java.util.List;

import com.alesegdia.virusex.RNG;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.assets.Sfx;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.World;
import com.alesegdia.virusex.entities.node.GoalNode;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.alesegdia.virusex.entities.node.StartNode;
import com.alesegdia.virusex.entities.node.WeakNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Virus extends Organism {
	
	public int health = 100;
	public float detectionBar = 0;
	public float barRate = 0;
	public boolean detected = false;
	public float detectedTimer = 0;
	private int stealthTimer = 0;
	public float nextStealth;
	public boolean allCaptured = false;
	private float oldBarRate;
	public boolean hasWon = false;
	public boolean hasLost = false;
	public int numStealths = 2;
	public boolean isStealth = false;
	public int virusHelpMsg = 0;
	public int timeadd;
	
	public Virus(World gw, float x, float y) {
		super(gw, x, y, Gfx.virusAnim, Faction.PLAYER);
	}
	
	public boolean isStealth()
	{
		//return this.stealthTimer > 0;
		return this.isStealth;
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);

		if( this.detectionBar >= 100 && !this.hasLost )
		{
			this.hasLost = true;
			Sfx.MusicOff();
			Sfx.lose.play();
		}
		
		if( this.stealthTimer > 0 )
		{
			this.stealthTimer -= delta;
		}
		
		if( isStealth() )
		{
			this.alfa = 0.5f;
		}
		else
		{
			this.alfa = 1f;
		}
		
		if( Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !hasLost )
		{
			this.stealthTimer = 100;
			this.isStealth = !this.isStealth;
			this.numStealths++;
		}
		detectedTimer -= delta;
		if( detectedTimer <= 0 && !hasWon && !hasLost )
		{
			detectedTimer = 0;
			detected = false;
			Sfx.Undetected();
		}
		
		if( isStealth() )
		{
			detectionBar += delta * (-20f);
		}
		else
		{
			detectionBar += delta * barRate;			
		}
		if( detectionBar <= 0 ) detectionBar = 0;
		if( detectionBar >= 100 ) detectionBar = 100;
		super.update(delta);
		
		this.virusHelpMsg = 0;
		if( Gdx.input.justTouched() && this.state == IDLE_STATE && !hasLost && !hasWon )
		{
			Node n = this.gw.findNear(new Vector2(Gdx.input.getX() + gw.camera.position.x - gw.camera.viewportWidth/2, Gdx.graphics.getHeight() - Gdx.input.getY() + gw.camera.position.y - gw.camera.viewportHeight/2));
			if( n instanceof GoalNode && !allCaptured )
			{
				this.virusHelpMsg = 1;
			}
			if( n instanceof SpawnerNode )
			{
				this.virusHelpMsg = 2;
			}
			if( (! (n instanceof SpawnerNode) && (!(n instanceof GoalNode))) || ((n instanceof GoalNode) && allCaptured)) {
				this.tryMove(n);
			}
		}
		
	}
	
	private void setTravelStartDetectionRateFor(Node targetNode)
	{
		if( targetNode instanceof StartNode || targetNode instanceof GoalNode )
		{
			barRate = -0.01f;
		}
		else
		{
			if( targetNode.faction == Faction.ENEMY || (targetNode.faction == Faction.PLAYER && this.getCurrentNode().faction == Faction.ENEMY) )
			{
				if( targetNode instanceof WeakNode ) {
					barRate = 4;
					speedFactor = 60;
				} else if( targetNode instanceof MidNode ) {
					barRate = 8;
					speedFactor = 40f;
				} else if( targetNode instanceof HardNode ) {
					barRate = 16;
					speedFactor = 20f;
				}
			}
			else
			{
				if( targetNode instanceof WeakNode ) {
					barRate = -0.005f;
					speedFactor = 90;
				} else if( targetNode instanceof MidNode ) {
					barRate = -0.010f;
					speedFactor = 60;
				} else if( targetNode instanceof HardNode ) {
					barRate = -0.015f;
					speedFactor = 30;
				}
			}
		}
	}

	private void setTravelEndDetectionRateFor(Node targetNode)
	{
		Node cnode = getCurrentNode();
		if( cnode instanceof StartNode )
		{
			barRate = 0;
		}
		else
		{
			if( cnode.faction == Faction.ENEMY )
			{
				if( cnode instanceof WeakNode ) {
					barRate = 2;
				} else if( cnode instanceof MidNode ) {
					barRate = 4;
				} else if( cnode instanceof HardNode ) {
					barRate = 8;
				}
			}
			else
			{
				if( cnode instanceof WeakNode ) {
					barRate = -1f;
				} else if( cnode instanceof MidNode ) {
					barRate = -2.5f;
				} else if( cnode instanceof HardNode ) {
					barRate = -8f;
				}
			}
		}
	}

	@Override
	public void onTravelStart()
	{
		setTravelStartDetectionRateFor(this.targetNode);
	}

	@Override
	public void onTravelEnd()
	{
		if( detected )
		{
			List<SpawnerNode> spawners = this.getCurrentNode().spawnerNeighboors();
			for( SpawnerNode sn : spawners )
			{
				if( !sn.areNeighboorsCaptured() )
				{
					sn.spawnAntibody();
				}
			}
		}
		
		if( !detected && this.getCurrentNode().faction == Faction.ENEMY && gw.connectedSpawners() ) rollDetected();

		if( !detected )
		{
			if( targetNode.faction == Faction.ENEMY )
			{
				targetNode.faction = Faction.PLAYER;
				Sfx.hack.play(1f);
				if( targetNode instanceof WeakNode ) this.timeadd = 5;
				if( targetNode instanceof MidNode ) this.timeadd = 10;
				if( targetNode instanceof HardNode ) this.timeadd = 15;
			}
		}
		setTravelEndDetectionRateFor(targetNode);

		if( getCurrentNode() instanceof GoalNode )
		{
			this.hasWon = true;
			Sfx.MusicOff();
			Sfx.win.play();
		}
	}

	private void rollDetected() {
		float rnd = RNG.rng.nextFloat();
		float p = 0;
		// antes era /16, quedaba mas o menos bien
		if( targetNode instanceof WeakNode ) {
			p = 0.01f * this.detectionBar/8;
		} else if( targetNode instanceof MidNode ) {
			p = 0.05f * this.detectionBar/8;
		} else if( targetNode instanceof HardNode ) {
			p = 0.10f * this.detectionBar/8;
		}
		if( rnd < p ) {
			this.detected = true;
			this.detectedTimer = 20f;
			Sfx.Detected();
			this.gw.spawnAntibodies();
		}
	}

	public void dealDamage(int i) {
		this.detectionBar += i;
	}

}
