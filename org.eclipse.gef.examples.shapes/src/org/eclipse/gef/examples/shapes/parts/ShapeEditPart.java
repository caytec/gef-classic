/*******************************************************************************
 * Copyright (c) 2004, 2023 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gef.examples.shapes.model.Connection;
import org.eclipse.gef.examples.shapes.model.EllipticalShape;
import org.eclipse.gef.examples.shapes.model.ModelElement;
import org.eclipse.gef.examples.shapes.model.RectangularShape;
import org.eclipse.gef.examples.shapes.model.Shape;
import org.eclipse.gef.examples.shapes.model.commands.ConnectionCreateCommand;
import org.eclipse.gef.examples.shapes.model.commands.ConnectionReconnectCommand;

/**
 * EditPart used for Shape instances (more specific for EllipticalShape and
 * RectangularShape instances).
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class ShapeEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

	private ConnectionAnchor anchor;

	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());
		// allow the creation of connections and
		// and the reconnection of connections between Shape instances
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
			 * getConnectionCompleteCommand
			 * (org.eclipse.gef.requests.CreateConnectionRequest)
			 */
			@Override
			protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
				ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
				cmd.setTarget((Shape) getHost().getModel());
				return cmd;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
			 * getConnectionCreateCommand (org.eclipse.gef.requests.CreateConnectionRequest)
			 */
			@Override
			protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
				Shape source = (Shape) getHost().getModel();
				int style = ((Integer) request.getNewObjectType()).intValue();
				ConnectionCreateCommand cmd = new ConnectionCreateCommand(source, style);
				request.setStartCommand(cmd);
				return cmd;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
			 * getReconnectSourceCommand (org.eclipse.gef.requests.ReconnectRequest)
			 */
			@Override
			protected Command getReconnectSourceCommand(ReconnectRequest request) {
				Connection conn = (Connection) request.getConnectionEditPart().getModel();
				Shape newSource = (Shape) getHost().getModel();
				ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
				cmd.setNewSource(newSource);
				return cmd;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
			 * getReconnectTargetCommand (org.eclipse.gef.requests.ReconnectRequest)
			 */
			@Override
			protected Command getReconnectTargetCommand(ReconnectRequest request) {
				Connection conn = (Connection) request.getConnectionEditPart().getModel();
				Shape newTarget = (Shape) getHost().getModel();
				ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
				cmd.setNewTarget(newTarget);
				return cmd;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		IFigure f = createFigureForModel();
		f.setOpaque(true); // non-transparent figure
		f.setBackgroundColor(ColorConstants.green);
		return f;
	}

	/**
	 * Return a IFigure depending on the instance of the current model element. This
	 * allows this EditPart to be used for both sublasses of Shape.
	 */
	private IFigure createFigureForModel() {
		if (getModel() instanceof EllipticalShape) {
			return new Ellipse();
		} else if (getModel() instanceof RectangularShape) {
			return new RectangleFigure();
		} else {
			// if Shapes gets extended the conditions above must be updated
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	@Override
	public Shape getModel() {
		return (Shape) super.getModel();
	}

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			if (getModel() instanceof EllipticalShape)
				anchor = new EllipseAnchor(getFigure());
			else if (getModel() instanceof RectangularShape)
				anchor = new ChopboxAnchor(getFigure());
			else
				// if Shapes gets extended the conditions above must be updated
				throw new IllegalArgumentException("unexpected model");
		}
		return anchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	@Override
	protected List getModelSourceConnections() {
		return getModel().getSourceConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	@Override
	protected List getModelTargetConnections() {
		return getModel().getTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (Shape.SIZE_PROP.equals(prop) || Shape.LOCATION_PROP.equals(prop)) {
			refreshVisuals();
		} else if (Shape.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (Shape.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		}
	}

	@Override
	protected void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds of this
		// figure and will not draw it correctly.
		Rectangle bounds = new Rectangle(getModel().getLocation(), getModel().getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
	}
}