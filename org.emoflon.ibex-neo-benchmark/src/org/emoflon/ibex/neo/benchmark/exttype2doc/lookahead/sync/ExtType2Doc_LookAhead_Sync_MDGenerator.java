package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.sync;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.ExtType2Doc_LookAhead_Params;

import ExtType2Doc_LookAhead.ExtType2Doc_LookAheadFactory;
import ExtTypeModel.Package;
import ExtTypeModel.Type;

public class ExtType2Doc_LookAhead_Sync_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_LookAheadFactory, ExtType2Doc_LookAhead_Params> {

	public ExtType2Doc_LookAhead_Sync_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtType2Doc_LookAheadFactory corrFactoryInstance() {
		return ExtType2Doc_LookAheadFactory.eINSTANCE;
	}

	//// MODEL ////

	@Override
	protected void genModels() {
		createContainer();
		createPackages();
	}

	private void createContainer() {
		// SRC
		createProject();
	}

	private void createPackages() {
		for (int i = 0; i < parameters.modelScale; i++)
			createRootPackage(i);
	}

	private void createRootPackage(int index) {
		String postfix = SEP + index;

		// SRC
		Package p = createRootPackage(postfix);

		createPackageHierarchies(p, 0, postfix);
	}

	private void createPackageHierarchies(Package rootP, int currentDepth, String oldPostfix) {
		if (currentDepth >= parameters.package_hierarchy_depth)
			return;

		for (int i = 0; i < parameters.horizontal_package_scale; i++)
			createPackageHierarchy(rootP, currentDepth, oldPostfix, i);
	}

	private void createPackageHierarchy(Package superP, int currentDepth, String oldPostfix, int index) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Package p = createPackage(postfix, superP);

		createTypes(p, postfix);
		createPackageHierarchies(p, currentDepth + 1, postfix);
	}

	private void createTypes(Package p, String oldPostfix) {
		String postfixSuper = oldPostfix + SEP + 0;
		String postfixSub = oldPostfix + SEP + 1;

		// SRC
		Type superT = createType(postfixSuper, false, p);
		Type subT = createType(postfixSub, false, p);
		createTypeInheritance(superT, subT);
	}

	//// DELTA ////

	@Override
	protected void genDelta() {
		// NO-OP
	}

}
