package com.alesegdia.virusex.entities.organism;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.Entity;
import com.alesegdia.virusex.entities.node.Node;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Organism extends Entity {
	
	private World gw;
	
	public Node getCurrentNode()
	{
		return gw.findNear(this.position);
	}

	public Organism(World gw, float x, float y, Animation anim) {
		super(x, y, anim);
		this.gw = gw;
	}
	
	

}
