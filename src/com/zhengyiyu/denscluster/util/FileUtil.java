package com.zhengyiyu.denscluster.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileUtil {
	public static double[][] load3ColumnDistanceMatrix(String distanceFilePath, ArrayList<String> instanceList) {
		String line = "";
		String[] splited = null;
		
		ArrayList<double[]> distanceMatrixList = new ArrayList<double[]>();
		
		int maxRowNumber = 0;
		int maxColNumber = 0;
		
		int minNum = 1;
		
		// how to handle 0 based or 1 based...
		// suppose the min row and col number are 1, then it is 1 based otherwise 0 based?!
		// otherwise if we insist it is 0 based, then the distance of the 0 position is all 0, it will be the highest density and highest dist, 
		// and the min dis for other points to this points is then all 0.
		// so we used the instance number 

		ArrayList<Double> distList = new ArrayList<Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(distanceFilePath));
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				splited = line.split(Common.SplitPattern_Blank);
				// 298 2 0
				
				double[] dist = new double[3];
				
				int rowNum = Integer.parseInt(splited[0]);
				int colNum = Integer.parseInt(splited[1]);
				
				dist[0] = rowNum;
				dist[1] = colNum;
				dist[2] = Double.parseDouble(splited[2]);
				distanceMatrixList.add(dist);
				
				distList.add(dist[2]);
				
				if (rowNum > maxRowNumber) {
					maxRowNumber = rowNum;
				}

				if (colNum > maxColNumber) {
					maxColNumber = colNum;
				}
				
				if (rowNum < minNum) {
					minNum = rowNum;
				}
				
				if (colNum < minNum) {
					minNum = colNum;
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int maxNum = Math.max(maxRowNumber, maxColNumber);
		
		// now we need to pay attention to the minNumber, if the minNum > 0, we need to minus the offset
		int itemNum = maxNum - minNum + 1;
		
		if (minNum > 0) {
			System.out.println("The start index is not 0, but " + minNum);
		}
		System.out.println("Total number of instance : " + itemNum);
		
		double[][] distanceMatrix = new double[itemNum][itemNum];
		for (double[] distRow : distanceMatrixList) {
			distanceMatrix[(int) distRow[0] - minNum][(int) distRow[1] - minNum] = distRow[2];
			distanceMatrix[(int) distRow[1] - minNum][(int) distRow[0] - minNum] = distRow[2];
		}
		
		// the distance cutoff estimated in the matlab code is based on the values from this file only
//		Collections.sort(distList);
//		System.out.println(distList.size());
//		int index = (int) (distList.size() * 0.02);
//		System.out.println(distList.get(index));
		
		return distanceMatrix;
	}

}
