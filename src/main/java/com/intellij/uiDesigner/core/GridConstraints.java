package com.intellij.uiDesigner.core;

import java.awt.*;

public final class GridConstraints implements Cloneable {
    public static final GridConstraints[] EMPTY_ARRAY = new GridConstraints[0];
    private int myRow;
    private int myColumn;
    private int myRowSpan;
    private int myColSpan;
    private int myVSizePolicy;
    private int myHSizePolicy;
    private int myFill;
    private int myAnchor;
    public final Dimension myMinimumSize;
    public final Dimension myPreferredSize;
    public final Dimension myMaximumSize;
    private int myIndent;
    private boolean myUseParentLayout;

    public GridConstraints(int row, int column, int rowSpan, int colSpan, int anchor, int fill, int HSizePolicy, int VSizePolicy, Dimension minimumSize, Dimension preferredSize, Dimension maximumSize) {
        this.myRow = row;
        this.myColumn = column;
        this.myRowSpan = rowSpan;
        this.myColSpan = colSpan;
        this.myAnchor = anchor;
        this.myFill = fill;
        this.myHSizePolicy = HSizePolicy;
        this.myVSizePolicy = VSizePolicy;
        this.myMinimumSize = minimumSize != null ? new Dimension(minimumSize) : new Dimension(-1, -1);
        this.myPreferredSize = preferredSize != null ? new Dimension(preferredSize) : new Dimension(-1, -1);
        this.myMaximumSize = maximumSize != null ? new Dimension(maximumSize) : new Dimension(-1, -1);
        this.myIndent = 0;
    }

    public GridConstraints(int row, int column, int rowSpan, int colSpan, int anchor, int fill, int HSizePolicy, int VSizePolicy, Dimension minimumSize, Dimension preferredSize, Dimension maximumSize, int indent) {
        this(row, column, rowSpan, colSpan, anchor, fill, HSizePolicy, VSizePolicy, minimumSize, preferredSize, maximumSize);
        this.myIndent = indent;
    }

    public GridConstraints(int row, int column, int rowSpan, int colSpan, int anchor, int fill, int HSizePolicy, int VSizePolicy, Dimension minimumSize, Dimension preferredSize, Dimension maximumSize, int indent, boolean useParentLayout) {
        this(row, column, rowSpan, colSpan, anchor, fill, HSizePolicy, VSizePolicy, minimumSize, preferredSize, maximumSize, indent);
        this.myUseParentLayout = useParentLayout;
    }

    public int getColumn() {
        return this.myColumn;
    }

    public int getRow() {
        return this.myRow;
    }

    public int getRowSpan() {
        return this.myRowSpan;
    }

    public int getColSpan() {
        return this.myColSpan;
    }

    public int getHSizePolicy() {
        return this.myHSizePolicy;
    }

    public int getVSizePolicy() {
        return this.myVSizePolicy;
    }

    public int getAnchor() {
        return this.myAnchor;
    }

    public int getFill() {
        return this.myFill;
    }

    public int getIndent() {
        return this.myIndent;
    }

    public boolean isUseParentLayout() {
        return this.myUseParentLayout;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof GridConstraints)) {
            return false;
        } else {
            GridConstraints gridConstraints = (GridConstraints) o;
            return this.myAnchor == gridConstraints.myAnchor && this.myColSpan == gridConstraints.myColSpan && this.myColumn == gridConstraints.myColumn && this.myFill == gridConstraints.myFill && this.myHSizePolicy == gridConstraints.myHSizePolicy && this.myRow == gridConstraints.myRow && this.myRowSpan == gridConstraints.myRowSpan && this.myVSizePolicy == gridConstraints.myVSizePolicy && ((this.myMaximumSize != null && this.myMaximumSize.equals(gridConstraints.myMaximumSize)) || gridConstraints.myMaximumSize == null) && ((this.myMinimumSize != null && this.myMinimumSize.equals(gridConstraints.myMinimumSize)) || gridConstraints.myMinimumSize == null) && ((this.myPreferredSize != null && this.myPreferredSize.equals(gridConstraints.myPreferredSize)) || gridConstraints.myPreferredSize == null) && this.myIndent == gridConstraints.myIndent && this.myUseParentLayout == gridConstraints.myUseParentLayout;
        }
    }

    @Override
    public int hashCode() {
        int result = this.myRow;
        result = 29 * result + this.myColumn;
        result = 29 * result + this.myRowSpan;
        result = 29 * result + this.myColSpan;
        result = 29 * result + this.myVSizePolicy;
        result = 29 * result + this.myHSizePolicy;
        result = 29 * result + this.myFill;
        result = 29 * result + this.myAnchor;
        result = 29 * result + (this.myMinimumSize != null ? this.myMinimumSize.hashCode() : 0);
        result = 29 * result + (this.myPreferredSize != null ? this.myPreferredSize.hashCode() : 0);
        result = 29 * result + (this.myMaximumSize != null ? this.myMaximumSize.hashCode() : 0);
        result = 29 * result + this.myIndent;
        result = 29 * result + (this.myUseParentLayout ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridConstraints (row=" + this.myRow + ", col=" + this.myColumn + ", rowspan=" + this.myRowSpan + ", colspan=" + this.myColSpan + ")";
    }

    @Override
    public Object clone() {
        return new GridConstraints(this.myRow, this.myColumn, this.myRowSpan, this.myColSpan, this.myAnchor, this.myFill, this.myHSizePolicy, this.myVSizePolicy, new Dimension(this.myMinimumSize), new Dimension(this.myPreferredSize), new Dimension(this.myMaximumSize), this.myIndent, this.myUseParentLayout);
    }
}
