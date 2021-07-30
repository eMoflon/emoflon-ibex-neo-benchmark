package org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ShortCut_Params extends BenchParameters {

	public final int num_of_root_packages;
	public final int[] horizontal_package_scales;
	public final boolean[] types_for_packages;

	public final int num_of_root_types;
	public final int type_inheritance_depth;
	public final int horizontal_type_inheritance_scale;
	public final int num_of_fields;
	public final int num_of_methods;
	public final int num_of_parameters;
	public final ShortCutDelta delta_type;

	public final int num_of_changes;
	
	public ExtType2Doc_ShortCut_Params(String name, int modelScale, ScaleOrientation scaleOrientation, int numOfChanges, ShortCutDelta deltaType) {
		super(name, modelScale, scaleOrientation);

		switch (scaleOrientation) {
		case HORIZONTAL:
			num_of_root_packages = modelScale;
			break;
		case VERTICAL:
			num_of_root_packages = 1;
			break;
		default:
			num_of_root_packages = -1;
			break;
		}

		horizontal_package_scales = new int[] { 3, 1, 2 };
		types_for_packages = new boolean[] { false, false, true };

		num_of_root_types = 1;
		type_inheritance_depth = 4;
		horizontal_type_inheritance_scale = 2;
		num_of_fields = 3;
		num_of_methods = 3;
		num_of_parameters = 2;

		num_of_changes = numOfChanges;
		delta_type = deltaType;
	}

	public ExtType2Doc_ShortCut_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				ScaleOrientation.valueOf(args[2]), // scale orientation
				Integer.valueOf(args[3]), // number of changes
				ShortCutDelta.valueOf(args[4]) // short-cut delta type
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				scaleOrientation.toString(), //
				String.valueOf(num_of_changes), //
				delta_type.toString() //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"scale_orientation", //
				"num_of_changes", //
				"delta_type" //
		};
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("_");
		builder.append(delta_type);
		return builder.toString();
	}

}
