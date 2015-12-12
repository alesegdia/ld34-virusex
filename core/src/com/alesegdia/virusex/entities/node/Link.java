package com.alesegdia.virusex.entities.node;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Link {

	public Node nodeA;
	public Node nodeB;
	
	public Link(Node nodeA, Node nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}

	public void render(ShapeRenderer srenderer)
	{
		srenderer.line(nodeA.position, nodeB.position);
	}

}
