package org.emoflon.ibex.neo.benchmark;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

public abstract class BenchCache {
	public int numOfElements = 0;
	public Collection<EObject> corrs = new LinkedList<>();
	public Collection<EObject> markers = new LinkedList<>();
	
	//// CORR ////
	public Map<EObject, EObject> src2corr = new HashMap<EObject, EObject>();
}
