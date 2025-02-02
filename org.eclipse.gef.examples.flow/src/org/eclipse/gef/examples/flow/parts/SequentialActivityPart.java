/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.examples.flow.figures.SequentialActivityFigure;

/**
 * @author hudsonr Created on Jul 18, 2003
 */
public class SequentialActivityPart extends StructuredActivityPart {

	/**
	 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new SequentialActivityFigure();
	}

	/**
	 * @see ActivityPart#contributeEdgesToGraph(org.eclipse.graph.CompoundDirectedGraph,
	 *      java.util.Map)
	 */
	@Override
	public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map) {
		super.contributeEdgesToGraph(graph, map);
		Node prev = null;
		for (ActivityPart a : getChildren()) {
			Node node = (Node) map.get(a);
			if (prev != null) {
				Edge e = new Edge(prev, node);
				e.weight = 50;
				graph.edges.add(e);
			}
			prev = node;
		}
	}

	/**
	 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#getAnchorOffset()
	 */
	@Override
	int getAnchorOffset() {
		return 15;
	}

}
