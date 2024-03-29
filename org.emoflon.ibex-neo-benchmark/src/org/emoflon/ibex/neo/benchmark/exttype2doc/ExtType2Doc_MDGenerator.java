package org.emoflon.ibex.neo.benchmark.exttype2doc;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.ibex.neo.benchmark.util.BenchParameters;

import ExtDocModel.Annotation;
import ExtDocModel.Doc;
import ExtDocModel.DocContainer;
import ExtDocModel.Entry;
import ExtDocModel.EntryType;
import ExtDocModel.ExtDocModelFactory;
import ExtDocModel.ExtDocModelPackage;
import ExtDocModel.Folder;
import ExtDocModel.Glossary;
import ExtDocModel.GlossaryEntry;
import ExtTypeModel.ExtTypeModelFactory;
import ExtTypeModel.ExtTypeModelPackage;
import ExtTypeModel.Field;
import ExtTypeModel.JavaDoc;
import ExtTypeModel.Method;
import ExtTypeModel.Package;
import ExtTypeModel.Parameter;
import ExtTypeModel.Project;
import ExtTypeModel.Type;
import delta.Delta;

public abstract class ExtType2Doc_MDGenerator<CF extends EFactory, BP extends BenchParameters>
		extends ModelAndDeltaGenerator<CF, ExtTypeModelFactory, ExtTypeModelPackage, ExtDocModelFactory, ExtDocModelPackage, BP> {

	protected Project sContainer;
	protected DocContainer tContainer;

	public ExtType2Doc_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected ExtTypeModelPackage srcPackageInstance() {
		return ExtTypeModelPackage.eINSTANCE;
	}

	@Override
	protected ExtDocModelPackage trgPackageInstance() {
		return ExtDocModelPackage.eINSTANCE;
	}

	@Override
	protected void clearAll() {
		sContainer = null;

		tContainer = null;
	}

	//// MODEL ////

	protected void createProject() {
		sContainer = sFactory.createProject();
		source.getContents().add(sContainer);
		addNumOfElements(1);
	}

	protected void createDocContainer() {
		tContainer = tFactory.createDocContainer();
		target.getContents().add(tContainer);
		addNumOfElements(1);
	}

	protected Glossary createGlossary() {
		Glossary g = tFactory.createGlossary();
		tContainer.setGlossary(g);
		addNumOfElements(1);
		return g;
	}

	protected Package createRootPackage(String postfix, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Package p = sFactory.createPackage();
		p.setName("Package" + postfix);
		if (addToParent)
			((InternalEList<Package>) sContainer.getRootPackages()).add(p);
		cache.name2package.put(p.getName(), p);
		cache.numOfElements++;
		return p;
	}

	protected Package createPackage(String postfix, Package superPackage, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Package p = sFactory.createPackage();
		p.setName("Package" + postfix);
		if (addToParent)
			((InternalEList<Package>) superPackage.getSubPackages()).addUnique(p);
		cache.name2package.put(p.getName(), p);
		cache.numOfElements++;
		return p;
	}

	protected Folder createRootFolder(String postfix, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Folder f = tFactory.createFolder();
		f.setName("Package" + postfix);
		if (addToParent)
			((InternalEList<Folder>) tContainer.getFolders()).add(f);
		cache.name2folder.put(f.getName(), f);
		cache.numOfElements++;
		return f;
	}

	protected Folder createFolder(String postfix, Folder superFolder, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Folder f = tFactory.createFolder();
		f.setName("Package" + postfix);
		if (addToParent)
			((InternalEList<Folder>) superFolder.getSubFolder()).addUnique(f);
		cache.name2folder.put(f.getName(), f);
		cache.numOfElements++;
		return f;
	}

	protected Type createType(String postfix, boolean isInterface, Package p, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Type t = sFactory.createType();
		t.setName("Type" + postfix);
		t.setInterface(isInterface);
		if (addToParent)
			((InternalEList<Type>) p.getTypes()).addUnique(t);
		cache.name2type.put(t.getName(), t);
		cache.numOfElements++;
		return t;
	}

	protected Doc createDoc(String postfix, Folder f, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Doc d = tFactory.createDoc();
		d.setName("Type" + postfix);
		if (addToParent)
			((InternalEList<Doc>) f.getDocs()).addUnique(d);
		cache.name2doc.put(d.getName(), d);
		cache.numOfElements++;
		return d;
	}

	protected void createTypeInheritance(Type extendee, Type extender) {
		((InternalEList<Type>) extendee.getExtendedBy()).addUnique(extender);
	}

	protected void createDocLink(Doc superDoc, Doc subDoc) {
		((InternalEList<Doc>) superDoc.getSubDocs()).addUnique(subDoc);
	}

	protected Method createMethod(String postfix, Type t, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Method m = sFactory.createMethod();
		m.setName("Method" + postfix);
		if (addToParent)
			((InternalEList<Method>) t.getMethods()).addUnique(m);
		cache.name2method.put(m.getName(), m);
		cache.numOfElements++;
		return m;
	}

	protected Field createField(String postfix, Type t, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Field f = sFactory.createField();
		f.setName("Field" + postfix);
		if (addToParent)
			((InternalEList<Field>) t.getFields()).addUnique(f);
		cache.name2field.put(f.getName(), f);
		cache.numOfElements++;
		return f;
	}

	protected Entry createEntry(String postfix, EntryType entryType, Doc d, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Entry e = tFactory.createEntry();
		String name = entryType == EntryType.METHOD ? "Method" : "Field";
		e.setName(name + postfix);
		e.setType(entryType);
		if (addToParent)
			((InternalEList<Entry>) d.getEntries()).addUnique(e);
		cache.name2entry.put(e.getName(), e);
		cache.numOfElements++;
		return e;
	}

	protected Parameter createParameter(String postfix, Method m, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Parameter p = sFactory.createParameter();
		p.setName("Param" + postfix);
		if (addToParent)
			((InternalEList<Parameter>) m.getParams()).addUnique(p);
		cache.name2param.put(p.getName(), p);
		cache.numOfElements++;
		return p;
	}

	protected JavaDoc createJavaDoc(String postfix, Method m, ExtType2Doc_BenchCache cache, boolean addToParent) {
		JavaDoc jd = sFactory.createJavaDoc();
		jd.setComment("JavaDoc" + postfix);
		if (addToParent)
			((InternalEList<JavaDoc>) m.getDocs()).addUnique(jd);
		cache.name2javadoc.put(jd.getComment(), jd);
		cache.numOfElements++;
		return jd;
	}

	protected Annotation createAnnotation(String postfix, Entry e, ExtType2Doc_BenchCache cache, boolean addToParent) {
		Annotation a = tFactory.createAnnotation();
		a.setValue("JavaDoc" + postfix);
		if (addToParent)
			((InternalEList<Annotation>) e.getAnnotations()).addUnique(a);
		cache.name2annotation.put(a.getValue(), a);
		cache.numOfElements++;
		return a;
	}

	protected GlossaryEntry createGlossaryEntry(String postfix, ExtType2Doc_BenchCache cache, boolean addToParent) {
		GlossaryEntry ge = tFactory.createGlossaryEntry();
		ge.setName("GlossaryEntry" + postfix);
		ge.setGlossary(tContainer.getGlossary());
		if (addToParent)
			((InternalEList<GlossaryEntry>) tContainer.getGlossary().getEntries()).addUnique(ge);
		cache.name2glossaryEntry.put(ge.getName(), ge);
		cache.numOfElements++;
		return ge;
	}

	protected void createGlossaryLink(Entry entry, GlossaryEntry glossaryEntry) {
		((InternalEList<GlossaryEntry>) entry.getGlossaryEntries()).addUnique(glossaryEntry);
	}

	//// DELTA ////

	protected void deleteType(Type type, Delta delta) {
		type.getMethods().forEach(m -> deleteMethod(m, delta));
		type.getFields().forEach(f -> deleteField(f, delta));

		deleteObject(type, delta);
		deleteLink(type.getPackage(), type, sPackage.getPackage_Types(), delta);
		for (Type subT : type.getExtendedBy()) {
			deleteLink(type, subT, sPackage.getType_ExtendedBy(), delta);
			deleteType(subT, delta);
		}
	}

	protected void deleteMethod(Method method, Delta delta) {
		method.getParams().forEach(p -> deleteParameter(p, delta));
		method.getDocs().forEach(jd -> deleteJavaDoc(jd, delta));

		deleteObject(method, delta);
		deleteLink(method.getType(), method, sPackage.getType_Methods(), delta);
	}

	protected void deleteField(Field field, Delta delta) {
		deleteObject(field, delta);
		deleteLink(field.getType(), field, sPackage.getType_Fields(), delta);
	}

	protected void deleteParameter(Parameter parameter, Delta delta) {
		deleteObject(parameter, delta);
		deleteLink(parameter.getMethod(), parameter, sPackage.getMethod_Params(), delta);
	}

	protected void deleteJavaDoc(JavaDoc javaDoc, Delta delta) {
		deleteObject(javaDoc, delta);
		deleteLink(javaDoc.getMethod(), javaDoc, sPackage.getMethod_Docs(), delta);
	}

}
