package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc;

import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;
import org.emoflon.ibex.tgg.compiler.patterns.ACStrategy;

public class ExtType2Doc_LookAhead_CC_Params extends BenchParameters {

	protected final ACStrategy acStrategy;

	public ExtType2Doc_LookAhead_CC_Params(String name, int modelScale, ScaleOrientation scaleOrientation, ACStrategy acStrategy) {
		super(name, modelScale, scaleOrientation);

		this.acStrategy = acStrategy;
	}

	public ExtType2Doc_LookAhead_CC_Params(String[] args) {
		this( //
				args[0], // name
				Integer.valueOf(args[1]), // model scale
				ScaleOrientation.valueOf(args[2]), // scale orientation
				ACStrategy.valueOf(args[3]) // filter analysis
		);
	}

	@Override
	public String[] serializeInputParameters() {
		return new String[] { //
				name, //
				String.valueOf(modelScale), //
				scaleOrientation.toString(), //
				acStrategy.toString() //
		};
	}

	@Override
	public String[] getInputParameterNames() {
		return new String[] { //
				"name", //
				"model_scale", //
				"scale_orientation", //
				"application_condition" //
		};
	}

}
