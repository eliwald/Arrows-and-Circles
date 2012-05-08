package testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import manager.DiagramProject;

import frontend.DrawingPanel;

import backend.DiagramObject;
import backend.Edge;
import backend.EdgeDirection;
import backend.InvalidDFSMException;
import backend.Node;
import junit.framework.TestCase;

public class DiagramTest extends TestCase {
	
	private Collection<Node> _nodes;
	private Collection<Edge> _edges;
	private DrawingPanel _container;

	public DiagramTest(String arg0) {
		super(arg0);
		_nodes = new ArrayList<Node>();
		_edges = new ArrayList<Edge>();
		_container = new DrawingPanel(DiagramProject.newProject());
		
		
	}

	public void testDeterministicSimulation() {
		Node n1 = new Node(0,0,_container);
		_container.getDiagram().addNode(n1);
		Node n2 = new Node(0,0,_container);
		_container.getDiagram().addNode(n2);
		Node n3 = new Node(0,0,_container);
		_container.getDiagram().addNode(n3);
		Node n4 = new Node(0,0,_container);
		_container.getDiagram().addNode(n4);
		Node n5 = new Node(0,0,_container);
		_container.getDiagram().addNode(n5);
		Node n6 = new Node(0,0,_container);
		_container.getDiagram().addNode(n6);
		Node n7 = new Node(0,0,_container);
		_container.getDiagram().addNode(n7);
		Node n8 = new Node(0,0,_container);
		_container.getDiagram().addNode(n8);
		Node n9 = new Node(0,0,_container);
		_container.getDiagram().addNode(n9);
		Node n0 = new Node(0,0,_container);
		_container.getDiagram().addNode(n0);
		Edge e1 = new Edge(n1,n2,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e1);
		e1.setLabel("0");
		Edge e2 = new Edge(n2,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e2);
		e2.setLabel("0");
		Edge e3 = new Edge(n3,n5,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e3);
		e3.setLabel("0");
		Edge e4 = new Edge(n5,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e4);
		e4.setLabel("0");
		Edge e5 = new Edge(n7,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e5);
		e5.setLabel("0");
		Edge e6 = new Edge(n0,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e6);
		e6.setLabel("0");
		Edge e7 = new Edge(n9,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e7);
		e7.setLabel("0");
		Edge e8 = new Edge(n8,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e8);
		e8.setLabel("0");
		Edge e9 = new Edge(n6,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e9);
		e9.setLabel("0");
		Edge e10 = new Edge(n4,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e10);
		e10.setLabel("0");
		
		
		
		Edge e11 = new Edge(n1,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e11);
		e11.setLabel("2");
		Edge e12 = new Edge(n2,n5,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e12);
		e12.setLabel("1,2");
		Edge e13 = new Edge(n3,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e13);
		e13.setLabel("2");
		Edge e14 = new Edge(n5,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e14);
		e14.setLabel("2");
		Edge e15 = new Edge(n7,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e15);
		e15.setLabel("2");
		Edge e16 = new Edge(n0,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e16);
		e16.setLabel("2");
		Edge e17 = new Edge(n9,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e17);
		e17.setLabel("2");
		Edge e18 = new Edge(n8,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e18);
		e18.setLabel("2");
		Edge e19 = new Edge(n6,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e19);
		e19.setLabel("2");
		Edge e20 = new Edge(n4,n2,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e20);
		e20.setLabel("1,2");
		
		
		
		Edge e21 = new Edge(n1,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e21);
		e21.setLabel("1");
		Edge e22 = new Edge(n3,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e22);
		e22.setLabel("1");
		Edge e23 = new Edge(n0,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e23);
		e23.setLabel("1");
		Edge e24 = new Edge(n8,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e24);
		e24.setLabel("1");
		Edge e25 = new Edge(n5,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e25);
		e25.setLabel("1");
		Edge e26 = new Edge(n9,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e26);
		e26.setLabel("1");
		Edge e27 = new Edge(n6,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e27);
		e27.setLabel("1");
		Edge e28 = new Edge(n7,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e28);
		e28.setLabel("1");
		
		for (Edge e : _container.getDiagram().getEdges()){
			e.getStartNode().addConnected(e);
			e.getEndNode().addConnected(e);
		}
		
		try {
			List<DiagramObject> sim = _container.getDiagram().deterministicSimulation("001021022220220101020121200201212210");
			for (DiagramObject d : sim){
				System.out.println(d.getName());
			}
		} catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assert(false);
		}
		
		
	}

}
