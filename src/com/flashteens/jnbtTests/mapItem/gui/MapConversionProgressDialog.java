package com.flashteens.jnbtTests.mapItem.gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class MapConversionProgressDialog extends JDialog {

	private static final long serialVersionUID = 429355172490555255L;

	private final JLabel dialogMessage = new JLabel("");
	private final JProgressBar progressBar = new JProgressBar(0, 100);

	private static final String PROGRESSING_PREFIX = "Âà´«¤¤...";

	public MapConversionProgressDialog() {
		super((JFrame) null, PROGRESSING_PREFIX, true);
		add(BorderLayout.CENTER, progressBar);
		add(BorderLayout.NORTH, dialogMessage);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setSize(300, 75);
		setLocationRelativeTo(null);
	}

	private double value = 0;

	public void setValue(double value) {
		this.value = value;
		int min = progressBar.getMinimum();
		int max = progressBar.getMaximum();
		int range = max - min;
		int percent = (int) (Math.rint(value * range) + min);
		progressBar.setValue(percent);
		setTitle(PROGRESSING_PREFIX + " (" + percent + "%)");
	}

	public double getValue() {
		return value;
	}

	public void setMessage(String msg) {
		dialogMessage.setText(msg);
	}

}
