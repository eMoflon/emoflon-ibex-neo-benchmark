package org.emoflon.ibex.neo.benchmark.exttype2doc.localcc.run;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.exttype2doc.localcc.ExtType2Doc_LocalCC_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.localcc.ExtType2Doc_LocalCC_Params;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_LocalCC_ScaledBenchRunner {

	public static void main(String[] args) throws Exception {
		List<String[]> execArgs = scaledModel_changes();

		ScaledBenchRunner<ExtType2Doc_LocalCC_Bench, ExtType2Doc_LocalCC_Params> runner = new ScaledBenchRunner<>( //
				ExtType2Doc_LocalCC_Bench.class, ExtType2Doc_LocalCC_Params.class, //
				Arrays.asList("-Xmx28G"), execArgs, 1);
		runner.run();
	}

	private static List<String[]> scaledModel_changes() {
		int[] modelSize = { //
				//100, 200, 300, 500//, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
				300
		};
		
		int[] changes = { //
				//100, 200, 300, 500//, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
				100, 200, 300
		};

		int delta_inheritance_depth = 5;

		List<String[]> scale = new LinkedList<>();

		for (int n = 0; n < modelSize.length; n++) {
			for(int c = 0; c < changes.length; c++) {
				String[] vars = new ExtType2Doc_LocalCC_Params( //
						"scaledModel_constChanges", //
						modelSize[n], //
						ScaleOrientation.HORIZONTAL, //
						changes[c], //
						delta_inheritance_depth //
						).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

}
