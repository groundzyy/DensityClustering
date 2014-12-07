package com.zhengyiyu.denscluster.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.zhengyiyu.denscluster.core.CutoffDensityCalculator;
import com.zhengyiyu.denscluster.core.DensityCalculator;
import com.zhengyiyu.denscluster.core.DensityClusterer;
import com.zhengyiyu.denscluster.core.GaussianKernalDensityCalculator;
import com.zhengyiyu.denscluster.core.Instance;
import com.zhengyiyu.denscluster.core.MatrixFileDistanceBuilder;
import com.zhengyiyu.denscluster.core.SimpleDistanceBuilder;
import com.zhengyiyu.denscluster.util.FileUtil;

import javax.swing.border.LineBorder;

import java.awt.Color;

public class Main {
	
	private ArrayList<String> logList = new ArrayList<String>();
	private ArrayList<DensityCalculator> densityCalList;
	private DensityClusterer dClusterer;
	private File lastFile = new File(".");

	private JFrame frmDensityCluster;
	private JTextField filePathTextField;
	private JTextField itemNumTextField;
	private JTextField dcTextField;
	private JTextField lowerTextField;
	private JTextField upperTextField;
	private JButton estimateDCButton;
	private JButton execCalculationButton;
	
	private JTabbedPane tabbedPane;
	private JPanel gammaPanel;
	private JPanel rhoDeltaPanel;
	private JLabel logLabel;
	private JComboBox<String> densityComboBox;
	
	private JFileChooser inputFC;
	private JFileChooser outputFC;
	private GammaDescionGraphPanel gammaDecisionGraph;
	private RhoDeltaDescionGraphPanel rhoDeltaDecisionGraph;
	private JButton btnOutputClusterResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Main window = new Main();
					window.frmDensityCluster.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		densityCalList = new ArrayList<DensityCalculator>();
		densityCalList.add(new CutoffDensityCalculator());
		densityCalList.add(new GaussianKernalDensityCalculator());
		
		initialize();
		inputFC = new JFileChooser();
		inputFC.setCurrentDirectory(lastFile);
		
		outputFC = new JFileChooser("Save File");
		outputFC.setCurrentDirectory(lastFile);
		
		disableButton(0);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDensityCluster = new JFrame();
		frmDensityCluster.setSize(800, 600);
		frmDensityCluster.setResizable(false);
		frmDensityCluster.setTitle("Density Cluster");
		
		frmDensityCluster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDensityCluster.getContentPane().setLayout(new BoxLayout(frmDensityCluster.getContentPane(), BoxLayout.X_AXIS));
		
		frmDensityCluster.setLocationRelativeTo(null);
		
		final JPanel panel = new JPanel();
		frmDensityCluster.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel calculatePanel = new JPanel();
		calculatePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(calculatePanel, BorderLayout.WEST);
		calculatePanel.setLayout(null);
		calculatePanel.setPreferredSize(new Dimension(360, 100));
		
		JPanel step1Panel = new JPanel();
		step1Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		step1Panel.setBounds(0, 0, 360, 166);
		calculatePanel.add(step1Panel);
		step1Panel.setLayout(null);
		
		JLabel step1Label = new JLabel("STEP 1 - Load Distance");
		step1Label.setBounds(89, 11, 160, 30);
		step1Panel.add(step1Label);
		step1Label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		filePathTextField = new JTextField();
		filePathTextField.setBounds(10, 47, 196, 20);
		step1Panel.add(filePathTextField);
		filePathTextField.setText("D:\\workspace_java\\DensityClustering\\sample\\example_distances.dat");
		filePathTextField.setEditable(false);
		filePathTextField.setColumns(10);
		
		JButton chooseFileButton = new JButton("Choose Dist File");
		chooseFileButton.setBounds(218, 46, 113, 23);
		step1Panel.add(chooseFileButton);
		
		JButton btnLoadDistanceMatrix = new JButton("Load Distance Matrix");
		btnLoadDistanceMatrix.setBounds(10, 77, 319, 38);
		step1Panel.add(btnLoadDistanceMatrix);
		
		JLabel itemNumLabel = new JLabel("# of Items:");
		itemNumLabel.setBounds(89, 126, 64, 14);
		step1Panel.add(itemNumLabel);
		
		itemNumTextField = new JTextField();
		itemNumTextField.setBounds(169, 124, 86, 20);
		step1Panel.add(itemNumTextField);
		itemNumTextField.setEditable(false);
		itemNumTextField.setColumns(10);
		btnLoadDistanceMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// decide which distance matrix loading method to use
				String filePath = filePathTextField.getText();
				
				File matrixFile = new File(filePath);
				
				if (matrixFile.exists() == false) {
					log("Please select a file");
					return;
				}
				
