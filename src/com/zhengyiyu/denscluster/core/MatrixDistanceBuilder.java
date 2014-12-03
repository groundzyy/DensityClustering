package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;

public class MatrixDistanceBuilder extends AbstractDistanceBuilder {

	private double[][] distanceMatrix;
	
	public MatrixDistanceBuilder(DensityCalculator densCalc, double[][] distMatrix) {
		super(densCalc);
		this.distanceMatrix = distMatrix;
	}

	/**
	 * @return the distanceMatrix
	 */
	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	/**
	 * @param distanceMatrix the distanceMatrix to set
	 */
	public void setDistanceMatrix(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	@Override
	public double estimateDistanceCutoff(double lower, double upper) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInstanceNumber() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public ArrayList<Instance> calculateLocalDensityArray() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void calculateMinDistance2HigherLocalDensityArray(
			ArrayList<Instance> insts) {
		// TODO Auto-generated method stub
		
	}

}
