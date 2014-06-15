package org.spacehq.iirc.html;

public enum HtmlColor {
	Aqua,
	Black,
	Blue,
	Fuchsia,
	Gray,
	Green,
	Lime,
	Maroon,
	Navy,
	Olive,
	Orange,
	Purple,
	Red,
	Silver,
	Teal,
	White,
	Yellow,
	End;

	@Override
	public String toString() {
		return this == End ? "</font>" : "<font color='" + this.name().toLowerCase() + "'>";
	}
}
