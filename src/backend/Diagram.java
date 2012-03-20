package backend;

import java.util.Collection;
import java.util.HashSet;

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

	public Object clone() {
		
	}
}