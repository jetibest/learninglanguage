package languagelearning.env;

import java.util.ArrayList;
import java.util.List;


public class EnvironmentConfig implements Cloneable {
	private int gridWidth;
	private int gridHeight;
	private int dustMin;
	private int dustMax;
	private int dustIncrement;
	private double dustStartPercentage;
	private double dustVariancePercentage;
	private boolean bounded;
	private List<DustMultiplierConfig> dustMultipliers = new ArrayList<DustMultiplierConfig>();
	
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
	public List<DustMultiplierConfig> getDustMultipliers() {
		return dustMultipliers;
	}
	public void setDustMultipliers(List<DustMultiplierConfig> dustMultipliers) {
		this.dustMultipliers = dustMultipliers;
	}
	
	
}
