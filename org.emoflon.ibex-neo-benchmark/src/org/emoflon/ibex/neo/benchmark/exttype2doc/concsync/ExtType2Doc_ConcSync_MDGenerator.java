package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emoflon.ibex.neo.benchmark.exttype2doc.BenchCache;
import org.emoflon.ibex.neo.benchmark.exttype2doc.ExtType2Doc_MDGenerator;
import org.moflon.smartemf.persistence.SmartEMFResource;

import ExtDocModel.Annotation;
import ExtDocModel.Doc;
import ExtDocModel.Entry;
import ExtDocModel.EntryType;
import ExtDocModel.Folder;
import ExtDocModel.Glossary;
import ExtDocModel.GlossaryEntry;
import ExtType2Doc_ConcSync.ExtType2Doc_ConcSyncFactory;
import ExtType2Doc_ConcSync.ExtendingType2Doc__Marker;
import ExtType2Doc_ConcSync.Field2Entry;
import ExtType2Doc_ConcSync.Field2Entry__Marker;
import ExtType2Doc_ConcSync.GlossaryEntry__Marker;
import ExtType2Doc_ConcSync.GlossaryLink__Marker;
import ExtType2Doc_ConcSync.Glossary__Marker;
import ExtType2Doc_ConcSync.JDoc2Annotation;
import ExtType2Doc_ConcSync.JDoc2Annotation__Marker;
import ExtType2Doc_ConcSync.Method2Entry;
import ExtType2Doc_ConcSync.Method2Entry__Marker;
import ExtType2Doc_ConcSync.Package2Folder;
import ExtType2Doc_ConcSync.Package2Folder__Marker;
import ExtType2Doc_ConcSync.Param2Entry;
import ExtType2Doc_ConcSync.Param2Entry__Marker;
import ExtType2Doc_ConcSync.Project2DocCont__Marker;
import ExtType2Doc_ConcSync.Project2DocContainer;
import ExtType2Doc_ConcSync.Type2Doc;
import ExtType2Doc_ConcSync.Type2Doc__Marker;
import ExtTypeModel.Field;
import ExtTypeModel.JavaDoc;
import ExtTypeModel.Method;
import ExtTypeModel.Package;
import ExtTypeModel.Parameter;
import ExtTypeModel.Type;
import delta.Delta;

public class ExtType2Doc_ConcSync_MDGenerator extends ExtType2Doc_MDGenerator<ExtType2Doc_ConcSyncFactory, ExtType2Doc_ConcSync_Params> {

	Project2DocContainer pr2dc;

	private Package rootPackage;
	private Folder rootFolder;
	Package2Folder rp2rf;
	
	private List<Type> rootTypes = Collections.synchronizedList(new LinkedList<>());

	private int glossaryLinkCounter;
	
	private Collection<Type> allTypes = Collections.synchronizedList(new LinkedList<>());
	private Collection<Doc> allDocs = Collections.synchronizedList(new LinkedList<>());
	private Collection<Entry> allEntries = Collections.synchronizedList(new LinkedList<>());
	
	private Map<String, Doc> name2doc = Collections.synchronizedMap(new HashMap<>());

	public ExtType2Doc_ConcSync_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtType2Doc_ConcSyncFactory corrFactoryInstance() {
		return ExtType2Doc_ConcSyncFactory.eINSTANCE;
	}

	//// MODEL ////

	@Override
	protected void genModels() {
		glossaryLinkCounter = 0;

		createContainers();
		createTypeAndDocHierarchies();
		createGlossaryEntriesAndLinks();
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
		Project2DocCont__Marker marker0 = cFactory.createProject2DocCont__Marker();
		allMarkers.add(marker0);
		marker0.setCREATE__SRC__pr(sContainer);
		marker0.setCREATE__CORR__pr2dc(pr2dc);
		marker0.setCREATE__TRG__dc(tContainer);

		// TRG
		Glossary g = createGlossary();
		// MARKER
		Glossary__Marker marker1 = cFactory.createGlossary__Marker();
		allMarkers.add(marker1);
		marker1.setCONTEXT__TRG__dc(tContainer);
		marker1.setCREATE__TRG__g(g);
		protocol.getContents().add(marker1);

		createRootPackageAndFolder();
	}

