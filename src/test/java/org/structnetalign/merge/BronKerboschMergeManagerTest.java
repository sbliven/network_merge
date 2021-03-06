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
 * 
 * @author dmyersturnbull
 */
package org.structnetalign.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.structnetalign.CleverGraph;
import org.structnetalign.util.GraphMLAdaptor;
import org.structnetalign.util.TestUtils;



public class BronKerboschMergeManagerTest {

	private static final String RESOURCE_DIR = "src/test/resources/merge/";
	
	@Test
	public void testTrivial() {
		File homologyInput = new File(RESOURCE_DIR + "trivial_hom.graphml.xml");
		File interactionInput = new File(RESOURCE_DIR + "trivial_int.graphml.xml");
		File homologyOutput = new File(RESOURCE_DIR + "trivial_hom_merged.graphml.xml");
		File interactionOutput = new File(RESOURCE_DIR + "trivial_int_merged.graphml.xml");
		MergeManagerTest.test(homologyInput, interactionInput, homologyOutput, interactionOutput, 6, 5, 3, new BronKerboschMergeManager());
	}

	@Test
	public void testTricky() {
		File homologyInput = new File(RESOURCE_DIR + "tricky_hom.graphml.xml");
		File interactionInput = new File(RESOURCE_DIR + "tricky_int.graphml.xml");
		File homologyOutput = new File(RESOURCE_DIR + "tricky_hom_merged.graphml.xml");
		File interactionOutput = new File(RESOURCE_DIR + "tricky_int_merged.graphml.xml");
		MergeManagerTest.test(homologyInput, interactionInput, homologyOutput, interactionOutput, 17, 23, 12, new BronKerboschMergeManager());
	}
	
	@Test
	public void testOverlapping() {
		File homologyInput = new File(RESOURCE_DIR + "overlapping_hom.graphml.xml");
		File interactionInput = new File(RESOURCE_DIR + "overlapping_int.graphml.xml");
		File homologyOutput = new File(RESOURCE_DIR + "overlapping_hom_merged.graphml.xml");
		File interactionOutput = new File(RESOURCE_DIR + "overlapping_int_merged.graphml.xml");
		MergeManagerTest.test(homologyInput, interactionInput, homologyOutput, interactionOutput, 9, 12, 7, new BronKerboschMergeManager());
	}
	
	@Test
	public void testDifferentSized() {
		File homologyInput = new File(RESOURCE_DIR + "different_sized_hom.graphml.xml");
		File interactionInput = new File(RESOURCE_DIR + "different_sized_int.graphml.xml");
		File homologyOutput = new File(RESOURCE_DIR + "different_sized_hom_merged.graphml.xml");
		File interactionOutput = new File(RESOURCE_DIR + "different_sized_int_merged.graphml.xml");
		MergeManagerTest.test(homologyInput, interactionInput, homologyOutput, interactionOutput, 8, 9, 6, new BronKerboschMergeManager());
	}
	
	@Test
	public void testMultiedged() {
		File homologyInput = new File(RESOURCE_DIR + "multiedge_hom.graphml.xml");
		File interactionInput = new File(RESOURCE_DIR + "multiedge_int.graphml.xml");
		File homologyOutput = new File(RESOURCE_DIR + "multiedge_hom_merged.graphml.xml");
		File interactionOutput = new File(RESOURCE_DIR + "multiedge_int_merged.graphml.xml");
		MergeManagerTest.test(homologyInput, interactionInput, homologyOutput, interactionOutput, 3, 2, 2, new BronKerboschMergeManager());
	}
}
