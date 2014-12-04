package com.zhengyiyu.denscluster.core;

public abstract class AbstractDistanceBuilder implements DistanceBuilder {
	private DensityCalculator densCalc;
	
	private double maxDistance;

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
	
}
