package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.GraphModel;
import model.Model;
import model.Model.MouseActionMode;

/**
 * Controller watching graphs
 * 
 * @author Ax
 */
public class GraphController extends AbstractController {
	/**
	 * Default constructor
	 * 
	 * @param controller
	 */
	public GraphController(MainController controller) {
		super(controller);
	}

	private ActionListener vertexLabelActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			int vertexIndex = controller.getModel().getCurrentVertexIndex();
			JCheckBox box = (JCheckBox) e.getSource();
			if (vertexIndex == -1) {
				controller.getModel().setNextVertexLabel(box.isSelected());
			} else {
				graph.setVertexLabel(vertexIndex, box.isSelected());
			}
			controller.getModel().notifyObservers();
		}
	};

	private ChangeListener vertexNameRoPositionChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			int vertexIndex = controller.getModel().getCurrentVertexIndex();
			if (vertexIndex == -1)
				return;
			JSlider slider = (JSlider) e.getSource();
			graph.setVertexNameDistance(vertexIndex, slider.getValue());
			controller.getModel().notifyObservers();
		}
	};

	private ChangeListener vertexNameThetaPositionChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			int vertexIndex = controller.getModel().getCurrentVertexIndex();
			if (vertexIndex == -1)
				return;
			JSlider slider = (JSlider) e.getSource();
			graph.getVertexNameAngle(vertexIndex, slider.getValue());
			controller.getModel().notifyObservers();
		}
	};

	private KeyListener typeVertexNameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField textField = (JTextField) e.getSource();
			String newName = textField.getText();
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			int currentVertexIndex = controller.getModel()
					.getCurrentVertexIndex();
			if (currentVertexIndex >= 0)
				graph.setVertexName(currentVertexIndex, newName);
			controller.getModel().notifyObservers();
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
		}
	};

	private KeyListener typeGraphNameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField textField = (JTextField) e.getSource();
			String newName = textField.getText();
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			graph.setGraphsName(newName);
			controller.getModel().notifyObservers();
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}
	};

	private KeyListener vertexNameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			int vertexIndex = controller.getModel().getCurrentVertexIndex();
			if (vertexIndex != -1) {
				controller
						.getModel()
						.getCurrentGraph()
						.setVertexName(
								vertexIndex,
								controller.getMainWindow().getToolPanel()
										.getVertexToolPanel().getNameField()
										.getText());
			}
			controller.getModel().notifyObservers();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
		}
	};

	private MouseListener graphPanelMouseListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent event) {
			Point point = event.getPoint();
			Model model = controller.getModel();
			MouseActionMode mouseActionMode = model.getMouseActionMode();
			JComponent component = (JComponent) event.getSource();
			switch (mouseActionMode) {

			case MOUSE_ADDS_VERTICES:
				break;

			case MOUSE_SELECTS_VERTICES:
				model.selectInRectangle(component.getGraphics());
				break;

			case MOUSE_DELETES_VERTICES:
				model.deleteVerticesInRectangle(component.getGraphics());
				break;

			case MOUSE_CHANGES_EDGES:
				model.changeEdge(point, component.getGraphics());
				break;
			}
			model.initPoints();
			model.notifyObservers();
		}

		@Override
		public void mousePressed(MouseEvent event) {
			Model model = controller.getModel();
			MouseActionMode mouseActionMode = model.getMouseActionMode();
			Point point = event.getPoint();
			JComponent component = (JComponent) event.getSource();
			switch (mouseActionMode) {
			case MOUSE_MOVES_VERTICES:
				model.startDraggingVertex(point, component.getGraphics());
				break;

			case MOUSE_ADDS_VERTICES:
				String name = controller.getMainWindow().getToolPanel()
						.getNextVertexName();
				model.setNextVertexName(name);
				model.addVertex(point);
				break;

			case MOUSE_SELECTS_VERTICES:
				model.setDrawingRectanglePosition(point);
				break;

			case MOUSE_DELETES_VERTICES:
				model.setDrawingRectanglePosition(point);
				break;

			case MOUSE_CHANGES_EDGES:
				model.selectVertex(point, component.getGraphics());
				break;
			}
			model.notifyObservers();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Model model = controller.getModel();
			MouseActionMode mouseActionMode = model.getMouseActionMode();
			JComponent component = (JComponent) e.getSource();
			switch (mouseActionMode) {
			case MOUSE_MOVES_VERTICES:
				model.selectVertex(e.getPoint(), component.getGraphics());
				break;

			case MOUSE_SELECTS_VERTICES:
				model.selectVertex(e.getPoint(), component.getGraphics());
				break;

			case MOUSE_DELETES_VERTICES:
				model.deleteVertice(e.getPoint(), component.getGraphics());
				break;
			}
			model.notifyObservers();

		}
	};

	private MouseMotionListener graphPanelMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			Point point = event.getPoint();
			Model model = controller.getModel();
			MouseActionMode mouseActionMode = model.getMouseActionMode();
			switch (mouseActionMode) {
			case MOUSE_MOVES_VERTICES:
				model.dragVertex(point);
				break;

			case MOUSE_ADDS_VERTICES:
				break;

			case MOUSE_SELECTS_VERTICES:
				break;

			case MOUSE_DELETES_VERTICES:
				break;

			case MOUSE_CHANGES_EDGES:
				model.setLinkingPosition(point);
				break;
			}
			model.updateSelectionRectangle(point);
			model.notifyObservers();
		}
	};

	private ActionListener shakeActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			graph.moveAllVertices();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener componentShakeActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			graph.moveAllVerticesByComponent();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener complementActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			graph.toComplement();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener orientedActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			GraphModel graph = controller.getModel().getCurrentGraph();
			if (graph == null)
				return;
			JCheckBox checkBox = (JCheckBox) e.getSource();
			boolean isChecked = checkBox.isSelected();
			graph.setOriented(isChecked);
			controller.getModel().notifyObservers();
		}
	};

	/**
	 * @return the vertexNameKeyListener
	 */
	public KeyListener getVertexNameKeyListener() {
		return vertexNameKeyListener;
	}

	/**
	 * @return the graphPanelMouseListener
	 */
	public MouseListener getGraphPanelMouseListener() {
		return graphPanelMouseListener;
	}

	/**
	 * @return the graphPanelMotionListener
	 */
	public MouseMotionListener getGraphPanelMotionListener() {
		return graphPanelMotionListener;
	}

	/**
	 * @return the shakeActionListener
	 */
	public ActionListener getShakeActionListener() {
		return shakeActionListener;
	}

	/**
	 * @return the componentShakeActionListener
	 */
	public ActionListener getComponentShakeActionListener() {
		return componentShakeActionListener;
	}

	/**
	 * @return the complementActionListener
	 */
	public ActionListener getComplementActionListener() {
		return complementActionListener;
	}

	/**
	 * @return the orientedActionListener
	 */
	public ActionListener getOrientedActionListener() {
		return orientedActionListener;
	}

	/**
	 * @return the typeGraphNameKeyListener
	 */
	public KeyListener getTypeGraphNameKeyListener() {
		return typeGraphNameKeyListener;
	}

	/**
	 * @return the typeVertexNameKeyListener
	 */
	public KeyListener getTypeVertexNameKeyListener() {
		return typeVertexNameKeyListener;
	}

	/**
	 * @return the vertexNameRoPositionChangeListener
	 */
	public ChangeListener getVertexNameRoPositionChangeListener() {
		return vertexNameRoPositionChangeListener;
	}

	/**
	 * @return the vertexNameThetaPositionChangeListener
	 */
	public ChangeListener getVertexNameThetaPositionChangeListener() {
		return vertexNameThetaPositionChangeListener;
	}

	public ActionListener getVertexLabelActionListener() {
		return vertexLabelActionListener;
	}
}
