package org.emoflon.ibex.neo.benchmark;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.exttype2doc.BenchCache;
import org.emoflon.ibex.neo.benchmark.util.BenchParameters;

import delta.AttributeDelta;
import delta.Delta;
import delta.DeltaContainer;
import delta.DeltaFactory;
import delta.Link;
import delta.StructuralDelta;

public abstract class ModelAndDeltaGenerator<CorrFactory extends EFactory, //
		SrcFactory extends EFactory, SrcPackage extends EPackage, //
		TrgFactory extends EFactory, TrgPackage extends EPackage, //
		BenchParams extends BenchParameters> {

	public final String SEP = "_";

	
	protected final Resource source;
	protected final Resource target;
	protected final Resource corr;
	protected final Resource protocol;
	protected final Resource delta;

	protected final SrcPackage sPackage = srcPackageInstance();
	@SuppressWarnings("unchecked")
	protected final SrcFactory sFactory = (SrcFactory) sPackage.getEFactoryInstance();
	protected final TrgPackage tPackage = trgPackageInstance();
	@SuppressWarnings("unchecked")
	protected final TrgFactory tFactory = (TrgFactory) tPackage.getEFactoryInstance();
	protected final CorrFactory cFactory = corrFactoryInstance();
	protected final DeltaFactory dFactory = DeltaFactory.eINSTANCE;

	protected BenchParams parameters;

	private AtomicInteger numOfElements;

	protected DeltaContainer dContainer;

	protected Collection<EObject> allCorrs = Collections.synchronizedList(new LinkedList<>());
	protected Collection<EObject> allMarkers = Collections.synchronizedList(new LinkedList<>());

	public ModelAndDeltaGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		this.numOfElements = new AtomicInteger(0);

		this.source = source;
		this.target = target;
		this.corr = corr;
		this.protocol = protocol;

		this.delta = delta;
	}

	protected abstract SrcPackage srcPackageInstance();

	protected abstract TrgPackage trgPackageInstance();

	protected abstract CorrFactory corrFactoryInstance();

	protected abstract void clearAll();

	protected abstract void genModels();

	protected abstract void genDelta();

	public void gen(BenchParams parameters) {
		this.parameters = parameters;
		clearAll();
		genModels();
		createDeltaContainer();
		genDelta();
	}

	public int getNumOfElements() {
		return numOfElements.get();
	}
	
	protected void addNumOfElements(int numOfElts) {
		this.numOfElements.addAndGet(numOfElts);
	}

	protected <Corr extends EObject> Corr createCorr(Corr corr, EObject src, EObject trg) {
		corr.eSet(corr.eClass().getEStructuralFeature("source"), src);
		corr.eSet(corr.eClass().getEStructuralFeature("target"), trg);
		return corr;
	}
	
	protected <Corr extends EObject> Corr createCorr(Corr corr, EObject src, EObject trg, BenchCache cache) {
		createCorr(corr, src, trg);
		cache.corrs.add(corr);
		cache.src2corr.put(src, corr);
		return corr;
	}
	
	protected void addCacheContent(BenchCache cache) {
		allCorrs.addAll(cache.corrs);
		allMarkers.addAll(cache.markers);
		addNumOfElements(cache.numOfElements);
	}
	
	//// DELTA ////

	private void createDeltaContainer() {
		dContainer = dFactory.createDeltaContainer();
		delta.getContents().add(dContainer);
	}

	protected Delta createDelta(boolean atomic, boolean structured) {
		Delta d = dFactory.createDelta();
		d.setAtomic(atomic);
		d.setContainer(dContainer);
		if (structured) {
			StructuralDelta sd = dFactory.createStructuralDelta();
			d.setStructuralDelta(sd);
		}
		return d;
	}

	protected AttributeDelta createAttrDelta(EObject obj, EAttribute attr, Object newVal, Delta delta) {
		AttributeDelta ad = dFactory.createAttributeDelta();
		ad.setObject(obj);
		ad.setAttribute(attr);
		ad.setNewValue(newVal);
		delta.getAttributeDeltas().add(ad);
		return ad;
	}

	protected void createObject(EObject obj, Delta delta) {
		delta.getStructuralDelta().getCreatedObjects().add(obj);
	}

	protected void deleteObject(EObject obj, Delta delta) {
		delta.getStructuralDelta().getDeletedObjects().add(obj);
	}

	protected Link createLink(EObject src, EObject trg, EReference type, Delta delta) {
		Link l = dFactory.createLink();
		l.setSrc(src);
		l.setTrg(trg);
		l.setType(type);
		delta.getStructuralDelta().getCreatedLinks().add(l);
		return l;
	}

	protected Link deleteLink(EObject src, EObject trg, EReference type, Delta delta) {
		Link l = dFactory.createLink();
		l.setSrc(src);
		l.setTrg(trg);
		l.setType(type);
		delta.getStructuralDelta().getDeletedLinks().add(l);
		return l;
	}

}
