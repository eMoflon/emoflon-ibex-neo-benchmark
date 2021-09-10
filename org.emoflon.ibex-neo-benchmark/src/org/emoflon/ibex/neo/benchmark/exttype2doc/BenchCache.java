package org.emoflon.ibex.neo.benchmark.exttype2doc;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import ExtDocModel.Annotation;
import ExtDocModel.Doc;
import ExtDocModel.DocContainer;
import ExtDocModel.Entry;
import ExtDocModel.Folder;
import ExtDocModel.GlossaryEntry;
import ExtTypeModel.Field;
import ExtTypeModel.JavaDoc;
import ExtTypeModel.Method;
import ExtTypeModel.Package;
import ExtTypeModel.Parameter;
import ExtTypeModel.Project;
import ExtTypeModel.Type;

public class BenchCache {
	public int numOfElements = 0;
	public Collection<EObject> corrs = new LinkedList<>();
	public Collection<EObject> markers = new LinkedList<>();
	
	//// SRC ////
	protected Map<String, Package> name2package = new HashMap<>();
	protected Map<String, Type> name2type = new HashMap<>();
	protected Map<String, Method> name2method = new HashMap<>();
	protected Map<String, Field> name2field = new HashMap<>();
	protected Map<String, Parameter> name2param = new HashMap<>();
	protected Map<String, JavaDoc> name2javadoc = new HashMap<>();

	//// CORR ////
	public Map<EObject, EObject> src2corr = new HashMap<EObject, EObject>();

	//// TRG ////
	protected Map<String, Folder> name2folder = new HashMap<>();
	protected Map<String, Doc> name2doc = new HashMap<>();
	protected Map<String, Entry> name2entry = new HashMap<>();
	protected Map<String, Annotation> name2annotation = new HashMap<>();
	protected Map<String, GlossaryEntry> name2glossaryEntry = new HashMap<>();
}
