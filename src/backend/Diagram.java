package backend;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;

public class Diagram implements Cloneable {
	private Collection<Node> _nodes;
	private Collection<Edge> _edges;
	private String _name;

	public Diagram() {
		_nodes = new HashSet<Node>();
		_edges = new HashSet<Edge>();
		_name = "";
	}

	public String getName(){
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean addNode(Node n) {
		return _nodes.add(n);
	}

	public boolean addEdge(Edge e) {
		return _edges.add(e);
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

	public Object clone() throws CloneNotSupportedException {
		Diagram cloned = (Diagram) super.clone();
		cloned.setName(getName());
		Collection<Node> cloned_nodes = new HashSet<Node>();
		Collection<Node> old_nodes = getNodes();
		Collection<Edge> cloned_edges = new HashSet<Edge>();
		Collection<Edge> old_edges = getEdges();
		for (Node n : old_nodes) {
			cloned_nodes.add((Node) n.clone());
		}
		for (Edge e : old_edges) {
			cloned_edges.add((Edge) e.clone());
		}
		return cloned;
	}

	public List<DiagramObject> deterministicSimulation(String input) throws InvalidDFSMException {
		String tempInput;
		Node tempNode = null;
		Node tempDest = null;
		Edge tempEdgeTaken = null;
		String message = "";
		LinkedList<DiagramObject> simulation = new LinkedList<DiagramObject>();
		HashSet<Node> start_nodes = new HashSet<Node>();
		HashSet<String> edge_labels = new HashSet<String>();
		HashSet<String> temp_edge_labels = new HashSet<String>();

		if (_nodes.size() == 0)
			throw new InvalidDFSMException("There are no nodes in the FSM.\n");

		for (Node n : _nodes) {
			if (n.isStart()) {
				start_nodes.add(n);
				if (tempNode == null || (tempNode != null && tempNode.getLabel().equals("")))
					tempNode = n;
			}
		}

		if (tempNode == null) {
			for (Node n : _nodes) {
				if (tempNode == null && !n.getLabel().equals("")) {
					tempNode = n;
					break;
				}
			}
		}

		if (tempNode == null) {
			for (Node n : _nodes) {
				tempNode = n;
				break;
			}
		}

		if (start_nodes.size() < 1)
			message += "There is no start node.\n";
		else if (start_nodes.size() > 1)
			message += "There are multiple start nodes.\n";

		for (Edge e : _edges) {
			if (e.getLabel().equals(""))
				message += "There is an edge without a label.\n";
		}
		
		for (Edge e : _edges) {
			if (e.getDirection() != EdgeDirection.SINGLE)
				message += !e.getLabel().equals("") ? "Edge " + e.getLabel() + " is not a singly-directed edge.\n"
														: "There is a non-singly directed edge.\n";
			if (e.getStartNode() == null || e.getEndNode() == null)
				message += !e.getLabel().equals("") ? "Edge " + e.getLabel() + " doesn't have a start or end node.\n"
														: "There is an edge without a start or end node.\n";
		}

		for (Edge e : tempNode.getConnected()) {
			if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == tempNode && !e.getLabel().equals("")) {
				if (!edge_labels.contains(e.getLabel()))
					edge_labels.add(e.getLabel());
				else
					message += "Node has two edges labeled " + e.getLabel() + ".\n";
			}
		}

		for (Node n : _nodes) {
			for (Edge e : n.getConnected()) {
				if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == n)
					temp_edge_labels.add(e.getLabel());
			}

			for (String s : edge_labels) {
				if(!temp_edge_labels.remove(s)) {
					if (!n.getLabel().equals(""))
						message += "Node " + n.getLabel() + " doesn't have an edge labeled " + s + ".\n";
					else
						message += "There is a node without label " + s + ".\n";
				}
			}

			for (String s : temp_edge_labels) {
				if (!n.getLabel().equals(""))
					message += "Node " + n.getLabel() + " has edge labeled " + s + ", which is a duplicate or is not in input alphabet.\n";
				else
					message += "There is a node with an edge labeled " + s + ", which is a duplicate or is not in input alphabet.\n";
			}
		}

		for (int i = 0; i < input.length(); i ++) {
			if (!edge_labels.contains(input.substring(i, i+1)))
				message += "Input character \'" + input.substring(i, i+1) + "\' is not in the input alphabet.\n";
		}

		if (!message.equals(""))
			throw new InvalidDFSMException(message);
		for (int i = 0; i < input.length(); i ++) {
			tempInput = input.substring(i, i+1);
			for (Edge e : tempNode.getConnected()){
				if (e.getStartNode() == tempNode && e.getLabel().equals(tempInput)) {
					tempDest = e.getEndNode();
					tempEdgeTaken = e;
					break;
				}
			}
			simulation.add(tempEdgeTaken);
			simulation.add(tempDest);
			tempNode = tempDest;
		}
		return simulation;
	}

	public static void main(String[] args) {
		Diagram dia = new Diagram();
		Node _q1 = new Node(0, 0);
		_q1.setStart(true);
		_q1.setLabel("Q_1");
	}
}