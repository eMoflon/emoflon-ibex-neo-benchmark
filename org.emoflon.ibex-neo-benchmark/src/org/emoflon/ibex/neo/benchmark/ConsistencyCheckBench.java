package org.emoflon.ibex.neo.benchmark;

import java.io.IOException;

import org.emoflon.delta.validation.InvalidDeltaException;
import org.emoflon.ibex.neo.benchmark.util.BenchEntry;
import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.tgg.operational.strategies.opt.CC;

public abstract class ConsistencyCheckBench<BP extends BenchParameters> extends IbexBench<CC, BP> {

	public ConsistencyCheckBench(String projectName, BP parameters) {
		super(projectName, parameters);
	}

	@Override
	protected BenchEntry<BP> applyDeltaAndRun(CC opStrat, boolean saveTransformedModels) throws IOException, InvalidDeltaException {
		long tic = System.currentTimeMillis();
		opStrat.run();
		long toc = System.currentTimeMillis();
		double resolve = (double) (toc - tic) / 1000;

		if (saveTransformedModels)
			opStrat.saveModels();

		int ram = getUsedRAM();

		opStrat.terminate();

		return new BenchEntry<>(parameters, numOfElements, 0, resolve, ram);
	}

}
