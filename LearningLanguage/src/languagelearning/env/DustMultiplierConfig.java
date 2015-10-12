package languagelearning.env;

public class DustMultiplierConfig {
	private int fromX;
	private int fromY;
	private int width;
	private int height;
	private double multiplier;
	
	public DustMultiplierConfig(int fromX,int fromY, int width, int height, double multiplier) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.width = width;
		this.height = height;
		this.multiplier = multiplier;
	}

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getMultiplier() {
		return multiplier;
	}
}