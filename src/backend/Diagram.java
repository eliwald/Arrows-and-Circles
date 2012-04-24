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
		/*cloned.setNodes((Collection<Node>) _nodes.clone());
		cloned.setEdges((Collection<Edge>) _edges.clone());
		cloned.setName(getName());*/
		return cloned;
	}

	public List<DiagramObject> deterministicSimulation(String input) throws InvalidDeterministicFSMException {
		String tempInput;
		Node tempNode = null;
		Node tempDest = null;
		Edge tempEdgeTaken = null;
		LinkedList<DiagramObject> simulation = new LinkedList<DiagramObject>();
		for (Node n : _nodes) {
			if (n.isStart()) {
				if (tempNode != null)
					throw new InvalidDeterministicFSMException("Can't have multiple start states in a deterministic FSM.");
				else
					tempNode = n;
			}
		}
		if (tempNode == null)
			throw new InvalidDeterministicFSMException("Must have exactly one start state in a deterministic FSM.");
		for (int i = 0; i < input.length(); i ++) {
			tempInput = input.substring(i, i+1);
			for (Edge e : tempNode.getConnected()){
				if (e.getDirection() != EdgeDirection.SINGLE)
					throw new InvalidDeterministicFSMException("Each edge must be a singly-directed edge.");
				if (e.getEnd() == null || e.getStart() == null)
					throw new InvalidDeterministicFSMException("Each edge must have a start and an end node.");
				if (e.getStart() == tempNode && e.getLabel().equals(tempInput)) {
					if (tempDest == null && tempEdgeTaken == null) {
						tempDest = e.getEnd();
						tempEdgeTaken = e;
					}
					else
						throw new InvalidDeterministicFSMException("A single node can't go to multiple nodes on the same input.");
				}
			}
			simulation.add(tempDest);
			simulation.add(tempEdgeTaken);
		}
		return simulation;
	}

	public static void main(String[] args) {
		Diagram dia = new Diagram();

	}
}