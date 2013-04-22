/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * @author dmyersturnbull
 */
package org.structnetalign.weight;

import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.structure.scop.ScopCategory;
import org.biojava.bio.structure.scop.ScopDatabase;
import org.biojava.bio.structure.scop.ScopDomain;
import org.biojava.bio.structure.scop.ScopFactory;
import org.structnetalign.util.IdentifierMapping;


public class ScopRelationWeight implements RelationWeight {

	private Map<ScopCategory,Double> weights;
	
	public static final Map<ScopCategory,Double> DEFAULT_WEIGHTS = new HashMap<ScopCategory,Double>();
	static {
		DEFAULT_WEIGHTS.put(ScopCategory.Class, 0.0);
		DEFAULT_WEIGHTS.put(ScopCategory.Fold, 0.1);
		DEFAULT_WEIGHTS.put(ScopCategory.Superfamily, 0.6);
		DEFAULT_WEIGHTS.put(ScopCategory.Family, 1.0);
		DEFAULT_WEIGHTS.put(ScopCategory.Domain, 1.4);
		DEFAULT_WEIGHTS.put(ScopCategory.Px, 1.5);
		DEFAULT_WEIGHTS.put(ScopCategory.Species, 1.5);
	}
	
	public ScopRelationWeight(Map<ScopCategory,Double> weights) {
		this.weights = weights;
	}
	
	@Override
	public double assignWeight(String uniProtId1, String uniProtId2) {
		final String scopId1 = IdentifierMapping.uniProtToScop(uniProtId1);
		final String scopId2 = IdentifierMapping.uniProtToScop(uniProtId2);
		final ScopDatabase scop = ScopFactory.getSCOP();
		final ScopDomain domain1 = scop.getDomainByScopID(scopId1);
		final ScopDomain domain2 = scop.getDomainByScopID(scopId2);
		for (ScopCategory category : ScopCategory.values()) {
			int categoryId1 = sunIdOfCategory(domain1, category);
			int categoryId2 = sunIdOfCategory(domain2, category);
			if (categoryId1 == categoryId2) return weights.get(category);
		}
		return 0.0;
	}

	private static int sunIdOfCategory(ScopDomain domain, ScopCategory category) {
		switch(category) {
		case Class:
			return domain.getClassId();
		case Fold:
			return domain.getFoldId();
		case Superfamily:
			return domain.getSuperfamilyId();
		case Family:
			return domain.getFamilyId();
		case Domain:
			return domain.getDomainId();
		case Px:
			return domain.getPx();
		case Species:
			return domain.getSpeciesId();
		default:
			throw new IllegalArgumentException("Invalid SCOP category " + category.name());
		}
	}
	
}
