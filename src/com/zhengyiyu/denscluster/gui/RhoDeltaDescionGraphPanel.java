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

import javax.swing.BoxLayout;

public class RhoDeltaDescionGraphPanel extends JPanel {
	
	private JFreeChart chart;
	private XYLineAnnotation rhoAnnotation; 
	private XYLineAnnotation deltaAnnotation; 
	
	private JSpinner rhoSpinner;
	private JSpinner deltaSpinner;
	
	private JLabel clusterNumberLabel;
	private ArrayList<Instance> instances;
	private int showInstanceNumber;
	private double maxRhoValue;
	private double maxDeltaValue;
	
	private static String clusterNumLableStr = "# of clusters : ";
	
	/**
	 * Create the panel.
	 * @param jf 
	 */
	public RhoDeltaDescionGraphPanel(ArrayList<Instance> insts, final JDialog jf) {
		this.instances = new ArrayList<Instance>();
		this.instances.addAll(insts);
		
		// just make sure the input instance is sorted by rho, but actually not needed.
//		Collections.sort(this.instances, new Comparator<Instance>() {
//			@Override
//			public int compare(Instance o1, Instance o2) {
//				return Double.compare(o1.getRho(), o2.getRho());
//			}
//		});
//		Collections.reverse(this.instances);
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(graphPanel, BorderLayout.CENTER);
		
		JPanel decisionPanel = new JPanel();
		decisionPanel.setPreferredSize(new Dimension(100, 40));
		add(decisionPanel, BorderLayout.SOUTH);
		decisionPanel.setLayout(null);
		
		JLabel rhoLabel = new JLabel("Rho Cutoff :");
		rhoLabel.setBounds(0, 0, 105, 19);
		rhoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		decisionPanel.add(rhoLabel);
		
		rhoSpinner = new JSpinner();
		rhoSpinner.setBounds(115, 0, 105, 19);
		decisionPanel.add(rhoSpinner);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setBounds(243, 21, 70, 20);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAnnotation();
			}
		});
		
		JLabel deltaLabel = new JLabel("Delta Cutoff :");
		deltaLabel.setBounds(0, 21, 105, 19);
		deltaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		decisionPanel.add(deltaLabel);
		
		deltaSpinner = new JSpinner();
		deltaSpinner.setBounds(115, 20, 105, 19);
		decisionPanel.add(deltaSpinner);
		decisionPanel.add(btnApply);
		
		clusterNumberLabel = new JLabel(clusterNumLableStr + "           ");
		clusterNumberLabel.setBounds(243, 0, 105, 19);
		decisionPanel.add(clusterNumberLabel);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
			}
		});
		btnClose.setBounds(318, 21, 70, 20);
		if (jf != null) {
			decisionPanel.add(btnClose);
		}

		if (this.instances.size() > 0) {
			// we would like to set the start value for both rho and delta
			Collections.sort(instances, new Comparator<Instance>() {
				@Override
				public int compare(Instance o1, Instance o2) {
					return Double.compare(o1.getRho(), o2.getRho());
				}
			});
			Collections.reverse(instances);
			
			maxRhoValue = this.instances.get(0).getRho();
			
			// we would like to set the start value for both rho and delta
			// sorted by delta
			Collections.sort(instances, new Comparator<Instance>() {
				@Override
				public int compare(Instance o1, Instance o2) {
					int compare = Double.compare(o1.getDelta(), o2.getDelta());
					
					if (compare == 0) {
						return Double.compare(o1.getRho(), o2.getRho());
					}
					return compare;
				}
			});
			Collections.reverse(instances);
			
			maxDeltaValue = this.instances.get(0).getDelta();

			double deltaStartValue = this.instances.size() > 1 ? this.instances.get(1).getDelta() : this.instances.get(0).getDelta();
			double rhoStartValue = this.instances.size() > 1 ? this.instances.get(1).getRho() : this.instances.get(0).getRho();
			
			rhoSpinner.setModel(new SpinnerNumberModel(rhoStartValue, 0, maxRhoValue, 0.02));
			rhoSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateAnnotation();
				}
			});
			
			deltaSpinner.setModel(new SpinnerNumberModel(deltaStartValue, 0, maxDeltaValue, 0.02));
			deltaSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateAnnotation();
				}
			});
			
			generateRhoDeltaChart(rhoStartValue, deltaStartValue);
		}
		
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JPanel subPanel = new JPanel(new BorderLayout());   
		subPanel.setBorder(BorderFactory.createTitledBorder("Please choose Rho and Delta cutoff based on Decision Graph"));
		subPanel.setPreferredSize(new Dimension(400, 200));    
		subPanel.add(chartPanel);
		
		graphPanel.setLayout(new GridLayout(0, 1, 0, 0));
		graphPanel.add(subPanel);
		
		graphPanel.validate();
	}
	
	protected void updateAnnotation() {
		double rhoCutoff = (Double) rhoSpinner.getValue();
		double deltaCutoff = (Double) deltaSpinner.getValue();
		
		XYPlot plot = (XYPlot) chart.getPlot();
		
		if (rhoAnnotation != null) {
			plot.removeAnnotation(rhoAnnotation);
		}
		
		if (deltaAnnotation != null) {
			plot.removeAnnotation(deltaAnnotation);
		}
		
		rhoAnnotation = new XYLineAnnotation(rhoCutoff, -0.5, rhoCutoff, maxDeltaValue * 1.05, new BasicStroke(2f), Color.BLACK);
		plot.addAnnotation(rhoAnnotation);
		
		deltaAnnotation = new XYLineAnnotation(-0.5, deltaCutoff, maxRhoValue + 5, deltaCutoff, new BasicStroke(2f), Color.BLACK);
		plot.addAnnotation(deltaAnnotation);
		
		int numOfCluster = 0;
		for (Instance inst : instances) {
			if (inst.getRho() >= rhoCutoff && inst.getDelta() >= deltaCutoff) {
				numOfCluster++;
			}
		}
		
		this.clusterNumberLabel.setText(clusterNumLableStr + numOfCluster);
		
	}

	/**
	 * @return the rhoSpinner
	 */
	public JSpinner getRhoSpinner() {
		return rhoSpinner;
	}

	/**
	 * @param rhoSpinner the rhoSpinner to set
	 */
	public void setRhoSpinner(JSpinner rhoSpinner) {
		this.rhoSpinner = rhoSpinner;
	}

	/**
	 * @return the deltaSpinner
	 */
	public JSpinner getDeltaSpinner() {
		return deltaSpinner;
	}

	/**
	 * @param deltaSpinner the deltaSpinner to set
	 */
	public void setDeltaSpinner(JSpinner deltaSpinner) {
		this.deltaSpinner = deltaSpinner;
	}

	private void generateRhoDeltaChart(double rhoStartValue, double deltaStartValue) {
//		showInstanceNumber = instances.size() / 10 > 100 ? 100 : instances.size() / 10;
		showInstanceNumber = instances.size();
		
		XYSeries series = new XYSeries("Rho/Delta Decision Graph");
		for (int i = 0; i < showInstanceNumber; i++) {
			Instance inst = instances.get(i);
			series.add(inst.getRho(), inst.getDelta());
		}
		
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		// Generate the graph
		chart = ChartFactory.createXYLineChart("Rho/Delta Decision Graph", // Title
				"Rho", // x-axis Label
				"Delta", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);
		
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();

		// X axis
		plot.getDomainAxis().setRange(-1, maxRhoValue + 5);
		
		double epsilon = maxDeltaValue * 0.05;
		plot.getRangeAxis().setRange(-epsilon, maxDeltaValue + epsilon);
		
		updateAnnotation();
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShape(0, new Ellipse2D.Double(0d, -5d, 5d, 5d));
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
