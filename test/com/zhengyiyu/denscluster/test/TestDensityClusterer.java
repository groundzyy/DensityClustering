package com.zhengyiyu.denscluster.test;

import com.zhengyiyu.denscluster.core.DensityClusterer;

public class TestDensityClusterer {
	public static void main(String[] args) {
		double distanceCutoff = 0.35;
		
		DensityClusterer dClusterer = new DensityClusterer(distanceCutoff);
	}
}