				int fileType = FileUtil.determinFileType(matrixFile);
				if (fileType == 0) {
					double[][] distMatrix = FileUtil.load3ColumnDistanceMatrix(matrixFile.getAbsolutePath(), new ArrayList<String>());
					dClusterer = new DensityClusterer();
					SimpleDistanceBuilder distBuilder = new SimpleDistanceBuilder(distMatrix, null);
					dClusterer.setDistanceBuilder(distBuilder);
				} else if (fileType == 1) {
					double[][] distMatrix = FileUtil.loadDistanceMatrix(matrixFile.getAbsolutePath());
				    dClusterer = new DensityClusterer();
					SimpleDistanceBuilder distBuilder = new SimpleDistanceBuilder(distMatrix, null);
					dClusterer.setDistanceBuilder(distBuilder);
				} else if (fileType == 2) {
					dClusterer = new DensityClusterer();
					MatrixFileDistanceBuilder distBuilder = new MatrixFileDistanceBuilder(matrixFile.getAbsolutePath(), null);
					dClusterer.setDistanceBuilder(distBuilder);
				}
				
				dClusterer.setNumOfItem(dClusterer.getDistanceBuilder().getInstanceNumber());
				itemNumTextField.setText(dClusterer.getNumOfItem() + "");
				
				log("Distance Matrix Loaded!");
				
				disableButton(2);
			}
		});
		chooseFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = inputFC.showOpenDialog(panel);
				
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File matrixFile = inputFC.getSelectedFile();
	                //This is where a real application would open the file.
	                
	                lastFile = matrixFile;
	                
	                log("Distance File Selected : " + matrixFile.getAbsolutePath());
	                
	                filePathTextField.setText(matrixFile.getAbsolutePath());
	            }
			}
		});
		
		JPanel step2Panel = new JPanel();
		step2Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		step2Panel.setBounds(0, 165, 360, 184);
		calculatePanel.add(step2Panel);
		step2Panel.setLayout(null);
		step2Panel.setEnabled(false);
		
		JLabel step2Label = new JLabel("STEP 2 - Distance Cutoff");
		step2Label.setBounds(83, 11, 160, 30);
		step2Panel.add(step2Label);
		step2Label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel dcLabel = new JLabel("Set Distance Cutoff:");
		dcLabel.setBounds(59, 54, 113, 14);
		step2Panel.add(dcLabel);
		
		dcTextField = new JTextField();
		dcTextField.setBounds(180, 49, 86, 20);
		step2Panel.add(dcTextField);
		dcTextField.setText("0.033");
		dcTextField.setColumns(10);
		
		estimateDCButton = new JButton("Estimate Distance Cutoff");
		estimateDCButton.setBounds(10, 87, 319, 33);
		step2Panel.add(estimateDCButton);
		
		lowerTextField = new JTextField();
		lowerTextField.setBounds(40, 135, 38, 20);
		step2Panel.add(lowerTextField);
		lowerTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		lowerTextField.setText("0.019");
		lowerTextField.setColumns(10);
		
		JLabel neighbourRateLabel = new JLabel("<= Ave # of neighbours <=");
		neighbourRateLabel.setBounds(85, 136, 148, 17);
		step2Panel.add(neighbourRateLabel);
		
		upperTextField = new JTextField();
		upperTextField.setBounds(229, 134, 38, 20);
		step2Panel.add(upperTextField);
		upperTextField.setText("0.02");
		upperTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		upperTextField.setColumns(10);
		estimateDCButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double lower = Double.parseDouble(lowerTextField.getText().trim());
				double upper = Double.parseDouble(upperTextField.getText().trim());
				
				double estimatedDistanceCutoff = dClusterer.getDistanceBuilder().estimateDistanceCutoff(lower, upper);
				dClusterer.setDistanceCutoff(estimatedDistanceCutoff);
				
				dcTextField.setText(estimatedDistanceCutoff + "");
				
				log("Estimated Distance Cutoff : " + estimatedDistanceCutoff);
				
				disableButton(2);
			}
		});
		
		JPanel step3Panel = new JPanel();
		step3Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		step3Panel.setBounds(0, 347, 360, 131);
		calculatePanel.add(step3Panel);
		step3Panel.setLayout(null);
		
		JLabel step3Label = new JLabel("STEP 3 - Clustering");
		step3Label.setBounds(101, 11, 136, 30);
		step3Panel.add(step3Label);
		step3Label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel densityLabel = new JLabel("Choose Density Method:");
		densityLabel.setBounds(26, 62, 136, 14);
		step3Panel.add(densityLabel);
		
		densityComboBox = new JComboBox<String>();
		densityComboBox.setBounds(184, 57, 113, 20);
		step3Panel.add(densityComboBox);
		
		// can be load from configuration instead of hard coded.
		densityComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Cutoff", "Gaussian Kernel"}));
		
		densityComboBox.setSelectedIndex(0);
		
		execCalculationButton = new JButton("Executing");
		execCalculationButton.setBounds(31, 489, 300, 40);
		calculatePanel.add(execCalculationButton);
		execCalculationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// which kernel
				int index = densityComboBox.getSelectedIndex();
				DensityCalculator densityCalc = densityCalList.get(index);
				
				if (dClusterer == null || dClusterer.getDistanceBuilder() == null) {
					log("Need to load distance matrix first");
					return;
				}
				
				double dc = Double.parseDouble(dcTextField.getText());
				dClusterer.setDistanceCutoff(dc);
				dClusterer.getDistanceBuilder().setDensCalc(densityCalc);
				
				// calculate the density and distance
				dClusterer.calculateLocalDensityArray();
				log("Local Density Calculated!");
				
				dClusterer.calculateMinDistance2HigherLocalDensityArray();
				log("Distance to higher density points calculated!");
				
				gammaDecisionGraph = new GammaDescionGraphPanel(dClusterer.getInstances(), null);		
				gammaPanel.add(gammaDecisionGraph, BorderLayout.CENTER);
				
				rhoDeltaDecisionGraph = new RhoDeltaDescionGraphPanel(dClusterer.getInstances(), null);
				rhoDeltaPanel.add(rhoDeltaDecisionGraph, BorderLayout.CENTER);
				
				disableButton(3);
			}
		});
		execCalculationButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel chartPanel = new JPanel();
		chartPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(chartPanel, BorderLayout.CENTER);
		chartPanel.setPreferredSize(new Dimension(500, 100));
		
		chartPanel.setPreferredSize(new Dimension(100, 100));
		chartPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(100, 60));
		chartPanel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(null);
		
		btnOutputClusterResult = new JButton("Output Cluster Result");
		btnOutputClusterResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int desicionPanelIndex = tabbedPane.getSelectedIndex();
				
				outputFC.setCurrentDirectory(lastFile);
				int returnVal = outputFC.showSaveDialog(panel);
				
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File output = outputFC.getSelectedFile();
	                //This is where a real application would open the file.
	                log("Result Saved to : " + output.getAbsolutePath());
	                
	                if (desicionPanelIndex == 0) {
						double gammaCutoff = (Double) gammaDecisionGraph.getSpinner().getValue();
						log("Gamma Cutoff: " + gammaCutoff);
						ArrayList<ArrayList<Instance>> clusters = dClusterer.cluster(gammaCutoff);
						
						dClusterer.recordCluster(clusters, output.getAbsolutePath());
					} else {
						double[] rhoDeltaCutoff = new double[2];
						rhoDeltaCutoff[0] = (Double) rhoDeltaDecisionGraph.getRhoSpinner().getValue();
						rhoDeltaCutoff[1] = (Double) rhoDeltaDecisionGraph.getDeltaSpinner().getValue();
						
						ArrayList<ArrayList<Instance>> clusters2 = dClusterer.cluster(rhoDeltaCutoff[0], rhoDeltaCutoff[1]);
						dClusterer.recordCluster(clusters2, output.getAbsolutePath());
					}
	                
	            } else {
	            	
	            }
			}
		});
		btnOutputClusterResult.setBounds(72, 11, 300, 40);
		panel_2.add(btnOutputClusterResult);
		btnOutputClusterResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel panel_3 = new JPanel();
		chartPanel.add(panel_3, BorderLayout.NORTH);
		panel_3.setPreferredSize(new Dimension(100, 40));
		panel_3.setLayout(null);
		
		JLabel step4Label = new JLabel("STEP 4 - Determine Cluster");
		step4Label.setBounds(138, 5, 154, 30);
		panel_3.add(step4Label);
		step4Label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JPanel panel_4 = new JPanel();
		chartPanel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_4.add(tabbedPane);
		
		gammaPanel = new JPanel();
		tabbedPane.addTab("Gamma Decision Graph", null, gammaPanel, null);
		gammaPanel.setLayout(new BorderLayout());
		
