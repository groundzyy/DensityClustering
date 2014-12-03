package com.zhengyiyu.denscluster.test;

import java.util.ArrayList;

import com.zhengyiyu.denscluster.core.CutoffDensityCalculator;
import com.zhengyiyu.denscluster.core.DensityClusterer;
import com.zhengyiyu.denscluster.core.DistanceBuilder;
import com.zhengyiyu.denscluster.core.GaussianKernalDensityCalculator;
import com.zhengyiyu.denscluster.core.SimpleDistanceBuilder;
import com.zhengyiyu.denscluster.util.FileUtil;

public class TestDensityClusterer {
	public static void main(String[] args) {
		// the cutoff estimated by the sample matlab code by the authors, 
	    // which is based only on the distance in the input 3 column files, 
		// while our estimation is based on the full distance matrix, so it is slightly different.
				
		double distanceCutoff = 0.033; 
		String distanceFilePath = ".//sample//example_distances.dat";
		
		ArrayList<String> instanceList = new ArrayList<String>();
		double[][] distMatrix = FileUtil.load3ColumnDistanceMatrix(distanceFilePath, instanceList);
		
		DensityClusterer dClusterer = new DensityClusterer();
		SimpleDistanceBuilder distBuilder = new SimpleDistanceBuilder(distMatrix, new GaussianKernalDensityCalculator());
		dClusterer.setDistanceBuilder(distBuilder);
		dClusterer.setNumOfItem(distBuilder.getInstanceNumber());
		
		dClusterer.setDistanceCutoff(distanceCutoff);
		
		double estimatedDistanceCutoff = distBuilder.estimateDistanceCutoff(0.02, 0.02);
		System.out.println("Estimated Distance Cutoff : " + estimatedDistanceCutoff);
		
		distBuilder.calculateLocalDensityArray();
		// TODO calculate density for each point
		
		// TODO calculate distance for each point
	}
}
