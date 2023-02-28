package org.emoflon.ibex.neo.benchmark.exttype2doc;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.emoflon.ibex.neo.benchmark.BenchCache;

import ExtDocModel.Annotation;
import ExtDocModel.Doc;
import ExtDocModel.Entry;
import ExtDocModel.Folder;
import ExtDocModel.GlossaryEntry;
import ExtTypeModel.Field;
import ExtTypeModel.JavaDoc;
import ExtTypeModel.Method;
import ExtTypeModel.Package;
import ExtTypeModel.Parameter;
import ExtTypeModel.Type;

public class ExtType2Doc_BenchCache extends BenchCache {
	//// SRC ////
	public Map<String, Package> name2package = new HashMap<>();
	public Map<String, Type> name2type = new HashMap<>();
	public Map<String, Method> name2method = new HashMap<>();
	public Map<String, Field> name2field = new HashMap<>();
	public Map<String, Parameter> name2param = new HashMap<>();
	public Map<String, JavaDoc> name2javadoc = new HashMap<>();
	//// TRG ////
	public Map<String, Folder> name2folder = new HashMap<>();
	public Map<String, Doc> name2doc = new HashMap<>();
	public Map<String, Entry> name2entry = new HashMap<>();
	public Map<String, Annotation> name2annotation = new HashMap<>();
	public Map<String, GlossaryEntry> name2glossaryEntry = new HashMap<>();
}
