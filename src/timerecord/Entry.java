package timerecord;

public class Entry implements Comparable<Entry>{
	private Long timestamp;
	private Double value;

	public Entry(Long timestamp, Double value) {
		this.timestamp = timestamp;
		this.value = value;
	}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public Double getValue() {
		return this.value;
	}
	
	/**
	 * Only compare the values from the two entries
	 */
	@Override
	public int compareTo(Entry otherEntry) {
		return this.value.compareTo(otherEntry.getValue());
	}
	
	/**
	 * Only compare the values from the two entries
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Entry && this.value.equals(((Entry)o).getValue())) {
			return true;
		}
		
		return false;
	}

}
