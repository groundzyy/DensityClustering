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

public class Task20141209_1_clusterIngHistone_quick {
	public static void main(String[] args) {

		String distanceFilePath = "./Gm12878.H3k4me1.tss.all.matrix2";
		
		DensityClusterer dClusterer = DensityClusterer.loadCluster(distanceFilePath + ".result");

		System.out.println("# of instance " + dClusterer.getInstances().size());
		
//		double gammaCutoff = decideClusterByGamma(dClusterer);
//		System.out.println("Gamma Cutoff: " + gammaCutoff);
//		ArrayList<ArrayList<Instance>> clusters = dClusterer.cluster(gammaCutoff);
//		dClusterer.recordCluster(clusters, distanceFilePath + ".cluster");

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
