package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

public final class Util {
	private static final Dimension MAX_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

	public static Dimension getMinimumSize(Component component, GridConstraints constraints, boolean addIndent) {
		try {
			Dimension npe = getSize(constraints.myMinimumSize, component.getMinimumSize());
			if(addIndent) {
				npe.width += 10 * constraints.getIndent();
			}

			return npe;
		} catch(NullPointerException e) {
			return new Dimension(0, 0);
		}
	}

	public static Dimension getMaximumSize(Component component, GridConstraints constraints, boolean addIndent) {
		try {
			Dimension e = getSize(constraints.myMaximumSize, MAX_SIZE);
			if(addIndent && e.width < MAX_SIZE.width) {
				e.width += 10 * constraints.getIndent();
			}

			return e;
		} catch(NullPointerException e) {
			return new Dimension(0, 0);
		}
	}

	public static Dimension getPreferredSize(Component component, GridConstraints constraints, boolean addIndent) {
		try {
			Dimension e = getSize(constraints.myPreferredSize, component.getPreferredSize());
			if(addIndent) {
				e.width += 10 * constraints.getIndent();
			}

			return e;
		} catch(NullPointerException e) {
			return new Dimension(0, 0);
		}
	}

	private static Dimension getSize(Dimension overridenSize, Dimension ownSize) {
		int overridenWidth = overridenSize.width >= 0 ? overridenSize.width : ownSize.width;
		int overridenHeight = overridenSize.height >= 0 ? overridenSize.height : ownSize.height;
		return new Dimension(overridenWidth, overridenHeight);
	}

	public static void adjustSize(Component component, GridConstraints constraints, Dimension size) {
		Dimension minimumSize = getMinimumSize(component, constraints, false);
		Dimension maximumSize = getMaximumSize(component, constraints, false);
		size.width = Math.max(size.width, minimumSize.width);
		size.height = Math.max(size.height, minimumSize.height);
		size.width = Math.min(size.width, maximumSize.width);
		size.height = Math.min(size.height, maximumSize.height);
	}

	public static int eliminate(int[] cellIndices, int[] spans, List<Integer> elimitated) {
		int size = cellIndices.length;
		if(size != spans.length) {
			throw new IllegalArgumentException("size mismatch: " + size + ", " + spans.length);
		} else if(elimitated != null && elimitated.size() != 0) {
			throw new IllegalArgumentException("eliminated must be empty");
		} else {
			int cellCount = 0;
			for(int cell = 0; cell < size; ++cell) {
				cellCount = Math.max(cellCount, cellIndices[cell] + spans[cell]);
			}

			for(int cell = cellCount - 1; cell >= 0; --cell) {
				boolean starts = false;
				boolean ends = false;

				for(int i = 0; i < size; ++i) {
					if(cellIndices[i] == cell) {
						starts = true;
					}

					if(cellIndices[i] + spans[i] - 1 == cell) {
						ends = true;
					}
				}

				if(!starts || !ends) {
					if(elimitated != null) {
						elimitated.add(cell);
					}

					for(int i = 0; i < size; ++i) {
						boolean decreaseSpan = cellIndices[i] <= cell && cell < cellIndices[i] + spans[i];
						boolean decreaseIndex = cellIndices[i] > cell;
						if(decreaseSpan) {
							--spans[i];
						}

						if(decreaseIndex) {
							--cellIndices[i];
						}
					}

					--cellCount;
				}
			}

			return cellCount;
		}
	}

}
