package languagelearning.env;

import java.util.ArrayList;
import java.util.List;

import languagelearning.util.Props;


public class EnvironmentConfig implements Cloneable {
	private int gridWidth;
	private int gridHeight;
	private int dustMin;
	private int dustMax;
	private int dustIncrement;
	private double dustStartPercentage;
	private double dustVariancePercentage;
	private boolean bounded;
	private int maxTicks;
	private List<DustMultiplierConfig> dustMultipliers = new ArrayList<DustMultiplierConfig>();
	
	public EnvironmentConfig() {
		
	}
	
	public EnvironmentConfig(Props props) {
		this.readFromProps(props);
	}
	
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
	
	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	public void fillProps(Props props) {
		props.addValue("gridWidth",gridWidth);
		props.addValue("gridHeight",gridHeight);
		props.addValue("dustMin",dustMin);
		props.addValue("dustMax",dustMax);
		props.addValue("dustIncrement",dustIncrement);
		props.addValue("dustStartPercentage",dustStartPercentage);
		props.addValue("dustVariancePercentage",dustVariancePercentage);
		props.addValue("bounded",bounded);
		props.addValue("maxTicks",maxTicks);
		for (int i = 0; i < dustMultipliers.size(); i++) {
			DustMultiplierConfig dustMultiplierConfig = dustMultipliers.get(i);
			String key = "dustMultiplier" + i + ".";
			props.addValue(key+"fromX", dustMultiplierConfig.getFromX());
			props.addValue(key+"fromY", dustMultiplierConfig.getFromY());
			props.addValue(key+"width", dustMultiplierConfig.getWidth());
			props.addValue(key+"height", dustMultiplierConfig.getHeight());
			props.addValue(key+"multiplier", dustMultiplierConfig.getMultiplier());
		}
	}
	
	public void readFromProps(Props props) {
		gridWidth = props.getIntValue("gridWidth");
		gridHeight = props.getIntValue("gridHeight");
		dustMin = props.getIntValue("dustMin");
		dustMax = props.getIntValue("dustMax");
		dustIncrement = props.getIntValue("dustIncrement");
		dustStartPercentage = props.getDoubleValue("dustStartPercentage");
		dustVariancePercentage = props.getDoubleValue("dustVariancePercentage");
		bounded = props.getBooleanValue("bounded");
		maxTicks = props.getIntValue("maxTicks");
		dustMultipliers.clear();
		for (int i = 0; i < 100; i++) {
			String key = "dustMultiplier" + i + ".";
			if (props.hasKey(key + "fromX")) {
				DustMultiplierConfig dustMultiplierConfig = 
						new DustMultiplierConfig(props.getIntValue(key + "fromX"),props.getIntValue(key + "fromY")
								,props.getIntValue(key + "width"),props.getIntValue(key + "height"),props.getDoubleValue(key + "multiplier"));
				dustMultipliers.add(dustMultiplierConfig);
			} else {
				break;
			}
		}
	}
}
