package manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import backend.Diagram;
import backend.Edge;
import backend.EdgeDirection;
import backend.Node;

import com.google.gson.stream.*;

import frontend.DrawingPanel;

/**
 * Represents a single project (single tab) of the FSM/Graph Diagram.
 * It consists of History Stack as well as utilities to save, open,
 * and export the diagram.
 * 
 * @author ajanthon
 */
public class DiagramProject {
	
	/** The number of maximum history. */
	private static final int MAX_HISTORY = 42; 
	
	/** The filename of this project. This could be null if it is a newly
	 * created diagram that has never been saved yet. */
	private String _filename; 
	
	/** The object that keeps track of recent changes to the diagram.
	 * It is useful for undo and redo features. */
	private HistoryStack _history;

	/** Boolean indicating whether the project is from a file, not a newly created one. */
	private boolean _loaded;
	
	/** Last saved revision number */
	private int _savedRevision; 
	
	/**
	 * Make sure the the constructor is private.
	 */
	private DiagramProject() {
		// do nothing
	}
	
	/**
	 * Factory method that creates a new project. 
	 * @return The new project.
	 */
	public static DiagramProject newProject() {
		DiagramProject project = new DiagramProject();
		project._filename = null;
		project._history = new HistoryStack(MAX_HISTORY);
		project._loaded = false;
		project._savedRevision = -1;
		return project;
	}
	
	/**
	 * Factory method that create a project from the given diagram and the filename.
	 * @param filename The filename associated
	 * @param openedDiagram The diagram opened.
	 * @return
	 */
	public static DiagramProject openProject(String filename, Diagram openedDiagram) {
		DiagramProject project = new DiagramProject();
		project._filename = filename;
		project._history = new HistoryStack(MAX_HISTORY, openedDiagram);
		project._loaded = true;
		project._savedRevision = 0;
		return project;
	}
	
	/**
	 * Returns the History stack of the diagram.
	 * @return History stack
	 */
	public HistoryStack getHistoryStack() {
		return _history;
	}
	
	/**
	 * Returns the History stack of the diagram.
	 * @return History stack
	 */
	public String getFilename() {
		return _filename;
	}
	
	/**
	 * Set the filename.
	 * @param filename
	 */
	public void setFilename(String filename) {
		_filename = filename;
	}
	
