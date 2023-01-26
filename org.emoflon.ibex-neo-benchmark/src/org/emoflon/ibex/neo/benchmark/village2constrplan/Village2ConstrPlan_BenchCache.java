package org.emoflon.ibex.neo.benchmark.village2constrplan;

import java.util.HashMap;
import java.util.Map;

import org.emoflon.ibex.neo.benchmark.BenchCache;

import ConstructionPlan.Construction;
import ConstructionPlan.Plan;
import Village.House;

public class Village2ConstrPlan_BenchCache extends BenchCache {
	//// SRC ////
	public Map<String, House> name2house = new HashMap<>();
	//// TRG ////
	public Map<String, Plan> name2plan = new HashMap<>();
	public Map<String, Construction> name2constr = new HashMap<>();
}
