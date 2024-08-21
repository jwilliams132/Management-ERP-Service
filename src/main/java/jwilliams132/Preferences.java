package jwilliams132;

public class Preferences {

	private boolean isNavBarLabelsVisible;
	private Themes theme;
	private FontSizes fontSize;

	public Preferences() {

	}

	// public Preferences() {

	// 	this.isNavBarLabelsVisible = true;
	// 	this.theme = Themes.DARK;
	// 	this.fontSize = FontSizes.MEDIUM;
	// }

	public Preferences(boolean isNavBarLabelsVisible,
			Themes theme,
			FontSizes fontSize) {

		this.isNavBarLabelsVisible = isNavBarLabelsVisible;
		this.theme = theme;
		this.fontSize = fontSize;
	}

	public boolean isNavBarLabelsVisible() {

		return isNavBarLabelsVisible;
	}

	public void setNavBarLabelsVisible(boolean isNavBarLabelsVisible) {

		this.isNavBarLabelsVisible = isNavBarLabelsVisible;
	}

	public Themes getTheme() {

		return theme;
	}

	public void setTheme(Themes theme) {

		this.theme = theme;
	}

	public FontSizes getFontSize() {

		return fontSize;
	}

	public void setFontSize(FontSizes fontSize) {

		this.fontSize = fontSize;
	}

}
