package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.neo.benchmark.SynchronizationBench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.ExtType2Doc_LookAhead_Params;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.matches.ITGGMatch;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.operational.strategies.opt.CO;
import org.emoflon.ibex.tgg.operational.strategies.sync.SYNC;
import org.emoflon.ibex.tgg.run.exttype2doc_lookahead.SYNC_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class ExtType2Doc_LookAhead_Sync_Bench extends SynchronizationBench<ExtType2Doc_LookAhead_Params> {

	protected CO co = null;

	public ExtType2Doc_LookAhead_Sync_Bench(String projectName, ExtType2Doc_LookAhead_Params parameters) {
		super(projectName, parameters);
	}

	@Override
	protected SYNC initStub(TGGResourceHandler resourceHandler) throws IOException {
		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Sat4J);
			return options;
		};

		SYNC_App sync = new SYNC_App(ibexOptions);
		sync.setUpdatePolicy(matchContainer -> {
			List<ITGGMatch> matches = new ArrayList<>(matchContainer.getMatches());
			int randomIndex = (int) (Math.random() * matches.size());
			return matches.get(randomIndex);
		});

		co = new CO(sync.getOptions());
		sync.getOptions().executable(sync);

		return sync;
	}

	@Override
	protected ModelAndDeltaGenerator<?, ?, ?, ?, ?, ExtType2Doc_LookAhead_Params> initModelAndDeltaGenerator(Resource s, Resource t, Resource c,
			Resource p, Resource d) {
		return new ExtType2Doc_LookAhead_Sync_MDGenerator(s, t, c, p, d);
	}

	@Override
	protected double calcSuccessRate() {
		try {
			co.run();
			co.terminate();
			return co.modelsAreConsistent() ? 1.0 : 0.0;
		} catch (IOException e) {
			return -1000;
		}
	}

	public static void main(String[] args) {
		ExtType2Doc_LookAhead_Params params = new ExtType2Doc_LookAhead_Params(args);
		ExtType2Doc_LookAhead_Sync_Bench bench = new ExtType2Doc_LookAhead_Sync_Bench("org.emoflon.ibex-neo-benchmark", params);
		System.out.println(bench.genAndBench(false));
	}

}
