package com.zhengyiyu.denscluster.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DensityClusterer {
	/**
	 * distance cutoff to count local density, currently fixed in the paper, 
	 * later may consider allow adjustment to this cutoff
	 * I guess should >= 0, but currently no check
	 */
	private double distanceCutoff;
	
	/**
	 * take care everything related to distance
	 * load distance matrix
	 * calculate local density
	 * calculate min distance to higher desity points
	 */
	private DistanceBuilder distanceBuilder;

	// it is hard to set the numOfItem at first, so should set it in the distanceBuilder
	int numOfItem;
	
	private ArrayList<Instance> instances;

	/**
	 * 
	 */
	public DensityClusterer() {
		super();
	}
	
	/**
	 * @param distanceCutoff
	 */
	public DensityClusterer(double distanceCutoff) {
		super();
		this.distanceCutoff = distanceCutoff;
	}

	/**
	 * @return the distanceCutoff
	 */
	public double getDistanceCutoff() {
		return distanceCutoff;
	}

	/**
	 * @param distanceCutoff the distanceCutoff to set
	 */
	public void setDistanceCutoff(double distanceCutoff) {
		this.distanceCutoff = distanceCutoff;
		this.getDistanceBuilder().setDistanceCutoff(distanceCutoff);
	}

	/**
	 * @return the distanceBuilder
	 */
	public DistanceBuilder getDistanceBuilder() {
		return distanceBuilder;
	}

	/**
	 * @param distanceBuilder the distanceBuilder to set
	 */
	public void setDistanceBuilder(DistanceBuilder distanceBuilder) {
		this.distanceBuilder = distanceBuilder;
	}

	/**
	 * @return the numOfItem
	 */
	public int getNumOfItem() {
		return numOfItem;
	}

	/**
	 * @param numOfItem the numOfItem to set
	 */
	public void setNumOfItem(int numOfItem) {
		this.numOfItem = numOfItem;
	}
	
	/**
	 * @return the instances
	 */
	public ArrayList<Instance> getInstances() {
		return instances;
	}

	/**
	 * @param instances the instances to set
	 */
	public void setInstances(ArrayList<Instance> instances) {
		this.instances = instances;
	}

	public void calculateLocalDensityArray() {
		ArrayList<Instance> instances = this.distanceBuilder.calculateLocalDensityArray(this.distanceCutoff);
		this.setInstances(instances);
	}

	public void calculateMinDistance2HigherLocalDensityArray() {
		// calculate distance for each point
		this.distanceBuilder.calculateMinDistance2HigherLocalDensityArray(this.instances);
	}

	public ArrayList<ArrayList<Instance>> cluster(double gamma) {
		// find all the instances that is larger than gamma
		ArrayList<ArrayList<Instance>> clusters = new ArrayList<ArrayList<Instance>>();
		
		for (Instance inst : instances) {
			if (inst.getGamma() >= gamma) {
				ArrayList<Instance> cluster = new ArrayList<Instance>();
				cluster.add(inst);
				inst.setClusterIndex(clusters.size());
				clusters.add(cluster);
			}
		}
		
		System.out.println("# of clusters : " + clusters.size());
		
		// try to assign all the other instances, to core and halo
		for (Instance inst : instances) {
			if (inst.getClusterIndex() == -1) {
				int clusterIndex = inst.getClosestHigherDensityInstance().getClusterIndex();
				inst.setClusterIndex(clusterIndex);
				clusters.get(clusterIndex).add(inst);
			}
		}
		
		// try to assign whether is halo
		double[] clusterRhoCutoff = this.distanceBuilder.calculateRhoBorder(instances, clusters.size());
		assignHalo(instances, clusterRhoCutoff);
		
		return clusters;
	}
	
	public ArrayList<ArrayList<Instance>> cluster(double rhoCutoff, double deltaCutoff) {
		// find all the instances that is larger than gamma
		ArrayList<ArrayList<Instance>> clusters = new ArrayList<ArrayList<Instance>>();
		
		for (Instance inst : instances) {
			if (inst.getRho() >= rhoCutoff && inst.getDelta() >= deltaCutoff) {
				ArrayList<Instance> cluster = new ArrayList<Instance>();
				cluster.add(inst);
				inst.setClusterIndex(clusters.size());
				clusters.add(cluster);
			}
		}
		
		System.out.println("# of clusters : " + clusters.size());
		
		// try to assign all the other instances, to core and halo
		for (Instance inst : instances) {
			if (inst.getClusterIndex() == -1) {
				int clusterIndex = inst.getClosestHigherDensityInstance().getClusterIndex();
				inst.setClusterIndex(clusterIndex);
				clusters.get(clusterIndex).add(inst);
			}
		}
		
		// try to assign whether is halo
		double[] clusterRhoCutoff = this.distanceBuilder.calculateRhoBorder(instances, clusters.size());
		assignHalo(instances, clusterRhoCutoff);
		
		return clusters;
	}

	private void assignHalo(ArrayList<Instance> instances, double[] clusterRhoCutoff) {
		for (Instance inst : instances) {
			if (inst.getRho() < clusterRhoCutoff[inst.getClusterIndex()]) {
				inst.setHalo(true);
			} else {
				inst.setHalo(false);
			}
		}
	}
	
	public void saveCluster(String resultFilePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFilePath));
			bw.write("#Instances: " + instances.size());
			bw.newLine();
			
			bw.write("#Distance Cutoff: " + distanceCutoff);
			
			for (int instIndex = 0; instIndex < instances.size(); instIndex++) {
				Instance inst = instances.get(instIndex);
				bw.write(instIndex + "\t" + inst.getRho() + "\t" + inst.getDelta());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recordCluster(ArrayList<ArrayList<Instance>> clusters, String resultFilePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFilePath));
			
			for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
				ArrayList<Instance> cluster = clusters.get(clusterIndex);
				bw.write("#cluster " + clusterIndex + "\tinstances: " +  cluster.size());
				bw.newLine();

				System.out.println("cluster " + clusterIndex + "\tinstances: " +  cluster.size());
				for (int i = 0; i < cluster.size(); i++) {
					Instance inst = cluster.get(i);
					bw.write(inst.getIndex() + "\t" + inst.getClusterIndex() + "\t" + (inst.isHalo() ? "Halo" : "Core" + "\t" + inst.getRho() + "\t" + inst.getDelta()));
					bw.newLine();
				}
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
