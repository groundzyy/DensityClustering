package com.zhengyiyu.denscluster.core;

public abstract class AbstractDistanceBuilder implements DistanceBuilder {
	private DensityCalculator densCalc;

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
	
	
}
