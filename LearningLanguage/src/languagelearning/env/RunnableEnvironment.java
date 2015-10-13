package languagelearning.env;

import languagelearning.LearningLanguage;

public class RunnableEnvironment extends Environment implements Runnable {
	public static final long SIMULATION_INTERVAL = 1000; // is affected by
															// simulationSpeed,
															// see
															// `getSimulationSpeedMultiplier()`
	public static final int SIM_SPEED_MIN = 0;
	public static final int SIM_SPEED_MAX = 1000;
	public static final int SIM_SPEED_DEFAULT = 500;
	
	private long ticks = 0;
	private long maxTicks = 500000;
	private Thread t;
	private int simulationSpeed = SIM_SPEED_DEFAULT;

	public RunnableEnvironment(EnvironmentConfig environmentConfig) {
		super(environmentConfig);
	}
	
	public void init() {
		super.init();

		t = new Thread(this);
	}
	
	@Override
	public void start() {
		t.start();
		
		super.start();
	}

	@Override
	public void run() {
		getLogger().log(this.getClass().getName(), "Started!");

		long start;
		while (LearningLanguage.MAIN.isRunning()) {
			start = System.currentTimeMillis();
			
			if(ticks >= maxTicks)
			{
				LearningLanguage.MAIN.stop();
				return;
			}
			
			tick();
			ticks++;
			
			try {
				Thread.sleep(Math.max(0,
						(long) (getSimulationSpeedMultiplier()
								* SIMULATION_INTERVAL - (System
								.currentTimeMillis() - start))));
			} catch (Exception e) {
			}
		}

		getLogger().log(this.getClass().getName(), "Stopped!");
	}
	
	public double getSimulationSpeedMultiplier() {
		return 1 - (simulationSpeed * 1.0D - SIM_SPEED_MIN)
				/ (SIM_SPEED_MAX - SIM_SPEED_MIN);
	}

	public void setSimulationSpeed(int speed) {
		// Beware, if speed is set too high, then multiplier is zero.
		// When Thread.sleep is 0, this means that some threads may receive an
		// unfair advantage. This cannot be guaranteed then.
		// Maybe we need to use ticks instead.
		this.simulationSpeed = speed;
	}

}
