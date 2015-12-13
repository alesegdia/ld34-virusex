package com.alesegdia.virusex.entities.organism;

import com.alesegdia.virusex.RNG;
import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
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
	
	public Virus(World gw, float x, float y) {
		super(gw, x, y, Gfx.virusAnim, Faction.PLAYER);
	}
	
	public boolean isStealth()
	{
		return this.stealthTimer > 0;
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
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
		
		if( Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) )
		{
			this.stealthTimer = 100;
		}
		detectedTimer -= delta;
		if( detectedTimer <= 0 )
		{
			detectedTimer = 0;
			detected = false;
		}
		detectionBar += delta * barRate;
		if( detectionBar <= 0 ) detectionBar = 0;
		if( detectionBar >= 100 ) detectionBar = 100;
		super.update(delta);
		
		if( Gdx.input.justTouched() && this.state == IDLE_STATE )
		{
			Node n = this.gw.findNear(new Vector2(Gdx.input.getX() + gw.camera.position.x - gw.camera.viewportWidth/2, Gdx.graphics.getHeight() - Gdx.input.getY() + gw.camera.position.y - gw.camera.viewportHeight/2));
			this.tryMove(n);
		}
	}
	
	private void setTravelDetectionRateFor(Node targetNode)
	{
		if( targetNode.faction == Faction.ENEMY )
		{
			if( targetNode instanceof WeakNode ) {
				barRate = 1;
				speedFactor = 2;
			} else if( targetNode instanceof MidNode ) {
				barRate = 5;
				speedFactor = 1;
			} else if( targetNode instanceof HardNode ) {
				barRate = 10;
				speedFactor = 0.5f;
			}
		}
		else
		{
			if( targetNode instanceof WeakNode ) {
				barRate = -0.5f;
				speedFactor = 4;
			} else if( targetNode instanceof MidNode ) {
				barRate = -2.5f;
				speedFactor = 5;
			} else if( targetNode instanceof HardNode ) {
				barRate = -5;
				speedFactor = 6;
			}
		}
	}

	@Override
	public void onTravelStart()
	{
		setTravelDetectionRateFor(this.targetNode);
	}

	@Override
	public void onTravelEnd()
	{
		if( !detected && this.getCurrentNode().faction == Faction.ENEMY ) rollDetected();

		targetNode.faction = Faction.PLAYER;
		if( targetNode.faction == Faction.ENEMY ) {
			targetNode.faction = Faction.PLAYER;
		}
		setTravelDetectionRateFor(targetNode);
	}

	private void rollDetected() {
		float rnd = RNG.rng.nextFloat();
		float p = 0;
		if( targetNode instanceof WeakNode ) {
			p = 0.2f;
		} else if( targetNode instanceof MidNode ) {
			p = 0.5f;
		} else if( targetNode instanceof HardNode ) {
			p = 0.7f;
		}
		if( rnd < p ) {
			this.detected = true;
			this.detectedTimer = 20f;
			this.gw.spawnAntibodies();
		}
	}

	public void dealDamage(int i) {
		this.detectionBar += i;
	}

}
