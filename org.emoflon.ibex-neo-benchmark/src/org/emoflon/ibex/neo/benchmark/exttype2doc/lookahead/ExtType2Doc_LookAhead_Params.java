package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_LookAhead_Params extends BenchParameters {
	
	public final int horizontal_package_scale;
	public final int package_hierarchy_depth;

	public ExtType2Doc_LookAhead_Params(String name, int modelScale, ScaleOrientation scaleOrientation) {
		super(name, modelScale, scaleOrientation);
		
		horizontal_package_scale = 2;
		package_hierarchy_depth = 1;
	}

	public ExtType2Doc_LookAhead_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				ScaleOrientation.valueOf(args[2]) // scale orientation
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				scaleOrientation.toString() //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"scale_orientation" //
		};
	}

}