	/** 
	 * Factory method that loads the saved project from a file. 
	 * @throws IOException 
	 */
	public static Diagram readDiagram(File file, DrawingPanel panel) throws IOException {
		
		// Creates a JSON Reader based on the given reader.
		JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
		reader.setLenient(true);
		
		// Start reading data.
		reader.beginObject();
		
		// === READING DIAGRAM NAME ===
		Diagram diagram = new Diagram();

		// === READING DIAGRAM NODES ===
		ArrayList<Node> nodes = new ArrayList<Node>();
		if(!reader.nextName().equals("nodes")) {
			throw new IOException();
		}
		reader.beginArray();
		while(reader.hasNext()) {
			
			// === READING EACH NODE ===
			reader.beginObject();
			
			if(!reader.nextName().equals("x")) {
				throw new IOException("Expecting x-coordinate when reading diagram file.");
			}
			double x = reader.nextDouble();
			
			if(!reader.nextName().equals("y")) {
				throw new IOException("Expecting y-coordinate when reading diagram file.");
			}
			double y = reader.nextDouble();

			if(!reader.nextName().equals("radius")) {
				throw new IOException("Expecting radius when reading diagram file.");
			}
			double radius = reader.nextDouble();

			if(!reader.nextName().equals("is_start")) {
				throw new IOException("Expecting is_start boolean when reading diagram file.");
			}
			boolean isStart = reader.nextBoolean();

			if(!reader.nextName().equals("is_accept")) {
				throw new IOException("Expecting is_accept boolean when reading diagram file.");
			}
			boolean isAccept = reader.nextBoolean();

			if(!reader.nextName().equals("label")) {
				throw new IOException("Expecting label when reading diagram file.");
			}
			String label = reader.nextString();
			
			// Create node from given data
			Node node = new Node(x, y, radius, isStart, isAccept, label);
			node.setContainerAndLabel(panel);
			
			// Add the node to the diagram.
			diagram.addNode(node);
			nodes.add(node);
			
			reader.endObject();
		}
		reader.endArray();

		// === READING DIAGRAM EDGES ===
		if(!reader.nextName().equals("edges")) {
			throw new IOException();
		}
		reader.beginArray();
		while(reader.hasNext()) {
			
			// === READING EACH NODE ===
			reader.beginObject();
			
			if(!reader.nextName().equals("node_start")) {
				throw new IOException("Expecting node_start index when reading diagram file.");
			}
			int tmpNodeStart = reader.nextInt();
			if(tmpNodeStart < 0 || tmpNodeStart >= nodes.size()) {
				throw new IOException("node_start index is out of bounds.");
			}
			Node nodeStart = nodes.get(tmpNodeStart);
			
			if(!reader.nextName().equals("node_end")) {
				throw new IOException("Expecting node_end index when reading diagram file.");
			}
			int tmpNodeEnd = reader.nextInt();
			if(tmpNodeEnd < 0 || tmpNodeEnd >= nodes.size()) {
				throw new IOException("node_end index is out of bounds.");
			}
			Node nodeEnd = nodes.get(tmpNodeEnd);

			if(!reader.nextName().equals("edge_direction")) {
				throw new IOException("Expecting edge_direction when reading diagram file.");
			}
			String tmpEdgeDirection = reader.nextString();
			EdgeDirection edgeDirection;
			if(tmpEdgeDirection.equals("NONE")) {
				edgeDirection = EdgeDirection.NONE;
			} else if(tmpEdgeDirection.equals("SINGLE")) {
				edgeDirection = EdgeDirection.SINGLE;
			} else if(tmpEdgeDirection.equals("DOUBLE")) {
				edgeDirection = EdgeDirection.DOUBLE;
			} else {
				throw new IOException("Edge direction is not correct.");
			}

			if(!reader.nextName().equals("label")) {
				throw new IOException("Expecting label when reading diagram file.");
			}
			String label = reader.nextString();
			
			Edge edge;
			if(nodeStart == nodeEnd) {
				
				if(!reader.nextName().equals("angle")) {
					throw new IOException("Expecting angle when reading diagram file.");
				}
				double angle = reader.nextDouble();
				
				edge = new Edge(nodeStart, nodeEnd, edgeDirection, label, angle);
				
			} else {
				
				if(!reader.nextName().equals("arc_chord_height")) {
					throw new IOException("Expecting arc chord height when reading diagram file.");
				}
				double arcChordHeight = reader.nextDouble();
				
				if(!reader.nextName().equals("arc_side")) {
					throw new IOException("Expecting arc side when reading diagram file.");
				}
				int arcSide = reader.nextInt();
				
				edge = new Edge(nodeStart, nodeEnd, edgeDirection, label, arcChordHeight, arcSide);
			}
			
			nodeStart.addConnected(edge);
			nodeEnd.addConnected(edge);
			
			// Add the edge to the diagram.
			edge.setContainerAndArea(panel);
			diagram.addEdge(edge);
			
			reader.endObject();
		}
		reader.endArray();
		
		// Stop reading data.
		reader.endObject();
		reader.close();
		
		return diagram;
	}

	/** 
	 * Factory method that writes the project to the file. 
	 * @throws IOException 
	 */
	public static void writeDiagram(File file, Diagram diagram) throws IOException {
		
		// Create a JSON writer
		OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(file));
		JsonWriter writer = new JsonWriter(osWriter);
		
		// Start writing data.
		writer.beginObject();
		
		// Write the nodes data.
		HashMap<Node, Integer> nodeMap = new HashMap<Node, Integer>();
		writer.name("nodes");
		writer.beginArray();
		for(Node node : diagram.getNodes()) {
			Integer size = nodeMap.size();
			nodeMap.put(node, size);
			writer.beginObject();
			writer.name("x").value(node.getCenter().getX());
			writer.name("y").value(node.getCenter().getY());
			writer.name("radius").value(node.getRadius());
			writer.name("is_start").value(node.isStart());
			writer.name("is_accept").value(node.isEnd());
			writer.name("label").value(node.getTextField().getText());
			writer.endObject();
		}
		writer.endArray();
		
		// Write the edges data.
		writer.name("edges");
		writer.beginArray();
		for(Edge edge : diagram.getEdges()) {
			writer.beginObject();
			writer.name("node_start").value(nodeMap.get(edge.getStartNode()));
			writer.name("node_end").value(nodeMap.get(edge.getEndNode()));
			if(edge.getDirection() == EdgeDirection.SINGLE)
				writer.name("edge_direction").value("SINGLE");
			else if(edge.getDirection() == EdgeDirection.DOUBLE) 
				writer.name("edge_direction").value("DOUBLE");
			else if(edge.getDirection() == EdgeDirection.NONE) 
				writer.name("edge_direction").value("NONE");
			else
				throw new IOException("Diagram contains invalid direction.");
			writer.name("label").value(edge.getTextField().getText());
			if(edge.getStartNode() == edge.getEndNode()) {
				writer.name("angle").value(edge.getAngle());
			} else {
				writer.name("arc_chord_height").value(edge.getHeight());
				writer.name("arc_side").value(edge.getTurn() ? 1 : -1);
			}
			writer.endObject();
		}
		writer.endArray();
		
		// Stop writing data.
		writer.endObject();
		writer.close();
	}
}
