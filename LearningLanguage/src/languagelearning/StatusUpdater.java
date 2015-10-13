package languagelearning;

public interface StatusUpdater {
    public void updateTime(long ticks);
    
    public void updateCumulativeAverageDustPercentage(double averageDustPercentage);
}
