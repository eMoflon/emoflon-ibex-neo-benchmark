package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.run;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;

public class ExtType2Doc_ConcSync_ScaledBenchRunner {

	public static void main(String[] args) throws IOException, InterruptedException {
		List<List<String>> execArgs;
		switch (args[0]) {
		case "m":
			execArgs = scaledModel_constConflict();
			break;

		default:
			execArgs = Collections.emptyList();
			break;
		}

		ScaledBenchRunner runner = new ScaledBenchRunner(ExtType2Doc_ConcSync_SingleBenchRunner.class, Arrays.asList("-Xmx28G"), execArgs, 20);
		runner.run();
	}

	private static List<List<String>> scaledModel_constConflict() {
		int[] modelSize = { //
				100, 200, 300, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
		};

		int conflictSize = 100;
		double conflictRatio = 1.0;

		List<List<String>> scale = new LinkedList<>();

		for (int i = 0; i < modelSize.length; i++) {
			List<String> vars = Arrays.asList( //
					"scaledModel_constConflict", //
					String.valueOf(modelSize[i]), //
					"h", //
					String.valueOf(conflictSize), //
					String.valueOf(conflictRatio));
			scale.add(vars);
		}

		return scale;
	}

}
