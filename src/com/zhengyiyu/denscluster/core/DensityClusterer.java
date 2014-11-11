package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;

public class DensityClusterer {
	/**
	 * distance cutoff to count local density, currently fixed in the paper, 
	 * later may consider allow adjustment to this cutoff
	 * I guess should >= 0, but currently no check
	 */
	private double distanceCutoff;
	
	/**
	 * load distance matrix
	 * calculate densityList
	 * calculate minDistanceList
	 */
	private DistanceBuilder distanceBuilder;

	// it is hard to set the numOfItem at first, so should set it in the distanceBuild
	int numOfItem;

	// based on densityList and minDistanceList, we can draw decision graph
	ArrayList<Integer> densityList;
	ArrayList<Double> minDistanceList;
	
	/**
	 * @param distanceCutoff
	 */
	public DensityClusterer(double distanceCutoff) {
		super();
		this.distanceCutoff = distanceCutoff;
		
		this.densityList = new ArrayList<Integer>();
		this.minDistanceList = new ArrayList<Double>();
	}
	
}
