package com.intellij.uiDesigner.core;

public final class VerticalInfo extends DimensionInfo {
	public VerticalInfo(LayoutState layoutState, int gap) {
		super(layoutState, gap);
	}

	protected int getOriginalCell(GridConstraints constraints) {
		return constraints.getRow();
	}

	protected int getOriginalSpan(GridConstraints constraints) {
		return constraints.getRowSpan();
	}

	protected int getSizePolicy(int componentIndex) {
		return this.myLayoutState.getConstraints(componentIndex).getVSizePolicy();
	}

	protected int getChildLayoutCellCount(GridLayoutManager childLayout) {
		return childLayout.getRowCount();
	}

	public int getMinimumWidth(int componentIndex) {
		return this.getMinimumSize(componentIndex).height;
	}

	public DimensionInfo getDimensionInfo(GridLayoutManager grid) {
		return grid.myVerticalInfo;
	}

	public int getCellCount() {
		return this.myLayoutState.getRowCount();
	}

	public int getPreferredWidth(int componentIndex) {
		return this.getPreferredSize(componentIndex).height;
	}
}
