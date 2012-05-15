package backend;

import java.util.*;

import frontend.DrawingPanel;
import frontend.MainFrame;

/**
 * 
 * @author ewald
 *
 */
public class Diagram implements Cloneable {
	private Collection<Node> _nodes;
	private Collection<Edge> _edges;
	private int _revision;
	private MainFrame _frame;
	private HashMap<Node, Node> _oldNodeToNew;
	private HashMap<Edge, Edge> _newEdgeToOld;

	public Diagram() {
		_nodes = new HashSet<Node>();
		_edges = new HashSet<Edge>();
		_revision = 0;
	}

	public void setDrawingPanel(DrawingPanel container, MainFrame frame) { 
		_frame = frame;
	}

	public boolean addNode(Node n) {
		return _nodes.add(n);
	}

	public boolean addEdge(Edge e) {
		return _edges.add(e);
	}
	
	public MainFrame getFrame() {
		return _frame;
	}

	public boolean removeNode(Node n) {
		return _nodes.remove(n);
	}

	public boolean removeEdge(Edge e) {
		return _edges.remove(e);
	}

	public Collection<Node> getNodes() {
		return _nodes;
	}

	public Collection<Edge> getEdges() {
		return _edges;
	}
	
	public HashMap<Node, Node> getNodeMap() {
		return _oldNodeToNew;
	}
	
	public HashMap<Edge, Edge> getEdgeMap() {
		return _newEdgeToOld;
	}
	
	public Diagram clone() throws CloneNotSupportedException {
		Diagram cloned = (Diagram) super.clone();
		cloned.setRevision(getRevision());
		Collection<Node> cloned_nodes = new HashSet<Node>();
		Collection<Node> old_nodes = getNodes();
		Collection<Edge> cloned_edges = new HashSet<Edge>();
		Collection<Edge> old_edges = getEdges();
		
		HashMap<Node, Node> nodeMap = new HashMap<Node, Node>();
		HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();
		HashMap<Edge, Edge> newEdgeMap = new HashMap<Edge, Edge>();
		
		for (Node oldNode : old_nodes) {
			Node newNode = oldNode.clone();
			nodeMap.put(oldNode, newNode);
			
			cloned_nodes.add(newNode);
		}
		for (Edge oldEdge : old_edges) {
			Edge newEdge = oldEdge.clone();
			edgeMap.put(newEdge, oldEdge);
			newEdgeMap.put(oldEdge, newEdge);
			cloned_edges.add(newEdge);
		}
		
		_newEdgeToOld = edgeMap;
		_oldNodeToNew = nodeMap;
		
		for (Node oldNode : old_nodes) {
			Collection<Edge> connected = new HashSet<Edge>();
			Node newNode = nodeMap.get(oldNode);
			for(Edge e : oldNode.getConnected()) {
				connected.add(newEdgeMap.get(e));
			}
			newNode.setConnected(connected);
		}
		for (Edge newEdge : cloned_edges) {
			newEdge.setStartNode(nodeMap.get(edgeMap.get(newEdge).getStartNode()));
			newEdge.setEndNode(nodeMap.get(edgeMap.get(newEdge).getEndNode()));
		}
		
		cloned._edges = cloned_edges;
		cloned._nodes = cloned_nodes;
		return cloned;
	}
	
