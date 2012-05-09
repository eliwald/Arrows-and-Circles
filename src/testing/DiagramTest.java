package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

import manager.DiagramProject;
import frontend.DrawingPanel;
import backend.DiagramObject;
import backend.Edge;
import backend.EdgeDirection;
import backend.InvalidDFSMException;
import backend.Node;

public class DiagramTest {

	private DrawingPanel _container;
	private Node n1, n2, n3, n4, n5, n6, n7, n8, n9, n0;
	private Edge e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25, e26, e27, e28;

	public DiagramTest() {

		_container = new DrawingPanel(DiagramProject.newProject());
		n1 = new Node(0,0,_container);
		_container.getDiagram().addNode(n1);
		n1.setStart(true);
		n2 = new Node(0,0,_container);
		_container.getDiagram().addNode(n2);
		n3 = new Node(0,0,_container);
		_container.getDiagram().addNode(n3);
		n4 = new Node(0,0,_container);
		_container.getDiagram().addNode(n4);
		n5 = new Node(0,0,_container);
		_container.getDiagram().addNode(n5);
		n6 = new Node(0,0,_container);
		_container.getDiagram().addNode(n6);
		n7 = new Node(0,0,_container);
		_container.getDiagram().addNode(n7);
		n8 = new Node(0,0,_container);
		_container.getDiagram().addNode(n8);
		n9 = new Node(0,0,_container);
		_container.getDiagram().addNode(n9);
		n0 = new Node(0,0,_container);
		_container.getDiagram().addNode(n0);



		e1 = new Edge(n1,n2,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e1);
		e1.getTextField().setText("0");
		e2 = new Edge(n2,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e2);
		e2.getTextField().setText("0");
		e3 = new Edge(n3,n5,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e3);
		e3.getTextField().setText("0");
		e4 = new Edge(n5,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e4);
		e4.getTextField().setText("0");
		e5 = new Edge(n7,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e5);
		e5.getTextField().setText("0");
		e6 = new Edge(n0,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e6);
		e6.getTextField().setText("0");
		e7 = new Edge(n9,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e7);
		e7.getTextField().setText("0");
		e8 = new Edge(n8,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e8);
		e8.getTextField().setText("0");
		e9 = new Edge(n6,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e9);
		e9.getTextField().setText("0");
		e10 = new Edge(n4,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e10);
		e10.getTextField().setText("0");



		e11 = new Edge(n1,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e11);
		e11.getTextField().setText("2");
		e12 = new Edge(n2,n5,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e12);
		e12.getTextField().setText("1,2");
		e13 = new Edge(n3,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e13);
		e13.getTextField().setText("2");
		e14 = new Edge(n5,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e14);
		e14.getTextField().setText("2");
		e15 = new Edge(n7,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e15);
		e15.getTextField().setText("2");
		e16 = new Edge(n0,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e16);
		e16.getTextField().setText("2");
		e17 = new Edge(n9,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e17);
		e17.getTextField().setText("2");
		e18 = new Edge(n8,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e18);
		e18.getTextField().setText("2");
		e19 = new Edge(n6,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e19);
		e19.getTextField().setText("2");
		e20 = new Edge(n4,n2,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e20);
		e20.getTextField().setText("1,2");



		e21 = new Edge(n1,n1,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e21);
		e21.getTextField().setText("1");
		e22 = new Edge(n3,n3,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e22);
		e22.getTextField().setText("1");
		e23 = new Edge(n0,n0,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e23);
		e23.getTextField().setText("1");
		e24 = new Edge(n8,n8,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e24);
		e24.getTextField().setText("1");
		e25 = new Edge(n5,n9,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e25);
		e25.getTextField().setText("1");
		e26 = new Edge(n9,n4,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e26);
		e26.getTextField().setText("1");
		e27 = new Edge(n6,n7,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e27);
		e27.getTextField().setText("1");
		e28 = new Edge(n7,n6,_container,EdgeDirection.SINGLE);
		_container.getDiagram().addEdge(e28);
		e28.getTextField().setText("1");
		for (Edge e : _container.getDiagram().getEdges()){
			e.getStartNode().addConnected(e);
			e.getEndNode().addConnected(e);
		}

	}

