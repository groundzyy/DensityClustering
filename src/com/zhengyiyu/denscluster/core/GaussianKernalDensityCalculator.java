package com.zhengyiyu.denscluster.core;

public class GaussianKernalDensityCalculator extends DensityCalculator {

	@Override
	public double calculateDensity(double distanceCutoff, double distance) {
		double d = distance / distanceCutoff;
		return Math.exp(- (d * d));
	}
}
