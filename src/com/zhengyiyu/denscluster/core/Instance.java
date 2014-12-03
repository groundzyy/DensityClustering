package com.zhengyiyu.denscluster.core;

public class Instance {
	private int index;
	private double rho;
	private double delta;
	
	/**
	 * @param index
	 * @param rho
	 */
	public Instance(int index, double rho) {
		super();
		this.index = index;
		this.rho = rho;
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
}
