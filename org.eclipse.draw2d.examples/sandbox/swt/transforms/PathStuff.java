/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.transforms;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;

import org.eclipse.draw2d.ColorConstants;

public class PathStuff extends AbstractSWTTransform {

	private float angle = 4;

	public static void main(String[] args) {
		new PathStuff().runTransformTest();
	}

	@Override
	protected void performPaint(PaintEvent e) {
		GC gc = e.gc;
		Transform t = new Transform(gc.getDevice());
		t.translate(60, 60);
		t.rotate(angle);
		t.scale(4f, 4f);
		gc.setTransform(t);

		gc.setBackground(ColorConstants.lightBlue);
		gc.fillOval(0, 0, 2, 2);
		gc.setLineWidth(1);
		gc.setBackground(ColorConstants.yellow);
		gc.fillOval(-3, 0, 2, 2);

		gc.drawOval(0, 3, 3, 3);
		gc.drawOval(-4, 3, 3, 3);
		angle += 0.3;
	}

}