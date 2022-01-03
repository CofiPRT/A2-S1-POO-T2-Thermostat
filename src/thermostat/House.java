package thermostat;
import java.util.concurrent.ConcurrentSkipListSet;

public class House {
	private Double globalTemp;
	private Double globalHum;
	private Long refTimestamp;
	
	private ConcurrentSkipListSet<Room> rooms;
	
	public House(Double globalTemp, Double globalHum, Long refTimestamp) {
		this.globalTemp = globalTemp;
		this.globalHum = globalHum;
		this.refTimestamp = refTimestamp;
		
		rooms = new ConcurrentSkipListSet<Room>();
	}
	
	/**
	 * Adds a new room to this house, along with a data record for it
	 * @param newRoom	The room to be added
	 */
	public void addRoom(Room newRoom) {
		newRoom.setDataRecord(refTimestamp);
		rooms.add(newRoom);
	}

	/**
	 * Records a temperature entry for a device
	 * @param deviceID		The device that recorded the temperature
	 * @param timestamp		The timestamp the temperature was recorded at
	 * @param temperature	The recorded temperature
	 */
	public void observe(String deviceID, Long timestamp, Double temperature) {
		Room foundRoom = findByDeviceID(deviceID);
		
		if (foundRoom == null) {
			// room doesn't exist
			return;
		}
		
		foundRoom.addTemp(timestamp, temperature);
	}

	/**
	 * Records a humidity entry for a device
	 * @param deviceID		The device that recorded the humidity
	 * @param timestamp		The timestamp the humidity was recorded at
	 * @param humidity		The recorded humidity
	 */
	public void observeh(String deviceID, Long timestamp, Double humidity) {
		Room foundRoom = findByDeviceID(deviceID);
		
		if (foundRoom == null) {
			// room doesn't exist
			return;
		}
		
		foundRoom.addHum(timestamp, humidity);
	}

	/**
	 * Sets the global temperature to a new value
	 * @param newTemp	The new temperature
	 */
	public void setTemp(Double newTemp) {
		this.globalTemp = newTemp;
		
	}

	/**
	 * Lists the recorded temperatures in a room within the given interval,
	 * in ascending order (for each hour interval)
	 * @param name			The name of the room
	 * @param startInterval	Beginning of the interval (timestamp)
	 * @param stopInterval	End of the interval (timestamp)
	 */
	public void list(String name, Long startInterval, Long stopInterval) {
		Room foundRoom = findByName(name);
		
		if (foundRoom == null) {
			// room doesn't exist
			return;
		}
		
		foundRoom.listTemp(startInterval, stopInterval);
	}

	/**
	 * Decides whether or not the heat should be turned on. If (optional) the
	 * humidity level is higher than the desired one, the answer is always NO.
	 * If the current temperature is lower than the desired one, the answer
	 * is YES. The answer is PRINTED.
	 */
	public void triggerHeat() {
		Double tempSum = 0D;
		Double tempSurfaceSum = 0D;
		Double humSum = 0D;
		Double humSurfaceSum = 0D;
		
		for (Room currentRoom : rooms) {
			Double lastTemp = currentRoom.getLastTemp();
			Double lastHum = currentRoom.getLastHum();
			Double surfaceArea = currentRoom.getSurfaceArea();
			
			if (lastTemp != null) {
				tempSum += lastTemp * surfaceArea;
				tempSurfaceSum += surfaceArea;
			}
			if (lastHum != null) {
				humSum += lastHum * surfaceArea;
				humSurfaceSum += surfaceArea;
			}
		}
		
		Double tempAvg = tempSum / tempSurfaceSum;
		Double humAvg = humSum / humSurfaceSum;
				
		if (globalHum != null && humAvg > globalHum) {
			System.out.println("NO");
		} else if (tempAvg >= globalTemp) {
			System.out.println("NO");
		} else {
			System.out.println("YES");
		}
	}
	
	/**
	 * Find a room by its installed device
	 * @param deviceID	The ID of the installed device
	 * @return			The found {@code Room}, or {@code null} if it hasn't
	 * been found
	 */
	private Room findByDeviceID(String deviceID) {
		for (Room currentRoom : rooms) {
			if (currentRoom.getDeviceID().equals(deviceID)) {
				return currentRoom;
			}
		}
		
		// not found
		return null;
	}
	
	/**
	 * Find a room by its name.
	 * @param name	The room's name
	 * @return		The found {@code Room}, or {@code null} if it hasn't been
	 * found
	 */
	private Room findByName(String name) {
		for (Room currentRoom : rooms) {
			if (currentRoom.getName().equals(name)) {
				return currentRoom;
			}
		}
		
		// not found
		return null;
	}
}
