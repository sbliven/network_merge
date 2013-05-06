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
package org.structnetalign.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import psidev.psi.mi.xml.PsimiXmlReader;
import psidev.psi.mi.xml.PsimiXmlReaderException;
import psidev.psi.mi.xml.PsimiXmlVersion;
import psidev.psi.mi.xml.PsimiXmlWriter;
import psidev.psi.mi.xml.PsimiXmlWriterException;
import psidev.psi.mi.xml.model.Entry;
import psidev.psi.mi.xml.model.EntrySet;
import psidev.psi.mi.xml.model.Interaction;
import psidev.psi.mi.xml.model.Participant;

public class NetworkUtils {

	static final Logger logger = Logger.getLogger(NetworkUtils.class.getName());

	public static final String NEWLINE;
	private static final PsimiXmlVersion XML_VERSION = PsimiXmlVersion.VERSION_254;
	static {
		NEWLINE = System.getProperty("line.separator");
	}

	public static NavigableSet<Integer> getVertexIds(Interaction interaction) {
		Collection<Participant> participants = interaction.getParticipants();
		if (participants.size() != 2) throw new IllegalArgumentException(
				"Cannot handle interactions involving more than 2 participants");
		NavigableSet<Integer> set = new TreeSet<>();
		for (Participant participant : participants) {
			int id = participant.getInteractor().getId();
			set.add(id);
		}
		return set;
	}

	public static EntrySet readNetwork(File file) {
		PsimiXmlReader reader = new PsimiXmlReader(XML_VERSION);
		EntrySet entrySet;
		try {
			entrySet = reader.read(file);
		} catch (PsimiXmlReaderException e) {
			throw new RuntimeException("Couldn't parse input file " + file.getPath(), e);
		}
		return entrySet;
	}

	public static EntrySet readNetwork(String string) {
		return readNetwork(new File(string));
	}

	public static String repeat(String s, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	public static Entry skeletonClone(Entry entry) {
		Entry myEntry = new Entry();
		myEntry.setSource(entry.getSource());
		myEntry.getAttributes().addAll(entry.getAttributes());
		myEntry.getAvailabilities().addAll(entry.getAvailabilities());
		myEntry.getExperiments().addAll(entry.getExperiments());
		return myEntry;
	}

	public static EntrySet skeletonClone(EntrySet entrySet) {
		EntrySet myEntrySet = new EntrySet();
		myEntrySet.setVersion(entrySet.getVersion());
		myEntrySet.setMinorVersion(entrySet.getMinorVersion());
		myEntrySet.setLevel(entrySet.getLevel());
		return myEntrySet;
	}

	/**
	 * Converts space indentation to tab indentation, assuming no lines have trailing whitespace.
	 * 
	 * @param input
	 * @param output
	 * @param nSpaces
	 * @throws IOException
	 */
	public static void spacesToTabs(File input, File output, int nSpaces) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter pw = new PrintWriter(output);
		String line = "";
		while ((line = br.readLine()) != null) {
			String trimmed = line.trim();
			int indent = (int) ((float) (line.length() - trimmed.length()) / (float) nSpaces);
			pw.println(repeat("\t", indent) + trimmed);
		}
		br.close();
		pw.close();
	}

	public static String spacesToTabs(String input, int nSpaces) throws IOException {
		StringBuilder sb = new StringBuilder();
		String[] lines = input.split(NEWLINE);
		for (String line : lines) {
			String trimmed = line.trim();
			int indent = (int) ((float) (line.length() - trimmed.length()) / (float) nSpaces);
			sb.append(repeat("\t", indent) + trimmed + NEWLINE);
		}
		return sb.toString();
	}

	public static void writeNetwork(EntrySet entrySet, File file) {

		PsimiXmlWriter psimiXmlWriter = new PsimiXmlWriter(XML_VERSION);
		try {
			psimiXmlWriter.write(entrySet, file);
		} catch (PsimiXmlWriterException e) {
			throw new RuntimeException("Couldn't write XML to " + file.getPath(), e);
		}
		
		// to reduce file size
		File tmp = new File(file + ".spaces.xml.tmp");
		try {
			FileUtils.moveFile(file, tmp);
			spacesToTabs(tmp, file, 4);
			tmp.delete();
		} catch (IOException e) {
			logger.warn("Could not convert spaces in " + file.getPath() + " to tabs", e);
		}
		
	}

	public static void writeNetwork(EntrySet entrySet, String file) {
		writeNetwork(entrySet, new File(file));
	}

}
