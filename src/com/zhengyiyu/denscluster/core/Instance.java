package com.zhengyiyu.denscluster.core;

public class Instance {
	private int index;
	private double rho;
	private double delta;
	
	private Instance closestHigherDensityInstance;
	private int clusterIndex;
	
	private boolean isHalo;
	
	/**
	 * @param index
	 * @param rho
	 */
	public Instance(int index, double rho) {
		super();
		this.index = index;
		this.rho = rho;
		
		this.clusterIndex = -1;
	}
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * @return the rho
	 */
	public double getRho() {
		return rho;
	}
	
	/**
	 * @param rho the rho to set
	 */
	public void setRho(double rho) {
		this.rho = rho;
	}
	
	/**
	 * @return the delta
	 */
	public double getDelta() {
		return delta;
	}
	
	/**
	 * @param delta the delta to set
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getGamma() {
		return this.rho * this.delta;
	}

	/**
	 * @return the clusterIndex
	 */
	public int getClusterIndex() {
		return clusterIndex;
	}

	/**
	 * @param clusterIndex the clusterIndex to set
	 */
	public void setClusterIndex(int clusterIndex) {
		this.clusterIndex = clusterIndex;
	}

	/**
	 * @return the closestHigherDensityInstance
	 */
	public Instance getClosestHigherDensityInstance() {
		return closestHigherDensityInstance;
	}

	/**
	 * @param closestHigherDensityInstance the closestHigherDensityInstance to set
	 */
	public void setClosestHigherDensityInstance(
			Instance closestHigherDensityInstance) {
		this.closestHigherDensityInstance = closestHigherDensityInstance;
	}

	/**
	 * @return the isHalo
	 */
	public boolean isHalo() {
		return isHalo;
	}

	/**
	 * @param isHalo the isHalo to set
	 */
	public void setHalo(boolean isHalo) {
		this.isHalo = isHalo;
	}
	
}
