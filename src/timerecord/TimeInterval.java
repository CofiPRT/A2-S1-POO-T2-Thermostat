package timerecord;
import java.util.concurrent.ConcurrentSkipListSet;

public class TimeInterval implements Comparable<TimeInterval> {
	private Long intervalStart;
	private ConcurrentSkipListSet<Entry> entries;
	
	public TimeInterval(Long intervalStart) {
		this.intervalStart = intervalStart;
		entries = new ConcurrentSkipListSet<Entry>();
	}
	
	public Long getIntStart() {
		return this.intervalStart;
	}

	/**
	 * Add a new entry (timestamp, value) to this interval
	 * @param newEntry The entry to be added
	 */
	public void addEntry(Entry newEntry) {
		entries.add(newEntry);
	}

	/**
	 * Get a string representing the recorded values in ascending order
	 * @return	The generated string
	 */
	public String getValues() {
		String printString = "";
				
		for (Entry currentEntry : entries) {
			printString += " " + String.format("%.2f", currentEntry.getValue());
		}
		
		return printString.trim();
	}

	/**
	 * Get the lowest value recorded in this interval
	 * @return	Lowest value in the interval
	 */
	public Double minValue() {
		Entry firstEntry = entries.first();
		
		if (firstEntry != null) {
			return firstEntry.getValue();
		}
		
		return null;
	}
	
	/**
	 * Get the highest value recorded in this interval
	 * @return	Highest value in the interval
	 */
	public Double maxValue() {
		Entry lastEntry = entries.last();
		
		if (lastEntry != null) {
			return lastEntry.getValue();
		}
		
		return null;
	}
	
	/**
	 * Only compare the start times for these intervals
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof TimeInterval) {
			if (this.intervalStart.equals(((TimeInterval)o).getIntStart())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Only compare the start times for these intervals
	 */
	@Override
	public int compareTo(TimeInterval otherInterval) {
		return this.intervalStart.compareTo(otherInterval.getIntStart());
	}

}
