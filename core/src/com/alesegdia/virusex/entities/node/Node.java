package com.alesegdia.virusex.entities.node;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.virusex.entities.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Node extends Entity {

	public Node(float x, float y, Animation anim) {
		super(x, y, anim);
	}
	
	public List<Node> neighboors = new ArrayList<Node>();

}
