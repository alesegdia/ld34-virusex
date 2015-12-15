package com.alesegdia.virusex.entities.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Link {

	public Node nodeA;
	public Node nodeB;
	public boolean isCaptured = false;
	
	public Link(Node nodeA, Node nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}

	public void render(ShapeRenderer srenderer)
	{
		Gdx.gl20.glLineWidth(2);
		if( nodeA instanceof GoalNode || nodeB instanceof GoalNode )
		{
			srenderer.setColor(0,0,1,1);
		}
		else if( nodeA instanceof SpawnerNode || nodeB instanceof SpawnerNode )
		{
			srenderer.setColor(1,1,0,1);
		}
		else if( isCaptured )
		{
			srenderer.setColor(0, 0.75f, 0, 0.1f);
		}
		else
		{
			srenderer.setColor(0.75f, 0, 0, 0.1f);						
		}
		srenderer.line(nodeA.position, nodeB.position);
		srenderer.setColor(1,1,1,1);
	}

}
