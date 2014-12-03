package com.zhengyiyu.denscluster.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class Main {

	private JFrame frmDensityCluster;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private JFileChooser fc;
	private JTextField textField_5;

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
		initialize();
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDensityCluster = new JFrame();
		frmDensityCluster.setTitle("Density Cluster");
		frmDensityCluster.setBounds(100, 100, 783, 482);
		frmDensityCluster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDensityCluster.getContentPane().setLayout(null);
		
		final JPanel panel = new JPanel();
		panel.setBounds(0, 0, 767, 443);
		panel.setLayout(null);
		frmDensityCluster.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 11, 352, 421);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnLoadFiles = new JButton("Choose Dist File");
		btnLoadFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(panel);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	                System.out.print(file.getName());
	                textField.setText(file.getAbsolutePath());
	            } else {
	            }
			}
		});
		btnLoadFiles.setBounds(216, 48, 113, 23);
		panel_1.add(btnLoadFiles);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 49, 196, 20);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("# of Items:");
		lblNewLabel.setBounds(20, 124, 64, 14);
		panel_1.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setText("2000");
		textField_1.setEditable(false);
		textField_1.setBounds(120, 121, 86, 20);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Set Distance Cutoff:");
		lblNewLabel_1.setBounds(10, 196, 113, 14);
		panel_1.add(lblNewLabel_1);
		
		textField_2 = new JTextField();
		textField_2.setText("0.295");
		textField_2.setBounds(120, 193, 86, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("Estimate Distance Cutoff");
		btnNewButton.setBounds(10, 221, 319, 23);
		panel_1.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("STEP 1 - Load Distance");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel_2.setBounds(10, 8, 242, 30);
		panel_1.add(lblNewLabel_2);
		
		JButton btnLoadDistanceMatrix = new JButton("Load Distance Matrix");
		btnLoadDistanceMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// decide which distance matrix loading method to use
			}
		});
		btnLoadDistanceMatrix.setBounds(10, 86, 319, 23);
		panel_1.add(btnLoadDistanceMatrix);
		
		textField_3 = new JTextField();
		textField_3.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_3.setText("0.02");
		textField_3.setBounds(46, 255, 38, 20);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("<= Ave # of neighbours <=");
		lblNewLabel_3.setBounds(93, 258, 148, 17);
		panel_1.add(lblNewLabel_3);
		
		textField_4 = new JTextField();
		textField_4.setText("0.02");
		textField_4.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_4.setColumns(10);
		textField_4.setBounds(251, 255, 38, 20);
		panel_1.add(textField_4);
		
		JLabel lblStepDistanceCutoff = new JLabel("STEP 2 - Distance Cutoff");
		lblStepDistanceCutoff.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblStepDistanceCutoff.setBounds(10, 152, 242, 30);
		panel_1.add(lblStepDistanceCutoff);
		
		JLabel lblStepDistance = new JLabel("STEP 3 - Clustering");
		lblStepDistance.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblStepDistance.setBounds(10, 297, 242, 30);
		panel_1.add(lblStepDistance);
		
		JButton btnExecuteDensityClustering = new JButton("Executing");
		btnExecuteDensityClustering.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExecuteDensityClustering.setBounds(10, 380, 319, 30);
		panel_1.add(btnExecuteDensityClustering);
		
		JLabel lblNewLabel_4 = new JLabel("Choose Density Method:");
		lblNewLabel_4.setBounds(10, 338, 136, 14);
		panel_1.add(lblNewLabel_4);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Cutoff \"Kernel\"", "Gaussian Kernel"}));
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(165, 335, 113, 20);
		panel_1.add(comboBox);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(405, 11, 352, 421);
		panel.add(panel_2);
		
		JLabel lblStep = new JLabel("STEP 4 - Finalize Cluster");
		lblStep.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblStep.setBounds(10, 11, 242, 30);
		panel_2.add(lblStep);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_3.setBounds(20, 52, 322, 215);
		panel_2.add(panel_3);
		
		JLabel lblDecisionGraph = new JLabel("GAMMA Decision Graph");
		lblDecisionGraph.setBounds(112, 38, 121, 14);
		panel_2.add(lblDecisionGraph);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Double(0.15), null, null, new Double(0.01)));
		spinner.setBounds(145, 287, 103, 20);
		panel_2.add(spinner);
		
		JLabel lblDecisionBounder = new JLabel("Decision Boundary :");
		lblDecisionBounder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDecisionBounder.setBounds(10, 290, 105, 14);
		panel_2.add(lblDecisionBounder);
		
		JButton btnOutputClusterResult = new JButton("Output Cluster Result");
		btnOutputClusterResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnOutputClusterResult.setBounds(86, 371, 186, 39);
		panel_2.add(btnOutputClusterResult);
		
		JLabel lblNewLabel_5 = new JLabel("# of Clusters :");
		lblNewLabel_5.setBounds(30, 321, 85, 14);
		panel_2.add(lblNewLabel_5);
		
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(145, 318, 43, 20);
		panel_2.add(textField_5);
		textField_5.setColumns(10);
	}
}
