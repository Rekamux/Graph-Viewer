package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.GraphModel;
import model.Model;
import model.Vertex;
import model.Model.ColorOption;
import model.Model.MouseActionMode;
import model.Model.Tool;
import utils.Computing;
import utils.Couple;
import utils.FileExtensions;
import utils.Messages;
import view.GraphPanel;
import view.GraphTabbedPane;
import view.OpenGraphDialog;
import view.ProgramWindow;

/**
 * Controller watching over openGraphDialog
 * 
 * @author Ax
 * 
 */
public class ProgramController extends AbstractController {
	private ActionListener changeColorActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (ColorOption option : ColorOption.values()) {
				if (controller.getMainWindow().getToolPanel()
						.getGraphToolPanel().getColorButton(option) == e
						.getSource()) {
					Color color = chooseColor(option);
					if (color != null) {
						controller.getModel().setColor(option, color);
						controller.getModel().notifyObservers();
					}
					break;
				}
			}
		}
	};

	private ActionListener closeGraphActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton closeButton = (JButton) e.getSource();
			GraphTabbedPane tab = controller.getMainWindow().getTab();
			for (int i = 0; i < tab.getComponentCount(); i++) {
				JPanel panel = (JPanel) tab.getTabComponentAt(i);
				JButton panelButton = (JButton) panel.getComponent(1);
				if (closeButton.getActionCommand().equals(
						panelButton.getActionCommand())) {
					controller.getModel().removeGraph(i);
					break;
				}
			}
			if (controller.getModel().getGraphsCount() == 0)
				controller.getMainWindow().getToolPanel().setVisible(false);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener createCustomGraphActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getMainWindow().getOpenGraphDialog().getNameField()
					.setText(controller.getModel().getNextGraphName());
			controller.getMainWindow().getOpenGraphDialog().setVisible(true);
		}
	};

	private ActionListener createEmptyGraphActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			GraphModel graph = new GraphModel(controller.getModel()
					.getNextGraphName(), controller.getModel());
			createGraph(graph);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener doUndidActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.getModel().getCurrentGraph().doAgain();
			controller.getModel().notifyObservers();
		}
	};

	private KeyListener integerTextFieldKeyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			JTextField listened = (JTextField) e.getSource();
			if (listened.getText().equals(""))
				listened.setText("0");
			Integer i = Computing.toInteger(String.valueOf((e.getKeyChar())));
			if (i == null) {
				listened.setText(listened.getText().replace(
						String.valueOf(e.getKeyChar()), ""));
				listened.setSelectionStart(0);
				listened.setSelectionEnd(listened.getText().length());
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	};

	private ActionListener openGraphDialogCancelButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			hideOpenGraphDialog();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener openGraphDialogOkButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			validateOpenGraphDialog();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener openGraphFromAdjacencyMatrixFileActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			openGraphFromAdjacencyMatrixFile();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener openGraphFromSerializedFileActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			openGraphFromSerializedFile();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener openGraphFromVerticesListFileActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			openGraphFromEdgesListFile();
			controller.getModel().notifyObservers();
		}
	};

	private ComponentListener resizeComponentListener = new ComponentListener() {

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentResized(ComponentEvent e) {
			GraphTabbedPane graphTabbedPane = controller.getMainWindow()
					.getTab();
			if (graphTabbedPane.getComponentCount() == 0)
				return;
			GraphPanel graphPanel = (GraphPanel) graphTabbedPane
					.getComponentAt(0);
			controller.getModel()
					.resizeGraphs(
							new Dimension(graphPanel.getWidth(), graphPanel
									.getHeight()));
			controller.getModel().notifyObservers();
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}
	};

	private ActionListener saveAdjacencyMatrixActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.textExtensionDescription,
					FileExtensions.textExtension);
			if (!selectedFileIsValid(file))
				return;
			writeGraphAdjacency(file);
		}
	};

	private ActionListener saveEdgesListActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.textExtensionDescription,
					FileExtensions.textExtension);
			if (!selectedFileIsValid(file))
				return;
			writeGraphEdges(file);
		}
	};

	private ActionListener saveGraphActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.serializedExtensionDescription,
					FileExtensions.serializedExtension);
			if (!selectedFileIsValid(file))
				return;
			serializeGraph(file);
		}
	};

	private ActionListener saveGraphJPGImageActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.jpgExtensionDescription,
					FileExtensions.jpgExtension);
			if (!selectedFileIsValid(file))
				return;
			writeGraphImage(file, "JPG");
		}
	};

	private ActionListener saveGraphPNGImageActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.pngExtensionDescription,
					FileExtensions.pngExtension);
			if (!selectedFileIsValid(file))
				return;
			writeGraphImage(file, "PNG");
		}
	};

	private ActionListener savePSActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = browseInOrderToSave(
					FileExtensions.psExtensionDescription,
					FileExtensions.pngExtension);
			if (!selectedFileIsValid(file))
				return;
			writeGraphPS(file);
		}
	};

	private ActionListener selectVertexToolActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (controller.getModel().getCurrentGraph() == null)
				createGraph(new GraphModel(controller.getModel()
						.getNextGraphName(), controller.getModel()));
			controller.getModel().setCurrentTool(Tool.VERTEX);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener selectEdgesToolActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (controller.getModel().getCurrentGraph() == null)
				createGraph(new GraphModel(controller.getModel()
						.getNextGraphName(), controller.getModel()));
			controller.getModel().setCurrentTool(Tool.EDGE);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener selectGraphToolActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (controller.getModel().getCurrentGraph() == null)
				createGraph(new GraphModel(controller.getModel()
						.getNextGraphName(), controller.getModel()));
			controller.getModel().setCurrentTool(Tool.GRAPH);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener undoActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.getModel().getCurrentGraph().undo();
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener selectCreateVerticesActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.getModel().setMouseActionMode(
					MouseActionMode.MOUSE_ADDS_VERTICES);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener selectDeleteVerticesActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.getModel().setMouseActionMode(
					MouseActionMode.MOUSE_DELETES_VERTICES);
			controller.getModel().notifyObservers();
		}
	};

	private ActionListener selectSelectVerticesActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getModel().setMouseActionMode(
					MouseActionMode.MOUSE_SELECTS_VERTICES);
			controller.getModel().notifyObservers();
		}
	};

	/**
	 * Default constructor
	 * 
	 * @param controller
	 *            the controller
	 */
	public ProgramController(MainController controller) {
		super(controller);
	}

	/**
	 * Shows a browse file and then checks it
	 */
	private File browseGraph(String categoryName, String extension) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(controller.getModel().getCurrentFile());
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileNameExtensionFilter(categoryName,
				extension));
		int response = chooser.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		return null;
	}

	/**
	 * Saves a graph
	 */
	private File browseInOrderToSave(String extensionDescription,
			String extensionName) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return null;
		}
		File file = null;
		JFileChooser chooser = new JFileChooser(controller.getModel()
				.getCurrentFile());
		chooser.setFileFilter(new FileNameExtensionFilter(extensionDescription,
				extensionName));
		chooser.setSelectedFile(new File(graph.getGraphsName() + "."
				+ extensionName));
		boolean haveChosen = false;
		while (!haveChosen) {
			haveChosen = true;
			int response = chooser.showSaveDialog(controller.getMainWindow());
			if (response == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				String fileName = file.getName();
				controller.getModel().setCurrentFile(file);
				if (file.exists()) {
					int replaceResponse = JOptionPane
							.showConfirmDialog(
									null,
									fileName
											+ " already exists. Do you want to replace it?",
									"File already exists",
									JOptionPane.YES_NO_OPTION);
					if (replaceResponse == JOptionPane.YES_OPTION) {
						if (!file.delete()) {
							haveChosen = false;
							JOptionPane.showMessageDialog(null,
									"Cannot delete " + fileName
											+ ". Choose an other file.",
									"Cannot delete", JOptionPane.OK_OPTION);
						}
						try {
							file.createNewFile();
						} catch (IOException e) {
							Messages.showErrorMessage(controller,
									"Cannot create this file !");
						}
					} else
						haveChosen = false;
				} else
					try {
						file.createNewFile();
					} catch (IOException e) {
						Messages.showErrorMessage(controller,
								"Cannot create this file !");
					}
			}
		}
		return file;
	}

	/**
	 * Selects an other graph
	 * 
	 * @param index
	 */
	public void changeCurrentGraphIndex(int index) {
		controller.getModel().setCurrentGraphIndex(index);
		controller.getModel().notifyObservers();
	}

	/**
	 * Let the user to choose a color
	 */
	private Color chooseColor(ColorOption option) {
		Color c = JColorChooser.showDialog(getWindow(), "Choose "
				+ controller.getModel().getColorSectionName(option) + " color",
				controller.getModel().getColor(option));
		return c;
	}

	/**
	 * Creates a graph and adds graph tool panel as an observer of it
	 * 
	 * @param graph
	 */
	private void createGraph(GraphModel graph) {
		controller.getModel().createGraph(graph);
		graph.addObserver(controller.getMainWindow().getTab());
		graph.addObserver(controller.getMainWindow().getToolPanel()
				.getGraphToolPanel());
		graph.addObserver(controller.getMainWindow().getToolBar());
		graph.addObserver(controller.getMainWindow().getWindowMenuBar());
		graph.setDimension(controller.getMainWindow().getTab().getSize());
	}

	/**
	 * @return the changeColorActionListener
	 */
	public ActionListener getChangeColorActionListener() {
		return changeColorActionListener;
	}

	/**
	 * @return the closeGraphActionListener
	 */
	public ActionListener getCloseGraphActionListener() {
		return closeGraphActionListener;
	}

	/**
	 * @return the createCustomGraphActionListener
	 */
	public ActionListener getCreateCustomGraphActionListener() {
		return createCustomGraphActionListener;
	}

	/**
	 * @return the createEmptyGraphActionListener
	 */
	public ActionListener getCreateEmptyGraphActionListener() {
		return createEmptyGraphActionListener;
	}

	/**
	 * @return the doUndidActionListener
	 */
	public ActionListener getDoUndidActionListener() {
		return doUndidActionListener;
	}

	/**
	 * @return the integerTextFieldKeyListener
	 */
	public KeyListener getIntegerTextFieldKeyListener() {
		return integerTextFieldKeyListener;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return controller.getModel();
	}

	/**
	 * @return the openGraphDialogCancelButtonActionListener
	 */
	public ActionListener getOpenGraphDialogCancelButtonActionListener() {
		return openGraphDialogCancelButtonActionListener;
	}

	/**
	 * @return the openGraphDialogOkButtonActionListener
	 */
	public ActionListener getOpenGraphDialogOkButtonActionListener() {
		return openGraphDialogOkButtonActionListener;
	}

	/**
	 * @return the openGraphFromAdjacencyMatrixFileActionListener
	 */
	public ActionListener getOpenGraphFromAdjacencyMatrixFileActionListener() {
		return openGraphFromAdjacencyMatrixFileActionListener;
	}

	/**
	 * @return the openGraphFromVerticesListFileActionListener
	 */
	public ActionListener getOpenGraphFromEdgesListFileActionListener() {
		return openGraphFromVerticesListFileActionListener;
	}

	/**
	 * @return the openGraphFromSerializedFileActionListener
	 */
	public ActionListener getOpenGraphFromSerializedFileActionListener() {
		return openGraphFromSerializedFileActionListener;
	}

	/**
	 * @return the openGraphFromVerticesListFileActionListener
	 */
	public ActionListener getOpenGraphFromVerticesListFileActionListener() {
		return openGraphFromVerticesListFileActionListener;
	}

	/**
	 * @return the resizeComponentListener
	 */
	public ComponentListener getResizeComponentListener() {
		return resizeComponentListener;
	}

	/**
	 * @return the saveAdjacencyMatrixActionListener
	 */
	public ActionListener getSaveAdjacencyMatrixActionListener() {
		return saveAdjacencyMatrixActionListener;
	}

	/**
	 * @return the saveEdgesListActionListener
	 */
	public ActionListener getSaveEdgesListActionListener() {
		return saveEdgesListActionListener;
	}

	/**
	 * @return the saveGraphActionListener
	 */
	public ActionListener getSaveGraphActionListener() {
		return saveGraphActionListener;
	}

	/**
	 * @return the saveGraphJPGImageActionListener
	 */
	public ActionListener getSaveGraphJPGImageActionListener() {
		return saveGraphJPGImageActionListener;
	}

	/**
	 * @return the saveGraphPNGImageActionListener
	 */
	public ActionListener getSaveGraphPNGImageActionListener() {
		return saveGraphPNGImageActionListener;
	}

	/**
	 * @return the savePSActionListener
	 */
	public ActionListener getSavePSActionListener() {
		return savePSActionListener;
	}

	/**
	 * @return the selectCreateVertexToolActionListener
	 */
	public ActionListener getSelectVertexToolActionListener() {
		return selectVertexToolActionListener;
	}

	/**
	 * @return the selectEdgesToolActionListener
	 */
	public ActionListener getSelectEdgesToolActionListener() {
		return selectEdgesToolActionListener;
	}

	/**
	 * @return the selectGraphToolActionListener
	 */
	public ActionListener getSelectGraphToolActionListener() {
		return selectGraphToolActionListener;
	}

	/**
	 * @return the undoActionListener
	 */
	public ActionListener getUndoActionListener() {
		return undoActionListener;
	}

	/**
	 * @return the window
	 */
	public ProgramWindow getWindow() {
		return controller.getMainWindow();
	}

	/**
	 * Hides open graph dialog
	 */
	private void hideOpenGraphDialog() {
		controller.getModel().setOpenGraphDialogShown(false);
	}

	/**
	 * Opens a graph from a adjacency matrix
	 */
	private void openGraphFromAdjacencyMatrixFile() {
		File file = browseGraph(FileExtensions.textExtensionDescription,
				FileExtensions.textExtension);
		if (!selectedFileIsValid(file))
			return;
		testAdjacencyFile(file);
	}

	/**
	 * Opens graph from a vertices list file
	 */
	private void openGraphFromEdgesListFile() {
		File file = browseGraph(FileExtensions.textExtensionDescription,
				FileExtensions.textExtension);
		if (!selectedFileIsValid(file))
			return;
		testEdgesFile(file);
	}

	/**
	 * Opens graph from a serialized file
	 */
	private void openGraphFromSerializedFile() {
		File file = browseGraph(FileExtensions.serializedExtensionDescription,
				FileExtensions.serializedExtension);
		if (!selectedFileIsValid(file))
			return;
		testSerializedFile(file);
	}

	/**
	 * Checks if selected file is valid (exists)
	 * 
	 * @throws IOException
	 */
	private boolean selectedFileIsValid(File selectedFile) {
		if (selectedFile == null) {
			return false;
		}
		if (!selectedFile.exists() || !selectedFile.isFile()) {
			Messages.showErrorMessage(controller,
					"Selected file doesn't exist !");
			return false;
		}
		return true;
	}

	/**
	 * Serializes graph into given file
	 * 
	 * @param file
	 */
	private void serializeGraph(File file) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return;
		}
		try {
			ObjectOutputStream stream = new ObjectOutputStream(
					new FileOutputStream(file));
			stream.writeObject(graph);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			Messages.showCannotWriteFileError(controller);
		}
	}

	/***
	 * Reads a file and tries to extract a graph using an adjacency matrix
	 * 
	 * @param reader
	 * @return
	 */
	private void testAdjacencyFile(File file) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String s = null;
			s = reader.readLine();
			if (s == null) {
				Messages.showErrorMessage(controller, "This file is empty !");
				return;
			}
			int i = 0;
			int n = 0;
			ArrayList<ArrayList<Boolean>> matrix = new ArrayList<ArrayList<Boolean>>();
			while (s != null) {
				matrix.add(new ArrayList<Boolean>());
				String values[] = s.split("[ ]");
				int length = 0;
				for (int j = 0; j < values.length; j++) {
					if (!values[j].equals("")) {
						Boolean b = Computing.toBoolean(values[j]);
						if (b == null) {
							Messages.showErrorMessage(controller, "At row " + i
									+ " column " + j
									+ ":\n value is not a 0 or a 1!");
							return;
						} else {
							matrix.get(i).add(b);
							length++;
						}
					}
				}
				if (n == 0) {
					n = length;
					if (n == 0) {
						Messages.showErrorMessage(controller,
								"Given matrix is empty !");
						return;
					}
				}
				if (n != length) {
					Messages.showErrorMessage(
							controller,
							"At row "
									+ i
									+ ":\n given matrix is not a square:\n row's length "
									+ length + " previous row's length " + n
									+ "!");
					return;
				}

				s = reader.readLine();

				i++;
			}
			if (i != n) {
				Messages.showErrorMessage(controller,
						"Given matrix is not a square:\n " + i + " rows " + n
								+ " columns!");
				return;
			}
			fileReader.close();
			GraphModel graph = new GraphModel(controller.getModel()
					.getNextGraphName(), controller.getMainWindow().getTab()
					.getWidth(), controller.getMainWindow().getTab()
					.getHeight(), matrix, controller.getModel());
			createGraph(graph);
		} catch (IOException e) {
			Messages.showCannotReadFileError(controller);
			return;
		}
	}

	/***
	 * Read a file and try to extract a graph using edges list from it
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private void testEdgesFile(File file) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = reader.readLine();
			if (line == null) {
				Messages.showEmptyFileError(controller);
				return;
			}
			String couples[] = line.split("[ ]");
			ArrayList<String> vertices = new ArrayList<String>();
			ArrayList<Couple<String>> edges = new ArrayList<Couple<String>>();
			for (int i = 0; i < couples.length; i++)
				if (!couples[i].equals("")) {
					String sides[] = couples[i].split(",");
					if (sides.length == 1 && !vertices.contains(sides[0]))
						vertices.add(sides[0]);
					else if (sides.length == 2) {
						if (sides[0].equals(sides[1])) {
							Messages.showErrorMessage(controller,
									"Selected file contains v,v edges !");
							return;
						}
						if (!vertices.contains(sides[0]))
							vertices.add(sides[0]);
						if (!vertices.contains(sides[1]))
							vertices.add(sides[1]);
						edges.add(new Couple<String>(sides[0], sides[1]));
					} else if (sides.length > 2) {
						Messages.showErrorMessage(controller,
								"Giving vertices couple containing more than 2 vertices !");
						return;
					}
				}
			ArrayList<ArrayList<Boolean>> matrix = new ArrayList<ArrayList<Boolean>>();
			int n = vertices.size();
			for (int i = 0; i < n; i++) {
				ArrayList<Boolean> vertexLine = new ArrayList<Boolean>();
				for (int j = 0; j < n; j++) {
					String vI = vertices.get(i);
					String vJ = vertices.get(j);
					boolean found = false;
					for (int k = 0; k < edges.size() && !found; k++) {
						String first = edges.get(k).getFirst();
						String second = edges.get(k).getSecond();
						if (first.equals(vI) && second.equals(vJ)) {
							found = true;
						}
					}
					vertexLine.add(found);
				}
				matrix.add(vertexLine);
			}
			// int diameter = new
			// GeometricGraph(controller.getModel()).getDiameter();
			int diameter = 10;
			ArrayList<Vertex> enteredVerticesList = new ArrayList<Vertex>();
			for (int i = 0; i < n; i++)
				enteredVerticesList.add(new Vertex(vertices.get(i), 0, 0,
						diameter, false));
			fileReader.close();
			GraphModel graph = new GraphModel(controller.getModel()
					.getNextGraphName(), controller.getMainWindow().getTab()
					.getWidth(), controller.getMainWindow().getTab()
					.getHeight(), matrix, enteredVerticesList,
					controller.getModel());
			createGraph(graph);
			controller.getModel().getCurrentGraph().moveAllVertices();
		} catch (IOException e) {
			Messages.showCannotReadFileError(controller);
		}
	}

	/**
	 * Reads a file and tries to extract a serialized graph
	 * 
	 * @param file
	 */
	private void testSerializedFile(File file) {
		try {
			ObjectInputStream stream = null;
			stream = new ObjectInputStream(new FileInputStream(file));
			GraphModel graph = (GraphModel) stream.readObject();
			graph.linkModelAndCheck(controller.getModel());
			stream.close();
			createGraph(graph);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"This file is not a valid Graph Viewer File !");
		}
	}

	/**
	 * Applies open graph model
	 */
	private void validateOpenGraphDialog() {
		JTabbedPane p = controller.getMainWindow().getTab();
		OpenGraphDialog dialog = controller.getMainWindow()
				.getOpenGraphDialog();
		controller.getModel().setOpenGraphDialogShown(false);
		GraphModel graph = new GraphModel(dialog.getEnteredName(),
				p.getWidth(), p.getHeight(), dialog.getEnteredN(),
				dialog.getEnteredM(), controller.getModel());
		createGraph(graph);
	}

	/**
	 * Saves graph's adjacency matrix
	 * 
	 * @param file
	 */
	private void writeGraphAdjacency(File file) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return;
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(graph.adjacencyMatrixToString());
			fileWriter.close();
		} catch (IOException e) {
			Messages.showCannotWriteFileError(controller);
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Writes current graph's edges list
	 * 
	 * @param file
	 */
	private void writeGraphEdges(File file) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return;
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(graph.verticesListToString());
			fileWriter.close();
		} catch (IOException e) {
			Messages.showCannotWriteFileError(controller);
			return;
		}
	}

	/**
	 * Writes graph into an image file
	 */
	private void writeGraphImage(File file, String type) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return;
		}
		BufferedImage bufferedImage = new BufferedImage(graph.getWidth(),
				graph.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bufferedImage.getGraphics();
		GraphPanel.drawGraph(controller, graph, g, graph.getDimension());
		try {
			ImageIO.write(bufferedImage, type, file);
		} catch (IOException e) {
			Messages.showCannotWriteFileError(controller);
		}
		g.dispose();
	}

	/**
	 * Writes graph into a ps file
	 * 
	 * @param file
	 */
	private void writeGraphPS(File file) {
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null) {
			Messages.showGraphDoesntExistSaveError(controller);
			return;
		}
		int graphWidth = graph.getWidth();
		int graphHeight = graph.getHeight();
		int sheetWidth = 612;
		int sheetHeight = 792;
		int n = graph.getN();
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("%!PS-Adobe-3.0\n" + "%%BoundingBox: 0 0 "
					+ sheetWidth + " " + sheetHeight + "\n");
			for (int i = 0; i < n; i++) {
				Vertex v = graph.getVertex(i);
				fileWriter.write(v.drawPointPS(graphWidth, graphHeight,
						sheetWidth, sheetHeight));
			}
			fileWriter.write("/Arial findfont " + graph.getDiameter() * 2
					+ " scalefont setfont\n");
			for (int i = 0; i < n; i++) {
				Vertex v = graph.getVertex(i);
				fileWriter.write(v.drawNamePS(graphWidth, graphHeight,
						sheetWidth, sheetHeight));
			}
			for (int i = 0; i < n; i++) {
				Vertex vI = graph.getVertex(i);
				int xI = vI.getXPosition();
				if (graphWidth > sheetWidth)
					xI = vI.getXPosition() * sheetWidth / graphWidth;
				int yI = vI.getYPosition();
				if (graphHeight > sheetHeight)
					yI = vI.getYPosition() * sheetHeight / graphHeight;
				for (int j = 0; j < n; j++)
					if (graph.areNeighbors(i, j)) {
						Vertex vJ = graph.getVertex(j);
						int xJ = vJ.getXPosition();
						if (graphWidth > sheetWidth)
							xJ = vJ.getXPosition() * sheetWidth / graphWidth;
						int yJ = vJ.getYPosition();
						if (graphHeight > sheetHeight)
							yJ = vJ.getYPosition() * sheetHeight / graphHeight;

						fileWriter.write("" + xI + " "
								+ Integer.toString(sheetHeight - yI)
								+ " moveto\n" + xJ + " "
								+ Integer.toString(sheetHeight - yJ)
								+ " lineto\n" + "stroke\n" + "\n");
						if (!graph.isNonOriented()) {
							int[] triangle = graph.getOrientedTriangle(xI, yI,
									xJ, yJ);
							for (int k = 0; k < 3; k++) {
								int xA, yA, xB, yB;
								xA = triangle[2 * k];
								yA = sheetHeight - triangle[2 * k + 1];
								if (k == 2) {
									xB = triangle[0];
									yB = sheetHeight - triangle[1];
								} else {
									xB = triangle[2 * k + 2];
									yB = sheetHeight - triangle[2 * k + 3];
								}
								fileWriter.write("" + xA + " " + yA
										+ " moveto\n" + xB + " " + yB
										+ " lineto\n" + "stroke\n" + "\n");
							}
						}
					}
				fileWriter.close();

			}
		}

		catch (IOException e) {
			Messages.showCannotWriteFileError(controller);
			return;
		}
	}

	/**
	 * @return the selectCreateVerticesActionListener
	 */
	public ActionListener getSelectCreateVerticesActionListener() {
		return selectCreateVerticesActionListener;
	}

	/**
	 * @return the selectSelectVerticesActionListener
	 */
	public ActionListener getSelectDeleteVerticesActionListener() {
		return selectDeleteVerticesActionListener;
	}

	/**
	 * @return the selectSelectVerticesActionListener
	 */
	public ActionListener getSelectSelectVerticesActionListener() {
		return selectSelectVerticesActionListener;
	}
}
