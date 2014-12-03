package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleDistanceBuilder extends AbstractDistanceBuilder {

	private double[][] distanceMatrix;
	
	public SimpleDistanceBuilder(double[][] distMatrix, DensityCalculator densCalc) {
		super(densCalc);
		this.distanceMatrix = distMatrix;
	}

	@Override
	public double estimateDistanceCutoff(double lower, double upper) {
		// might want to check the range of lower and upper
		// however, here only the mean is needed and if the mean is out of range (0, 1), the default value is used
		
		ArrayList<Double> doubleList = new ArrayList<Double>();
		for (double[] distRow : distanceMatrix) {
			for (double dist : distRow) {
				doubleList.add(dist);
			}
		}
		
		if (doubleList.size() <= 0) {
			return Double.NaN;
		}
		
		Collections.sort(doubleList);
		
		double percentage = (lower + upper) / 2;
		
		if (percentage <= 0 || percentage >= 1) {
			percentage = DistanceCutoffPercentage_Default;
		}
		
		int cutoffPosition = (int) (doubleList.size() * percentage);
		
		if (cutoffPosition > doubleList.size() || cutoffPosition < 0) {
			// should not happen
			cutoffPosition = doubleList.size() / 2;
		}
		
		return doubleList.get(cutoffPosition);
	}

	@Override
	public ArrayList<Instance> calculateLocalDensityArray() {
		ArrayList<Instance> instancesWithRho = new ArrayList<Instance>();
		
		
		return instancesWithRho;
	}

	@Override
	public void calculateMinDistance2HigherLocalDensityArray(ArrayList<Instance> insts) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getInstanceNumber() {
		return distanceMatrix.length;
	}

}
