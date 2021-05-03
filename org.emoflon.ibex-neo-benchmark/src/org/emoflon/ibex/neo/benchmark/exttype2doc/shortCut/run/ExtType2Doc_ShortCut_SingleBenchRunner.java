package org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.run;

import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Bench;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ExtType2Doc_ShortCut_Params;
import org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut.ShortCutDelta;
import org.emoflon.ibex.neo.benchmark.util.ScaleOrientation;

public class ExtType2Doc_ShortCut_SingleBenchRunner {

	public static void main(String[] args) {
		ShortCutDelta deltaType = null;
		switch (args[3]) {
		case "cp":
			deltaType = ShortCutDelta.CREATE_ROOT;
			break;
		case "mp":
			deltaType = ShortCutDelta.MOVE_PACKAGE;
			break;
		case "tr":
			deltaType = ShortCutDelta.MOVE_TYPE_ROOT;
			break;
		case "tl":
			deltaType = ShortCutDelta.MOVE_TYPE_LEAVE;
			break;
		case "ct":
			deltaType = ShortCutDelta.CREATE_TYPE_ROOT;
		default:
			break;
		}

		ExtType2Doc_ShortCut_Params params = new ExtType2Doc_ShortCut_Params( //
				args[0], //
				Integer.valueOf(args[1]), //
				"h".equals(args[2]) ? ScaleOrientation.HORIZONTAL : ScaleOrientation.VERTICAL, //
				deltaType // delta type
		);

		ExtType2Doc_ShortCut_Bench bench = new ExtType2Doc_ShortCut_Bench("org.emoflon.ibex-neo-benchmark");

		System.out.println(bench.genAndBench(params, false));
	}

}
