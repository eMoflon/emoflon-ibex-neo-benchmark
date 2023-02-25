package org.emoflon.ibex.neo.benchmark.village2constrplan;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class Village2ConstrPlan_Params extends BenchParameters {

	public final int num_of_streets;
	public final int street_length;
	public final int num_of_changes;
	public final DeltaType delta_type;
	public final RepairMode repair_mode;

	public Village2ConstrPlan_Params(String name, int modelScale, int numOfChanges, ScaleOrientation scaleOrientation, DeltaType deltaType, RepairMode repairMode) {
		super(name, modelScale, numOfChanges, scaleOrientation);

		switch (scaleOrientation) {
		case HORIZONTAL:
			num_of_streets = modelScale;
			street_length = 5;
			break;
		case VERTICAL:
			num_of_streets = 2;
			street_length = modelScale;
			break;
		default:
			throw new IllegalArgumentException(scaleOrientation + " is no a supported scale orientation!");
		}

		num_of_changes = numOfChanges;
		delta_type = deltaType;
		repair_mode = repairMode;
	}

	public Village2ConstrPlan_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				Integer.valueOf(args[2]), // number of changes
				ScaleOrientation.valueOf(args[3]), // scale orientation
				DeltaType.valueOf(args[4]), // delta type
				RepairMode.valueOf(args[5]) // repair mode
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				String.valueOf(num_of_changes), //
				scaleOrientation.toString(), //
				delta_type.toString(), //
				repair_mode.toString() //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"num_of_changes", //
				"scale_orientation", //
				"delta_type", //
				"repair_mode" //
		};
	}

}
