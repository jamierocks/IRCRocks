package com.intellij.uiDesigner.core;

import javax.swing.JComponent;
import java.awt.Dimension;

public class Spacer extends JComponent {
	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}

	public final Dimension getPreferredSize() {
		return this.getMinimumSize();
	}
}
