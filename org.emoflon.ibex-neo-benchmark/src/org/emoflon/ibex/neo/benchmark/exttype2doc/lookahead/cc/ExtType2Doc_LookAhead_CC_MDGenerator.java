package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc;

import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;

import ExtDocModel.Doc;
import ExtDocModel.Folder;
import ExtType2Doc_LookAhead.ExtType2Doc_LookAheadFactory;
import ExtTypeModel.Package;
import ExtTypeModel.Type;

public class ExtType2Doc_LookAhead_CC_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_LookAheadFactory, ExtType2Doc_LookAhead_CC_Params> {

	public ExtType2Doc_LookAhead_CC_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtType2Doc_LookAheadFactory corrFactoryInstance() {
		return ExtType2Doc_LookAheadFactory.eINSTANCE;
	}

	//// MODEL ////

	@Override
	protected void genModels() {
		createContainers();
		createPackagesAndFolders();
	}

	private void createContainers() {
		// SRC
		createProject();
		// TRG
		createDocContainer();
	}

	//// DELTA ////

	private void createPackagesAndFolders() {
		for (int i = 0; i < parameters.modelScale; i++)
			createRootPackageAndFolder(i);
	}

	private void createRootPackageAndFolder(int index) {
		String postfix = SEP + index;

		// SRC
		Package p = createRootPackage(postfix);
		// TRG
		Folder f = createFolder(postfix);

		createTypesAndDocs(p, f, postfix);
	}

	private void createTypesAndDocs(Package p, Folder f, String oldPostfix) {
		String postfixSuper = oldPostfix + SEP + 0;
		String postfixSub = oldPostfix + SEP + 1;

		// SRC
		Type superT = createType(postfixSuper, false, p);
		Type subT = createType(postfixSub, false, p);
		createTypeInheritance(superT, subT);
		// TRG
		Doc superD = createDoc(postfixSuper, f);
		Doc subD = createDoc(postfixSub, f);
		createDocLink(superD, subD);
	}

	@Override
	protected void genDelta() {
		// NO-OP
	}

}
