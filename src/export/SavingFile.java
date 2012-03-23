package export;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import backend.*;

public class SavingFile {

	public static String saving(Diagram g) {
		
		Collection<Node> nodes = g.getNodes();
		Collection<Edge> edges = g.getEdges();
		int nodeCounter = 0;
		int edgeCounter = 0;
		Map<Integer, Integer> nodesMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> edgesMap = new HashMap<Integer, Integer>();

		String xml = "<diagram><edgeset>";
		for(Edge edge : edges) {
			int edgeID = edgeCounter++;
			edgesMap.put(edge.hashCode(), edgeID);
			xml += "<edge id=\"" + edgeID + "\"></edge>";
		}
		
		xml += "</edgeset><nodeset>";
		
		for(Node node : nodes) {
			int nodeID = nodeCounter++;
			nodesMap.put(node.hashCode(), nodeID);
			xml += "<node id=\"" + nodeID + "\">"
					+ "<center_x>" + node.getCenter().getX() + "</center_x>"
					+ "<center_y>" + node.getCenter().getY() + "</center_y>"
					+ "<radius>" + node.getRadius() + "</radius>"
					+ "<label>" + node.getLabel() + "</label>"
					+ "<rgb>" + node.getColor().getRGB() + "</rgb>"
					+ "<label>" + node.getLabel() + "</label>" ;
			if(node.isStart()) {
				xml += "<startstate></startstate>";
			}
			if(node.isEnd()) {
				xml += "<endstate></endstate>";
			}
			xml += "<incident>";
			for(Edge incidentEdge : node.getConnected()) {
				xml += "<edge id=\"" + edgesMap.get(incidentEdge.hashCode()) + "\"></edge>";
			}
			xml += "</incident></node>";
		}
		xml += "</nodeset></diagram>";
		
		return xml;
	}
}
