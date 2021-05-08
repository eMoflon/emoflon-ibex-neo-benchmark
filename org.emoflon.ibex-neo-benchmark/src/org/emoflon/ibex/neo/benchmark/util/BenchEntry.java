package org.emoflon.ibex.neo.benchmark.util;

import java.lang.reflect.Constructor;

public class BenchEntry<BP extends BenchParameters> {

	public BP parameters;
	public int elts;
	public double init;
	public double resolve;
	public int ram;

	public BenchEntry(BP parameters, int elts, double init, double resolve, int ram) {
		this.parameters = parameters;
		this.elts = elts;
		this.init = init;
		this.resolve = resolve;
		this.ram = ram;
	}

	public BenchEntry(String args, Class<BP> clazz) throws Exception {
		String[] splitted = args.strip().split(";");
		// remove all irrelevant lines before first parameter:
		String[] firstParam = splitted[0].split("\n");
		splitted[0] = firstParam[firstParam.length - 1];

		Constructor<BP> constructor = clazz.getConstructor(String[].class);

		this.parameters = constructor.newInstance(new Object[] { splitted });
		this.elts = Integer.parseInt(splitted[splitted.length - 4]);
		this.init = Double.parseDouble(splitted[splitted.length - 3]);
		this.resolve = Double.parseDouble(splitted[splitted.length - 2]);
		this.ram = Integer.parseInt(splitted[splitted.length - 1]);
	}

	@Override
	public String toString() {
		return String.join(";", parameters.serializeInputParameters()) + ";" + elts + ";" + init + ";" + resolve + ";" + ram;
	}

}
