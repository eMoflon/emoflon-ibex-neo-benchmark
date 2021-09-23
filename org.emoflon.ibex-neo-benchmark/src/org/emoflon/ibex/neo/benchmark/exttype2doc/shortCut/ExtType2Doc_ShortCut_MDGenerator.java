package org.emoflon.ibex.neo.benchmark.exttype2doc.shortCut;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emoflon.ibex.neo.benchmark.exttype2doc.BenchCache;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;
import org.moflon.smartemf.persistence.SmartEMFResource;

import ExtDocModel.Doc;
import ExtDocModel.Entry;
import ExtDocModel.EntryType;
import ExtDocModel.Folder;
import ExtType2Doc_ShortCut.ExtType2Doc_ShortCutFactory;
import ExtType2Doc_ShortCut.ExtendingType2Doc__Marker;
import ExtType2Doc_ShortCut.Field2Entry;
import ExtType2Doc_ShortCut.Field2Entry__Marker;
import ExtType2Doc_ShortCut.Method2Entry;
import ExtType2Doc_ShortCut.Method2Entry__Marker;
import ExtType2Doc_ShortCut.Package2Folder;
import ExtType2Doc_ShortCut.Package2Folder__Marker;
import ExtType2Doc_ShortCut.Param2Entry;
import ExtType2Doc_ShortCut.Param2Entry__Marker;
import ExtType2Doc_ShortCut.Project2DocCont__Marker;
import ExtType2Doc_ShortCut.Project2DocContainer;
import ExtType2Doc_ShortCut.SubPackage2Folder__Marker;
import ExtType2Doc_ShortCut.Type2Doc;
import ExtType2Doc_ShortCut.Type2Doc__Marker;
import ExtTypeModel.Field;
import ExtTypeModel.Method;
import ExtTypeModel.Package;
import ExtTypeModel.Parameter;
import ExtTypeModel.Type;
import delta.Delta;

public class ExtType2Doc_ShortCut_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_ShortCutFactory, ExtType2Doc_ShortCut_Params> {

	private Project2DocContainer pr2dc;
	private Collection<Package> rootPackages = Collections.synchronizedList(new LinkedList<>());
	private Collection<Folder> rootFolders = Collections.synchronizedList(new LinkedList<>());
	
	public ExtType2Doc_ShortCut_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtType2Doc_ShortCutFactory corrFactoryInstance() {
		return ExtType2Doc_ShortCutFactory.eINSTANCE;
	}

	//// MODEL ////

	@Override
	protected void genModels() {
//		long tic = System.currentTimeMillis();
		createContainers();
		createPackageAndFolderHierarchies();
//		System.out.println("Generation took " + ((double) (System.currentTimeMillis() - tic)) / 1000  + "s");
	}

	private void createContainers() {
		// SRC
		createProject();
		// TRG
		createDocContainer();
		// CORR
		pr2dc = createCorr(cFactory.createProject2DocContainer(), sContainer, tContainer);
		allCorrs.add(pr2dc);

		// MARKER
		Project2DocCont__Marker marker = cFactory.createProject2DocCont__Marker();
		allMarkers.add(marker);
		marker.setCREATE__SRC__pr(sContainer);
		marker.setCREATE__CORR__pr2dc(pr2dc);
		marker.setCREATE__TRG__dc(tContainer);
	}

	private void createPackageAndFolderHierarchies() {
		IntStream.range(0, parameters.num_of_root_packages).parallel().forEach(this::createRootPackageAndFolder);
		((InternalEList<Package>) sContainer.getRootPackages()).addAllUnique(rootPackages);
		((InternalEList<Folder>) tContainer.getFolders()).addAllUnique(rootFolders);
		
		if (corr instanceof SmartEMFResource) {
			corr.getContents().addAll(allCorrs);
			protocol.getContents().addAll(allMarkers);
		} else {
			((InternalEList<EObject>) corr.getContents()).addAllUnique(allCorrs);
			((InternalEList<EObject>) protocol.getContents()).addAllUnique(allMarkers);
		}
	}

