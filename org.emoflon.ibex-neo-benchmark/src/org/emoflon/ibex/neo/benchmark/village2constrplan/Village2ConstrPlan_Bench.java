package org.emoflon.ibex.neo.benchmark.village2constrplan;

import java.io.IOException;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.IntegrationBench;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.strategies.integrate.INTEGRATE;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.run.village2constrplan.INTEGRATE_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class Village2ConstrPlan_Bench extends IntegrationBench<Village2ConstrPlan_Params> {

	public Village2ConstrPlan_Bench(String projectName, Village2ConstrPlan_Params parameters) {
		super(projectName, parameters);
	}

	@Override
	protected INTEGRATE initStub(TGGResourceHandler resourceHandler) throws IOException {
		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Sat4J);
			options.propagate.usePrecedenceGraph(true);
			options.repair.useShortcutRules(true);
			options.repair.advancedOverlapStrategies(false);
			options.repair.relaxedSCPatternMatching(true);
			options.repair.omitUnnecessaryContext(true);
			options.repair.disableInjectivity(true);
			options.repair.usePGbasedSCruleCreation(true);
			return options;
		};
		
		return new INTEGRATE_App(ibexOptions);
	}

	@Override
	protected ModelAndDeltaGenerator<?, ?, ?, ?, ?, Village2ConstrPlan_Params> initModelAndDeltaGenerator(Resource s, Resource t, Resource c, Resource p,
			Resource d) {
		return new Village2ConstrPlan_MDGenerator(s, t, c, p, d);
	}
	
	public static void main(String[] args) {
		Village2ConstrPlan_Params params = new Village2ConstrPlan_Params(args);
		Village2ConstrPlan_Bench bench = new Village2ConstrPlan_Bench("org.emoflon.ibex-neo-benchmark", params);
		System.out.println(bench.genAndBench(true));
//		bench.genAndStore();
	}

}
