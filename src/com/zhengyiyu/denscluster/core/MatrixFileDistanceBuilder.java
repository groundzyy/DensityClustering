package com.zhengyiyu.denscluster.core;

import java.beans.DesignMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import com.zhengyiyu.denscluster.util.Common;

/**
 * @author Yiyu
 * 
 * Sometimes the number of instances to cluster is huge, e.g., > 50,000 instance, 
 * and it is impossible to load the distance matrix (n * n) into limited memory space.
 * Instead, but scan the distance matrix file for several times, we can still perform the clustering.
 *
 */
public class MatrixFileDistanceBuilder extends AbstractDistanceBuilder {
	
	private int numOfInstance;
	private String matrixFilePath;
	
	private double minDistance;
	private double maxDistance;
	
	
	/**
	 * max value of interger is 2147483647 (< 50k * 50k) 
	 * max value of long is 9223372036854775807L, should be enough
	 */
	private long numOfDistanceCount;

	public MatrixFileDistanceBuilder(String filePath, DensityCalculator densCalc) {
		this(filePath, densCalc, true);
//		super(densCalc);
//		this.matrixFilePath = filePath;
//		
//		// first time read the file, also scan the whole file for the max and min for estimate distance cutoff
//		// set the size of the instances by reading the first line
//		
//		int lineCount = 0;
//		numOfDistanceCount = 0;
//		
//		System.out.println("First Round Loading the file : try to get the max");
//		try {
//			minDistance = Double.MAX_VALUE;
//			maxDistance = 0.0;
//			
//			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
//			
//			String line = "";
//			String[] splited = null;
//			
//			while ((line = br.readLine()) != null) {
//				if (line.startsWith("#")) {
//					continue;
//				}
//				
//				splited = line.split(Common.SplitPattern_Blank);
//				
//				if (lineCount == 0) {
//					this.numOfInstance = splited.length;
//				}
//				
//				numOfDistanceCount += splited.length;
//				
//				for (String s : splited) {
//					double d = Double.parseDouble(s);
//					if (d > maxDistance) {
//						maxDistance = d;
//					}
//					
//					if (d < minDistance) {
//						minDistance = d;
//					}
//				}
//				
//				lineCount++;
//				
//				if (lineCount % 500 == 0) {
//					System.out.print(".");
//				}
//			}
//			
//			System.out.println();
//			
//			br.close();
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		if (lineCount != this.numOfInstance) {
//			System.out.println("The matrix file is not N * N, but " + this.numOfInstance + " * " + lineCount);
//		}
//		
//		long totalNumInTheroy = (long) lineCount * lineCount;
//		
//		if (this.numOfDistanceCount != totalNumInTheroy) {
//			System.out.println("The matrix file is not N * N, we only have " + numOfDistanceCount + " distance values while should be " + totalNumInTheroy);
//		}
//		
//		System.out.println(this.numOfDistanceCount);
//		System.out.println(this.numOfInstance);
//		
//		System.out.println("Max : " + maxDistance + ", Min : " + minDistance);
	}
	
	public MatrixFileDistanceBuilder(String filePath, DensityCalculator densCalc, boolean scanForMaxMin) {
		super(densCalc);
		this.matrixFilePath = filePath;
		
		// first time read the file, also scan the whole file for the max and min for estimate distance cutoff
		// set the size of the instances by reading the first line
		
		int lineCount = 0;
		numOfDistanceCount = 0;
		
		try {
			minDistance = Double.MAX_VALUE;
			maxDistance = 0.0;
			
			System.out.print("Scan Distance Matrix File ");
			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
			
			String line = "";
			String[] splited = null;
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				
				if (lineCount == 0) {
					this.numOfInstance = splited.length;
					
					if (scanForMaxMin == false) {
						// the only thing we need is how many instance in the first row
						break;
					}
				}
				
				numOfDistanceCount += splited.length;
				
				for (String s : splited) {
					double d = Double.parseDouble(s);
					if (d > maxDistance) {
						maxDistance = d;
					}
					
					if (d < minDistance) {
						minDistance = d;
					}
				}
				
				lineCount++;
				
				if (lineCount % 500 == 0) {
					System.out.print(".");
				}
			}
			
			System.out.println();
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (scanForMaxMin == true) {
			// we want also to check whether it is an N * N matrix
			if (lineCount != this.numOfInstance) {
				System.out.println("The matrix file is not N * N, but " + this.numOfInstance + " * " + lineCount);
			}
			
			long totalNumInTheroy = (long) lineCount * lineCount;
			
			if (this.numOfDistanceCount != totalNumInTheroy) {
				System.out.println("The matrix file is not N * N, we only have " + numOfDistanceCount + " distance values while should be " + totalNumInTheroy);
			}
		} else {
			// we simply set the total number to N * N
			this.numOfDistanceCount = (long) this.numOfInstance * this.numOfInstance;
		}
		
		System.out.println("# of Dinstance Count : " + this.numOfDistanceCount);
		System.out.println("# of Instance " + this.numOfInstance);
		System.out.println("Max : " + maxDistance + ", Min : " + minDistance);
	}

	/**
	 * @return the matrixFilePath
	 */
	public String getMatrixFilePath() {
		return matrixFilePath;
	}

	/**
	 * @param matrixFilePath the matrixFilePath to set
	 */
	public void setMatrixFilePath(String matrixFilePath) {
		this.matrixFilePath = matrixFilePath;
	}

	/**
	 * @return the numOfInstance
	 */
	public int getNumOfInstance() {
		return numOfInstance;
	}

	/**
	 * @param numOfInstance the numOfInstance to set
	 */
	public void setNumOfInstance(int numOfInstance) {
		this.numOfInstance = numOfInstance;
	}

	@Override
	// take the methods in R implementation
	public double estimateDistanceCutoff(double lower, double upper) {
		// this will need to read the file multiple times
		
		if ((upper - lower) / upper < 0.1) {
			// too close
			lower = upper * 0.9;
		}
		
		System.out.println("Trying to find neighbour rate between " + lower + " and " + upper);

		double distanceCutoff = this.minDistance;
		double epi = (this.minDistance + this.maxDistance) * 0.01;
		
		distanceCutoff = 0.31;
		epi = 0.02;
		
		System.out.println("EPI:" + epi);
		
		int iteration = 1;
		
		while (true) {
			System.out.println(Common.dateFormat.format(new Date()));
			double neighbourRate = getNeighbourRate(distanceCutoff);
			
			System.out.println("Iteration " + iteration++ + "\t" + "distance cutoff : " + distanceCutoff + "\tepi : " + epi + "\tneighbour rate : " + neighbourRate);

			if (neighbourRate >= lower && neighbourRate <= upper) {
				break;
			}
			
			if (neighbourRate > upper) {
				// reduce the dc
				distanceCutoff = distanceCutoff - epi;
				epi = epi / 2;
			} else {
				// < lower
				distanceCutoff = distanceCutoff + epi;
			}
			
		}
		 
		return distanceCutoff;
	}

	private double getNeighbourRate(double distanceCutoff) {
		long neighbourCount = 0;
		
		int count = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
			
			String line = "";
			String[] splited = null;
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				
				for (String s : splited) {
					double d = Double.parseDouble(s);
					
					if (d < distanceCutoff) {
						neighbourCount++;
					}
				}
				
				count++;
				if (count % 500 == 0) {
					System.out.print(".");
				}
			}
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\t" + neighbourCount + "\t" + this.numOfDistanceCount + "\t" + neighbourCount * 1.0 / this.numOfDistanceCount);
		return neighbourCount * 1.0 / this.numOfDistanceCount;
	}

