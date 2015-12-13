package com.alesegdia.virusex.entities.organism;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.Entity;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.node.Node;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Organism extends Entity {

	public static final int MOVING_STATE = 0;
	public static final int IDLE_STATE = 1;
	
	protected World gw;
	protected Pathfinder pf;
	protected int state = IDLE_STATE;
	protected float speedFactor = 1;
	private Vector2 speed = new Vector2(0,0);
	protected Node targetNode;
	
	public Node lastVisitedNode = null;

	public Organism(World gw, float x, float y, Animation anim, Faction faction) {
		super(x, y, anim, faction);
		this.gw = gw;
		this.pf = new Pathfinder(gw.getRootNode());
		this.lastVisitedNode = gw.findNear(this.position);
	}
	
	public void onTravelStart()
	{
		
	}
	
	public void onTravelEnd()
	{
		
	}
	
	public Node getCurrentNode()
	{
		return gw.findNear(this.position);
	}

	public Node findNextPathNode( Node target )
	{
		return getCurrentNode();
		//return pf.findNextNode( getCurrentNode(), target );
	}
	
	public void recomputeSpeed()
	{
		this.speed = this.speed.nor().scl(speedFactor);
	}
	
	public boolean tryMove( Node n )
	{
		Node currentNode = this.getCurrentNode();
		if( currentNode == null || !currentNode.neighboors.contains(n) )
		{
			return false;
		}
		this.targetNode = n;
		
		onTravelStart();

		this.speed = new Vector2(this.targetNode.position);
		this.speed = this.speed.sub(this.position).nor().scl(speedFactor);
		
		this.state = MOVING_STATE;
		
		return true;
	}
	
	Vector2 tmp = new Vector2(0,0);
	public void update(float delta)
	{
		super.update(delta);
		
		if( this.state == MOVING_STATE )
		{
			this.position = this.position.add(this.speed);
			tmp.x = this.position.x;
			tmp.y = this.position.y;
			float len = tmp.sub(targetNode.position).len();
			if( len < 5 )
			{
				if( this.lastVisitedNode != targetNode )
				{
					onTravelEnd();
				}
				this.state = IDLE_STATE;
				this.lastVisitedNode = gw.findNear(this.position);
			}
		}
	}

}
