package com.zhengyiyu.denscluster.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.zhengyiyu.denscluster.core.Instance;

public class GammaDescionGraphPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6542967018736574429L;
	
	private JFreeChart chart;
	private XYLineAnnotation gammaAnnotation; 
	
	private JSpinner spinner;
	private JLabel lblNewLabel;
	private ArrayList<Instance> instances;
	private int showInstanceNumber;
	double maxGammaValue;
	
	private static String clusterNumLableStr = "# of clusters : ";
	
	/**
	 * Create the panel.
	 * @param jf 
	 */
	public GammaDescionGraphPanel(ArrayList<Instance> insts, final JDialog jf) {
		this.instances = new ArrayList<Instance>();
		this.instances.addAll(insts);
		
		Collections.sort(this.instances, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return Double.compare(o1.getGamma(), o2.getGamma());
			}
		});
		Collections.reverse(this.instances);
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(graphPanel, BorderLayout.CENTER);
		
		JPanel decisionPanel = new JPanel();
		decisionPanel.setPreferredSize(new Dimension(100, 40));
		add(decisionPanel, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("Decision Boundary :");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		decisionPanel.add(label);
		
		spinner = new JSpinner();
		decisionPanel.add(spinner);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAnnotation();
			}
		});
		decisionPanel.add(btnApply);
		
		lblNewLabel = new JLabel(clusterNumLableStr + "           ");
		decisionPanel.add(lblNewLabel);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
			}
		});
		
		if (jf != null) {
			decisionPanel.add(btnClose);
		}

		if (this.instances.size() > 0) {
			maxGammaValue = this.instances.get(0).getGamma();
			
			double startValue = this.instances.size() > 1 ? this.instances.get(1).getGamma() : this.instances.get(0).getGamma();
			
			double roundOff = Math.round(startValue * 100.0) / 100.0;
			
			spinner.setModel(new SpinnerNumberModel(roundOff, 0.01, maxGammaValue, 0.02));
			spinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateAnnotation();
				}
			});
			generateGammaChart(startValue);
		}
		
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JPanel subPanel = new JPanel(new BorderLayout());   
		subPanel.setBorder(BorderFactory.createTitledBorder("Please choose a GAMMA cutoff based on Decision Graph"));
		subPanel.setPreferredSize(new Dimension(400, 200));    
		subPanel.add(chartPanel);
		
		graphPanel.setLayout(new GridLayout(0, 1, 0, 0));
		graphPanel.add(subPanel);
		
		graphPanel.validate();
	}
	
	protected void updateAnnotation() {
		double gammaCutoff = (Double) spinner.getValue();
		XYPlot plot = (XYPlot) chart.getPlot();
		
		if (gammaAnnotation != null) {
			plot.removeAnnotation(gammaAnnotation);
		}
		
		gammaAnnotation = new XYLineAnnotation(-0.5, gammaCutoff, showInstanceNumber + 1, gammaCutoff, new BasicStroke(2f), Color.BLACK);
		plot.addAnnotation(gammaAnnotation);
		
		int numOfCluster = 0;
		for (Instance inst : instances) {
			if (inst.getGamma() >= gammaCutoff) {
				numOfCluster++;
			} else {
				break;
			}
		}
		
		this.lblNewLabel.setText(clusterNumLableStr + numOfCluster);
		
	}

	/**
	 * @return the spinner
	 */
	public JSpinner getSpinner() {
		return spinner;
	}

	/**
	 * @param spinner the spinner to set
	 */
	public void setSpinner(JSpinner spinner) {
		this.spinner = spinner;
	}

	private void generateGammaChart(double startValue) {
		showInstanceNumber = instances.size() / 10 > 100 ? 100 : instances.size() / 10;
		
		XYSeries series = new XYSeries("Gamma Decision Graph");
		for (int i = 0; i < showInstanceNumber; i++) {
			Instance inst = instances.get(i);
			series.add(i, inst.getGamma());
//			System.out.println(inst.getGamma());
		}
		
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		// Generate the graph
		chart = ChartFactory.createXYLineChart("Gamma Decision Graph", // Title
				"n", // x-axis Label
				"Gamma", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);
		
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();

		// X axis
		plot.getDomainAxis().setRange(-1, showInstanceNumber + 1);
		
		double maxValue = instances.get(0).getGamma();
		double epsilon = maxValue * 0.05;
		plot.getRangeAxis().setRange(-epsilon, maxValue + epsilon);
		
		updateAnnotation();
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShape(0, new Ellipse2D.Double(-5d, -5d, 5d, 5d));
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

}
