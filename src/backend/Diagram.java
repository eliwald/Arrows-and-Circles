package backend;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

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
		Set<Node> start_nodes = Collections.synchronizedSet(new HashSet<Node>());
		Set<String> edge_labels = Collections.synchronizedSet(new HashSet<String>());
		Set<String> temp_edge_labels = Collections.synchronizedSet(new HashSet<String>());
		Set<String> already_seen = Collections.synchronizedSet(new HashSet<String>());

		if (_nodes.size() == 0)
			throw new InvalidDFSMException("There are no nodes in the FSM.\n");

		for (Node n : _nodes) {
			if (n.isStart()) {
				start_nodes.add(n);
				if (tempNode == null || (tempNode != null && tempNode.getTextField().getText().equals("")))
					tempNode = n;
			}
		}

		if (tempNode == null) {
			for (Node n : _nodes) {
				if (tempNode == null && !n.getTextField().getText().equals("")) {
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
		
		int no_label_count = 0;
		for (Edge e : _edges) {
			if (e.getTextField().getText().equals(""))
				no_label_count ++;
		}
		if (no_label_count > 0)
			message += "There are " + no_label_count + " edges without a label.\n";
		
		for (Edge e : _edges) {
			if (e.getDirection() != EdgeDirection.SINGLE)
				message += !e.getTextField().getText().equals("") ? "Edge " + e.getTextField().getText() + " is not a singly-directed edge.\n"
														: "There is a non-singly directed edge.\n";
			if (e.getStartNode() == null || e.getEndNode() == null)
				message += !e.getTextField().getText().equals("") ? "Edge " + e.getTextField().getText() + " doesn't have a start or end node.\n"
														: "There is an edge without a start or end node.\n";
		}

		for (Edge e : tempNode.getConnected()) {
			if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == tempNode && !e.getTextField().getText().equals("")) {
				if (!edge_labels.contains(e.getTextField().getText()))
					edge_labels.add(e.getTextField().getText());
				else {
					if (!already_seen.contains(e.getTextField().getText()))
						message += "Start node (if exists) has multiple edges labeled " + e.getTextField().getText() + ".\n";
					already_seen.add(e.getTextField().getText());
				}
			}
		}
		
		for (Node n : _nodes) {
			if (n != tempNode) {
				already_seen = Collections.synchronizedSet(new HashSet<String>());
				for (Edge e : n.getConnected()) {
					if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() == n)
						if (temp_edge_labels.contains(e.getTextField().getText())) {
							if (!already_seen.contains(e.getTextField().getText())) {
								if (!n.getTextField().getText().equals(""))
									message += "Node " + n.getTextField().getText() + " has a duplicate edge labeled " + e.getTextField().getText() + ".\n";
								else
									message += "There is a node with a duplicate label " + e.getTextField().getText() + ".\n";
								already_seen.add(e.getTextField().getText());
							}
						}						
						else
							temp_edge_labels.add(e.getTextField().getText());
				}
				already_seen = Collections.synchronizedSet(new HashSet<String>());
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
				}

				for (String s : temp_edge_labels) {
					if (!n.getTextField().getText().equals(""))
						message += "Node " + n.getTextField().getText() + " has edge labeled " + s + ", which is not in input alphabet.\n";
					else
						message += "There is a node with an edge labeled " + s + ", which is not in input alphabet.\n";
					temp_edge_labels.remove(s);
				}
			}
		}
		already_seen = Collections.synchronizedSet(new HashSet<String>());
		for (int i = 0; i < input.length(); i ++) {
			if (!already_seen.contains(input.substring(i, i+1)) && !edge_labels.contains(input.substring(i, i+1))) {
				message += "Input character \'" + input.substring(i, i+1) + "\' is not in the input alphabet.\n";
				already_seen.add(input.substring(i, i+1));
			}
		}

		if (!message.equals(""))
			throw new InvalidDFSMException(message);
		if (input.length() == 0) {
			simulation.add(tempNode);
			return simulation;
		}
		
		simulation.add(tempNode);
		for (int i = 0; i < input.length(); i ++) {
			tempInput = input.substring(i, i+1);
			for (Edge e : tempNode.getConnected()){
				if (e.getStartNode() == tempNode && e.getTextField().getText().equals(tempInput)) {
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
}