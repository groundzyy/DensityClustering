package com.zhengyiyu.denscluster.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.zhengyiyu.denscluster.util.FileUtil;

public class GenerateSampleDistanceMatrix {

	public static void main(String[] args) throws IOException {
		String distanceFilePath = ".//sample//example_distances.dat";
		
		ArrayList<String> instanceList = new ArrayList<String>();
		double[][] distMatrix = FileUtil.load3ColumnDistanceMatrix(distanceFilePath, instanceList);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(".//sample//example.distancematrix"));
		for (double[] dist : distMatrix) {
			for (int i = 0; i < dist.length; i++) {
				if (i == 0) {
					bw.write("" + dist[i]);
				} else {
					bw.write("\t" + dist[i]);
				}
			}
			bw.newLine();
		}
		
		bw.close();
		
	}

}
