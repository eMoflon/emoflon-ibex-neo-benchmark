package org.emoflon.ibex.neo.benchmark.exttype2doc;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

public class BenchCache {
	public int numOfElements = 0;
	public Collection<EObject> corrs = new LinkedList<>();
	public Collection<EObject> markers = new LinkedList<>();
	public Map<EObject, EObject> src2corr = new HashMap<EObject, EObject>();
}
