package org.emoflon.ibex.neo.benchmark.village2constrplan;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emoflon.ibex.neo.benchmark.ModelAndDeltaGenerator;
import org.emoflon.smartemf.persistence.SmartEMFResource;

import ConstructionPlan.Basement;
import ConstructionPlan.Cellar;
import ConstructionPlan.Component;
import ConstructionPlan.Construction;
import ConstructionPlan.ConstructionPlanFactory;
import ConstructionPlan.ConstructionPlanPackage;
import ConstructionPlan.Plan;
import ConstructionPlan.PlanCollection;
import ConstructionPlan.SaddleRoof;
import Village.House;
import Village.HouseType;
import Village.VillageFactory;
import Village.VillagePackage;
import Village.VillageSquare;
import Village2ConstrPlan.Corner2Constr__Marker;
import Village2ConstrPlan.Cube2Constr__Marker;
import Village2ConstrPlan.House2Constr;
import Village2ConstrPlan.Villa2Constr__Marker;
import Village2ConstrPlan.Village2ConstrPlanFactory;
import Village2ConstrPlan.VillageSquare2PlanCollection;
import Village2ConstrPlan.VillageSquare2PlanCollection__Marker;
import delta.Delta;

public class Village2ConstrPlan_MDGenerator extends
		ModelAndDeltaGenerator<Village2ConstrPlanFactory, VillageFactory, VillagePackage, ConstructionPlanFactory, ConstructionPlanPackage, Village2ConstrPlan_Params> {

	protected VillageSquare sContainer;
	protected PlanCollection tContainer;
	protected VillageSquare2PlanCollection vs2pc;
	protected List<House> rootHouses = Collections.synchronizedList(new LinkedList<>());
	protected List<Plan> plans = Collections.synchronizedList(new LinkedList<>());

	public Village2ConstrPlan_MDGenerator(Resource source, Resource target, Resource corr, Resource protocol, Resource delta) {
		super(source, target, corr, protocol, delta);
	}

	@Override
	protected VillagePackage srcPackageInstance() {
		return VillagePackage.eINSTANCE;
	}

	@Override
	protected ConstructionPlanPackage trgPackageInstance() {
		return ConstructionPlanPackage.eINSTANCE;
	}

	@Override
	protected Village2ConstrPlanFactory corrFactoryInstance() {
		return Village2ConstrPlanFactory.eINSTANCE;
	}

	@Override
	protected void clearAll() {
		sContainer = null;
		tContainer = null;
	}

	//// MODEL ////

	protected void createVillageSquare() {
		sContainer = sFactory.createVillageSquare();
		source.getContents().add(sContainer);
		addNumOfElements(1);
	}

	protected void createPlanCollection() {
		tContainer = tFactory.createPlanCollection();
		target.getContents().add(tContainer);
		addNumOfElements(1);
	}

	protected House createRootHouse(String postfix, Village2ConstrPlan_BenchCache cache, boolean addToParent) {
		House h = sFactory.createHouse();
		h.setType(HouseType.CORNER);
		h.setName("House" + postfix);
		if (addToParent)
			sContainer.getStreetCorner().add(h);
		if (cache != null) {
			cache.name2house.put(h.getName(), h);
			cache.numOfElements++;
		}
		return h;
	}

	protected House createHouse(String postfix, House prevHouse, HouseType type, Village2ConstrPlan_BenchCache cache, boolean addToParent) {
		House h = sFactory.createHouse();
		h.setName("House" + postfix);
		h.setType(type);
		if (addToParent)
			prevHouse.setNextHouse(h);
		if (cache != null) {
			cache.name2house.put(h.getName(), h);
			cache.numOfElements++;
		}
		return h;
	}

	protected Plan createPlan(String postfix, Village2ConstrPlan_BenchCache cache, boolean addToParent) {
		Plan p = tFactory.createPlan();
		p.setName("Plan" + postfix);
		if (addToParent)
			tContainer.getPlans().add(p);
		if (cache != null) {
			cache.name2plan.put(p.getName(), p);
			cache.numOfElements++;
		}
		return p;
	}

	protected Construction createConstruction(String postfix, Plan p, Village2ConstrPlan_BenchCache cache, boolean addToParent) {
		Construction c = tFactory.createConstruction();
		c.setName("House" + postfix);
		if (addToParent)
			p.getConstructions().add(c);
		if (cache != null) {
			cache.name2constr.put(c.getName(), c);
			cache.numOfElements++;
		}
		return c;
	}

	protected Cellar createCellar(Construction c, boolean addToParent) {
		Cellar cl = tFactory.createCellar();
		if (addToParent)
			c.setFirstStep(cl);
		return cl;
	}

	protected Cellar createCellar(Component c, boolean addToParent) {
		Cellar cl = tFactory.createCellar();
		if (addToParent)
			c.setNextStep(cl);
		return cl;
	}

	protected Basement createBasement(Construction c, boolean addToParent) {
		Basement bt = tFactory.createBasement();
		if (addToParent)
			c.setFirstStep(bt);
		return bt;
	}

	protected Basement createBasement(Component c, boolean addToParent) {
		Basement bt = tFactory.createBasement();
		if (addToParent)
			c.setNextStep(bt);
		return bt;
	}

	protected SaddleRoof createSaddleRoof(Construction c, boolean addToParent) {
		SaddleRoof sr = tFactory.createSaddleRoof();
		if (addToParent)
			c.setFirstStep(sr);
		return sr;
	}

	protected SaddleRoof createSaddleRoof(Component c, boolean addToParent) {
		SaddleRoof sr = tFactory.createSaddleRoof();
		if (addToParent)
			c.setNextStep(sr);
		return sr;
	}

	@Override
	protected void genModels() {
		createContainers();
		createStreets();
	}

	protected void createContainers() {
		// SRC
		createVillageSquare();
		// TRG
		createPlanCollection();
		// CORR
		vs2pc = createCorr(cFactory.createVillageSquare2PlanCollection(), sContainer, tContainer);
		allCorrs.add(vs2pc);

		// MARKER
		VillageSquare2PlanCollection__Marker marker = cFactory.createVillageSquare2PlanCollection__Marker();
		allMarkers.add(marker);
		marker.setCREATE__SRC__vs(sContainer);
		marker.setCREATE__CORR__vs2pl(vs2pc);
		marker.setCREATE__TRG__pc(tContainer);
	}

	protected void createStreets() {
		if (parameters.num_of_streets > 1)
			IntStream.range(0, parameters.num_of_streets).parallel().forEach(this::createStreetBeginning);
		else
			createStreetBeginning(0);

		if (corr instanceof SmartEMFResource) {
			sContainer.getStreetCorner().addAll(rootHouses);
			tContainer.getPlans().addAll(plans);
			corr.getContents().addAll(allCorrs);
			protocol.getContents().addAll(allMarkers);
		} else {
			((InternalEList<House>) sContainer.getStreetCorner()).addAllUnique(rootHouses);
			((InternalEList<Plan>) tContainer.getPlans()).addAllUnique(plans);
			((InternalEList<EObject>) corr.getContents()).addAllUnique(allCorrs);
			((InternalEList<EObject>) protocol.getContents()).addAllUnique(allMarkers);
		}
	}

	protected void createStreetBeginning(int index) {
		Village2ConstrPlan_BenchCache cache = new Village2ConstrPlan_BenchCache();

		String postfix = SEP + index;
		int currentDepth = 0;

		// SRC
		House h = createRootHouse(postfix + SEP + currentDepth, cache, false);
		rootHouses.add(h);
		// TRG
		Plan p = createPlan(postfix, cache, false);
		plans.add(p);
		Construction c = createConstruction(postfix + SEP + currentDepth, p, cache, true);
		Basement bt = createBasement(c, true);
		// CORR
		House2Constr h2c = createCorr(cFactory.createHouse2Constr(), h, c, cache);

		// MARKER
		Corner2Constr__Marker marker = cFactory.createCorner2Constr__Marker();
		cache.markers.add(marker);
		marker.setCREATE__SRC__h(h);
		marker.setCREATE__CORR__h2cst(h2c);
		marker.setCREATE__TRG__p(p);
		marker.setCREATE__TRG__cst(c);
		marker.setCREATE__TRG__bt(bt);
		marker.setCONTEXT__SRC__vs(sContainer);
		marker.setCONTEXT__CORR__vs2pc(vs2pc);
		marker.setCONTEXT__TRG__pc(tContainer);

		if (parameters.street_length > 0)
			createCubeAndConstr(h, c, p, currentDepth + 1, postfix, cache);

		addCacheContent(cache);
	}

	protected void createCubeAndConstr(House prevH, Construction prevC, Plan p, int currentDepth, String postfix, Village2ConstrPlan_BenchCache cache) {
		// SRC
		House h = createHouse(postfix + SEP + currentDepth, prevH, HouseType.CUBE, cache, true);
		// TRG
		Construction c = createConstruction(postfix + SEP + currentDepth, p, cache, true);
		Cellar cl = createCellar(c, true);
		Basement bt = createBasement(cl, true);
		// CORR
		House2Constr h2c = createCorr(cFactory.createHouse2Constr(), h, c, cache);

		// MARKER
		Cube2Constr__Marker marker = cFactory.createCube2Constr__Marker();
		cache.markers.add(marker);
		marker.setCREATE__SRC__nh(h);
		marker.setCREATE__CORR__nh2ncst(h2c);
		marker.setCREATE__TRG__ncst(c);
		marker.setCREATE__TRG__cl(cl);
		marker.setCREATE__TRG__bt(bt);
		marker.setCONTEXT__SRC__h(prevH);
		marker.setCONTEXT__CORR__h2cst((House2Constr) cache.src2corr.get(prevH));
		marker.setCONTEXT__TRG__p(p);
		marker.setCONTEXT__TRG__cst(prevC);

		if (parameters.street_length > currentDepth)
			createVillaAndConstr(h, c, p, currentDepth + 1, postfix, cache);
	}

	protected void createVillaAndConstr(House prevH, Construction prevC, Plan p, int currentDepth, String postfix, Village2ConstrPlan_BenchCache cache) {
		// SRC
		House h = createHouse(postfix + SEP + currentDepth, prevH, HouseType.VILLA, cache, true);
		// TRG
		Construction c = createConstruction(postfix + SEP + currentDepth, p, cache, true);
		Basement bt = createBasement(c, true);
		SaddleRoof sr = createSaddleRoof(bt, true);
		// CORR
		House2Constr h2c = createCorr(cFactory.createHouse2Constr(), h, c, cache);

		// MARKER
		Villa2Constr__Marker marker = cFactory.createVilla2Constr__Marker();
		cache.markers.add(marker);
		marker.setCREATE__SRC__nh(h);
		marker.setCREATE__CORR__nh2ncst(h2c);
		marker.setCREATE__TRG__ncst(c);
		marker.setCREATE__TRG__bt(bt);
		marker.setCREATE__TRG__sr(sr);
		marker.setCONTEXT__SRC__h(prevH);
		marker.setCONTEXT__CORR__h2cst((House2Constr) cache.src2corr.get(prevH));
		marker.setCONTEXT__TRG__p(p);
		marker.setCONTEXT__TRG__cst(prevC);

		if (parameters.street_length > currentDepth)
			createCubeAndConstr(h, c, p, currentDepth + 1, postfix, cache);
	}

	//// DELTA ////

	@Override
	protected void genDelta() {
		for (int i = 0; i < parameters.num_of_changes; i++) {
			House h = rootHouses.get(i);

			switch (parameters.delta_type) {
			case ADD_ROOT -> addRoot(h);
			case REMOVE_ROOT -> removeRoot(h);
			case MOVE_ROW -> {
				if (i % 2 == 0 && i + 1 < parameters.num_of_changes)
					moveRow(h, rootHouses.get(i + 1));
			}
			default -> throw new IllegalArgumentException(parameters.delta_type + " is no a supported delta type!");
			}
		}
	}

	protected void addRoot(House h) {
		Delta delta = createDelta(false, true);

		House newRoot = createHouse("_ROOT", null, HouseType.CORNER, null, false);

		createObject(newRoot, delta);
		createLink(sContainer, newRoot, sPackage.getVillageSquare_StreetCorner(), delta);
		createLink(newRoot, h, sPackage.getHouse_NextHouse(), delta);
		createAttrDelta(h, sPackage.getHouse_Type(), HouseType.CUBE, delta);
	}

	protected void removeRoot(House h) {
		Delta delta = createDelta(false, true);

		House nextH = h.getNextHouse();

		deleteObject(h, delta);
		deleteLink(h, nextH, sPackage.getHouse_NextHouse(), delta);
		deleteLink(sContainer, h, sPackage.getVillageSquare_StreetCorner(), delta);
		createLink(sContainer, nextH, sPackage.getVillageSquare_StreetCorner(), delta);
		createAttrDelta(nextH, sPackage.getHouse_Type(), HouseType.CORNER, delta);
	}

	protected void moveRow(House firstH, House secondH) {
		Delta delta = createDelta(false, true);

		House firstNextH = firstH.getNextHouse();
		House secondLastH = secondH;
		while (secondLastH.getNextHouse() != null)
			secondLastH = secondLastH.getNextHouse();

		deleteLink(firstH, firstNextH, sPackage.getHouse_NextHouse(), delta);
		createLink(secondLastH, firstNextH, sPackage.getHouse_NextHouse(), delta);
	}

}
