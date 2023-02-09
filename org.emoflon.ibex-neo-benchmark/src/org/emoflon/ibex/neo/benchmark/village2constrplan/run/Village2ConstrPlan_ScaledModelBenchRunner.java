package org.emoflon.ibex.neo.benchmark.village2constrplan.run;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.ScaledBenchRunner;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;
import org.emoflon.ibex.neo.benchmark.village2constrplan.DeltaType;
import org.emoflon.ibex.neo.benchmark.village2constrplan.RepairMode;
import org.emoflon.ibex.neo.benchmark.village2constrplan.Village2ConstrPlan_Bench;
import org.emoflon.ibex.neo.benchmark.village2constrplan.Village2ConstrPlan_Params;

public class Village2ConstrPlan_ScaledModelBenchRunner {

	public static void main(String[] args) throws Exception {
		List<String[]> execArgs = scaledModel_differentDeltas(DeltaType.valueOf(args[0]), RepairMode.valueOf(args[1]));

		ScaledBenchRunner<Village2ConstrPlan_Bench, Village2ConstrPlan_Params> runner = new ScaledBenchRunner<>( //
				Village2ConstrPlan_Bench.class, Village2ConstrPlan_Params.class, //
				Arrays.asList("-Xmx10G"), execArgs, 5);
		runner.run();
	}

	private static List<String[]> scaledModel_differentDeltas(DeltaType deltaType, RepairMode repairMode) {
		int[] modelSize = { //
//				100 , 200, 300, 400, 500 , 750, 1000, 1500, 2000, 2500, 3000 , 3500, 4000, 4500, 5000, 5500, 6000, 6500, 7000, 7500, 8000, 8500, 9000, 9500, 10000 //
				10, 25, 50 //
		};

		int[] numOfChanges = { //
				10 //
		};

		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < numOfChanges.length; i++) {
			for (int j = 0; j < modelSize.length; j++) {
				String[] vars = new Village2ConstrPlan_Params( //
						"scaledModel", //
						modelSize[j], //
						numOfChanges[i], //
						ScaleOrientation.HORIZONTAL, //
						deltaType, //
						repairMode //
				).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

}
