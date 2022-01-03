package thermostat;

import timerecord.DataRecord;

public class Room implements Comparable<Room>{
	private String name;
	private String deviceID;
	private Double surfaceArea;
	
	private DataRecord data;

	public Room(String name, String deviceID, Double surfaceArea) {
		this.name = name;
		this.deviceID = deviceID;
		this.surfaceArea = surfaceArea;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDeviceID() {
		return this.deviceID;
	}
	
	public Double getSurfaceArea() {
		return this.surfaceArea;
	}

	/**
	 * Adds a temperature entry for this room
	 * @param timestamp		The timestamp the temperature was recorded at
	 * @param temperature	The recorded temperature
	 */
	public void addTemp(Long timestamp, Double temperature) {
		data.addTemp(timestamp, temperature);
	}

	/**
	 * Adds a humidity entry for this room
	 * @param timestamp		The timestamp the humidity was recorded at
	 * @param humidity		The recorded humidity
	 */
	public void addHum(Long timestamp, Double humidity) {
		data.addHum(timestamp, humidity);
	}

	/**
	 * Initializes a data record for this room
	 * @param refTimestamp	The reference timestamp that the data record shall
	 * work with
	 */
	public void setDataRecord(Long refTimestamp) {
		this.data = new DataRecord(refTimestamp);
	}

	/**
	 * Lists the recorded temperatures for this room within the given interval,
	 * in ascending order (for each hour interval)
	 * @param startInterval		Beginning of the interval (timestamp)
	 * @param stopInterval		End of the interval (timestamp)
	 */
	public void listTemp(Long startInterval, Long stopInterval) {
		String printString = this.name + " ";
		printString += data.listTemp(startInterval, stopInterval);
		
		System.out.println(printString.trim());
	}
	
	/**
	 * Get the minimum recorded temperature in the last recorded hour
	 * @return	Said temperature
	 */
	public Double getLastTemp() {
		return data.getLastTemp();
	}

	/**
	 * Get the maximum recorded humidity level in the last recorded hour
	 * @return	Said humidity level
	 */
	public Double getLastHum() {
		return data.getLastHum();
	}
	
	/**
	 * Only compare the names of the rooms
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Room && this.name.equals(((Room)o).getName())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Only compare the names of the rooms
	 */
	@Override
	public int compareTo(Room otherRoom) {
		return this.name.compareTo(otherRoom.getName());
	}
}
