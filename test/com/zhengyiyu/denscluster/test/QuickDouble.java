package com.zhengyiyu.denscluster.test;

import java.text.NumberFormat;
import java.util.Random;

public class QuickDouble {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();

	    NumberFormat format = NumberFormat.getInstance();

	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

	    System.out.println("free memory: " + format.format(freeMemory / 1024));
	    System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
	    System.out.println("max memory: " + format.format(maxMemory / 1024));
	    System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
	    
	    int length = 10000;
		double[][] array = new double[length][length];
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				array[i][j] = rand.nextDouble();
			}
		}
		
		System.out.println("After array");
	    System.out.println("free memory: " + format.format(freeMemory / 1024));
	    System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
	    System.out.println("max memory: " + format.format(maxMemory / 1024));
	    System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
	}
}
