package com.zhengyiyu.denscluster.util;

import java.text.SimpleDateFormat;

public class Common {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String LinuxLine = "\n";

	/**
	 * regular expression for blank space
	 * \\s for a white space character, + for one or more times
	 */
	public static String SplitPattern_Blank = "\\s+";
	
	/**
	 * regular expression pattern for tab
	 * \t for a white space character, + for one or more times
	 */
	public static String SplitPattern_Tab = "\t";
	
	/**
	 * regular expression pattern for tab and multiple tab
	 * \t for a white space character, + for one or more times
	 */
	public static String SplitPattern_Tab_Multiple = "\t+";
	
	/**
	 * regular expression pattern for tab and multiple tab
	 * \t for a white space character, + for one or more times
	 */
	public static String SplitPattern_DOT = "\\.";

}
