package org.animalchipreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Config config = null;

		try (InputStream stream = getConfigInputStream()) {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			config = mapper.readValue(stream, Config.class);
		} catch (Exception e) {
			log.error("Unable to read config.yml: ", e);
			return;
		}

		log.info("Config file successfully read. Search for " + config.getKnownIds().size() + " IDs");

		String[] ports = SerialPortList.getPortNames();

		log.info("Found " + ports.length + " ports");

		for (String name : ports) {
			if (name.toLowerCase().contains("ttyusb")) {
				log.info("Found USB port");

				try {
					SerialPort port = new SerialPort(name);

					port.openPort();
					port.setParams(9600, 8, 1, 0);
					port.setEventsMask(SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR);

					port.addEventListener(new EventListener(port, config));

					log.info("Port " + name + " successfully configured");
				} catch (SerialPortException e) {
					log.error("Unable to setup USB port " + name + ": ", e);
					return;
				}

				break;
			}
		}
	}

	@SuppressWarnings("resource")
	private static InputStream getConfigInputStream() throws Exception {
		File file = new File("config.yml");

		InputStream stream = null;

		if (file.exists()) {
			stream = new FileInputStream(file);
		}

		if (stream == null) {
			stream = Main.class.getResourceAsStream("/config.yml");

			if (stream != null) {
				return stream;
			}
		}

		return stream;
	}
}