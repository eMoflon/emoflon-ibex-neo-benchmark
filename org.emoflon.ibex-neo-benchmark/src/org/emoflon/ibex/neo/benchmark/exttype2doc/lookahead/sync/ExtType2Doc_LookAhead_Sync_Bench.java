package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.neo.benchmark.SynchronizationBench;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.ibex.tgg.operational.strategies.sync.SYNC;
import org.emoflon.ibex.tgg.run.exttype2doc_lookahead.SYNC_App;
import org.emoflon.ibex.tgg.util.ilp.ILPFactory.SupportedILPSolver;

public class ExtType2Doc_LookAhead_Sync_Bench extends SynchronizationBench<ExtType2Doc_LookAhead_Sync_Params> {

	public ExtType2Doc_LookAhead_Sync_Bench(String projectName, ExtType2Doc_LookAhead_Sync_Params parameters) {
		super(projectName, parameters);
	}

	@Override
	protected SYNC initStub(TGGResourceHandler resourceHandler) throws IOException {
		Function<IbexOptions, IbexOptions> ibexOptions = options -> {
			options.resourceHandler(resourceHandler);
			options.ilpSolver(SupportedILPSolver.Sat4J);
			return options;
		};

		return new SYNC_App(ibexOptions);
	}

	@Override
	protected ModelAndDeltaGenerator<?, ?, ?, ?, ?, ExtType2Doc_LookAhead_Sync_Params> initModelAndDeltaGenerator(Resource s, Resource t, Resource c,
			Resource p, Resource d) {
		return new ExtType2Doc_LookAhead_Sync_MDGenerator(s, t, c, p, d);
	}

	@Override
	protected int getUsedRAM() {
		// misused this method to access untranslated elements
		AtomicInteger numOfTranslatedElts = new AtomicInteger(0);
		protocol.getContents().forEach(ra -> ra.eCrossReferences().forEach(obj -> {
			Resource res = obj.eResource();
			if (res != null && (res.equals(source) || res.equals(target)))
				numOfTranslatedElts.incrementAndGet();
		}));
		double successRate = ((double) numOfTranslatedElts.get()) / ((double) numOfElements);
		return (int) (successRate * 100);
	}

	public static void main(String[] args) {
		ExtType2Doc_LookAhead_Sync_Params params = new ExtType2Doc_LookAhead_Sync_Params(args);
		ExtType2Doc_LookAhead_Sync_Bench bench = new ExtType2Doc_LookAhead_Sync_Bench("org.emoflon.ibex-neo-benchmark", params);
		System.out.println(bench.genAndBench(false));
	}

}