//		gammaDecisionGraph = new GammaDescionGraphPanel(new ArrayList<Instance>(), null);		
//		gammaPanel.add(gammaDecisionGraph, BorderLayout.CENTER);
		
		rhoDeltaPanel = new JPanel();
		tabbedPane.addTab("Rho/Delta Decision Graph", null, rhoDeltaPanel, null);
		rhoDeltaPanel.setLayout(new BorderLayout());
		
//		rhoDeltaDecisionGraph = new RhoDeltaDescionGraphPanel(new ArrayList<Instance>(), null);		
//		rhoDeltaPanel.add(rhoDeltaDecisionGraph, BorderLayout.CENTER);
		
		tabbedPane.setSelectedIndex(0);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(100, 30));
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(10, 0));
		
		JButton btnNewButton_1 = new JButton("Log");
		panel_1.add(btnNewButton_1, BorderLayout.WEST);
		
		logLabel = new JLabel("Start by seleteting a file");
		panel_1.add(logLabel, BorderLayout.CENTER);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	public void log(String log) {
		logList.add(log);
		System.out.println(log);
		this.logLabel.setText(log);
	}
	
	private void disableButton(int step) {
		estimateDCButton.setEnabled(step > 0);
		execCalculationButton.setEnabled(step > 1);
		btnOutputClusterResult.setEnabled(step > 2);
	}
}
