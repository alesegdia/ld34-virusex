package com.alesegdia.virusex.entities.organism;

import java.util.List;

import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.World;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.badlogic.gdx.math.Vector2;

public class Antibody extends Organism {

	SpawnerNode spawner;
	
	public Antibody(SpawnerNode spawner, World gw, float x, float y) {
		super(gw, x, y, Gfx.enemyAnim, Faction.ENEMY);
		this.speedFactor = 0.7f;
		this.spawner = spawner;
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		if( gw.getVirus() != null && gw.getVirus().detected == true )
		{

			if( state == IDLE_STATE )
			{
				List<Node> path = pf.findNextNode(this.getCurrentNode(), gw.getVirus().getCurrentNode());
				if( path.size() < 2)
				{
					this.tryMove(gw.getVirus().targetNode);
				}
				else
				{
					this.tryMove(path.get(path.size()-2));
				}
			}
		}
		else
		{
			if( state == IDLE_STATE )
			{
				List<Node> path = pf.findNextNode(this.getCurrentNode(), spawner);
				if( path.size() < 2 )
				{
					this.gw.removeAntibody(this);
				}
				else
				{
					this.tryMove(path.get(path.size()-2));
				}
				
				if( this.getCurrentNode() instanceof SpawnerNode )
				{
					this.gw.removeAntibody(this);
				}
			}
		}
		
		Vector2 v = new Vector2(this.position);
		v.sub(gw.getVirus().position);
		
		if( v.len() < 10 && !gw.getVirus().isStealth() )
		{
			this.gw.removeAntibody(this);
			this.gw.getVirus().dealDamage(50);
		}

		onTravelStart();
	}
	
	@Override
	public void onTravelStart()
	{
		if( gw.getVirus().detected )
		{
			if( this.targetNode.faction == Faction.ENEMY || this.lastVisitedNode.faction == Faction.ENEMY )
			{
				this.speedFactor = 90;
				this.recomputeSpeed();
			}
			else
			{
				this.speedFactor = 50;
				this.recomputeSpeed();
			}
		}
		else
		{
			this.speedFactor = 160;
			this.recomputeSpeed();
		}
	}

}
