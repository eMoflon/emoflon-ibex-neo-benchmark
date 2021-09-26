package org.emoflon.ibex.neo.benchmark.util;

import com.google.common.base.Objects;

public abstract class BenchParameters {

	public final String name;
	public final int modelScale;
	public final int changes;
	public final ScaleOrientation scaleOrientation;

	public BenchParameters(String name, int modelScale, int changes, ScaleOrientation scaleOrientation) {
		this.name = name;
		this.modelScale = modelScale;
		this.changes = changes;
		this.scaleOrientation = scaleOrientation;
	}

	public abstract String[] serializeInputParameters();

	public abstract String[] getInputParameterNames();

	@Override
	public int hashCode() {
		return Objects.hashCode((Object[]) this.serializeInputParameters());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!this.getClass().equals(obj.getClass()))
			return false;
		BenchParameters other = (BenchParameters) obj;
		String[] thisSerial = this.serializeInputParameters();
		String[] otherSerial = other.serializeInputParameters();
		if (thisSerial.length != otherSerial.length)
			return false;
		for (int i = 0; i < thisSerial.length; i++) {
			if (!Objects.equal(thisSerial[i], otherSerial[i]))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append("_n");
		builder.append(modelScale);
		builder.append("_");
		builder.append("_c");
		builder.append(changes);
		builder.append("_");
		builder.append(scaleOrientation == ScaleOrientation.HORIZONTAL ? "H" : "V");
		return builder.toString();
	}

}
