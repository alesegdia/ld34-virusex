package com.alesegdia.virusex.entities.node;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Node extends Entity {
	
	private World gw;

	public Node(World world, float x, float y, Animation anim) {
		super(x, y, anim);
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