	private void createRootPackageAndFolder() {
		BenchCache cache = new BenchCache();
		
		// SRC
		rootPackage = createRootPackage("", cache);
		// TRG
		rootFolder = createRootFolder("", cache);
		// CORR
		rp2rf = createCorr(cFactory.createPackage2Folder(), rootPackage, rootFolder);
		cache.corrs.add(rp2rf);
		// MARKER
		Package2Folder__Marker marker = cFactory.createPackage2Folder__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__pr(sContainer);
		marker.setCONTEXT__CORR__pr2dc(pr2dc);
		marker.setCONTEXT__TRG__dc(tContainer);
		marker.setCREATE__SRC__p(rootPackage);
		marker.setCREATE__CORR__p2f(rp2rf);
		marker.setCREATE__TRG__f(rootFolder);
		
		addCacheContent(cache);
		((InternalEList<Package>) sContainer.getRootPackages()).add(rootPackage);
		((InternalEList<Folder>) tContainer.getFolders()).add(rootFolder);
	}

	private void createTypeAndDocHierarchies() {
		IntStream.range(0, parameters.num_of_root_types).parallel().forEach(this::createRootTypeAndDoc);
		((InternalEList<Type>) rootPackage.getTypes()).addAllUnique(allTypes);
		((InternalEList<Doc>) rootFolder.getDocs()).addAllUnique(allDocs);
		
		if (corr instanceof SmartEMFResource) {
			corr.getContents().addAll(allCorrs);
			protocol.getContents().addAll(allMarkers);
		} else {
			((InternalEList<EObject>) corr.getContents()).addAllUnique(allCorrs);
			((InternalEList<EObject>) protocol.getContents()).addAllUnique(allMarkers);
		}
	}

	private void createRootTypeAndDoc(int index) {
		Collection<Type> types = new LinkedList<>();
		LinkedList<Doc> docs = new LinkedList<>();
		BenchCache cache = new BenchCache();
		
		String postfix = SEP + index;

		// SRC
		Type t = createType(postfix, false, rootPackage, cache);
		rootTypes.add(t);
		types.add(t);
		// TRG
		Doc d = createDoc(postfix, rootFolder, cache);
		docs.add(d);
		// CORR
		Type2Doc t2d = createCorr(cFactory.createType2Doc(), t, d, cache);
		// MARKER
		Type2Doc__Marker marker = cFactory.createType2Doc__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__p(rootPackage);
		marker.setCONTEXT__CORR__p2f(rp2rf);
		marker.setCONTEXT__TRG__f(rootFolder);
		marker.setCREATE__SRC__t(t);
		marker.setCREATE__CORR__t2d(t2d);
		marker.setCREATE__TRG__d(d);

		createMethodsAndEntries(t, d, postfix, cache);
		createFieldsAndEntries(t, d, postfix, cache);

		createTypeAndDocHierarchy(t, d, 1, postfix, cache);
		
		addCacheContent(cache);
		allEntries.addAll(cache.name2entry.values());
		name2doc.putAll(cache.name2doc);
		allTypes.addAll(types);
		allDocs.addAll(docs);
	}

	private void createTypeAndDocHierarchy(Type rootT, Doc rootD, int currentDepth, String oldPostfix, BenchCache cache) {
		if (currentDepth >= parameters.inheritance_depth)
			return;

		for (int i = 0; i < parameters.horizontal_inheritance_scale; i++)
			createTypeAndDocInheritance(rootT, rootD, currentDepth, oldPostfix, i, cache);
	}

	private void createTypeAndDocInheritance(Type superT, Doc superD, int currentDepth, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Type t = createType(postfix, false, rootPackage, cache);
		createTypeInheritance(superT, t);
		// TRG
		Doc d = createDoc(postfix, rootFolder, cache);
		createDocLink(superD, d);
		// CORR
		Type2Doc t2d = createCorr(cFactory.createType2Doc(), t, d, cache);
		// MARKER
		ExtendingType2Doc__Marker marker = cFactory.createExtendingType2Doc__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__p(rootPackage);
		marker.setCONTEXT__CORR__p2f((Package2Folder) rp2rf);
		marker.setCONTEXT__TRG__f(rootFolder);
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
			createTypeAndDocHierarchy(t, d, currentDepth + 1, postfix, cache);
			break;
		case VERTICAL:
			if (index == 0)
				createTypeAndDocHierarchy(t, d, currentDepth + 1, postfix, cache);
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
		Method m = createMethod(postfix, t, cache);
		// TRG
		Entry e = createEntry(postfix, EntryType.METHOD, d, cache);
		// CORR
		Method2Entry m2e = createCorr(cFactory.createMethod2Entry(), m, e, cache);
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
		createJavaDocsAndAnnotations(m, e, postfix, cache);
	}

