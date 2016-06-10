package task;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JDialog;

import com.zhengyiyu.denscluster.core.DensityClusterer;
import com.zhengyiyu.denscluster.core.GaussianKernalDensityCalculator;
import com.zhengyiyu.denscluster.core.Instance;
import com.zhengyiyu.denscluster.core.MatrixFileDistanceBuilder;
import com.zhengyiyu.denscluster.gui.GammaDescionGraphPanel;
import com.zhengyiyu.denscluster.gui.RhoDeltaDescionGraphPanel;
import com.zhengyiyu.denscluster.util.Common;

public class Task20141208_1_clusterIngHistone {
	public static void main(String[] args) {

		// the cutoff estimated by the sample matlab code by the authors, 
	    // which is based only on the distance in the input 3 column files, 
		// while our estimation is based on the full distance matrix, so it is slightly different.
				
		double distanceCutoff = 0.31; 
		String distanceFilePath = "./Gm12878.H3k4me1.tss.all.matrix2";
		
		System.out.println(Common.dateFormat.format(new Date()));
		DensityClusterer dClusterer = new DensityClusterer();
		MatrixFileDistanceBuilder distBuilder = new MatrixFileDistanceBuilder(distanceFilePath, new GaussianKernalDensityCalculator(), false);
		dClusterer.setDistanceBuilder(distBuilder);
		
		System.out.println(Common.dateFormat.format(new Date()));
		
		// number of item
		// but of no use...
		dClusterer.setNumOfItem(distBuilder.getInstanceNumber());
		
		// distance cutoff related
		dClusterer.setDistanceCutoff(distanceCutoff);
		
//		double estimatedDistanceCutoff = distBuilder.estimateDistanceCutoff(0.01, 0.02);
//		System.out.println("Estimated Distance Cutoff : " + estimatedDistanceCutoff);
		
		// calculate density and min distance
		System.out.println("Calculate rho");
		dClusterer.calculateLocalDensityArray();
		System.out.println("Calculate delta");
		dClusterer.calculateMinDistance2HigherLocalDensityArray();
		
		dClusterer.saveCluster(distanceFilePath + ".result");
		// now we want to determine the number of clusters by cutting based on the gamma graph
		// finally determine the assignment of clusters and output

		double gammaCutoff = decideClusterByGamma(dClusterer);
		System.out.println("Gamma Cutoff: " + gammaCutoff);
		ArrayList<ArrayList<Instance>> clusters = dClusterer.cluster(gammaCutoff);
		dClusterer.recordCluster(clusters, distanceFilePath + ".cluster");

//		double[] rhoDeltaCutoff = decideClusterByRhoDelta(dClusterer);
//		ArrayList<ArrayList<Instance>> clusters2 = dClusterer.cluster(rhoDeltaCutoff[0], rhoDeltaCutoff[1]);
//		dClusterer.recordCluster(clusters2, ".//sample//example_by_rhodelta.cluster");
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
