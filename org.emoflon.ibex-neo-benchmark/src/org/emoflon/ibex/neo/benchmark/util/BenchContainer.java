package org.emoflon.ibex.neo.benchmark.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BenchContainer<BP extends BenchParameters> {

	Map<BP, List<BenchEntry<BP>>> params2entries = new HashMap<>();
	boolean atLeastOneAdvancedStats = false;

	public void addBench(BenchEntry<BP> entry) {
		if (entry.hasAdvancedStats)
			atLeastOneAdvancedStats = true;
		params2entries.computeIfAbsent(entry.parameters, key -> new LinkedList<>()).add(entry);
	}

	public void print() {
		BP bp = null;
		for (BP p : params2entries.keySet()) {
			bp = p;
			break;
		}

		if (bp == null) {
			System.out.println("No measurements found! Aborting...");
			return;
		}

		System.out.println();
		String header = String.join(";", bp.getInputParameterNames())
				+ ";elts;avg_init;median_init;avg_resolve;median_resolve;avg_ram;median_ram;success_rate";
		if (atLeastOneAdvancedStats)
			header += ";elts_created;elts_deleted;matches_found;matches_repaired;matches_revoked;matches_applied";
		System.out.println(header);
		for (BP params : params2entries.keySet()) {
			System.out.println(average(params, params2entries.get(params)));
		}
	}

	private String average(BP params, List<BenchEntry<BP>> entries) {
		double avg_init = 0;
		double avg_resolve = 0;
		double avg_ram = 0;
		double avg_successRate = 0;
		int elts = -1;

		long elts_created = -1;
		long elts_deleted = -1;
		long matches_found = -1;
		long matches_repaired = -1;
		long matches_revoked = -1;
		long matches_applied = -1;

		List<Double> inits = new LinkedList<>();
		List<Double> resolves = new LinkedList<>();
		List<Integer> rams = new LinkedList<>();

		for (BenchEntry<BP> entry : entries) {
			elts = entry.elts;
			avg_init += entry.init;
			avg_resolve += entry.resolve;
			avg_ram += entry.ram;
			avg_successRate += entry.successRate;

			inits.add(entry.init);
			resolves.add(entry.resolve);
			rams.add(entry.ram);

			if (entry.hasAdvancedStats) {
				elts_created = entry.eltsCreated;
				elts_deleted = entry.eltsDeleted;
				matches_found = entry.matchesFound;
				matches_repaired = entry.matchesRepaired;
				matches_revoked = entry.matchesRevoked;
				matches_applied = entry.matchesApplied;
			}
		}

		Collections.sort(inits);
		Collections.sort(resolves);
		Collections.sort(rams);

		avg_init /= entries.size();
		avg_resolve /= entries.size();
		avg_ram /= entries.size();
		avg_successRate /= entries.size();
		double med_init = inits.get((int) (inits.size() / 2));
		double med_resolve = resolves.get((int) (resolves.size() / 2));
		int med_ram = rams.get((int) (rams.size() / 2));

		String result = String.join(";", params.serializeInputParameters()) + ";" + elts + ";" //
				+ avg_init + ";" + med_init + ";" //
				+ avg_resolve + ";" + med_resolve + ";" //
				+ avg_ram + ";" + med_ram + ";" //
				+ avg_successRate;
		if (elts_created != -1)
			result += ";" + elts_created + ";" + elts_deleted //
					+ ";" + matches_found + ";" + matches_repaired + ";" + matches_revoked + ";" + matches_applied;
		return result;
	}
}