	private void createRootPackageAndFolder(int index) {
		BenchCache cache = new BenchCache();
		
		String postfix = SEP + index;

		// SRC
		Package p = createRootPackage(postfix, cache, false);
		rootPackages.add(p);
		
		// TRG
		Folder f = createRootFolder(postfix, cache, false);
		rootFolders.add(f);
		
		// CORR
		Package2Folder p2f = createCorr(cFactory.createPackage2Folder(), p, f);
		cache.corrs.add(p2f);
		cache.src2corr.put(p, p2f);
		
		// MARKER
		Package2Folder__Marker marker = cFactory.createPackage2Folder__Marker();
		cache.markers.add(marker);
		marker.setCREATE__SRC__p(p);
		marker.setCREATE__CORR__p2f(p2f);
		marker.setCREATE__TRG__f(f);
		marker.setCONTEXT__SRC__pr(sContainer);
		marker.setCONTEXT__CORR__pr2dc(pr2dc);
		marker.setCONTEXT__TRG__dc(tContainer);

		if (parameters.horizontal_package_scales.length != 0)
			createPackageAndFolderHierarchies(p, f, 0, postfix, cache);
		
		addCacheContent(cache);
	}

	private void createPackageAndFolderHierarchies(Package rootP, Folder rootF, int currentDepth, String oldPostfix, BenchCache cache) {
		if (currentDepth >= parameters.horizontal_package_scales.length)
			return;

		for (int i = 0; i < parameters.horizontal_package_scales[currentDepth]; i++)
			createPackageAndFolderHierarchy(rootP, rootF, currentDepth, oldPostfix, i, cache);
	}

	private Package createPackageAndFolderHierarchy(Package superP, Folder f, int currentDepth, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Package p = createPackage(postfix, superP, cache, true);
		// CORR
		Package2Folder p2f = createCorr(cFactory.createPackage2Folder(), p, f);
		cache.corrs.add(p2f);
		cache.src2corr.put(p, p2f);

		// MARKER
		SubPackage2Folder__Marker marker = cFactory.createSubPackage2Folder__Marker();
		cache.markers.add(marker);
		marker.setCREATE__SRC__sp(p);
		marker.setCREATE__CORR__sp2f(p2f);
		marker.setCONTEXT__SRC__p(superP);
		marker.setCONTEXT__CORR__p2f((Package2Folder) cache.src2corr.get(superP));
		marker.setCONTEXT__TRG__f(f);

		if (currentDepth < parameters.types_for_packages.length && parameters.types_for_packages[currentDepth])
			createTypeAndDocHierarchies(p, f, postfix, cache);

		createPackageAndFolderHierarchies(p, f, currentDepth + 1, postfix, cache);
		return p;
	}