	@Override
	public ArrayList<Instance> calculateLocalDensityArray(double distanceCutoff) {
		ArrayList<Instance> instancesWithRho = new ArrayList<Instance>();
		
		String line = "";
		String[] splited = null;
			
		int instIndex = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				double[] distRow = new double[splited.length];
				for (int i = 0; i < splited.length; i++) {
					distRow[i] = Double.parseDouble(splited[i]);
				}
				
				double rho = this.getDensCalc().calculateDensity(distanceCutoff, distRow, instIndex);
				Instance inst = new Instance(instIndex, rho);
				instancesWithRho.add(inst);

				instIndex++;
			}
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instancesWithRho;
	}

	@Override
	public void calculateMinDistance2HigherLocalDensityArray(ArrayList<Instance> insts) {
		// sort by density
		// calculate the distance
		Collections.sort(insts, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return Double.compare(o1.getRho(), o2.getRho());
			}
		});
		Collections.reverse(insts);
		
		double maxDelta = 0.0;
		
		HashMap<Integer, Integer> instanceOrderMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < insts.size(); i++) {
			instanceOrderMap.put(insts.get(i).getIndex(), i);
		}
		
		int instIndex = 0;

		// again we scan the matrix file
		String line = "";
		String[] splited = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				double[] distRow = new double[splited.length];
				for (int i = 0; i < splited.length; i++) {
					distRow[i] = Double.parseDouble(splited[i]);
				}
				
				int rhoIndex = instanceOrderMap.get(instIndex);
				maxDelta = calculateMinDistance2HigherDensityInstances(insts, rhoIndex, distRow, maxDelta);

				instIndex++;
			}
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// In the matlab code, the delta for the point with highest rho are set as the largest delta for all the other points, 
		// since this setting will not affect clustering decision, but make the decision chart better to inteprate.
		
		insts.get(0).setDelta(maxDelta);
	}

	@Override
	public int getInstanceNumber() {
		return numOfInstance;
	}

	@Override
	public double[] calculateRhoBorder(ArrayList<Instance> instances, int numOfCluster) {
		double[] rhoBorderArray = new double[numOfCluster];
		
		HashMap<Integer, Integer> instanceOrderMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < instances.size(); i++) {
			instanceOrderMap.put(instances.get(i).getIndex(), i);
		}
		
		int instIndex = 0;

		// again we scan the matrix file
		String line = "";
		String[] splited = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(matrixFilePath));
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				double[] distRow = new double[splited.length];
				for (int i = 0; i < splited.length; i++) {
					distRow[i] = Double.parseDouble(splited[i]);
				}
				
				int rhoIndex = instanceOrderMap.get(instIndex);
				Instance instA = instances.get(rhoIndex);
				
				for (int j = rhoIndex + 1; j < instances.size(); j++) {
					Instance instB = instances.get(j);
					
					// the border regions for each cluster are defined as the set of points assigned to this cluster, 
					// and be within a distance cutoff from points belonging to other clusters (thus all pairs of two points in different clusters will be checked)
					// the border cutoff is defined by the point of highest density within its border region
					// However in the matlab code provide by the authors, they used the average of the rho of two points as the border.
					
					if (instA.getClusterIndex() != instB.getClusterIndex() && distRow[j] < this.getDistanceCutoff()) {
						double rhoAve = (instA.getRho() + instB.getRho()) / 2;
						if (rhoAve > rhoBorderArray[instA.getClusterIndex()]) {
							rhoBorderArray[instA.getClusterIndex()] = rhoAve;
						}
						
						if (rhoAve > rhoBorderArray[instB.getClusterIndex()]) {
							rhoBorderArray[instB.getClusterIndex()] = rhoAve;
						}
					}
				}

				instIndex++;
			}
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rhoBorderArray;
	}

	/**
	 * @return the minDistance
	 */
	public double getMinDistance() {
		return minDistance;
	}

	/**
	 * @param minDistance the minDistance to set
	 */
	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	/**
	 * @return the maxDistance
	 */
	public double getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

}
