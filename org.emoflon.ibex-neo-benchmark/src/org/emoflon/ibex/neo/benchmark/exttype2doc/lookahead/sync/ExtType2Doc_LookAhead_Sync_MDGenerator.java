package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;

import ExtType2Doc_LookAhead.ExtType2Doc_LookAheadFactory;
import ExtTypeModel.Package;
import ExtTypeModel.Type;

public class ExtType2Doc_LookAhead_Sync_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_LookAheadFactory, ExtType2Doc_LookAhead_Sync_Params> {

	public ExtType2Doc_LookAhead_Sync_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ExtType2Doc_LookAheadFactory corrFactoryInstance() {
		return ExtType2Doc_LookAheadFactory.eINSTANCE;
	}

////MODEL ////

	@Override
	protected void genModels() {
		createContainer();
		createPackages();
	}

	private void createContainer() {
		// SRC
		createProject();
	}

	//// DELTA ////

	private void createPackages() {
		for (int i = 0; i < parameters.modelScale; i++)
			createRootPackageAndFolder(i);
	}

	private void createRootPackageAndFolder(int index) {
		String postfix = SEP + index;

		// SRC
		Package p = createRootPackage(postfix);

		createTypes(p, postfix);
	}

	private void createTypes(Package p, String oldPostfix) {
		String postfixSuper = oldPostfix + SEP + 0;
		String postfixSub = oldPostfix + SEP + 1;

		// SRC
		Type superT = createType(postfixSuper, false, p);
		Type subT = createType(postfixSub, false, p);
		createTypeInheritance(superT, subT);
	}

	@Override
	protected void genDelta() {
		// NO-OP
	}

}
