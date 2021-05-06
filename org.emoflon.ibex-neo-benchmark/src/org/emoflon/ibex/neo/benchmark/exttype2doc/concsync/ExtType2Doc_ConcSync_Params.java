package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ConcSync_Params extends BenchParameters {

	public final int num_of_root_types;
	public final int inheritance_depth;
	public final int horizontal_inheritance_scale;
	public final int num_of_fields;
	public final int num_of_methods;
	public final int num_of_parameters;
	public final int num_of_javadocs;
	public final int num_of_glossar_entries;
	public final int num_of_glossar_links_per_entry;

	public final int num_of_changes;
	public final double conflict_ratio;

	public ExtType2Doc_ConcSync_Params(String name, int modelScale, ScaleOrientation scaleOrientation, int numOfChanges, double conflictRatio) {
		super(name, modelScale, scaleOrientation);

		switch (scaleOrientation) {
		case HORIZONTAL:
			num_of_root_types = modelScale;
			inheritance_depth = 3;
			break;
		case VERTICAL:
			num_of_root_types = 1;
			inheritance_depth = modelScale;
			break;
		default:
			num_of_root_types = -1;
			inheritance_depth = -1;
			break;
		}

		horizontal_inheritance_scale = 3;
		num_of_fields = 3;
		num_of_methods = 3;
		num_of_parameters = 2;
		num_of_javadocs = 1;
		num_of_glossar_entries = (int) (Math.sqrt(modelScale) / 10) + 1;
		num_of_glossar_links_per_entry = 2;

		num_of_changes = numOfChanges;
		conflict_ratio = conflictRatio;
	}

	public ExtType2Doc_ConcSync_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				ScaleOrientation.valueOf(args[2]), // scale orientation
				Integer.valueOf(args[3]), // number of changes
				Double.valueOf(args[4]) // conflict ratio
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				scaleOrientation.toString(), //
				String.valueOf(num_of_changes), //
				String.valueOf(conflict_ratio) //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"scale_orientation", //
				"changes", //
				"conflicts" //
		};
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("_c");
		builder.append((int) (num_of_changes * conflict_ratio));
		return builder.toString();
	}

}
