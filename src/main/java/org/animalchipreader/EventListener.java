package org.animalchipreader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class EventListener implements SerialPortEventListener {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SerialPort serialPort;
	private final Config config;

	private StringBuilder currentString = new StringBuilder();

	public EventListener(SerialPort serialPort, Config config) {
		this.serialPort = serialPort;
		this.config = config;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR()) {
			int eventValue = event.getEventValue();

			if (eventValue > 0) {
				try {
					byte[] buffer = serialPort.readBytes(eventValue);
					currentString.append(new String(buffer).trim());
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		}

		String string = currentString.toString();

		for (String id : config.getKnownIds()) {
			if (string.contains(id)) {
				log.info("Found ID " + id + "!");
				currentString = new StringBuilder();
				break;
			}
		}

		log.debug("currentString: " + currentString);
		
		if (currentString.length() > 30) {
			currentString = new StringBuilder();
		}
	}
}