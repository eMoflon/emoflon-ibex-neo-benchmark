package org.emoflon.ibex.neo.benchmark;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.emoflon.delta.validation.InvalidDeltaException;
import org.emoflon.ibex.neo.benchmark.util.BenchEntry;
import org.emoflon.ibex.neo.benchmark.util.BenchParameters;
import org.emoflon.ibex.tgg.operational.strategies.OperationalStrategy;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;
import org.emoflon.smartemf.persistence.SmartEMFResourceFactoryImpl;

import delta.impl.DeltaPackageImpl;

public abstract class IbexBench<OpStrat extends OperationalStrategy, BenchParams extends BenchParameters> {

	protected final String projectName;
	protected final String genModelFolder = "gen-models";

	protected final String filename_src = "/src.xmi";
	protected final String filename_trg = "/trg.xmi";
	protected final String filename_corr = "/corr.xmi";
	protected final String filename_protocol = "/protocol.xmi";
	protected final String filename_delta = "/delta.xmi";

	protected final String workspacePath = "../";
	protected final URI base = URI.createPlatformResourceURI("/", true);
	protected ResourceSet rs;

	protected Resource source;
	protected Resource target;
	protected Resource corr;
	protected Resource protocol;
	protected Resource delta;

	protected int numOfElements = -1;

	protected BenchParams parameters;

	public IbexBench(String projectName, BenchParams parameters) {
		this.projectName = projectName;
		this.parameters = parameters;
	}

	public BenchEntry<BenchParams> genAndBench(boolean saveTransformedModels) {
		try {
			OpStrat opStrat = initStub(new TGGResourceHandler() {
				@Override
				public void loadModels() throws IOException {
					source = createResource(options.project.path() + "/instances" + filename_src);
					target = createResource(options.project.path() + "/instances" + filename_trg);
					corr = createResource(options.project.path() + "/instances" + filename_trg);
					protocol = createResource(options.project.path() + "/instances" + filename_protocol);

					DeltaPackageImpl.init();
					delta = createResource(options.project.path() + "/instances" + filename_delta);
				}
			});
			initResources(opStrat);

			ModelAndDeltaGenerator<?, ?, ?, ?, ?, BenchParams> mdGenerator = initModelAndDeltaGenerator(source, target, corr, protocol, delta);
			mdGenerator.gen(parameters);
			this.numOfElements = mdGenerator.getNumOfElements();

			return applyDeltaAndRun(opStrat, saveTransformedModels);
		} catch (IOException | InvalidDeltaException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void genAndStore() {
		try {
			initResourceSet();
			initModels(projectName + "/" + genModelFolder + "/" + parameters.toString());

			ModelAndDeltaGenerator<?, ?, ?, ?, ?, BenchParams> mdGenerator = initModelAndDeltaGenerator(source, target, corr, protocol, delta);
			mdGenerator.gen(parameters);
			this.numOfElements = mdGenerator.getNumOfElements();

			// TODO persist numOfElements!

			saveModels();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BenchEntry<BenchParams> loadAndBench(boolean saveTransformedModels) {
		String workspaceRelativeFolder = projectName + "/" + genModelFolder + "/" + parameters.toString();
		try {
			OpStrat opStrat = initStub(new TGGResourceHandler() {
				@Override
				public void loadModels() throws IOException {
					source = loadResource(workspaceRelativeFolder + filename_src);
					target = loadResource(workspaceRelativeFolder + filename_trg);
					corr = loadResource(workspaceRelativeFolder + filename_corr);
					protocol = loadResource(workspaceRelativeFolder + filename_protocol);

					DeltaPackageImpl.init();
					delta = loadResource(workspaceRelativeFolder + filename_delta);

					changeURI(source, "/instances" + filename_src);
					changeURI(target, "/instances" + filename_trg);
					changeURI(corr, "/instances" + filename_corr);
					changeURI(protocol, "/instances" + filename_protocol);

					EcoreUtil.resolveAll(rs);
				}

				private void changeURI(Resource r, String path) {
					URI uri = URI.createURI(options.project.path() + path);
					r.setURI(uri.resolve(base));
				}
			});
			initResources(opStrat);

			return applyDeltaAndRun(opStrat, saveTransformedModels);
		} catch (IOException | InvalidDeltaException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract OpStrat initStub(TGGResourceHandler resourceHandler) throws IOException;

	protected abstract ModelAndDeltaGenerator<?, ?, ?, ?, ?, BenchParams> initModelAndDeltaGenerator(Resource s, Resource t, Resource c, Resource p,
			Resource d);

	protected abstract BenchEntry<BenchParams> applyDeltaAndRun(OpStrat opStrat, boolean saveTransformedModels)
			throws IOException, InvalidDeltaException;

	private void initResources(OpStrat opStrat) {
		source = opStrat.getOptions().resourceHandler().getSourceResource();
		target = opStrat.getOptions().resourceHandler().getTargetResource();
		corr = opStrat.getOptions().resourceHandler().getCorrResource();
		protocol = opStrat.getOptions().resourceHandler().getProtocolResource();
	}

	private void initResourceSet() {
		rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap() //
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new SmartEMFResourceFactoryImpl(workspacePath));
		try {
			rs.getURIConverter().getURIMap().put(URI.createPlatformResourceURI("/", true),
					URI.createFileURI(new File(workspacePath).getCanonicalPath() + File.separator));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Resource createResource(String workspaceRelativePath) {
		URI uri = URI.createURI(workspaceRelativePath);
		Resource res = rs.createResource(uri.resolve(base), ContentHandler.UNSPECIFIED_CONTENT_TYPE);
		return res;
	}

	private void initModels(String path) {
		source = createResource(path + filename_src);
		target = createResource(path + filename_trg);
		corr = createResource(path + filename_corr);
		protocol = createResource(path + filename_protocol);
		delta = createResource(path + filename_delta);
	}

	private void saveModels() throws IOException {
		source.save(null);
		target.save(null);
		corr.save(null);
		protocol.save(null);
		delta.save(null);
	}

	protected int calcUsedRAM() {
		Runtime.getRuntime().gc();
		return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	}

	protected double calcSuccessRate() {
		return 1.0;
	}

	public BenchParams getParameters() {
		return parameters;
	}

	public void setParameters(BenchParams parameters) {
		this.parameters = parameters;
	}

}
