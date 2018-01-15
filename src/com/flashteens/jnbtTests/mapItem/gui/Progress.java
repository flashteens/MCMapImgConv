package com.flashteens.jnbtTests.mapItem.gui;

import java.util.ArrayList;
import java.util.List;

public class Progress {

	private final ArrayList<Integer> hierarchyCurrent = new ArrayList<>();
	private final ArrayList<Integer> hierarchyTotal = new ArrayList<>();
	private final ArrayList<Double[]> hierarchyWeights = new ArrayList<>();
	private final ArrayList<Double> hierarchyWeightSums = new ArrayList<>();

	private String message = "";

	private final MapConversionProgressDialog dialog;

	public Progress() {
		this(null);
		setMessage(null);
	}

	public Progress(MapConversionProgressDialog dialog) {
		this.dialog = dialog;
	}

	public synchronized void pushTaskCount(int count) {
		pushTaskCount(count, (Double[]) null);
	}

	/**
	 * @throws IllegalArgumentException
	 *             if count <= 0
	 */
	public synchronized void pushTaskCount(int count, Double... weightArgs) {
		if (count <= 0) {
			throw new IllegalArgumentException("Argument 'count' must be a positive integer.");
		}
		hierarchyCurrent.add(0);
		hierarchyTotal.add(count);
		Double[] weights = makeWeights(weightArgs, count);
		hierarchyWeights.add(weights);
		hierarchyWeightSums.add(sum(weights));
	}

	public synchronized void popTaskCount() {
		if (hierarchyCurrent.size() > 0) {
			popLastElem(hierarchyCurrent);
			popLastElem(hierarchyTotal);
			popLastElem(hierarchyWeights);
			popLastElem(hierarchyWeightSums);
		}
	}

	public synchronized boolean doneTask() {
		setMessage(null);
		boolean yield;
		if (hierarchyCurrent.size() <= 0) {
			yield = true;
		} else {
			int curr = getLastElem(hierarchyCurrent) + 1;
			int total = getLastElem(hierarchyTotal);
			if (curr >= total) {
				setLastElem(hierarchyCurrent, total);
				yield = true;
			} else {
				setLastElem(hierarchyCurrent, curr);
				yield = false;
			}
		}
		if (dialog != null) {
			dialog.setValue(getValue());
		}
		return yield;
	}

	private <T> T getLastElem(List<T> list) {
		return list.get(list.size() - 1);
	}

	private <T> void setLastElem(List<T> list, T i) {
		list.set(list.size() - 1, i);
	}

	private <T> void popLastElem(List<T> list) {
		list.remove(list.size() - 1);
	}

	private Double[] makeWeights(Double[] weightArgs, int size) {
		if (weightArgs == null) {
			weightArgs = new Double[0];
		}
		Double[] weights = new Double[size];
		for (int i = 0; i < weights.length; i++) {
			if (i < weightArgs.length) {
				if (weightArgs[i] < 0) {
					throw new IllegalArgumentException("Negative weights are not allowed!");
				}
				weights[i] = weightArgs[i];
			} else {
				weights[i] = 1.0;
			}
		}
		return weights;
	}

	private double sum(Double[] arr) {
		double result = 0;
		for (int i = 0; i < arr.length; i++) {
			result += arr[i];
		}
		return result;
	}

	public synchronized double getValue() {
		double fullRangeIter = 1;
		double value = 0;
		for (int i = 0; i < hierarchyCurrent.size(); i++) {
			final Double[] currLevelWeights = hierarchyWeights.get(i);
			final double weightSum = hierarchyWeightSums.get(i);
			final int currDoneCount = hierarchyCurrent.get(i);
			double localRatio = 0;
			for (int j = 0; j < currDoneCount; j++) {
				localRatio += currLevelWeights[j];
			}
			localRatio /= weightSum;

			value += localRatio * fullRangeIter;
			// fullRangeIter /= hierarchyTotal.get(i);
			if (currDoneCount < currLevelWeights.length) {
				fullRangeIter *= currLevelWeights[currDoneCount] / weightSum;
			} else {
				break;
			}
		}
		return value;
	}

	public synchronized void setMessage(String message) {
		this.message = (message != null && message.isEmpty()) ? message : "¡@";
		if (dialog != null) {
			dialog.setMessage(message);
		}
	}

	public synchronized String getMessage() {
		return message;
	}

}
