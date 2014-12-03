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
	 * 
	 */
	public DensityClusterer() {
		super();
	}
	
	/**
	 * @param distanceCutoff
	 */
	public DensityClusterer(double distanceCutoff) {
		super();
		this.distanceCutoff = distanceCutoff;
		
		this.densityList = new ArrayList<Integer>();
		this.minDistanceList = new ArrayList<Double>();
	}

	/**
	 * @return the distanceCutoff
	 */
	public double getDistanceCutoff() {
		return distanceCutoff;
	}

	/**
	 * @param distanceCutoff the distanceCutoff to set
	 */
	public void setDistanceCutoff(double distanceCutoff) {
		this.distanceCutoff = distanceCutoff;
	}

	/**
	 * @return the distanceBuilder
	 */
	public DistanceBuilder getDistanceBuilder() {
		return distanceBuilder;
	}

	/**
	 * @param distanceBuilder the distanceBuilder to set
	 */
	public void setDistanceBuilder(DistanceBuilder distanceBuilder) {
		this.distanceBuilder = distanceBuilder;
	}

	/**
	 * @return the numOfItem
	 */
	public int getNumOfItem() {
		return numOfItem;
	}

	/**
	 * @param numOfItem the numOfItem to set
	 */
	public void setNumOfItem(int numOfItem) {
		this.numOfItem = numOfItem;
	}

	/**
	 * @return the densityList
	 */
	public ArrayList<Integer> getDensityList() {
		return densityList;
	}

	/**
	 * @param densityList the densityList to set
	 */
	public void setDensityList(ArrayList<Integer> densityList) {
		this.densityList = densityList;
	}

	/**
	 * @return the minDistanceList
	 */
	public ArrayList<Double> getMinDistanceList() {
		return minDistanceList;
	}

	/**
	 * @param minDistanceList the minDistanceList to set
	 */
	public void setMinDistanceList(ArrayList<Double> minDistanceList) {
		this.minDistanceList = minDistanceList;
	}
	
}
