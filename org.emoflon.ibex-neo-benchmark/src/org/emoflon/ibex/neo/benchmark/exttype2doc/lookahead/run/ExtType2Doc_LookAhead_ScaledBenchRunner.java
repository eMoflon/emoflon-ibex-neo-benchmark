package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.run;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc.ExtType2Doc_LookAhead_CC_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc.ExtType2Doc_LookAhead_CC_Params;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync.ExtType2Doc_LookAhead_Sync_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync.ExtType2Doc_LookAhead_Sync_Params;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;
import org.emoflon.ibex.tgg.compiler.patterns.ACStrategy;

public class ExtType2Doc_LookAhead_ScaledBenchRunner {

	public static void main(String[] args) throws Exception {
		ScaledBenchRunner<?, ?> runner = null;

		switch (args[0]) {
		case "cc":
			runner = new ScaledBenchRunner<>( //
					ExtType2Doc_LookAhead_CC_Bench.class, ExtType2Doc_LookAhead_CC_Params.class, //
					Arrays.asList("-Xmx28G"), scaledModel_cc(), 5);
			break;
		case "sync":
			runner = new ScaledBenchRunner<>( //
					ExtType2Doc_LookAhead_Sync_Bench.class, ExtType2Doc_LookAhead_Sync_Params.class, //
					Arrays.asList("-Xmx28G"), scaledModel_sync(), 5);
		default:
			break;
		}

		if (runner != null)
			runner.run();
	}

	private static List<String[]> scaledModel_cc() {
		int[] modelSize = { //
				100, 200, 300, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
		};
		ACStrategy[] acStrategies = ACStrategy.values();

		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < acStrategies.length; i++) {
			for (int j = 0; j < modelSize.length; j++) {
				String[] vars = new ExtType2Doc_LookAhead_CC_Params( //
						"scaledModel_cc", //
						modelSize[j], //
						ScaleOrientation.HORIZONTAL, //
						acStrategies[i] //
				).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

	private static List<String[]> scaledModel_sync() {
		int[] modelSize = { //
				0, 1, 2, 3, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100, 140 //
		};

		List<String[]> scale = new LinkedList<>();

		for (int j = 0; j < modelSize.length; j++) {
			String[] vars = new ExtType2Doc_LookAhead_Sync_Params( //
					"scaledModel_cc", //
					modelSize[j], //
					ScaleOrientation.HORIZONTAL //
			).serializeInputParameters();
			scale.add(vars);
		}

		return scale;
	}

}
