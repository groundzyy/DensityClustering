package com.zhengyiyu.denscluster.core;

public class CutoffDensityCalculator extends DensityCalculator {
	@Override
	public double calculateDensity(double distanceCutoff, double distance) {
		return distance > distanceCutoff ? 0 : 1;
	}
}
