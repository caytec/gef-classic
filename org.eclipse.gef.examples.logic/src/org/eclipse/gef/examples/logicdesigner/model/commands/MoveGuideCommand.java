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
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

/**
 * @author Pratik Shah
 */
public class MoveGuideCommand extends Command {

	private int pDelta;
	private LogicGuide guide;

	public MoveGuideCommand(LogicGuide guide, int positionDelta) {
		super(LogicMessages.MoveGuideCommand_Label);
		this.guide = guide;
		pDelta = positionDelta;
	}

	@Override
	public void execute() {
		guide.setPosition(guide.getPosition() + pDelta);
		for (LogicSubpart part : guide.getParts()) {
			Point location = part.getLocation().getCopy();
			if (guide.isHorizontal()) {
				location.y += pDelta;
			} else {
				location.x += pDelta;
			}
			part.setLocation(location);
		}
	}

	@Override
	public void undo() {
		guide.setPosition(guide.getPosition() - pDelta);
		for (LogicSubpart part : guide.getParts()) {
			Point location = part.getLocation().getCopy();
			if (guide.isHorizontal()) {
				location.y -= pDelta;
			} else {
				location.x -= pDelta;
			}
			part.setLocation(location);
		}
	}

}
