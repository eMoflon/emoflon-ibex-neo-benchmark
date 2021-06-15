package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.run;

import static org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ConflictType.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ConflictType;
import org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ExtType2Doc_ConcSync_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ExtType2Doc_ConcSync_Params;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ConcSync_ScaledBenchRunner {

	public static void main(String[] args) throws Exception {
		List<String[]> execArgs;
		switch (args[0]) {
		case "m":
			execArgs = scaledModel_constConflict();
			break;

		default:
			execArgs = Collections.emptyList();
			break;
		}

		ScaledBenchRunner<ExtType2Doc_ConcSync_Bench, ExtType2Doc_ConcSync_Params> runner = new ScaledBenchRunner<>( //
				ExtType2Doc_ConcSync_Bench.class, ExtType2Doc_ConcSync_Params.class, //
				Arrays.asList("-Xmx28G"), execArgs, 20);
		runner.run();
	}

	private static List<String[]> scaledModel_constConflict() {
		int[] modelSize = { //
				100, 200, 300, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
		};

		int changes = 100;
		double conflictRatio = 1.0;

		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < modelSize.length; i++) {
			String[] vars = new ExtType2Doc_ConcSync_Params( //
					"scaledModel_constConflict", //
					modelSize[i], //
					ScaleOrientation.HORIZONTAL, //
					changes, //
					conflictRatio, //
					new ConflictType[] { ATTR, PRES_DEL, CONTR_MOVE } //
			).serializeInputParameters();
			scale.add(vars);
		}

		return scale;
	}

}
