package languagelearning;

import languagelearning.util.Props;


public class SimulationConfig {
	private int runs;
	private int trainingTicks;
	private int testTicks;
	private boolean writeStats;
	
	public SimulationConfig() {
		
	}
	
	public SimulationConfig(Props props) {
		readFromProps(props);
	}
	
	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}
	public int getTrainingTicks() {
		return trainingTicks;
	}
	public void setTrainingTicks(int trainingTicks) {
		this.trainingTicks = trainingTicks;
	}
	public int getTestTicks() {
		return testTicks;
	}
	public void setTestTicks(int testTicks) {
		this.testTicks = testTicks;
	}
	public boolean isWriteStats() {
		return writeStats;
	}
	public void setWriteStats(boolean writeStats) {
		this.writeStats = writeStats;
	}
	
	public void fillProps(Props props) {
		props.addValue("runs",runs);
		props.addValue("trainingTicks",trainingTicks);
		props.addValue("testTicks",testTicks);
		props.addValue("writeStats",writeStats);
	}
	
	public void readFromProps(Props props) {
		runs = props.getIntValue("runs");
		trainingTicks = props.getIntValue("trainingTicks");
		testTicks = props.getIntValue("testTicks");
		writeStats = props.getBooleanValue("writeStats");
	}
	
}
