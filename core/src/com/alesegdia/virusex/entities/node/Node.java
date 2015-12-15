package com.alesegdia.virusex.entities.node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.virusex.RNG;
import com.alesegdia.virusex.entities.Entity;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.World;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Node extends Entity {
	
	protected World gw;
	public float capturedTimer = RNG.rng.nextFloat();

	public Node(World world, float x, float y, Animation anim, Faction faction) {
		super(x, y, anim, faction);
		this.gw = world;
	}
	
	public void removeLinkWith( Node n )
	{
		neighboors.remove(n);
		this.gw.removeLink( n, this );
	}
	
	@Override
	public void update(float delta )
	{
		super.update(delta);
		this.capturedTimer += delta;
	}
	
	public List<Node> neighboors = new ArrayList<Node>();
	public float cost = 1;

	public List<SpawnerNode> spawnerNeighboors() {
		List<SpawnerNode> spawners = new LinkedList<SpawnerNode>();
		for( Node n : neighboors )
		{
			if( n instanceof SpawnerNode )
			{
				spawners.add((SpawnerNode)n);
			}
		}
		return spawners;
	}

	public boolean areNeighboorsCaptured() {
		for( Node n : neighboors )
		{
			if( n.faction == Faction.ENEMY ) return false;
		}
		return true;
	}

}
