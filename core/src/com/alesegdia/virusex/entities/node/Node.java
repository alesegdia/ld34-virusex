package com.alesegdia.virusex.entities.node;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.Entity;
import com.alesegdia.virusex.entities.Faction;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Node extends Entity {
	
	protected World gw;

	public Node(World world, float x, float y, Animation anim, Faction faction) {
		super(x, y, anim, faction);
		this.gw = world;
	}
	
	public void removeLinkWith( Node n )
	{
		neighboors.remove(n);
		this.gw.removeLink( n, this );
	}
	
	public List<Node> neighboors = new ArrayList<Node>();
	public float cost = 1;

}
