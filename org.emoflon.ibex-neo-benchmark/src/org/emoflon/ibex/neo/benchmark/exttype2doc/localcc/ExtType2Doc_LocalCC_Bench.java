package org.emoflon.ibex.neo.benchmark.exttype2doc.localcc;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.IntegrationBench;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.strategies.integrate.FragmentProvider;
import org.emoflon.ibex.tgg.operational.strategies.integrate.INTEGRATE;
import org.emoflon.ibex.tgg.operational.strategies.integrate.pattern.IntegrationPattern;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.run.exttype2doc_concsync.INTEGRATE_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class ExtType2Doc_LocalCC_Bench extends IntegrationBench<ExtType2Doc_LocalCC_Params> {

	public ExtType2Doc_LocalCC_Bench(String projectName, ExtType2Doc_LocalCC_Params parameters) {
		super(projectName, parameters);
	}

	private final IntegrationPattern pattern = new IntegrationPattern(Arrays.asList( //
			FragmentProvider.CHECK_LOCAL_CONSISTENCY //
			, FragmentProvider.CLEAN_UP //
	));

	@Override
	protected INTEGRATE initStub(TGGResourceHandler resourceHandler) throws IOException {
		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Sat4J);
			options.propagate.usePrecedenceGraph(true);
			options.integration.pattern(pattern);
			return options;
		};
		return new INTEGRATE_App(ibexOptions);
	}

	@Override
	protected ModelAndDeltaGenerator<?, ?, ?, ?, ?, ExtType2Doc_LocalCC_Params> initModelAndDeltaGenerator(Resource s, Resource t, Resource c,
			Resource p, Resource d) {
		return new ExtType2Doc_LocalCC_MDGenerator(s, t, c, p, d);
	}

	public static void main(String[] args) {
		ExtType2Doc_LocalCC_Params params = new ExtType2Doc_LocalCC_Params(args);
		ExtType2Doc_LocalCC_Bench bench = new ExtType2Doc_LocalCC_Bench("org.emoflon.ibex-neo-benchmark", params);
		System.out.println(bench.genAndBench(false));
	}

}
