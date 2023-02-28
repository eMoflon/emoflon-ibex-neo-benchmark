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

public class Village2ConstrPlan_ScaledModelBenchRunner_Vertical {

	public static void main(String[] args) throws Exception {
		List<List<String[]>> execArgsList = Arrays.asList( //
				scaledModel_differentDeltas(DeltaType.ADD_ROOT, RepairMode.NONE), //
				scaledModel_differentDeltas(DeltaType.REMOVE_ROOT, RepairMode.NONE), //
				scaledModel_differentDeltas(DeltaType.MOVE_ROW, RepairMode.NONE), //

				scaledModel_differentDeltas(DeltaType.ADD_ROOT, RepairMode.LEGACY), //
				scaledModel_differentDeltas(DeltaType.REMOVE_ROOT, RepairMode.LEGACY), //
				scaledModel_differentDeltas(DeltaType.MOVE_ROW, RepairMode.LEGACY), //

				scaledModel_differentDeltas(DeltaType.ADD_ROOT, RepairMode.PG_BASED), //
				scaledModel_differentDeltas(DeltaType.REMOVE_ROOT, RepairMode.PG_BASED), //
				scaledModel_differentDeltas(DeltaType.MOVE_ROW, RepairMode.PG_BASED) //
		);

		for (List<String[]> execArgs : execArgsList) {
			ScaledBenchRunner<Village2ConstrPlan_Bench, Village2ConstrPlan_Params> runner = new ScaledBenchRunner<>( //
					Village2ConstrPlan_Bench.class, Village2ConstrPlan_Params.class, //
					Arrays.asList("-Xmx30G"), execArgs, 5);
			runner.run();

			System.out.println("--------------------");
		}

	}

	private static List<String[]> scaledModel_differentDeltas(DeltaType deltaType, RepairMode repairMode) {
		int[] modelSize = { //
				10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180, 200 //
		};

		int[] numOfChanges = { //
				2 //
		};

		List<String[]> scale = new LinkedList<>();

		for (int i = 0; i < numOfChanges.length; i++) {
			for (int j = 0; j < modelSize.length; j++) {
				String[] vars = new Village2ConstrPlan_Params( //
						"scaledModel", //
						modelSize[j], //
						numOfChanges[i], //
						ScaleOrientation.VERTICAL, //
						deltaType, //
						repairMode //
				).serializeInputParameters();
				scale.add(vars);
			}
		}

		return scale;
	}

}
