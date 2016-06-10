package com.zhengyiyu.denscluster.core;

public abstract class DensityCalculator {
	
	public abstract double calculateDensity(double distanceCutoff, double distance);
	
	public double calculateDensity(double distanceCutoff, double[] distanceArray, int selfIndex) {
		double rho = 0.0;
		
		for (int i = 0; i < distanceArray.length; i++) {
			if (i == selfIndex) {
				continue;
			}
			rho += calculateDensity(distanceCutoff, distanceArray[i]);
		}
		return rho;
	}
}
