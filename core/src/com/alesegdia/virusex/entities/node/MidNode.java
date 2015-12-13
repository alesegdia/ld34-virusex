package com.alesegdia.virusex.entities.node;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.Faction;

public class MidNode extends Node {

	public MidNode(World w, float x, float y) {
		super(w, x, y, Gfx.midNodeAnim, Faction.ENEMY);
	}

}
