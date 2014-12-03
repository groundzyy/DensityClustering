package com.zhengyiyu.denscluster.core;

import java.util.ArrayList;

public class MatrixFileDistanceBuilder extends AbstractDistanceBuilder {
	
	private String matrixFilePath;

	public MatrixFileDistanceBuilder(DensityCalculator densCalc, String filePath) {
		super(densCalc);
		this.matrixFilePath = filePath;
	}

	/**
	 * @return the matrixFilePath
	 */
	public String getMatrixFilePath() {
		return matrixFilePath;
	}

	/**
	 * @param matrixFilePath the matrixFilePath to set
	 */
	public void setMatrixFilePath(String matrixFilePath) {
		this.matrixFilePath = matrixFilePath;
	}

	@Override
	public double estimateDistanceCutoff(double lower, double upper) {
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

	@Override
	public int getInstanceNumber() {
		// TODO Auto-generated method stub
		return 0;
	}


}