	@Test
	public void testValidLongSimulation() {




		try {
			List<DiagramObject> sim = _container.getDiagram().deterministicSimulation("001021022220220101020121200201212210");
			StringBuilder testString = new StringBuilder();
			for (DiagramObject d : sim){
				testString.append(d.getName()+"\n");
			}
			String correct = "Node: q_0\nNode: q_1\nNode: q_2\nNode: q_2\nNode: q_4\nNode: q_9\nNode: q_9\nNode: q_8\nNode: q_5\nNode: q_0\nNode: q_2\nNode: q_6\nNode: q_9"+
			"\nNode: q_7\nNode: q_3\nNode: q_0\nNode: q_0\nNode: q_1\nNode: q_4\nNode: q_6\nNode: q_8\nNode: q_7\nNode: q_7\nNode: q_3\nNode: q_1\nNode: q_4\nNode: q_6" +
			"\nNode: q_9\nNode: q_7\nNode: q_5\nNode: q_6\nNode: q_8\nNode: q_3\nNode: q_1\nNode: q_4\nNode: q_8\nNode: q_7\n";
			assertTrue(testString.toString().equals(correct));
		} catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			System.out.println(e29.getMessage());
			fail();
		}
		try {
			List<DiagramObject> sim = _container.getDiagram().deterministicSimulation("000000000011111111112222222222");
			StringBuilder testString = new StringBuilder();
			for (DiagramObject d : sim){
				testString.append(d.getName()+"\n");
			}
			String correct = "Node: q_0\nNode: q_1\nNode: q_2\nNode: q_4\nNode: q_6\nNode: q_9\nNode: q_8\nNode: q_7\nNode: q_5" +
			"\nNode: q_3\nNode: q_0\nNode: q_0\nNode: q_0\nNode: q_0\nNode: q_0\nNode: q_0\nNode: q_0\nNode: q_0\nNode: " +
			"q_0\nNode: q_0\nNode: q_0\nNode: q_2\nNode: q_6\nNode: q_8\nNode: q_5\nNode: q_0\nNode: q_2\nNode: q_6\nNode: q_8\nNode: q_5\nNode: q_0\n";
			assertTrue(testString.toString().equals(correct));
		} catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			fail();
		}


	}
	
	@Test
	public void testInvalidInput() {
		try {
			_container.getDiagram().deterministicSimulation("00076~~\nk\t\t<><<<    64547");
		} catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			String correct = "Input character '7' is not in the input alphabet.\nInput character '6' is not in the input alphabet." +
					"\nInput character '~' is not in the input alphabet.\nInput character '\n' is not in the input alphabet.\n" +
					"Input character 'k' is not in the input alphabet.\nInput character '	' is not in the input alphabet.\nInput character " +
					"'<' is not in the input alphabet.\nInput character '>' is not in the input alphabet.\nInput character ' ' is not in the input " +
					"alphabet.\nInput character '4' is not in the input alphabet.\nInput character '5' is not in the input alphabet.\n";
			assertTrue(correct.equals(e29.getMessage()));
			
		}
		try {
			_container.getDiagram().deterministicSimulation("00076464547");
		} catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			String correct = "Input character '7' is not in the input alphabet.\nInput character '6' is not in the input alphabet.\nInput character '4' is not in the input alphabet.\nInput character '5' is not in the input alphabet.\n";
			assertTrue(correct.equals(e29.getMessage()));
			
		}
	}
	@Test
	public void testBadFSM1(){
		e25.setDirection(EdgeDirection.DOUBLE);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("Edge (q_4, q_8) is not a singly-directed edge.\nNode q_4 doesn't have an edge labeled 1.\n"));
			
		}
	}
	
	@Test
	public void testBadFSM2(){
		e25.setDirection(EdgeDirection.NONE);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("Edge (q_4, q_8) is not a singly-directed edge.\nNode q_4 doesn't have an edge labeled 1.\n"));
			
		}
	}
	
	@Test
	public void testBadFSM3(){
		n3.setStart(true);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("There are multiple start nodes.\n"));			
		}
	}
	
	@Test
	public void testBadFSM4(){
		Edge e = new Edge(n1, n2, _container, EdgeDirection.SINGLE);
		_container.getDiagram().getEdges().add(e);
		e.getStartNode().addConnected(e);
		e.getEndNode().addConnected(e);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("Node q_0 has multiple edges labeled 0.\n"));			
		}
	}
	
	@Test
	public void testBadFSM5() {
		n1.setStart(false);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("There is no start node.\n"));			
		}
	}
	
	@Test
	public void testBadFSM6() {
		n2.setStart(true);
		try{
			_container.getDiagram().deterministicSimulation("001122");
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block
			assertTrue(e29.getMessage().equals("There are multiple start nodes.\n"));			
		}
	}
	
	@Test
	public void testEndState() {
		n3.setEnd(true);
		try{
			List<DiagramObject> sim = _container.getDiagram().deterministicSimulation("00");
			Node n = (Node)sim.get(sim.size()-1);
			assertTrue(n.isEnd());
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block		
		}
		
		n3.setEnd(true);
		try{
			List<DiagramObject> sim = _container.getDiagram().deterministicSimulation("000");
			Node n = (Node)sim.get(sim.size()-1);
			assertFalse(n.isEnd());
		}catch (InvalidDFSMException e29) {
			// TODO Auto-generated catch block		
		}
	}

}
