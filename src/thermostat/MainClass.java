package thermostat;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class MainClass {
	public static Scanner inputStream;
	public static Scanner inputLine;
	public static PrintStream outputStream;
	public static PrintStream console;
	
	public static House myHouse;
	
	/** 
	 * Initializes input and output streams
	 */
	public static void setupIO() {
		try {
			inputStream = new Scanner(new File("therm.in"));
			outputStream = new PrintStream(new File("therm.out"));
			
			// backup the console and set the new output stream
			console = System.out;
			System.setOut(outputStream);
		}
		catch (Exception exc) {
			System.out.println(exc + "Exception in setupIO");
		}
	}
	
	/**
	 * Closes input and output streams
	 */
	public static void restoreIO() {
		inputStream.close();
		inputLine.close();
		outputStream.close();
		
		// restore console
		System.setOut(console);
	}
	
	/**
	 * Initializes the house and registers
	 * every room
	 */
	public static void registerHouse() {
		String firstLine = inputStream.nextLine();
		inputLine = new Scanner(firstLine);
		
		Integer roomCount = inputLine.nextInt();
		Double globalTemp = inputLine.nextDouble();
		Double globalHum = null;
		Long refTimestamp = 0L;
		
		int wordCount = firstLine.split("\\s+").length;
		if (wordCount == 4) {
			// also read the the humidity level
			globalHum = inputLine.nextDouble();
			refTimestamp = inputLine.nextLong();
		} else if (wordCount == 3) {
			// don't read humidity level
			refTimestamp = inputLine.nextLong();
		} else {
			// should not happen
			System.out.println("Only 3 or 4 arguments allowed!");
			System.exit(-1);
		}
		
		myHouse = new House(globalTemp, globalHum, refTimestamp);
		
		for (int i = 0; i < roomCount; i++) {
			String name = inputStream.next();
			String deviceID = inputStream.next();
			Double surfaceArea = inputStream.nextDouble();
			
			inputStream.nextLine();
			
			myHouse.addRoom(new Room(name, deviceID, surfaceArea));
		}
	}
	
	/**
	 * Executes every command extracted from the input stream
	 */
	public static void executeCommands() {
		while (inputStream.hasNextLine()) {
			inputLine = new Scanner(inputStream.nextLine());
			
			// find command
			String command;
			if (inputLine.hasNext()) {
				command = inputLine.next();
			} else {
				// entered the last empty line, skip this
				continue;
			}
			
			if (command.equals("OBSERVE")) {
				String deviceID = inputLine.next();
				Long timestamp = inputLine.nextLong();
				Double temperature = inputLine.nextDouble();
				
				myHouse.observe(deviceID, timestamp, temperature);
			}
			
			if (command.equals("OBSERVEH")) {
				String deviceID = inputLine.next();
				Long timestamp = inputLine.nextLong();
				Double humidity = inputLine.nextDouble();
				
				myHouse.observeh(deviceID, timestamp, humidity);
			}
			
			if (command.equals("TEMPERATURE")) {
				Double newTemp = inputLine.nextDouble();
				
				myHouse.setTemp(newTemp);
			}
			
			if (command.equals("LIST")) {
				String name = inputLine.next();
				Long startInterval = inputLine.nextLong();
				Long stopInterval = inputLine.nextLong();
				
				myHouse.list(name, startInterval, stopInterval);
			}
			
			if (command.equals("TRIGGER")) {
				myHouse.triggerHeat();
			}
		}
	}
	
	public static void main(String[] args) {
		setupIO();
		registerHouse();
		executeCommands();
		restoreIO();
	}

}