	/**
	 * The deterministicSimulation method returns the simulation of the FSM.  If it is an invalid FSM,
	 * then it throws an InvalidFSMException.  Else, it returns the list (in order) of objects to
	 * visit in the simulation.
	 * @param input						The input string.
	 * @return							The output list of objects to visit in the simulation.
	 * @throws InvalidDFSMException		If the FSM is invalid.
	 */
	public List<DiagramObject> deterministicSimulation(String input) throws InvalidDFSMException {
		//Temporary helper variables used throughout the method.
		//tempNode is the start node we base the simulation on. tempDest is used in the simulation
		//loop where we add objects.  Message is the error message to return.
		String tempInput;
		Node tempNode = null;
		Node tempDest = null;
		//Edge tempEdgeTaken = null;
		String message = "";
		
		/*	simulation is the list of objects to return in simulation.
		*	start_nodes contains the set of start nodes in the FSM
		*	edge_labels contains in the input alphabet (it is the union of all the edge
		*	labels seen in the FSM).
		*	temp_edge_labels is used when we go through each node to make sure that
		*	each node has all the characters in the input alphabet.
		*	already_seen is used to keep track of which error messages we have already
		*	printed out, so that we don't print out duplicate error messages for anything.
		*/
		LinkedList<DiagramObject> simulation = new LinkedList<DiagramObject>();
		Set<Node> start_nodes = Collections.synchronizedSet(new HashSet<Node>());
		Set<String> edge_labels = Collections.synchronizedSet(new HashSet<String>());
		Set<String> temp_edge_labels = Collections.synchronizedSet(new HashSet<String>());
		Set<String> already_seen = Collections.synchronizedSet(new HashSet<String>());
		
		//If the FSM is empty.
		if (_nodes.size() == 0)
			throw new InvalidDFSMException("There are no nodes in the FSM.\n");
		
		//Set tempNode to be a start node that doesn't have an empty label
		for (Node n : _nodes) {
			if (n.isStart()) {
				start_nodes.add(n);
				if (tempNode == null || (tempNode != null && tempNode.getTextField().getText().equals("")))
					tempNode = n;
			}
		}
		
		//If no start node doesn't have an empty label, then just pick any start node.
		if (tempNode == null) {
			for (Node n : _nodes) {
				if (tempNode == null && !n.getTextField().getText().equals("")) {
					tempNode = n;
					break;
				}
			}
		}
		
		//If there are no or multiple start nodes, alert the user.
		if (start_nodes.size() < 1)
			message += "There is no start node.\n";
		else if (start_nodes.size() > 1)
			message += "There are multiple start nodes.\n";
		
		//Count the number of edges without a label, and alert the user.
		int no_label_count = 0;
		for (Edge e : _edges) {
			if (e.getTextField().getText().equals(""))
				no_label_count ++;
		}
		if (no_label_count > 0)
			message += "There are " + no_label_count + " edges without a label.\n";
		
		//Make sure each edge is singly directed.
		for (Edge e : _edges) {
			if (e.getDirection() != EdgeDirection.SINGLE)
				message += !e.getTextField().getText().equals("") ? "Edge " + e.getNodeString() + " is not a singly-directed edge.\n"
														: "There is a non-singly directed edge.\n";
		}
		
		//Sets up the input alphabet (fills the edge_labels set).
		//Goes through each edge of each node, and adds that node's labels to the input
		//alphabet.  If any node has multiple edges (which we determine by checking to see
		//if they've already been added to temp_edge_labels), then we print an error to the
		//user.  Already_seen makes sure we don't print multiple messages to the user.
		for (Node n : _nodes) {
			already_seen = Collections.synchronizedSet(new HashSet<String>());
			temp_edge_labels = Collections.synchronizedSet(new HashSet<String>());
			for (Edge e : n.getConnected()) {
				if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == n && !e.getTextField().getText().equals("")) {
					//Split on commas so that the user can input multiple characters per edge
					String[] splitcomma = e.getTextField().getText().split(",");
					String[] labels = new String[splitcomma.length];
					for (int i = 0; i < splitcomma.length; i ++) {
						String s = splitcomma[i];
						String[] tempArray = s.split("(\\s*)");
						for (String inputchar : tempArray)
							labels[i] = inputchar;
					}
					for (String s : labels) {
						//If we haven't already seen this character from this node, then add it to the edge_labels (possibly already
						//there), and to temp_edge_labels, so we know if we see a duplicate.
						if (!temp_edge_labels.contains(s)) {
							edge_labels.add(s);
							temp_edge_labels.add(s);
						}
						//If we see a duplicate from a given node, check to make sure we haven't already alerted the user of this same error.
						else if (!already_seen.contains(s)) {
							message += "Node " + n.getTextField().getText() + " has multiple edges labeled " + s + ".\n";
							already_seen.add(s);
						}
					}
				}
			}
		}
		
