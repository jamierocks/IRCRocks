package com.intellij.uiDesigner.core;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public final class GridLayoutManager extends AbstractLayout {
    private int myMinCellSize;
    private final int[] myRowStretches;
    private final int[] myColumnStretches;
    private final int[] myYs;
    private final int[] myHeights;
    private final int[] myXs;
    private final int[] myWidths;
    private LayoutState myLayoutState;
    protected DimensionInfo myHorizontalInfo;
    protected DimensionInfo myVerticalInfo;
    private boolean mySameSizeHorizontally;
    private boolean mySameSizeVertically;
    public static Object DESIGN_TIME_INSETS = new Object();

    public GridLayoutManager(int rowCount, int columnCount) {
        this.myMinCellSize = 20;
        if(columnCount < 1) {
            throw new IllegalArgumentException("wrong columnCount: " + columnCount);
        } else if(rowCount < 1) {
            throw new IllegalArgumentException("wrong rowCount: " + rowCount);
        } else {
            this.myRowStretches = new int[rowCount];

            int i;
            for(i = 0; i < rowCount; ++i) {
                this.myRowStretches[i] = 1;
            }

            this.myColumnStretches = new int[columnCount];

            for(i = 0; i < columnCount; ++i) {
                this.myColumnStretches[i] = 1;
            }

            this.myXs = new int[columnCount];
            this.myWidths = new int[columnCount];
            this.myYs = new int[rowCount];
            this.myHeights = new int[rowCount];
        }
    }

    public GridLayoutManager(int rowCount, int columnCount, Insets margin, int hGap, int vGap) {
        this(rowCount, columnCount);
        this.setMargin(margin);
        this.setHGap(hGap);
        this.setVGap(vGap);
        this.myMinCellSize = 0;
    }

    public GridLayoutManager(int rowCount, int columnCount, Insets margin, int hGap, int vGap, boolean sameSizeHorizontally, boolean sameSizeVertically) {
        this(rowCount, columnCount, margin, hGap, vGap);
        this.mySameSizeHorizontally = sameSizeHorizontally;
        this.mySameSizeVertically = sameSizeVertically;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        GridConstraints c = (GridConstraints) constraints;
        int row = c.getRow();
        int rowSpan = c.getRowSpan();
        int rowCount = this.getRowCount();
        if(row >= 0 && row < rowCount) {
            if(row + rowSpan - 1 >= rowCount) {
                throw new IllegalArgumentException("wrong row span: " + rowSpan + "; row=" + row + " rowCount=" + rowCount);
            } else {
                int column = c.getColumn();
                int colSpan = c.getColSpan();
                int columnCount = this.getColumnCount();
                if(column >= 0 && column < columnCount) {
                    if(column + colSpan - 1 >= columnCount) {
                        throw new IllegalArgumentException("wrong col span: " + colSpan + "; column=" + column + " columnCount=" + columnCount);
                    } else {
                        super.addLayoutComponent(comp, constraints);
                    }
                } else {
                    throw new IllegalArgumentException("wrong column: " + column);
                }
            }
        } else {
            throw new IllegalArgumentException("wrong row: " + row);
        }
    }

    public int getRowCount() {
        return this.myRowStretches.length;
    }

    public int getColumnCount() {
        return this.myColumnStretches.length;
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public Dimension minimumLayoutSize(Container container) {
        this.validateInfos(container);
        DimensionInfo horizontalInfo = this.myHorizontalInfo;
        DimensionInfo verticalInfo = this.myVerticalInfo;
        Dimension result = this.getTotalGap(container, horizontalInfo, verticalInfo);
        int[] widths = this.getMinSizes(horizontalInfo);
        if(this.mySameSizeHorizontally) {
            makeSameSizes(widths);
        }

        result.width += sum(widths);
        int[] heights = this.getMinSizes(verticalInfo);
        if(this.mySameSizeVertically) {
            makeSameSizes(heights);
        }

        result.height += sum(heights);
        return result;
    }

    private static void makeSameSizes(int[] widths) {
        int max = widths[0];

        int i;
        for(i = 0; i < widths.length; ++i) {
            int width = widths[i];
            max = Math.max(width, max);
        }

        for(i = 0; i < widths.length; ++i) {
            widths[i] = max;
        }

    }

    private static int[] getSameSizes(DimensionInfo info, int totalWidth) {
        int[] widths = new int[info.getCellCount()];
        int average = totalWidth / widths.length;
        int rest = totalWidth % widths.length;

        for(int i = 0; i < widths.length; ++i) {
            widths[i] = average;
            if(rest > 0) {
                ++widths[i];
                --rest;
            }
        }

        return widths;
    }

    public Dimension preferredLayoutSize(Container container) {
        this.validateInfos(container);
        DimensionInfo horizontalInfo = this.myHorizontalInfo;
        DimensionInfo verticalInfo = this.myVerticalInfo;
        Dimension result = this.getTotalGap(container, horizontalInfo, verticalInfo);
        int[] widths = this.getPrefSizes(horizontalInfo);
        if(this.mySameSizeHorizontally) {
            makeSameSizes(widths);
        }

        result.width += sum(widths);
        int[] heights = this.getPrefSizes(verticalInfo);
        if(this.mySameSizeVertically) {
            makeSameSizes(heights);
        }

        result.height += sum(heights);
        return result;
    }

    private static int sum(int[] ints) {
        int result = 0;
        for(int i = ints.length - 1; i >= 0; --i) {
            result += ints[i];
        }

        return result;
    }

    private Dimension getTotalGap(Container container, DimensionInfo hInfo, DimensionInfo vInfo) {
        Insets insets = getInsets(container);
        return new Dimension(insets.left + insets.right + countGap(hInfo, 0, hInfo.getCellCount()) + this.myMargin.left + this.myMargin.right, insets.top + insets.bottom + countGap(vInfo, 0, vInfo.getCellCount()) + this.myMargin.top + this.myMargin.bottom);
    }

    private static int getDesignTimeInsets(Container container) {
        for(; container != null; container = container.getParent()) {
            if(container instanceof JComponent) {
                Integer designTimeInsets = (Integer) ((JComponent) container).getClientProperty(DESIGN_TIME_INSETS);
                if(designTimeInsets != null) {
                    return designTimeInsets;
                }
            }
        }

        return 0;
    }

    private static Insets getInsets(Container container) {
        Insets insets = container.getInsets();
        int insetsValue = getDesignTimeInsets(container);
        return insetsValue != 0 ? new Insets(insets.top + insetsValue, insets.left + insetsValue, insets.bottom + insetsValue, insets.right + insetsValue) : insets;
    }

    private static int countGap(DimensionInfo info, int startCell, int cellCount) {
        int counter = 0;

        for(int cellIndex = startCell + cellCount - 2; cellIndex >= startCell; --cellIndex) {
            if(shouldAddGapAfterCell(info, cellIndex)) {
                ++counter;
            }
        }

        return counter * info.getGap();
    }

    private static boolean shouldAddGapAfterCell(DimensionInfo info, int cellIndex) {
        if(cellIndex >= 0 && cellIndex < info.getCellCount()) {
            boolean endsInThis = false;
            boolean startsInNext = false;
            int indexOfNextNotEmpty = -1;

            int i;
            for(i = cellIndex + 1; i < info.getCellCount(); ++i) {
                if(!isCellEmpty(info, i)) {
                    indexOfNextNotEmpty = i;
                    break;
                }
            }

            for(i = 0; i < info.getComponentCount(); ++i) {
                Component component = info.getComponent(i);
                if(!(component instanceof Spacer)) {
                    if(info.componentBelongsCell(i, cellIndex) && DimensionInfo.findAlignedChild(component, info.getConstraints(i)) != null) {
                        return true;
                    }

                    if(info.getCell(i) == indexOfNextNotEmpty) {
                        startsInNext = true;
                    }

                    if(info.getCell(i) + info.getSpan(i) - 1 == cellIndex) {
                        endsInThis = true;
                    }
                }
            }

            return startsInNext && endsInThis;
        } else {
            throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
        }
    }

    private static boolean isCellEmpty(DimensionInfo info, int cellIndex) {
        if(cellIndex >= 0 && cellIndex < info.getCellCount()) {
            for(int i = 0; i < info.getComponentCount(); ++i) {
                Component component = info.getComponent(i);
                if(info.getCell(i) == cellIndex && !(component instanceof Spacer)) {
                    return false;
                }
            }

            return true;
        } else {
            throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + info.getCellCount());
        }
    }

    public void layoutContainer(Container container) {
        this.validateInfos(container);
        LayoutState layoutState = this.myLayoutState;
        DimensionInfo horizontalInfo = this.myHorizontalInfo;
        DimensionInfo verticalInfo = this.myVerticalInfo;
        Insets insets = getInsets(container);
        int skipLayout = this.checkSetSizesFromParent(container, insets);
        Dimension gap = this.getTotalGap(container, horizontalInfo, verticalInfo);
        Dimension size = container.getSize();
        size.width -= gap.width;
        size.height -= gap.height;
        Dimension prefSize = this.preferredLayoutSize(container);
        prefSize.width -= gap.width;
        prefSize.height -= gap.height;
        Dimension minSize = this.minimumLayoutSize(container);
        minSize.width -= gap.width;
        minSize.height -= gap.height;
        int[] i;
        int c;
        int component;
        if((skipLayout & 1) == 0) {
            if(this.mySameSizeVertically) {
                i = getSameSizes(verticalInfo, Math.max(size.height, minSize.height));
            } else if(size.height < prefSize.height) {
                i = this.getMinSizes(verticalInfo);
                this.new_doIt(i, 0, verticalInfo.getCellCount(), size.height, verticalInfo, true);
            } else {
                i = this.getPrefSizes(verticalInfo);
                this.new_doIt(i, 0, verticalInfo.getCellCount(), size.height, verticalInfo, false);
            }

            c = insets.top + this.myMargin.top;

            for(component = 0; component < i.length; ++component) {
                this.myYs[component] = c;
                this.myHeights[component] = i[component];
                c += i[component];
                if(shouldAddGapAfterCell(verticalInfo, component)) {
                    c += verticalInfo.getGap();
                }
            }
        }

        if((skipLayout & 2) == 0) {
            if(this.mySameSizeHorizontally) {
                i = getSameSizes(horizontalInfo, Math.max(size.width, minSize.width));
            } else if(size.width < prefSize.width) {
                i = this.getMinSizes(horizontalInfo);
                this.new_doIt(i, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, true);
            } else {
                i = this.getPrefSizes(horizontalInfo);
                this.new_doIt(i, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, false);
            }

            c = insets.left + this.myMargin.left;

            for(component = 0; component < i.length; ++component) {
                this.myXs[component] = c;
                this.myWidths[component] = i[component];
                c += i[component];
                if(shouldAddGapAfterCell(horizontalInfo, component)) {
                    c += horizontalInfo.getGap();
                }
            }
        }

        for(int index = 0; index < layoutState.getComponentCount(); ++index) {
            GridConstraints constraints = layoutState.getConstraints(index);
            Component cmpnt = layoutState.getComponent(index);
            int column = horizontalInfo.getCell(index);
            int colSpan = horizontalInfo.getSpan(index);
            int row = verticalInfo.getCell(index);
            int rowSpan = verticalInfo.getSpan(index);
            int cellWidth = this.myXs[column + colSpan - 1] + this.myWidths[column + colSpan - 1] - this.myXs[column];
            int cellHeight = this.myYs[row + rowSpan - 1] + this.myHeights[row + rowSpan - 1] - this.myYs[row];
            Dimension componentSize = new Dimension(cellWidth, cellHeight);
            if((constraints.getFill() & 1) == 0) {
                componentSize.width = Math.min(componentSize.width, horizontalInfo.getPreferredWidth(index));
            }

            if((constraints.getFill() & 2) == 0) {
                componentSize.height = Math.min(componentSize.height, verticalInfo.getPreferredWidth(index));
            }

            Util.adjustSize(cmpnt, constraints, componentSize);
            int dx = 0;
            int dy = 0;
            if((constraints.getAnchor() & 4) != 0) {
                dx = cellWidth - componentSize.width;
            } else if((constraints.getAnchor() & 8) == 0) {
                dx = (cellWidth - componentSize.width) / 2;
            }

            if((constraints.getAnchor() & 2) != 0) {
                dy = cellHeight - componentSize.height;
            } else if((constraints.getAnchor() & 1) == 0) {
                dy = (cellHeight - componentSize.height) / 2;
            }

            int indent = 10 * constraints.getIndent();
            componentSize.width -= indent;
            dx += indent;
            cmpnt.setBounds(this.myXs[column] + dx, this.myYs[row] + dy, componentSize.width, componentSize.height);
        }

    }

    private int checkSetSizesFromParent(Container container, Insets insets) {
        int skipLayout = 0;
        GridLayoutManager parentGridLayout = null;
        GridConstraints parentGridConstraints = null;
        Container parent = container.getParent();
        if(parent != null) {
            if(parent.getLayout() instanceof GridLayoutManager) {
                parentGridLayout = (GridLayoutManager) parent.getLayout();
                parentGridConstraints = parentGridLayout.getConstraintsForComponent(container);
            } else {
                Container col = parent.getParent();
                if(col != null && col.getLayout() instanceof GridLayoutManager) {
                    parentGridLayout = (GridLayoutManager) col.getLayout();
                    parentGridConstraints = parentGridLayout.getConstraintsForComponent(parent);
                }
            }
        }

        if(parentGridLayout != null && parentGridConstraints.isUseParentLayout()) {
            int i;
            if(this.myRowStretches.length == parentGridConstraints.getRowSpan()) {
                int row = parentGridConstraints.getRow();
                this.myYs[0] = insets.top + this.myMargin.top;
                this.myHeights[0] = parentGridLayout.myHeights[row] - this.myYs[0];

                for(i = 1; i < this.myRowStretches.length; ++i) {
                    this.myYs[i] = parentGridLayout.myYs[i + row] - parentGridLayout.myYs[row];
                    this.myHeights[i] = parentGridLayout.myHeights[i + row];
                }

                this.myHeights[this.myRowStretches.length - 1] -= insets.bottom + this.myMargin.bottom;
                skipLayout |= 1;
            }

            if(this.myColumnStretches.length == parentGridConstraints.getColSpan()) {
                int column = parentGridConstraints.getColumn();
                this.myXs[0] = insets.left + this.myMargin.left;
                this.myWidths[0] = parentGridLayout.myWidths[column] - this.myXs[0];

                for(i = 1; i < this.myColumnStretches.length; ++i) {
                    this.myXs[i] = parentGridLayout.myXs[i + column] - parentGridLayout.myXs[column];
                    this.myWidths[i] = parentGridLayout.myWidths[i + column];
                }

                this.myWidths[this.myColumnStretches.length - 1] -= insets.right + this.myMargin.right;
                skipLayout |= 2;
            }
        }

        return skipLayout;
    }

    public void invalidateLayout(Container container) {
        this.myLayoutState = null;
        this.myHorizontalInfo = null;
        this.myVerticalInfo = null;
    }

    protected void validateInfos(Container container) {
        if(this.myLayoutState == null) {
            this.myLayoutState = new LayoutState(this, getDesignTimeInsets(container) == 0);
            this.myHorizontalInfo = new HorizontalInfo(this.myLayoutState, getHGapImpl(container));
            this.myVerticalInfo = new VerticalInfo(this.myLayoutState, getVGapImpl(container));
        }
    }

    private int[] getMinSizes(DimensionInfo info) {
        return this.getMinOrPrefSizes(info, true);
    }

    private int[] getPrefSizes(DimensionInfo info) {
        return this.getMinOrPrefSizes(info, false);
    }

    private int[] getMinOrPrefSizes(DimensionInfo info, boolean min) {
        int[] widths = new int[info.getCellCount()];

        int toProcess;
        for(toProcess = 0; toProcess < widths.length; ++toProcess) {
            widths[toProcess] = this.myMinCellSize;
        }

        for(toProcess = info.getComponentCount() - 1; toProcess >= 0; --toProcess) {
            if(info.getSpan(toProcess) == 1) {
                int i = min ? getMin2(info, toProcess) : Math.max(info.getMinimumWidth(toProcess), info.getPreferredWidth(toProcess));
                int size = info.getCell(toProcess);
                int span = countGap(info, size, info.getSpan(toProcess));
                i = Math.max(i - span, 0);
                widths[size] = Math.max(widths[size], i);
            }
        }

        updateSizesFromChildren(info, min, widths);
        boolean[] priority = new boolean[info.getCellCount()];
        for(int i = info.getComponentCount() - 1; i >= 0; --i) {
            int size = min ? getMin2(info, i) : Math.max(info.getMinimumWidth(i), info.getPreferredWidth(i));
            int span = info.getSpan(i);
            int cell = info.getCell(i);
            int gap = countGap(info, cell, span);
            size = Math.max(size - gap, 0);
            Arrays.fill(priority, false);
            int curSize = 0;

            for(int higherPriorityCells = 0; higherPriorityCells < span; ++higherPriorityCells) {
                curSize += widths[higherPriorityCells + cell];
                priority[higherPriorityCells + cell] = true;
            }

            if(curSize < size) {
                boolean[] higherPriorityCells = new boolean[priority.length];
                this.getCellsWithHigherPriorities(info, priority, higherPriorityCells, false, widths);
                distribute(higherPriorityCells, info, size - curSize, widths);
            }
        }

        return widths;
    }

    private static void updateSizesFromChildren(DimensionInfo info, boolean min, int[] widths) {
        for(int i = info.getComponentCount() - 1; i >= 0; --i) {
            Component child = info.getComponent(i);
            GridConstraints c = info.getConstraints(i);
            if(c.isUseParentLayout() && child instanceof Container) {
                Container container = (Container) child;
                if(container.getLayout() instanceof GridLayoutManager) {
                    updateSizesFromChild(info, min, widths, container, i);
                } else if(container.getComponentCount() == 1 && container.getComponent(0) instanceof Container) {
                    Container childContainer = (Container) container.getComponent(0);
                    if(childContainer.getLayout() instanceof GridLayoutManager) {
                        updateSizesFromChild(info, min, widths, childContainer, i);
                    }
                }
            }
        }

    }

    private static void updateSizesFromChild(DimensionInfo info, boolean min, int[] widths, Container container, int childIndex) {
        GridLayoutManager childLayout = (GridLayoutManager) container.getLayout();
        if(info.getSpan(childIndex) == info.getChildLayoutCellCount(childLayout)) {
            childLayout.validateInfos(container);
            DimensionInfo childInfo = info instanceof HorizontalInfo ? childLayout.myHorizontalInfo : childLayout.myVerticalInfo;
            int[] sizes = childLayout.getMinOrPrefSizes(childInfo, min);
            int cell = info.getCell(childIndex);

            for(int j = 0; j < sizes.length; ++j) {
                widths[cell + j] = Math.max(widths[cell + j], sizes[j]);
            }
        }

    }

    private static int getMin2(DimensionInfo info, int componentIndex) {
        int s;
        if((info.getSizePolicy(componentIndex) & 1) != 0) {
            s = info.getMinimumWidth(componentIndex);
        } else {
            s = Math.max(info.getMinimumWidth(componentIndex), info.getPreferredWidth(componentIndex));
        }

        return s;
    }

    private void new_doIt(int[] widths, int cell, int span, int minWidth, DimensionInfo info, boolean checkPrefs) {
        int toDistribute = minWidth;

        for(int allowedCells = cell; allowedCells < cell + span; ++allowedCells) {
            toDistribute -= widths[allowedCells];
        }

        if(toDistribute > 0) {
            boolean[] priority = new boolean[info.getCellCount()];
            for(int higherPriorityCells = cell; higherPriorityCells < cell + span; ++higherPriorityCells) {
                priority[higherPriorityCells] = true;
            }

            boolean[] higherPriorityCells = new boolean[info.getCellCount()];
            this.getCellsWithHigherPriorities(info, priority, higherPriorityCells, checkPrefs, widths);
            distribute(higherPriorityCells, info, toDistribute, widths);
        }
    }

    private static void distribute(boolean[] higherPriorityCells, DimensionInfo info, int toDistribute, int[] widths) {
        int stretches = 0;

        int i;
        for(i = 0; i < info.getCellCount(); ++i) {
            if(higherPriorityCells[i]) {
                stretches += info.getStretch(i);
            }
        }

        i = toDistribute;

        for(int i1 = 0; i1 < info.getCellCount(); ++i1) {
            if(higherPriorityCells[i1]) {
                int addon = i * info.getStretch(i1) / stretches;
                widths[i1] += addon;
                toDistribute -= addon;
            }
        }

        if(toDistribute != 0) {
            for(i = 0; i < info.getCellCount(); ++i) {
                if(higherPriorityCells[i]) {
                    ++widths[i];
                    --toDistribute;
                    if(toDistribute == 0) {
                        break;
                    }
                }
            }
        }

        if(toDistribute != 0) {
            throw new IllegalStateException("toDistribute = " + toDistribute);
        }
    }

    private void getCellsWithHigherPriorities(DimensionInfo info, boolean[] allowedCells, boolean[] higherPriorityCells, boolean checkPrefs, int[] widths) {
        Arrays.fill(higherPriorityCells, false);
        int foundCells = 0;
        if(checkPrefs) {
            int[] cell = this.getMinOrPrefSizes(info, false);

            for(int cell1 = 0; cell1 < allowedCells.length; ++cell1) {
                if(allowedCells[cell1] && !isCellEmpty(info, cell1) && cell[cell1] > widths[cell1]) {
                    higherPriorityCells[cell1] = true;
                    ++foundCells;
                }
            }

            if(foundCells > 0) {
                return;
            }
        }

        for(int i = 0; i < allowedCells.length; ++i) {
            if(allowedCells[i] && (info.getCellSizePolicy(i) & 4) != 0) {
                higherPriorityCells[i] = true;
                ++foundCells;
            }
        }

        if(foundCells <= 0) {
            for(int i = 0; i < allowedCells.length; ++i) {
                if(allowedCells[i] && (info.getCellSizePolicy(i) & 2) != 0) {
                    higherPriorityCells[i] = true;
                    ++foundCells;
                }
            }

            if(foundCells <= 0) {
                for(int i = 0; i < allowedCells.length; ++i) {
                    if(allowedCells[i] && !isCellEmpty(info, i)) {
                        higherPriorityCells[i] = true;
                        ++foundCells;
                    }
                }

                if(foundCells <= 0) {
                    for(int i = 0; i < allowedCells.length; ++i) {
                        if(allowedCells[i]) {
                            higherPriorityCells[i] = true;
                        }
                    }

                }
            }
        }
    }
}
