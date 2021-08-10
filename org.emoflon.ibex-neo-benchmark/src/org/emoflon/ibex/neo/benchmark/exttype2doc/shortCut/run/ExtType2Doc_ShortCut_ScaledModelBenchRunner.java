package org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.run;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Params;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ShortCutDelta;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public abstract class ExtType2Doc_ShortCut_ScaledModelBenchRunner {

	public static void main(String[] args) throws Exception {
		List<String[]> execArgs = scaledModel_differentDeltas(ShortCutDelta.valueOf(args[0]));

		ScaledBenchRunner<ExtType2Doc_ShortCut_Bench, ExtType2Doc_ShortCut_Params> runner = new ScaledBenchRunner<>( //
				ExtType2Doc_ShortCut_Bench.class, ExtType2Doc_ShortCut_Params.class, //
				Arrays.asList("-Xmx110G"), execArgs, 5);
		runner.run();
	}

	private static List<String[]> scaledModel_differentDeltas(ShortCutDelta delta) {
		int[] modelSize = { //
				100 , 200, 300, 400, 500 , 750, 1000, 1500, 2000, 2500, 3000 , 3500, 4000, 4500, 5000, 5500, 6000, 6500, 7000, 7500, 8000, 8500, 9000, 9500, 10000 //
				//10, 25 , 50
		};
		
		int[] num_of_changes = { //
				50 //, 20, 30, 40, 50//
		}; // 
		
		
		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < num_of_changes.length; i++) {
			for (int j = 0; j < modelSize.length; j++) {
				String[] vars = new ExtType2Doc_ShortCut_Params( //
						"scaledModel_" + delta.name(), //
						modelSize[j], //
						ScaleOrientation.HORIZONTAL, //
						num_of_changes[i], //
						delta //
				).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

}
