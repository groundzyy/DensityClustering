package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;

public abstract class AbstractDistanceBuilder implements DistanceBuilder {
	private DensityCalculator densCalc;
	
	private double maxDistance;
	
	private double distanceCutoff;

	/**
	 * @param densCalc
	 */
	public AbstractDistanceBuilder(DensityCalculator densCalc) {
		super();
		this.densCalc = densCalc;
	}

	/**
	 * @return the densCalc
	 */
	public DensityCalculator getDensCalc() {
		return densCalc;
	}

	/**
	 * @param densCalc the densCalc to set
	 */
	public void setDensCalc(DensityCalculator densCalc) {
		this.densCalc = densCalc;
	}

	/**
	 * @return the maxDistance
	 */
	public double getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
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
	
	public double calculateMinDistance2HigherDensityInstances(ArrayList<Instance> insts, int rhoIndex, double[] distRow, double maxDelta) {
		Instance inst = insts.get(rhoIndex);

		if (rhoIndex == 0) {
			// max value
			// actually the else section will do the same thing..
			
			double max = distRow[0];
			for (double d : distRow) {
				if (d > max) {
					max = d;
				}
			}
			inst.setDelta(max);
			inst.setClosestHigherDensityInstance(null);
		} else {
			// find the min distance with all instance with higher local density
			double min = this.getMaxDistance();
			
			for (int higherDensityInstIndex = 0; higherDensityInstIndex < rhoIndex; higherDensityInstIndex++) {
				Instance higherDensityInstance = insts.get(higherDensityInstIndex);
				
				if (distRow[higherDensityInstance.getIndex()] < min) {
					min = distRow[higherDensityInstance.getIndex()];
					inst.setClosestHigherDensityInstance(higherDensityInstance);
				}
			}
			inst.setDelta(min);
			
			if (min > maxDelta) {
				maxDelta = min;
			}
		}
		return maxDelta;
	}
}
