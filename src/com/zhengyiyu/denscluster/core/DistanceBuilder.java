package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;

public interface DistanceBuilder {
	public static double DistanceCutoffPercentage_Default = 0.02;
	
	// estimate distance cutoff
	public double estimateDistanceCutoff(double lower, double upper);
	
	// calculate the local density
	public ArrayList<Instance> calculateLocalDensityArray(double distanceCutoff);
	
	// calculate the distance to the higher local density
	public void calculateMinDistance2HigherLocalDensityArray(ArrayList<Instance> insts);
	
	// return the number of instance to cluster
	public int getInstanceNumber();

	public double[] calculateRhoBorder(ArrayList<Instance> instances, int numOfCluster);
}
