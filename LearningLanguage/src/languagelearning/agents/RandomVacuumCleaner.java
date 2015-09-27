package languagelearning.agents;


public class RandomVacuumCleaner extends VacuumCleaner {
	public RandomVacuumCleaner(int x, int y) {
		super(x, y);
	}

	@Override
	public void run() {
		// Move around, and sometimes randomly change direction
		// And collect dust on the way
		collectDust();

		if (Math.random() < 0.1) {
			if (Math.random() < 0.5) {
				turnLeft();
			} else {
				turnRight();
			}
		} else {
			moveForward();
		}

	}
}
