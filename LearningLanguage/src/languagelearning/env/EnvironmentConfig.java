package languagelearning.env;


public class EnvironmentConfig implements Cloneable {
	private int gridWidth;
	private int gridHeight;
	private int dustMin;
	private int dustMax;
	private int dustIncrement;
	private double dustStartPercentage;
	private double dustVariancePercentage;
	private boolean bounded;
	
	public int getGridWidth() {
		return gridWidth;
	}
	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}
	public int getGridHeight() {
		return gridHeight;
	}
	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}
	public int getDustMin() {
		return dustMin;
	}
	public void setDustMin(int dustMin) {
		this.dustMin = dustMin;
	}
	public int getDustMax() {
		return dustMax;
	}
	public void setDustMax(int dustMax) {
		this.dustMax = dustMax;
	}
	public int getDustIncrement() {
		return dustIncrement;
	}
	public void setDustIncrement(int dustIncrement) {
		this.dustIncrement = dustIncrement;
	}
	public double getDustStartPercentage() {
		return dustStartPercentage;
	}
	public void setDustStartPercentage(double dustStartPercentage) {
		this.dustStartPercentage = dustStartPercentage;
	}
	public double getDustVariancePercentage() {
		return dustVariancePercentage;
	}
	public void setDustVariancePercentage(double dustVariancePercentage) {
		this.dustVariancePercentage = dustVariancePercentage;
	}
	public boolean isBounded() {
		return bounded;
	}
	public boolean isBoundless() {
		return !bounded;
	}
	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
	
	
}
