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
		
		double maxDelta = 0.0;
		
		for (int rhoIndex = 0; rhoIndex < insts.size(); rhoIndex++) {
			double[] distRow = this.distanceMatrix[insts.get(rhoIndex).getIndex()];
			maxDelta = calculateMinDistance2HigherDensityInstances(insts, rhoIndex, distRow, maxDelta);
		}
		
		// In the matlab code, the delta for the point with highest rho are set as the largest delta for all the other points, 
		// since this setting will not affect clustering decision, but make the decision chart better to inteprate.
		
		insts.get(0).setDelta(maxDelta);
	}

	@Override
	public int getInstanceNumber() {
		return distanceMatrix.length;
	}

	@Override
	public double[] calculateRhoBorder(ArrayList<Instance> instances, int numOfCluster) {
		double[] rhoBorderArray = new double[numOfCluster];
		
		for (int i = 0; i < instances.size(); i++) {
			Instance instA = instances.get(i);
			
			for (int j = i + 1; j < instances.size(); j++) {
				Instance instB = instances.get(j);
				
				// the border regions for each cluster are defined as the set of points assigned to this cluster, 
				// and be within a distance cutoff from points belonging to other clusters (thus all pairs of two points in different clusters will be checked)
				// the border cutoff is defined by the point of highest density within its border region
				// However in the matlab code provide by the authors, they used the average of the rho of two points as the border.
				
				if (instA.getClusterIndex() != instB.getClusterIndex() && distanceMatrix[i][j] < this.getDistanceCutoff()) {
					double rhoAve = (instA.getRho() + instB.getRho()) / 2;
					if (rhoAve > rhoBorderArray[instA.getClusterIndex()]) {
						rhoBorderArray[instA.getClusterIndex()] = rhoAve;
					}
					
					if (rhoAve > rhoBorderArray[instB.getClusterIndex()]) {
						rhoBorderArray[instB.getClusterIndex()] = rhoAve;
					}
				}
			}
		}
		return rhoBorderArray;
	}

}
