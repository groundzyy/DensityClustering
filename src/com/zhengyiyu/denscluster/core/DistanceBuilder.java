package com.zhengyiyu.denscluster.core;

public interface DistanceBuilder {
	// calculate the local density
	public void calculateLocalDensityArray();
	
	// calculate the distance to the higher local density
	public void calculateMinDistance2HigherLocalDensityArray();
}
