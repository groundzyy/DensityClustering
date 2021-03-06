package com.zhengyiyu.denscluster.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JDialog;

import com.zhengyiyu.denscluster.core.DensityClusterer;
import com.zhengyiyu.denscluster.core.GaussianKernalDensityCalculator;
import com.zhengyiyu.denscluster.core.Instance;
import com.zhengyiyu.denscluster.core.SimpleDistanceBuilder;
import com.zhengyiyu.denscluster.gui.GammaDescionGraphPanel;
import com.zhengyiyu.denscluster.gui.RhoDeltaDescionGraphPanel;
import com.zhengyiyu.denscluster.util.FileUtil;

public class TestDensityClusterer_SimpleDistanceBuilder {
	public static void main(String[] args) throws IOException {
		// the cutoff estimated by the sample matlab code by the authors, 
	    // which is based only on the distance in the input 3 column files, 
		// while our estimation is based on the full distance matrix, so it is slightly different.
				
		double distanceCutoff = 0.033; 
		String distanceFilePath = ".//sample//example_distances.dat";
		
		ArrayList<String> instanceList = new ArrayList<String>();
		double[][] distMatrix = FileUtil.load3ColumnDistanceMatrix(distanceFilePath, instanceList);
		
		distMatrix = FileUtil.loadDistanceMatrix(distanceFilePath);
		
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

		double gammaCutoff = decideClusterByGamma(dClusterer);
//		System.out.println("Gamma Cutoff: " + gammaCutoff);
//		ArrayList<ArrayList<Instance>> clusters = dClusterer.cluster(gammaCutoff);
//		dClusterer.recordCluster(clusters, ".//sample//example_by_gamma.cluster");

		double[] rhoDeltaCutoff = decideClusterByRhoDelta(dClusterer);
		ArrayList<ArrayList<Instance>> clusters2 = dClusterer.cluster(rhoDeltaCutoff[0], rhoDeltaCutoff[1]);
		dClusterer.recordCluster(clusters2, ".//sample//example_by_rhodelta.cluster");
		
	}

	public static double decideClusterByGamma(DensityClusterer dClusterer) {
		double gammaCutoff = dClusterer.getInstances().get(0).getGamma();
		
		final JDialog jf = new JDialog((Window) null, "Density Cluster Decision Graph");
		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jf.setSize(800, 600);
		jf.setModal(true);
		
		GammaDescionGraphPanel decisionGraph = new GammaDescionGraphPanel(dClusterer.getInstances(), jf);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(decisionGraph, BorderLayout.CENTER);
		
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						jf.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException e) {
			
		} catch (InterruptedException e1) {
			
		}
		
		// estimate gamma or rho + delta cutoff by drawing decision graph
		gammaCutoff = (Double) decisionGraph.getSpinner().getValue();
		return gammaCutoff;
	}
	
	public static double[] decideClusterByRhoDelta(DensityClusterer dClusterer) {
		double[] cutoff = new double[2];
		cutoff[0] = dClusterer.getInstances().get(0).getRho();
		cutoff[1] = dClusterer.getInstances().get(0).getDelta();
		
		final JDialog jf = new JDialog((Window) null, "Density Cluster Decision Graph");
		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jf.setSize(800, 600);
		jf.setModal(true);
		
		RhoDeltaDescionGraphPanel decisionGraph = new RhoDeltaDescionGraphPanel(dClusterer.getInstances(), jf);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(decisionGraph, BorderLayout.CENTER);
		
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						jf.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException e) {
			
		} catch (InterruptedException e1) {
			
		}
		
		// estimate gamma or rho + delta cutoff by drawing decision graph
		cutoff[0] = (Double) decisionGraph.getRhoSpinner().getValue();
		cutoff[1] = (Double) decisionGraph.getDeltaSpinner().getValue();
		return cutoff;
	}
}
