package org.emoflon.ibex.neo.benchmark.exttype2doc.localcc;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_LocalCC_Params extends BenchParameters {

	public final int num_of_root_types;
	public final int inheritance_depth;
	public final int horizontal_inheritance_scale;
	public final int num_of_fields;
	public final int num_of_methods;
	public final int num_of_parameters;
	public final int num_of_javadocs;

	public final int num_of_changes;
	public final int delta_inheritance_depth;

	public ExtType2Doc_LocalCC_Params(String name, int modelScale, ScaleOrientation scaleOrientation, int numOfChanges, int delta_inheritance_depth) {
		super(name, modelScale, numOfChanges,scaleOrientation);

		num_of_root_types = modelScale;
		inheritance_depth = 3;
		horizontal_inheritance_scale = 3;
		num_of_fields = 3;
		num_of_methods = 3;
		num_of_parameters = 2;
		num_of_javadocs = 1;

		num_of_changes = numOfChanges;
		this.delta_inheritance_depth = delta_inheritance_depth;
	}

	public ExtType2Doc_LocalCC_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				ScaleOrientation.valueOf(args[2]), // scale orientation
				Integer.valueOf(args[3]), // number of changes
				Integer.valueOf(args[4]) // inheritance depth
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				scaleOrientation.toString(), //
				String.valueOf(num_of_changes), //
				String.valueOf(delta_inheritance_depth) //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"scale_orientation", //
				"changes", //
				"inheritanceDepth"
		};
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("_c");
		builder.append(num_of_changes);
		return builder.toString();
	}

}
