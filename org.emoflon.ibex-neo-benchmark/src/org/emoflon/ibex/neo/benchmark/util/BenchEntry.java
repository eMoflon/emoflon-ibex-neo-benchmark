package org.emoflon.ibex.neo.benchmark.util;

import java.lang.reflect.Constructor;

public class BenchEntry<BP extends BenchParameters> {

	public final BP parameters;
	public final int elts;
	public final double init;
	public final double resolve;
	public final int ram;
	public final double successRate;

	public long eltsCreated;
	public long eltsDeleted;
	public long matchesFound;
	public long matchesRepaired;
	public long matchesRevoked;
	public long matchesApplied;
	public boolean hasAdvancedStats;

	public BenchEntry(BP parameters, int elts, double init, double resolve, int ram, double successRate) {
		this.parameters = parameters;
		this.elts = elts;
		this.init = init;
		this.resolve = resolve;
		this.ram = ram;
		this.successRate = successRate;

		this.hasAdvancedStats = false;
	}

	public BenchEntry(String args, Class<BP> clazz) throws Exception {
		String[] splitted = args.strip().split(";");
		// remove all irrelevant lines before first parameter:
		String[] firstParam = splitted[0].split("\n");
		splitted[0] = firstParam[firstParam.length - 1];

		Constructor<BP> constructor = clazz.getConstructor(String[].class);

		this.parameters = constructor.newInstance(new Object[] { splitted });

		this.hasAdvancedStats = Boolean.parseBoolean(splitted[splitted.length - 1]);
		if (hasAdvancedStats) {
			this.elts = Integer.parseInt(splitted[splitted.length - 11]);
			this.init = Double.parseDouble(splitted[splitted.length - 10]);
			this.resolve = Double.parseDouble(splitted[splitted.length - 9]);
			this.ram = Integer.parseInt(splitted[splitted.length - 8]);
			this.successRate = Double.parseDouble(splitted[splitted.length - 7]);

			this.eltsCreated = Long.parseLong(splitted[splitted.length - 6]);
			this.eltsDeleted = Long.parseLong(splitted[splitted.length - 5]);
			this.matchesFound = Long.parseLong(splitted[splitted.length - 4]);
			this.matchesRepaired = Long.parseLong(splitted[splitted.length - 3]);
			this.matchesRevoked = Long.parseLong(splitted[splitted.length - 2]);
			this.matchesApplied = Long.parseLong(splitted[splitted.length - 1]);
		} else {
			this.elts = Integer.parseInt(splitted[splitted.length - 6]);
			this.init = Double.parseDouble(splitted[splitted.length - 5]);
			this.resolve = Double.parseDouble(splitted[splitted.length - 4]);
			this.ram = Integer.parseInt(splitted[splitted.length - 3]);
			this.successRate = Double.parseDouble(splitted[splitted.length - 2]);
		}
	}

	public void addAdvancedStats(long eltsCreated, long eltsDeleted, long matchesFound, long matchesRepaired, long matchesRevoked, long matchesApplied) {
		this.eltsCreated = eltsCreated;
		this.eltsDeleted = eltsDeleted;
		this.matchesFound = matchesFound;
		this.matchesRepaired = matchesRepaired;
		this.matchesRevoked = matchesRevoked;
		this.matchesApplied = matchesApplied;
		this.hasAdvancedStats = true;
	}

	@Override
	public String toString() {
		String result = String.join(";", parameters.serializeInputParameters()) + ";" + elts + ";" + init + ";" + resolve + ";" + ram + ";" + successRate;
		if (hasAdvancedStats)
			result += ";" + eltsCreated + ";" + eltsDeleted + ";" + matchesFound + ";" + matchesRepaired + ";" + matchesRevoked + ";" + matchesApplied;
		return result;
	}

}
