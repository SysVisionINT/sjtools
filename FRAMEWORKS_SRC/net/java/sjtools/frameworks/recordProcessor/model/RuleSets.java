/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package net.java.sjtools.frameworks.recordProcessor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.RuleSetError;

public class RuleSets implements Serializable {

	private static final long serialVersionUID = -6806742098010681241L;

	private List ruleSets = new ArrayList();

	public void add(RuleSet ruleSet) {
		ruleSets.add(ruleSet);
	}

	public List getRuleSets() {
		return ruleSets;
	}

	public RuleSet getRuleSet(String name) throws RuleSetError {
		RuleSet ruleSet = null;
		boolean found = false;

		for (Iterator iterator = ruleSets.iterator(); iterator.hasNext();) {
			ruleSet = (RuleSet) iterator.next();

			if (ruleSet.getName().equals(name)) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new RuleSetError(name);
		}

		return ruleSet;
	}

}
