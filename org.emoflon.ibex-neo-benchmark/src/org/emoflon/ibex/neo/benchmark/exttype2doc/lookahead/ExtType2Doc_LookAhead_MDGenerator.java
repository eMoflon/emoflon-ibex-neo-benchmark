package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.exttype2doc.BenchCache;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;

import ExtDocModel.Folder;
import ExtType2Doc_LookAhead.ExtType2Doc_LookAheadFactory;
import ExtTypeModel.Package;

public abstract class ExtType2Doc_LookAhead_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_LookAheadFactory, ExtType2Doc_LookAhead_Params> {

	protected Package sContainer;
	protected Folder tContainer;

	public ExtType2Doc_LookAhead_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtType2Doc_LookAheadFactory corrFactoryInstance() {
		return ExtType2Doc_LookAheadFactory.eINSTANCE;
	}

	@Override
	protected void clearAll() {
		sContainer = null;
		tContainer = null;
		super.clearAll();
	}

	protected Package createContainerPackage(String postfix, BenchCache cache) {
		sContainer = sFactory.createPackage();
		sContainer.setName("Package" + postfix);
		source.getContents().add(sContainer);
		cache.numOfElements++;
		return sContainer;
	}

	protected Folder createContainerFolder(String postfix, BenchCache cache) {
		tContainer = tFactory.createFolder();
		tContainer.setName("Package" + postfix);
		target.getContents().add(tContainer);
		cache.numOfElements++;
		return tContainer;
	}

	@Override
	protected void genDelta() {
		// NO-OP
	}

}
