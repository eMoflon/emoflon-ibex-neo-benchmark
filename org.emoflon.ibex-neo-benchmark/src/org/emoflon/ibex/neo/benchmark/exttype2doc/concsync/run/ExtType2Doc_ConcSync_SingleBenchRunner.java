package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.run;

import org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ExtType2Doc_ConcSync_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.concsync.ExtType2Doc_ConcSync_Params;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ConcSync_SingleBenchRunner {

	public static void main(String[] args) {
		ExtType2Doc_ConcSync_Params params = new ExtType2Doc_ConcSync_Params( //
				args[0], //
				Integer.valueOf(args[1]), //
				"h".equals(args[2]) ? ScaleOrientation.HORIZONTAL : ScaleOrientation.VERTICAL, //
				Integer.valueOf(args[3]), //
				Double.valueOf(args[4]));

		ExtType2Doc_ConcSync_Bench bench = new ExtType2Doc_ConcSync_Bench("org.emoflon.ibex-neo-benchmark");

		System.out.println(bench.genAndBench(params, false));
	}

}
