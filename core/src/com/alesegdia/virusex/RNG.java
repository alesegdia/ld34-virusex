package com.alesegdia.virusex;

import java.util.Random;

public class RNG extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static RNG rng;
	
	public RNG()
	{
		super();
		rng = this;
	}
	
	public int nextInt( int min, int max )
	{
		return nextInt( (max - min) + 1 ) + min;
	}
	
}
