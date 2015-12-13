package com.alesegdia.virusex.entities.node;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.Faction;

public class HardNode extends Node {

	public HardNode(World w, float x, float y) {
		super(w, x, y, Gfx.hardNodeAnim, Faction.ENEMY);
		// TODO Auto-generated constructor stub
	}

}
