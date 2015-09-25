package languagelearning.states;

/*
 * Basic environment/perception state - is there dust and/or obstacle ahead
 */
public class LookAroundState extends State {
	private boolean obstacleNorth;
	private boolean obstacleEast;
	private boolean obstacleSouth;
	private boolean obstacleWest;
	private boolean dustNorth;
	private boolean dustEast;
	private boolean dustSouth;
	private boolean dustWest;
	private boolean dustBelow;
	
	public LookAroundState() {
	}



	public boolean isObstacleNorth() {
		return obstacleNorth;
	}



	public void setObstacleNorth(boolean obstacleNorth) {
		this.obstacleNorth = obstacleNorth;
	}



	public boolean isObstacleEast() {
		return obstacleEast;
	}



	public void setObstacleEast(boolean obstacleEast) {
		this.obstacleEast = obstacleEast;
	}



	public boolean isObstacleSouth() {
		return obstacleSouth;
	}



	public void setObstacleSouth(boolean obstacleSouth) {
		this.obstacleSouth = obstacleSouth;
	}



	public boolean isObstacleWest() {
		return obstacleWest;
	}



	public void setObstacleWest(boolean obstacleWest) {
		this.obstacleWest = obstacleWest;
	}



	public boolean isDustNorth() {
		return dustNorth;
	}



	public void setDustNorth(boolean dustNorth) {
		this.dustNorth = dustNorth;
	}



	public boolean isDustEast() {
		return dustEast;
	}



	public void setDustEast(boolean dustEast) {
		this.dustEast = dustEast;
	}



	public boolean isDustSouth() {
		return dustSouth;
	}



	public void setDustSouth(boolean dustSouth) {
		this.dustSouth = dustSouth;
	}



	public boolean isDustWest() {
		return dustWest;
	}



	public void setDustWest(boolean dustWest) {
		this.dustWest = dustWest;
	}



	public boolean isDustBelow() {
		return dustBelow;
	}

	public void setDustBelow(boolean dustBelow) {
		this.dustBelow = dustBelow;
	}

	@Override
	public String toString() {
		return "O-N="+obstacleNorth+" O-E="+obstacleEast+" O-S="+obstacleSouth+" O-W="+obstacleWest+" D-N="+dustNorth+" D-E="+dustEast+" D-S="+dustSouth+" D-W="+dustWest+" D-B="+dustBelow;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LookAroundState) {
			LookAroundState other = (LookAroundState)o;
			return this.toString().equals(other.toString());
		}
		return false;
	}
}
