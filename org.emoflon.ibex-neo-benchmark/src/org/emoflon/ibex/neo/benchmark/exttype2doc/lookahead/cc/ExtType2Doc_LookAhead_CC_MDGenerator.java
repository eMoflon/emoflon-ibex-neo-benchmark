package org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.cc;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emoflon.ibex.neo.benchmark.exttype2doc.lookahead.ExtType2Doc_LookAhead_MDGenerator;

import ExtDocModel.Doc;
import ExtDocModel.Folder;
import ExtTypeModel.Package;
import ExtTypeModel.Type;

public class ExtType2Doc_LookAhead_CC_MDGenerator extends ExtType2Doc_LookAhead_MDGenerator {
	
	private Collection<Package> rootPackages = Collections.synchronizedList(new LinkedList<>());
	private Collection<Folder> rootFolders = Collections.synchronizedList(new LinkedList<>());

	public ExtType2Doc_LookAhead_CC_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	//// MODEL ////

	@Override
	protected void genModels() {
//		createContainers();
//		createPackagesAndFolders();
	}

//	private void createContainers() {
//		String postfix = SEP + "ROOT";
//		
//		// SRC
//		createContainerPackage(postfix);
//		// TRG
//		createContainerFolder(postfix);
//	}
//
//	private void createPackagesAndFolders() {
//		IntStream.range(0, parameters.modelScale).parallel().forEach(this::createRootPackageAndFolder);
//		((InternalEList<Package>) sContainer.getSubPackages()).addAllUnique(rootPackages);
//		((InternalEList<Folder>) tContainer.getSubFolder()).addAllUnique(rootFolders);
//	}
//
//	private void createRootPackageAndFolder(int index) {
//		String postfix = SEP + index;
//
//		// SRC
//		Package p = createRootPackage(postfix);
//		rootPackages.add(p);
//		// TRG
//		Folder f = createRootFolder(postfix);
//		rootFolders.add(f);
//
//		createPackageAndFolderHierarchies(p, f, 0, postfix);
//	}
//
//	private void createPackageAndFolderHierarchies(Package rootP, Folder rootF, int currentDepth, String oldPostfix) {
//		if (currentDepth >= parameters.package_hierarchy_depth)
//			return;
//
//		for (int i = 0; i < parameters.horizontal_package_scale; i++)
//			createPackageAndFolderHierarchy(rootP, rootF, currentDepth, oldPostfix, i);
//	}
//
//	private void createPackageAndFolderHierarchy(Package superP, Folder superF, int currentDepth, String oldPostfix, int index) {
//		String postfix = oldPostfix + SEP + index;
//
//		// SRC
//		Package p = createPackage(postfix, superP);
//		// TRG
//		Folder f = createFolder(postfix, superF);
//
//		createTypesAndDocs(p, f, postfix);
//		createPackageAndFolderHierarchies(p, f, currentDepth + 1, postfix);
//	}
//
//	private void createTypesAndDocs(Package p, Folder f, String oldPostfix) {
//		String postfixSuper = oldPostfix + SEP + 0;
//		String postfixSub = oldPostfix + SEP + 1;
//
//		// SRC
//		Type superT = createType(postfixSuper, false, p);
//		Type subT = createType(postfixSub, false, p);
//		createTypeInheritance(superT, subT);
//		// TRG
//		Doc superD = createDoc(postfixSuper, f);
//		Doc subD = createDoc(postfixSub, f);
//		createDocLink(superD, subD);
//	}

}