	private void createTypeAndDocHierarchies(Package p, Folder f, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_root_types; i++)
			createRootTypeAndDoc(p, f, oldPostfix, i, cache);
	}

	private void createRootTypeAndDoc(Package p, Folder f, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Type t = createType(postfix, false, p, cache, true);
		// TRG
		Doc d = createDoc(postfix, f, cache, true);
		// CORR
		Type2Doc t2d = createCorr(cFactory.createType2Doc(), t, d);
		cache.corrs.add(t2d);
		cache.src2corr.put(t, t2d);
		// MARKER
		Type2Doc__Marker marker = cFactory.createType2Doc__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__p(p);
		marker.setCONTEXT__CORR__p2f((Package2Folder) cache.src2corr.get(p));
		marker.setCONTEXT__TRG__f(f);
		marker.setCREATE__SRC__t(t);
		marker.setCREATE__CORR__t2d(t2d);
		marker.setCREATE__TRG__d(d);

		createMethodsAndEntries(t, d, postfix, cache);
		createFieldsAndEntries(t, d, postfix, cache);

		createTypeAndDocHierarchy(p, f, t, d, 1, postfix, cache);
	}

	private void createTypeAndDocHierarchy(Package p, Folder f, Type rootT, Doc rootD, int currentDepth, String oldPostfix, BenchCache cache) {
		if (currentDepth >= parameters.type_inheritance_depth)
			return;

		for (int i = 0; i < parameters.horizontal_type_inheritance_scale; i++)
			createTypeAndDocInheritance(p, f, rootT, rootD, currentDepth, oldPostfix, i, cache);
	}

	private void createTypeAndDocInheritance(Package p, Folder f, Type superT, Doc superD, int currentDepth, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Type t = createType(postfix, false, p, cache, true);
		createTypeInheritance(superT, t);
		// TRG
		Doc d = createDoc(postfix, f, cache, true);
		createDocLink(superD, d);
		// CORR
		Type2Doc t2d = createCorr(cFactory.createType2Doc(), t, d);
		cache.corrs.add(t2d);
		cache.src2corr.put(t, t2d);

		// MARKER
		ExtendingType2Doc__Marker marker = cFactory.createExtendingType2Doc__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__p(p);
		marker.setCONTEXT__CORR__p2f((Package2Folder) cache.src2corr.get(p));
		marker.setCONTEXT__TRG__f(f);
		marker.setCONTEXT__SRC__t(superT);
		marker.setCONTEXT__CORR__t2d((Type2Doc) cache.src2corr.get(superT));
		marker.setCONTEXT__TRG__d(superD);
		marker.setCREATE__SRC__nt(t);
		marker.setCREATE__CORR__nt2nd(t2d);
		marker.setCREATE__TRG__nd(d);

		createMethodsAndEntries(t, d, postfix, cache);
		createFieldsAndEntries(t, d, postfix, cache);

		switch (parameters.scaleOrientation) {
		case HORIZONTAL:
			createTypeAndDocHierarchy(p, f, t, d, currentDepth + 1, postfix, cache);
			break;
		case VERTICAL:
			if (index == 0)
				createTypeAndDocHierarchy(p, f, t, d, currentDepth + 1, postfix, cache);
			break;
		default:
			break;
		}
	}

	private void createMethodsAndEntries(Type t, Doc d, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_methods; i++)
			createMethodAndEntry(t, d, oldPostfix, i, cache);
	}

	private void createMethodAndEntry(Type t, Doc d, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Method m = createMethod(postfix, t, cache, true);
		// TRG
		Entry e = createEntry(postfix, EntryType.METHOD, d, cache, true);
		// CORR
		Method2Entry m2e = createCorr(cFactory.createMethod2Entry(), m, e);
		cache.corrs.add(m2e);
		cache.src2corr.put(m, m2e);
		
		// MARKER
		Method2Entry__Marker marker = cFactory.createMethod2Entry__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__t(t);
		marker.setCONTEXT__CORR__t2d((Type2Doc) cache.src2corr.get(t));
		marker.setCONTEXT__TRG__d(d);
		marker.setCREATE__SRC__m(m);
		marker.setCREATE__CORR__m2e(m2e);
		marker.setCREATE__TRG__e(e);

		createParameters(m, e, postfix, cache);
	}

	private void createFieldsAndEntries(Type t, Doc d, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_fields; i++)
			createFieldAndEntry(t, d, oldPostfix, i, cache);
	}

	private void createFieldAndEntry(Type t, Doc d, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Field f = createField(postfix, t, cache, true);
		// TRG
		Entry e = createEntry(postfix, EntryType.FIELD, d, cache, true);
		// CORR
		Field2Entry f2e = createCorr(cFactory.createField2Entry(), f, e);
		cache.corrs.add(f2e);
		cache.src2corr.put(f, f2e);

		// MARKER
		Field2Entry__Marker marker = cFactory.createField2Entry__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__t(t);
		marker.setCONTEXT__CORR__t2d((Type2Doc) cache.src2corr.get(t));
		marker.setCONTEXT__TRG__d(d);
		marker.setCREATE__SRC__f(f);
		marker.setCREATE__CORR__f2e(f2e);
		marker.setCREATE__TRG__e(e);
	}

	private void createParameters(Method m, Entry e, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_parameters; i++)
			createParameters(m, e, oldPostfix, i, cache);
	}

	private void createParameters(Method m, Entry e, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Parameter p = createParameter(postfix, m, cache, true);
		// CORR
		Param2Entry p2e = createCorr(cFactory.createParam2Entry(), p, e);
		cache.corrs.add(p2e);
		cache.src2corr.put(p, p2e);

		// MARKER
		Param2Entry__Marker marker = cFactory.createParam2Entry__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__m(m);
		marker.setCONTEXT__CORR__m2e((Method2Entry) cache.src2corr.get(m));
		marker.setCONTEXT__TRG__e(e);
		marker.setCREATE__SRC__p(p);
		marker.setCREATE__CORR__p2e(p2e);
	}

	//// DELTA ////

	@Override
	protected void genDelta() {
		int deltaCount = 0;
		for (Package p : sContainer.getRootPackages()) {
			switch (parameters.delta_type) {
			case CREATE_ROOT:
				createRoot(p);
				break;
			case MOVE_PACKAGE:
				movePackage(p);
				break;
			case MOVE_TYPE_ROOT:
				moveTypeRoot(p);
				break;
			case MOVE_TYPE_LEAF:
				moveTypeLeave(p);
				break;
			case CREATE_TYPE_ROOT:
				createTypeRoot(p);
			default:
				break;
			}
			deltaCount++;
			if(deltaCount >= parameters.num_of_changes) {
				return;
			}
		}
	}

	private void createRoot(Package p) {
		Delta delta = createDelta(false, true);

		Package newRoot = sFactory.createPackage();
		newRoot.setName("NewRoot" + p.getName());
//		name2package.put(newRoot.getName(), newRoot);

		createObject(newRoot, delta);
		createLink(sContainer, newRoot, sPackage.getProject_RootPackages(), delta);
		createLink(newRoot, p, sPackage.getPackage_SubPackages(), delta);
	}

	private void movePackage(Package p) {
		Delta delta = createDelta(false, true);

		Package lastPackage = p;
		while (!lastPackage.getSubPackages().isEmpty())
			lastPackage = lastPackage.getSubPackages().get(0);

		createLink(p, lastPackage, sPackage.getPackage_SubPackages(), delta);
	}

	private void moveTypeRoot(Package p) {
		Delta delta = createDelta(false, true);

		Package firstPackageWithTypes = p;
		while (!firstPackageWithTypes.getSubPackages().isEmpty()) {
			firstPackageWithTypes = firstPackageWithTypes.getSubPackages().get(0);
			if (!firstPackageWithTypes.getTypes().isEmpty())
				break;
		}

		for (Type type : firstPackageWithTypes.getTypes())
			createLink(p, type, sPackage.getPackage_Types(), delta);
	}

	private void moveTypeLeave(Package p) {
		Delta delta = createDelta(false, true);

		Package firstPackageWithTypes = p;
		while (!firstPackageWithTypes.getSubPackages().isEmpty()) {
			firstPackageWithTypes = firstPackageWithTypes.getSubPackages().get(0);
			if (!firstPackageWithTypes.getTypes().isEmpty())
				break;
		}
		Type lastType = null;
		for (Type t : firstPackageWithTypes.getTypes()) {
			if (t.getExtendedBy().isEmpty()) {
				lastType = t;
				break;
			}
		}

		createLink(p, lastType, sPackage.getPackage_Types(), delta);
		for (Type superType : lastType.getInheritsFrom())
			deleteLink(superType, lastType, sPackage.getType_ExtendedBy(), delta);
	}

	private void createTypeRoot(Package p) {
		Delta delta = createDelta(false, true);

		Package firstPackageWithTypes = p;
		while (!firstPackageWithTypes.getSubPackages().isEmpty()) {
			firstPackageWithTypes = firstPackageWithTypes.getSubPackages().get(0);
			if (!firstPackageWithTypes.getTypes().isEmpty())
				break;
		}
		Type rootType = null;
		for (Type t : firstPackageWithTypes.getTypes()) {
			if (t.getInheritsFrom().isEmpty()) {
				rootType = t;
				break;
			}
		}

		Type newRootType = sFactory.createType();
		newRootType.setName("NewRoot" + rootType.getName());
//		name2type.put(newRootType.getName(), newRootType);

		createObject(newRootType, delta);
		createLink(firstPackageWithTypes, newRootType, sPackage.getPackage_Types(), delta);
		createLink(newRootType, rootType, sPackage.getType_ExtendedBy(), delta);
	}
}
