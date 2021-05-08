package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc;

import java.io.IOException;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.ConsistencyCheckBench;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.operational.strategies.opt.CC;
import org.emoflon.ibex.tgg.run.exttype2doc_lookahead.CC_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class ExtType2Doc_LookAhead_CC_Bench extends ConsistencyCheckBench<ExtType2Doc_LookAhead_CC_Params> {

	public ExtType2Doc_LookAhead_CC_Bench(String projectName, ExtType2Doc_LookAhead_CC_Params parameters) {
		super(projectName, parameters);
	}

	@Override
	protected CC initStub(TGGResourceHandler resourceHandler) throws IOException {
		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Gurobi);
			options.patterns.acStrategy(parameters.acStrategy);
			return options;
		};

		return new CC_App(ibexOptions);
	}

	@Override
	protected ModelAndDeltaGenerator<?, ?, ?, ?, ?, ExtType2Doc_LookAhead_CC_Params> initModelAndDeltaGenerator(Resource s, Resource t, Resource c,
			Resource p, Resource d) {
		return new ExtType2Doc_LookAhead_CC_MDGenerator(s, t, c, p, d);
	}

	public static void main(String[] args) {
		ExtType2Doc_LookAhead_CC_Params params = new ExtType2Doc_LookAhead_CC_Params(args);
		ExtType2Doc_LookAhead_CC_Bench bench = new ExtType2Doc_LookAhead_CC_Bench("org.emoflon.ibex-neo-benchmark", params);
		System.out.println(bench.genAndBench(false));
	}

}
