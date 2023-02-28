package org.emoflon.ibex.neo.benchmark.village2constrplan;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.IntegrationBench;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.neo.benchmark.util.BenchEntry;
import org.emoflon.ibex.tgg.operational.benchmark.FullBenchmarkLogger;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.strategies.integrate.FragmentProvider;
import org.emoflon.ibex.tgg.operational.strategies.integrate.INTEGRATE;
import org.emoflon.ibex.tgg.operational.strategies.integrate.pattern.IntegrationFragment;
import org.emoflon.ibex.tgg.operational.strategies.integrate.pattern.IntegrationPattern;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.run.village2constrplan.INTEGRATE_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class Village2ConstrPlan_Bench extends IntegrationBench<Village2ConstrPlan_Params> {

	public final FullBenchmarkLogger benchLogger = new FullBenchmarkLogger();

	public Village2ConstrPlan_Bench(String projectName, Village2ConstrPlan_Params parameters) {
		super(projectName, parameters);
	}

	@Override
	protected INTEGRATE initStub(TGGResourceHandler resourceHandler) throws IOException {
		List<IntegrationFragment> fragments = new LinkedList<>(Arrays.asList( //
				FragmentProvider.REPAIR, //
				FragmentProvider.RESOLVE_BROKEN_MATCHES, //
				FragmentProvider.TRANSLATE, //
				FragmentProvider.CLEAN_UP //
		));
		
		if (parameters.repair_mode == RepairMode.NONE)
			fragments.remove(FragmentProvider.REPAIR);
			

		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Sat4J);
			options.propagate.usePrecedenceGraph(true);
			options.repair.useShortcutRules(parameters.repair_mode != RepairMode.NONE);
			options.repair.repairAttributes(parameters.repair_mode != RepairMode.NONE);
			options.repair.advancedOverlapStrategies(false);
			options.repair.relaxedSCPatternMatching(parameters.repair_mode != RepairMode.NONE);
			options.repair.omitUnnecessaryContext(parameters.repair_mode != RepairMode.NONE);
			options.repair.disableInjectivity(true);
			options.repair.usePGbasedSCruleCreation(parameters.repair_mode == RepairMode.PG_BASED);
			options.integration.pattern(new IntegrationPattern(fragments));
			options.debug.benchmarkLogger(benchLogger);
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
		BenchEntry<Village2ConstrPlan_Params> benchEntry = bench.genAndBench(false);
		benchEntry.addAdvancedStats( //
				bench.benchLogger.getTotalElementsCreated(), //
				bench.benchLogger.getTotalElementsDeleted(), //
				bench.benchLogger.getTotalMatchesFound(), //
				bench.benchLogger.getTotalMatchesRepaired(), //
				bench.benchLogger.getTotalMatchesRevoked(), //
				bench.benchLogger.getTotalMatchesApplied() //
		);
		System.out.println(benchEntry);
	}

}
