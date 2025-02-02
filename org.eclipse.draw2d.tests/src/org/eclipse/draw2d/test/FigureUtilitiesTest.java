/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;

import org.junit.Assert;
import org.junit.Test;

public class FigureUtilitiesTest extends Assert {

	@Test
	public void test_findCommonAncestor_happypath() {
		IFigure figureParent = new Figure();
		IFigure figureChild1 = new Figure();
		IFigure figureChild2 = new Figure();
		IFigure figureChild3 = new Figure();

		figureParent.add(figureChild1);
		figureChild1.add(figureChild2);
		figureParent.add(figureChild3);

		IFigure result = FigureUtilities.findCommonAncestor(figureChild2, figureChild3);
		assertTrue(figureParent == result);
	}

	@Test
	public void test_findCommonAncestor_bugzilla130042() {
		IFigure figureParent = new Figure();
		IFigure figureChild = new Figure();
		figureParent.add(figureChild);

		IFigure result = FigureUtilities.findCommonAncestor(figureParent, figureChild);
		assertTrue(figureParent == result);
	}

	@Test
	public void test_findCommonAncestor_check_finds_nearest_ancestor() {
		IFigure figureGrandParent = new Figure();
		IFigure figureParent = new Figure();
		IFigure figureChild1 = new Figure();
		IFigure figureChild2 = new Figure();
		figureGrandParent.add(figureParent);
		figureParent.add(figureChild1);
		figureParent.add(figureChild2);

		IFigure result = FigureUtilities.findCommonAncestor(figureChild1, figureChild2);
		assertTrue(figureParent == result);
	}

	@Test
	public void test_findCommonAncestor_parent_is_common_ancestor() {
		IFigure figureParent = new Figure();
		IFigure figureChild1 = new Figure();
		IFigure figureChild2 = new Figure();
		figureParent.add(figureChild1);
		figureParent.add(figureChild2);

		IFigure result = FigureUtilities.findCommonAncestor(figureChild1, figureChild2);
		assertTrue(figureParent == result);
	}

	@Test
	public void test_findCommonAncestor_orphaned_child() {
		IFigure orphanFigure = new Figure();
		IFigure figureParent = new Figure();
		IFigure figureChild = new Figure();
		figureParent.add(figureChild);

		IFigure result = FigureUtilities.findCommonAncestor(figureChild, orphanFigure);
		assertNull(result);
	}
}