		//For each node, make sure that it has all the labels from the input alphabet.
		//Fill up temp_edge_labels with each of the labels coming out of a node, then check
		//to make sure that each string in temp_edge_labels is in the input alphabet, and each
		//character of the input alphabet is in temp_edge_labels (these sets should be equal).
		for (Node n : _nodes) {
			already_seen = Collections.synchronizedSet(new HashSet<String>());
			temp_edge_labels = Collections.synchronizedSet(new HashSet<String>());
			for (Edge e : n.getConnected()) {
				if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == n && !e.getTextField().getText().equals("")) {
					//Split on commas so that the user can input multiple characters per edge
					String[] splitcomma = e.getTextField().getText().split(",");
					String[] labels = new String[splitcomma.length];
					for (int i = 0; i < splitcomma.length; i ++) {
						String s = splitcomma[i];
						String[] tempArray = s.split("(\\s*)");
						for (String inputchar : tempArray)
							labels[i] = inputchar;
					}
					for (String s : labels) {
						//Add all characters coming out a node to the temp_edge_labels set.
						if (!temp_edge_labels.contains(s))
							temp_edge_labels.add(s);
					}
				}
			}
			
			//mjmahone: This is not quite right: it should only fail if it hits
			//a node without an edge it needs
			//Make sure that all the strings in the input alphabet are in the temp_edge_labels, meaning
			//that this node has all characters in the input alphabet.
			/*already_seen = Collections.synchronizedSet(new HashSet<String>());
			for (String s : edge_labels) {
				if(!temp_edge_labels.remove(s)) {
					if (!already_seen.contains(s)) {
						if (!n.getTextField().getText().equals(""))
							message += "Node " + n.getTextField().getText() + " doesn't have an edge labeled " + s + ".\n";
						else
							message += "There is a node without label " + s + ".\n";
						already_seen.add(s);
					}
				}
			}*/
		}
		
		//For each character in the input string, make sure that the character is in the input alphabet.
		//If it's not and we haven't already alerted the user, then add to the error message.
		already_seen = Collections.synchronizedSet(new HashSet<String>());
		for (int i = 0; i < input.length(); i ++) {
			if (!already_seen.contains(input.substring(i, i+1)) && !edge_labels.contains(input.substring(i, i+1))) {
				message += "Input character \'" + input.substring(i, i+1) + "\' is not in the input alphabet.\n";
				already_seen.add(input.substring(i, i+1));
			}
		}
		
		//If we have found at least one error, then throw the exception.  Otherwise continue.
		if (!message.equals(""))
			throw new InvalidDFSMException(message);
		if (input.length() == 0) {
			simulation.add(tempNode);
			return simulation;
		}

		//Step through simulation, starting from the start node, progressively adding objects to the
		//set to return.
		simulation.add(tempNode);
		for (int i = 0; i < input.length(); i ++) {
			boolean tookEdge = false;
			tempInput = input.substring(i, i+1);
			for (Edge e : tempNode.getConnected()){
				String[] splitcomma = e.getTextField().getText().split(",");
				String[] labels = new String[splitcomma.length];
				for (int j = 0; j < splitcomma.length; j ++) {
					String s = splitcomma[j];
					String[] tempArray = s.split("(\\s*)");
					for (String inputchar : tempArray)
						labels[j] = inputchar;
				}
				for (String s : labels) {
					if (e.getStartNode() == tempNode && s.equals(tempInput)) {
						tempDest = e.getEndNode();
						tookEdge = true;
						//tempEdgeTaken = e;
						break;
					}
				}
			}
			if (!tookEdge) {
				String tookEdgeMessage = "Node " + tempNode.getTextField().getText(); 
				tookEdgeMessage += " doesn't have an edge labeled " + tempInput + ".\n";
				throw new InvalidDFSMException(tookEdgeMessage);
			}
			//simulation.add(tempEdgeTaken);
			simulation.add(tempDest);
			tempNode = tempDest;
		}
		return simulation;
	}
	
//	public List<Set<DiagramObject>> nondeterministicSimulation(String input) throws InvalidDFSMException {
//		String tempInput;
//		Node tempDest = null;
//		Set<DiagramObject> one_step = new HashSet<DiagramObject>();
//		LinkedList<Set<DiagramObject>> simulation = new LinkedList<Set<DiagramObject>>();
//		
//		
//		return null;
//	}

	public void setRevision(int revision) {
		_revision = revision;
	}

	public int getRevision() {
		return _revision;
	}
}
