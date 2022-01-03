package timerecord;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

public class DataRecord {
	private Long refTimestamp;
	private final Long timeInterval = 3600L; // seconds
	private final Long maxIntervals = 24L; // hours in a day
	
	private ConcurrentSkipListSet<TimeInterval> tempRecord;
	private ConcurrentSkipListSet<TimeInterval> humRecord;

	public DataRecord(Long refTimestamp) {
		this.refTimestamp = refTimestamp;
		tempRecord = new ConcurrentSkipListSet<TimeInterval>();
		humRecord = new ConcurrentSkipListSet<TimeInterval>();
	}
	
	/**
	 * Add a temperature entry to the records, based on its timestamp. If the
	 * timestamp is not valid, the entry is not added.
	 * @param timestamp		The timestamp of the recorded temperature
	 * @param temperature	The recorded temperature
	 */
	public void addTemp(Long timestamp, Double temperature) {
		if (!isTimestamp(timestamp)) {
			// do not add
			return;
		}
		
		Long intStart = calcIntStart(timestamp);
		TimeInterval foundInt = findByIntStart(tempRecord, intStart); 
				
		if (foundInt == null) {
			foundInt = new TimeInterval(intStart);
			
			// add interval to temperature record
			tempRecord.add(foundInt);
		}
		
		// add entry to interval
		foundInt.addEntry(new Entry(timestamp, temperature));
	}
	
	/**
	 * Add a humidity entry to the records, based on its timestamp. If the
	 * timestamp is not valid, the entry is not added.
	 * @param timestamp		The timestamp of the recorded humidity
	 * @param humidity		The recorded humidity
	 */
	public void addHum(Long timestamp, Double humidity) {
		if (!isTimestamp(timestamp)) {
			// do not add
			return;
		}
		
		Long intStart = calcIntStart(timestamp);
		TimeInterval foundInt = findByIntStart(humRecord, intStart); 
		
		if (foundInt == null) {
			foundInt = new TimeInterval(intStart);
			
			// add interval to temperature record
			humRecord.add(foundInt);
		}
		
		// add entry to interval
		foundInt.addEntry(new Entry(timestamp, humidity));
	}

	/**
	 * List the recorded temperatures for this room within the given interval,
	 * in ascending order (for each interval)
	 * @param startInterval		Beginning of the interval (timestamp)
	 * @param stopInterval		End of the interval (timestamp)
	 * @return 					A string consisting of the aforementioned
	 * values
	 */
	public String listTemp(Long startInterval, Long stopInterval) {
		Iterator<TimeInterval> hourIterator = tempRecord.descendingIterator();
		
		String printString = "";
		
		// iterate hour by hour
		while (hourIterator.hasNext()) {
			TimeInterval currentHour = hourIterator.next();
						
			Long currentStartInt = currentHour.getIntStart();
						
			boolean isAbove = currentStartInt >= startInterval;
			boolean isBelow = currentStartInt + timeInterval - 1 <= stopInterval;
			
			// if within bounds, print
			if (isAbove && isBelow) {
				printString += " " + currentHour.getValues();
			}
		}
		
		return printString.trim();
	}

	/**
	 * Get the lowest temperature recorded in the last recorded hour
	 * @return	Said temperature
	 */
	public Double getLastTemp() {
		if (tempRecord.isEmpty()) {
			return null;
		}
		
		return tempRecord.last().minValue();
	}

	/**
	 * Get the highest humidity level recorded in the last recorded hour
	 * @return	Said humidity level
	 */
	public Double getLastHum() {
		if (humRecord.isEmpty()) {
			return null;
		}
		
		return humRecord.last().maxValue();
	}
	
	/**
	 * Decides if a timestamp is within bounds
	 * @param timestamp		Given timestamp
	 * @return 				{@code true} if the timestamp is between {@code 0}
	 * and {@code refTimestamp}, {@code false} otherwise
	 */
	private boolean isTimestamp(Long timestamp) {
		if (timestamp < 0 || timestamp > refTimestamp) {
			return false;
		}
		
		if (timestamp <= refTimestamp - maxIntervals*timeInterval) {
			// too old
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the start of the hour-interval containing the given timestamp
	 * @param timestamp		The timestamp which interval shall be searched
	 * @return				The start of the corresponding time interval
	 */
	private Long calcIntStart(Long timestamp) {
		Long quotinent = (refTimestamp - timestamp) / timeInterval + 1;
		
		return Math.max(0L, refTimestamp - quotinent * timeInterval + 1);
	}

	/**
	 * Find an hour-interval in the given recorded based on its starting
	 * timestamp.
	 * @param record		The record to search in
	 * @param intStart		The start of the interval that shall be searched
	 * @return				The found {@code TimeInterval}, or {@code null} if
	 * it hasn't been found
	 */
	private TimeInterval findByIntStart(ConcurrentSkipListSet<TimeInterval> record,
									Long intStart) {
		for (TimeInterval currentInterval : record) {
			if (currentInterval.getIntStart().equals(intStart)) {
				return currentInterval;
			}
		}
		
		// not found
		return null;
	}
}