	private void createFieldsAndEntries(Type t, Doc d, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_fields; i++)
			createFieldAndEntry(t, d, oldPostfix, i, cache);
	}

	private void createFieldAndEntry(Type t, Doc d, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		Field f = createField(postfix, t, cache);
		// TRG
		Entry e = createEntry(postfix, EntryType.FIELD, d, cache);
		// CORR
		Field2Entry f2e = createCorr(cFactory.createField2Entry(), f, e, cache);
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
		Parameter p = createParameter(postfix, m, cache);
		// CORR
		Param2Entry p2e = createCorr(cFactory.createParam2Entry(), p, e, cache);
		// MARKER
		Param2Entry__Marker marker = cFactory.createParam2Entry__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__SRC__m(m);
		marker.setCONTEXT__CORR__m2e((Method2Entry) cache.src2corr.get(m));
		marker.setCONTEXT__TRG__e(e);
		marker.setCREATE__SRC__p(p);
		marker.setCREATE__CORR__p2e(p2e);
	}

	private void createJavaDocsAndAnnotations(Method m, Entry e, String oldPostfix, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_javadocs; i++)
			createJavaDocAndAnnotation(m, e, oldPostfix, i, cache);
	}

	private void createJavaDocAndAnnotation(Method m, Entry e, String oldPostfix, int index, BenchCache cache) {
		String postfix = oldPostfix + SEP + index;

		// SRC
		JavaDoc jd = createJavaDoc(postfix, m, cache);
		// TRG
		Annotation a = createAnnotation(postfix, e, cache);
		// CORR
		JDoc2Annotation jd2a = createCorr(cFactory.createJDoc2Annotation(), jd, a, cache);
		// MARKER
		JDoc2Annotation__Marker marker = cFactory.createJDoc2Annotation__Marker();
		marker.setCONTEXT__SRC__m(m);
		marker.setCONTEXT__CORR__m2e((Method2Entry) cache.src2corr.get(m));
		marker.setCONTEXT__TRG__e(e);
		marker.setCREATE__SRC__j(jd);
		marker.setCREATE__CORR__j2a(jd2a);
		marker.setCREATE__TRG__a(a);
		protocol.getContents().add(marker);
	}

	private void createGlossaryEntriesAndLinks() {
		BenchCache cache = new BenchCache();
		
		for (int i = 0; i < parameters.num_of_glossar_entries; i++)
			createGlossaryEntry(i, cache);
		
		for (Entry e : allEntries)
			createGlossaryLinks(e, cache);
		
		addCacheContent(cache);
	}

	private void createGlossaryEntry(int index, BenchCache cache) {
		String postfix = SEP + index;

		// TRG
		GlossaryEntry ge = createGlossaryEntry(postfix, cache);
		// MARKER
		GlossaryEntry__Marker marker = cFactory.createGlossaryEntry__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__TRG__g(tContainer.getGlossary());
		marker.setCREATE__TRG__ge(ge);
	}

	private void createGlossaryLinks(Entry e, BenchCache cache) {
		for (int i = 0; i < parameters.num_of_glossar_links_per_entry; i++)
			createGlossaryLink(e, i, cache);
	}

	private void createGlossaryLink(Entry e, int index, BenchCache cache) {
		String glossaryEntryName = "GlossaryEntry" + SEP + (glossaryLinkCounter % parameters.num_of_glossar_entries);
		GlossaryEntry ge = cache.name2glossaryEntry.get(glossaryEntryName);
		glossaryLinkCounter++;
		

		// TRG
		createGlossaryLink(e, ge);
		// MARKER
		GlossaryLink__Marker marker = cFactory.createGlossaryLink__Marker();
		cache.markers.add(marker);
		marker.setCONTEXT__TRG__e(e);
		marker.setCONTEXT__TRG__ge(ge);
	}

	//// DELTA ////

	@Override
	protected void genDelta() {
		List<BiConsumer<Type, Boolean>> deltaFunctions = fillDeltasFunctionContainer();

		int num_of_conflicts = (int) (parameters.num_of_changes * parameters.conflict_ratio);
		for (int i = 0; i < parameters.num_of_changes; i++) {
			int deltaIndex = i % deltaFunctions.size();
			boolean generateConflict = i <= num_of_conflicts;
			deltaFunctions.get(deltaIndex).accept(rootTypes.get(i), generateConflict);
		}
	}

	private List<BiConsumer<Type, Boolean>> fillDeltasFunctionContainer() {
		List<BiConsumer<Type, Boolean>> deltaFunctions = new LinkedList<>();

		switch (parameters.scaleOrientation) {
		case HORIZONTAL:
			for (ConflictType conflictType : parameters.conflict_types) {
				switch (conflictType) {
				case ATTR:
					deltaFunctions.add(this::createAttributeConflict);
					break;
				case CONTR_MOVE:
					deltaFunctions.add(this::createContradictingMoveConflict);
					break;
				case PRES_DEL:
					deltaFunctions.add(this::createDeletePreserveConflict_Horizontal);
					break;
				default:
					throw new IllegalArgumentException("No support for conflict type '" + conflictType + "' in horizontal scale orientation!");
				}
			}

			if (parameters.num_of_changes > parameters.num_of_root_types)
				throw new RuntimeException("Too many changes for this model");
			break;
		case VERTICAL:
			for (ConflictType conflictType : parameters.conflict_types) {
				switch (conflictType) {
				case PRES_DEL:
					deltaFunctions.add(this::createDeletePreserveConflict_Vertical);
					break;
				default:
					throw new IllegalArgumentException("No support for conflict type '" + conflictType + "' in vertical scale orientation!");
				}
			}

			if (parameters.num_of_changes > parameters.inheritance_depth)
				throw new RuntimeException("Too many changes for this model");
			break;
		}

		return deltaFunctions;
	}

	private void createDeletePreserveConflict_Horizontal(Type t, boolean generateConflict) {
		Delta delta = createDelta(false, true);

		Doc d = name2doc.get(t.getName());
		String newRootName = "DELETE_PRESERVE_" + t.getName();

		createAttrDelta(t, sPackage.getNamedElement_Name(), newRootName, delta);
		createAttrDelta(d, tPackage.getNamedElement_Name(), newRootName, delta);

		Type subT = t.getExtendedBy().get(0);
		Doc subD = d.getSubDocs().get(0);

		deleteType(subT, delta);

		if (generateConflict) {
			if (!subD.getSubDocs().isEmpty())
				subD = subD.getSubDocs().get(0);

			Entry newE = createEntry(t.getName().substring(4) + "_new_method", EntryType.METHOD, null, new BenchCache());

			createObject(newE, delta);
			createLink(subD, newE, tPackage.getDoc_Entries(), delta);
		}
	}

	private void createDeletePreserveConflict_Vertical(Type t, boolean generateConflict) {
		Delta delta = createDelta(false, true);

		Doc d = name2doc.get(t.getName());
		String newRootName = "DELETE_PRESERVE_" + t.getName();

		createAttrDelta(t, sPackage.getNamedElement_Name(), newRootName, delta);
		createAttrDelta(d, tPackage.getNamedElement_Name(), newRootName, delta);

		Type subT = t.getExtendedBy().get(0);
		Doc subD = d.getSubDocs().get(0);

		// go to last Type in hierarchy
		while (!subT.getExtendedBy().isEmpty()) {
			subT = subT.getExtendedBy().get(0);
			subD = d.getSubDocs().get(0);
		}

		// traverse backward and increase conflict size
		for (int i = 1; i < parameters.num_of_changes; i++)
			subT = subT.getInheritsFrom().get(0);

		deleteType(subT, delta);

		Entry newE = createEntry(t.getName().substring(4) + "_new_method", EntryType.METHOD, null, new BenchCache());

		createObject(newE, delta);
		createLink(subD, newE, tPackage.getDoc_Entries(), delta);
	}

	private void createAttributeConflict(Type t, boolean generateConflict) {
		Delta delta = createDelta(false, true);

		Doc d = name2doc.get(t.getName());
		String newRootName = "ATTR_CONFLICT_" + t.getName();

		createAttrDelta(t, sPackage.getNamedElement_Name(), newRootName + "_a", delta);
		if (generateConflict)
			createAttrDelta(d, tPackage.getNamedElement_Name(), newRootName + "_b", delta);
	}

	private void createContradictingMoveConflict(Type t, boolean generateConflict) {
		Delta delta = createDelta(false, true);

		Doc d = name2doc.get(t.getName());
		String newRootName = "MOVE_CONFLICT_" + t.getName();

		createAttrDelta(t, sPackage.getNamedElement_Name(), newRootName, delta);
		createAttrDelta(d, tPackage.getNamedElement_Name(), newRootName, delta);

		Doc subD1 = d.getSubDocs().get(0);
		Doc subD2 = d.getSubDocs().get(1);

		deleteLink(d, subD1, tPackage.getDoc_SubDocs(), delta);
		createLink(subD2, subD1, tPackage.getDoc_SuperDocs(), delta);

		if (generateConflict) {
			Type subT1 = t.getExtendedBy().get(0);
			Type subT3 = t.getExtendedBy().get(2);

			deleteLink(t, subT1, sPackage.getType_ExtendedBy(), delta);
			createLink(subT3, subT1, sPackage.getType_ExtendedBy(), delta);
		}
	}

}
