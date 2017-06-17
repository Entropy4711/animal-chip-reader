package org.animalchipreader;

import java.io.Serializable;
import java.util.List;

public class Config implements Serializable {

	private static final long serialVersionUID = -4984771396801239187L;

	private List<String> knownIds;

	public List<String> getKnownIds() {
		return knownIds;
	}

	public void setKnownIds(List<String> knownIds) {
		this.knownIds = knownIds;
	}
}