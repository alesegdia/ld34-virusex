package com.alesegdia.virusex.entities.organism;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import com.alesegdia.virusex.entities.node.Node;
import com.badlogic.gdx.math.Vector2;

public class Pathfinder {
	
	class MapNode {
		public Node n;
		public MapNode parent;
		public float distance;
		public List<MapNode> neighboors = new LinkedList<MapNode>();
		public float cost(MapNode n2) {
			Vector2 tmp = new Vector2(n2.n.position);
			return tmp.sub(n.position).len() + n2.n.cost; // + random()? para aleatorizar un poco los caminos elegidos
		}
	}
	
	List<MapNode> mapnodes = new LinkedList<MapNode>();
	
	public Pathfinder( Node base )
	{
		Deque<Node> toprocess = new ArrayDeque<Node>();
		toprocess.add(base);
		
		List<Node> processed = new ArrayList<Node>();
		
		while( !toprocess.isEmpty() )
		{
			Node n = toprocess.poll();
			MapNode mn = new MapNode();
			mn.n = n;
			mn.distance = Float.MAX_VALUE;
			mn.parent = null;
			mapnodes.add(mn);
			toprocess.addAll(n.neighboors);
			processed.add(n);
			toprocess.removeAll(processed);
		}
		
		for( MapNode mn : mapnodes )
		{
			for( Node n : mn.n.neighboors )
			{
				MapNode mnn = findMapNode(n);
				mn.neighboors.add(mnn);
			}
		}
	}

	public MapNode findMapNode(  Node n )
	{
		for( MapNode mn : mapnodes )
		{
			if( mn.n == n )
			{
				return mn;
			}
		}
		return null;
	}
	
	public List<Node> findNextNode(Node source, Node target)
	{
		MapNode mnsource = findMapNode(source);

		List<MapNode> q = new LinkedList<MapNode>();

		for( MapNode mn : mapnodes )
		{
			mn.distance = Float.MAX_VALUE;
			mn.parent = null;
			q.add(mn);
		}

		mnsource.distance = 0;
		
		while( !q.isEmpty() )
		{
			MapNode u = getNodeMinimumDist(q);
			q.remove(u);

			for( MapNode v : u.neighboors )
			{
				float alt = u.distance + v.cost(u);
				if( alt < v.distance )
				{
					v.distance = alt;
					v.parent = u;
				}
			}
		}
		
		List<Node> nodes = new LinkedList<Node>();
		MapNode mntarget = findMapNode(target);
		MapNode current = mntarget;
		while( current != null )
		{
			nodes.add(current.n);
			current = current.parent;
		}
		return nodes;
	}

	private MapNode getNodeMinimumDist(List<MapNode> l) {
		float min_dist = l.get(0).distance;
		MapNode min = l.get(0);
		for( MapNode mn : l )
		{
			if( mn.distance < min_dist )
			{
				min = mn;
				min_dist = mn.distance;
			}
		}
		
		return min;
	}
	

}
