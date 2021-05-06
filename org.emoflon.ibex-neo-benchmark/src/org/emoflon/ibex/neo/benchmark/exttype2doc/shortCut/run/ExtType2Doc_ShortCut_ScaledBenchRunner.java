package org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.run;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Params;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ShortCutDelta;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ShortCut_ScaledBenchRunner {

	public static void main(String[] args) throws Exception {
		List<String[]> execArgs = scaledModel_differentDeltas();

		ScaledBenchRunner<ExtType2Doc_ShortCut_Bench, ExtType2Doc_ShortCut_Params> runner = new ScaledBenchRunner<>( //
				ExtType2Doc_ShortCut_Bench.class, ExtType2Doc_ShortCut_Params.class, //
				Arrays.asList("-Xmx28G"), execArgs, 20);
		runner.run();
	}

	private static List<String[]> scaledModel_differentDeltas() {
		int[] modelSize = { //
				100, 200, 300, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 //
		};
		ShortCutDelta[] delta = ShortCutDelta.values();

		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < delta.length; i++) {
			for (int j = 0; j < modelSize.length; j++) {
				String[] vars = new ExtType2Doc_ShortCut_Params( //
						"scaledModel_differentDeltas", //
						modelSize[j], //
						ScaleOrientation.HORIZONTAL, //
						delta[i] //
				).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

}
