package com.zhengyiyu.denscluster.test;

import java.util.ArrayList;

import com.zhengyiyu.denscluster.core.CutoffDensityCalculator;
import com.zhengyiyu.denscluster.core.DensityClusterer;
import com.zhengyiyu.denscluster.core.DistanceBuilder;
import com.zhengyiyu.denscluster.core.GaussianKernalDensityCalculator;
import com.zhengyiyu.denscluster.core.Instance;
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
		
		// number of item
		// but of no use...
		dClusterer.setNumOfItem(distBuilder.getInstanceNumber());
		
		
		// distance cutoff related
		dClusterer.setDistanceCutoff(distanceCutoff);
		double estimatedDistanceCutoff = distBuilder.estimateDistanceCutoff(0.02, 0.02);
		System.out.println("Estimated Distance Cutoff : " + estimatedDistanceCutoff);
		
		// calculate density and min distance
		dClusterer.calculateLocalDensityArray();
		dClusterer.calculateMinDistance2HigherLocalDensityArray();
		
		// now we want to determine the number of clusters by cutting based on the gamma graph
		// finally determine the assignment of clusters and output

		// TODO estimate gamma or rho + delta cutoff by drawing decision graph
		double gammaCutoff = 0.05;
		ArrayList<ArrayList<Instance>> clusters = dClusterer.cluster(gammaCutoff);
		dClusterer.recordCluster(clusters, ".//sample//example_by_gamma.cluster");

		double rhoCutoff = 0.5;
		double deltaCutoff = 0.5;
		ArrayList<ArrayList<Instance>> clusters2 = dClusterer.cluster(rhoCutoff, deltaCutoff);
		dClusterer.recordCluster(clusters, ".//sample//example_by_rhodelta.cluster");
	}
}
