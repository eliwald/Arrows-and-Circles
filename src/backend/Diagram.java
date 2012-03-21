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

	public boolean removeNode(Node n) {
		return _nodes.remove(n);
	}

	public boolean removeEdge(Edge e) {
		return _edges.remove(e);
	}

	public Collection<Node> getNodes() {
		return _nodes;
	}

	public void setNodes(Collection<Node> nodes) {
		_nodes = nodes;
	}

	public Collection<Edge> getEdges() {
		return _edges;
	}

	public void setEdges(Collection<Edge> edges) {
		_edges = edges;
	}

	public Object clone() throws CloneNotSupportedException {
		Diagram cloned = (Diagram) super.clone();
		/*cloned.setNodes((Collection<Node>) _nodes.clone());
		cloned.setEdges((Collection<Edge>) _edges.clone());
		cloned.setName((String) getName().clone());*/
		return cloned;
	}
}