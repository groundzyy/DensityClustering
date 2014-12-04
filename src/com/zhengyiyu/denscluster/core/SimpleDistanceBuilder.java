package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimpleDistanceBuilder extends AbstractDistanceBuilder {

	private double[][] distanceMatrix;
	
	public SimpleDistanceBuilder(double[][] distMatrix, DensityCalculator densCalc) {
		super(densCalc);
		this.distanceMatrix = distMatrix;
		
		// Temporarily get the max distance here, might be done when calculating local density
		for (int i = 0; i < distMatrix.length; i++) {
			for (double d : distMatrix[i]) {
				if (d > this.getMaxDistance()) {
					this.setMaxDistance(d);
				}
			}
		}
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
	public ArrayList<Instance> calculateLocalDensityArray(double distanceCutoff) {
		ArrayList<Instance> instancesWithRho = new ArrayList<Instance>();
		
		for (int instIndex = 0; instIndex < distanceMatrix.length; instIndex++) {
			double rho = this.getDensCalc().calculateDensity(distanceCutoff, distanceMatrix[instIndex], instIndex);
			Instance inst = new Instance(instIndex, rho);
			instancesWithRho.add(inst);
		}
		
		return instancesWithRho;
	}

	@Override
	public void calculateMinDistance2HigherLocalDensityArray(ArrayList<Instance> insts) {
		// sort by density
		// calculate the distance
		Collections.sort(insts, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return Double.compare(o1.getRho(), o2.getRho());
			}
		});
		Collections.reverse(insts);
		
		ArrayList<Instance> higherDensityInstList = new ArrayList<Instance>();
		for (int index = 0; index < insts.size(); index++) {
			Instance inst = insts.get(index);
			if (index == 0) {
				// max value
				// actually the else section will do the same thing..
				inst.setDelta(this.getMaxDistance());
				inst.setClosestHigherDensityInstance(null);
			} else {
				// find the min distance with all instance with higher local density
				double min = this.getMaxDistance();
				double[] distRow = this.distanceMatrix[inst.getIndex()];
				
				for (Instance higherDensityInstance : higherDensityInstList) {
					if (distRow[higherDensityInstance.getIndex()] < min) {
						min = distRow[higherDensityInstance.getIndex()];
						inst.setClosestHigherDensityInstance(higherDensityInstance);
					}
				}
				inst.setDelta(min);
			}
			higherDensityInstList.add(inst);
		}
	}

	@Override
	public int getInstanceNumber() {
		return distanceMatrix.length;
	}

	@Override
	public double[] calculateRhoBorder(ArrayList<Instance> instances,
			int numOfCluster) {
		// TODO Auto-generated method stub
		return null;
	}

}
