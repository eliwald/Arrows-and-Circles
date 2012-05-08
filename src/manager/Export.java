package manager;

import java.awt.geom.Point2D;

import backend.*;

public class Export {
	private static final double TEX_SCALE = .1;
	public static String toLatex(Diagram d) {
		return
			"\\documentclass[12pt]{article}\n" +
			"\\usepackage{tikz}\n" +
			"\n" +
			"\\begin{document}\n" +
			"\n" +
			"\\begin{center}\n" +
			"\\begin{tikzpicture}[scale=0.2]\n" +
			"\\tikzstyle{every node}+=[inner sep=0pt]\n" +
			latexData(d) +
			"\\end{tikzpicture}\n" +
			"\\end{center}\n" +
			"\n" +
			"\\end{document}\n";
	}

	private static String latexData(Diagram d) {
		String dataToReturn = "";
		
		for (Edge e : d.getEdges()) {

			Point2D.Double arcCenter = new Point2D.Double(e.getArcCenter().getX(), (-1)*e.getArcCenter().getY()); // center of the arc
			Point2D.Double p = new Point2D.Double(e.getBackward().xpoints[2], (-1)*e.getBackward().ypoints[2]);
			Point2D.Double q = new Point2D.Double(e.getForward().xpoints[2], (-1)*e.getForward().ypoints[2]);
			double alpha = Math.atan2(p.getY() - arcCenter.getY(), p.getX() - arcCenter.getX());
			double beta = Math.atan2(q.getY() - arcCenter.getY(), q.getX() - arcCenter.getX());
			if(!e.getTurn() && e.getStartNode() != e.getEndNode()) {
				double tmp = alpha;
				alpha = beta;
				beta = tmp;
				Point2D.Double t = p;
				p = q;
				q = t;
			}
			
			double dx = e.getBackward().xpoints[2] - arcCenter.getX();
			double dy = (-1)*e.getBackward().ypoints[2] - arcCenter.getY();
			double radius = Math.sqrt(dx * dx + dy * dy);
			
			if(beta < alpha) {
				alpha -= Math.PI * 2;	
			}

			if (e.getStartNode() == e.getEndNode()) {
				if (alpha < - Math.PI * 2) {
					alpha += Math.PI * 2;
					beta += Math.PI*2;
				}
				if (alpha > Math.PI * 2) {
					alpha -= Math.PI * 2;
					beta -= Math.PI * 2;
				}
				System.out.println(alpha + "    " + beta);
			}
			
			double gamma = (alpha + beta) / 2;
			while(gamma >= Math.PI * 2)
				gamma -= Math.PI * 2;
						
			String labelLocation;
			if(gamma >= Math.PI / 4 && gamma <= 3 * Math.PI / 4) {
				labelLocation = "above";
			} else if(gamma >= 3 * Math.PI / 4 && gamma <= 5 * Math.PI / 4) {
				labelLocation = "left";
			} else if(gamma >= 5 * Math.PI / 4 && gamma <= 7 * Math.PI / 4) {
				labelLocation = "below";
			} else {
				labelLocation = "right";
			}
			
			double[] midpoint = new double[2];
			if(Math.abs(e.getHeight()) > 5000) {
				midpoint[0] = (p.getX() + q.getX()) / 2;
				midpoint[1] = (p.getY() + q.getY()) / 2;
			} else {
				midpoint[0] = arcCenter.getX() + (radius + 20) * Math.cos(gamma);
				midpoint[1] = arcCenter.getY() + (radius + 20) * Math.sin(gamma);
			}
			
			// Draw line and label
			if (Math.abs(e.getHeight()) > 5000)
				dataToReturn += "\\draw [black] (" + p.getX()*TEX_SCALE + "," + p.getY()*TEX_SCALE + ") -- ("
					+ q.getX()*TEX_SCALE + "," + q.getY()*TEX_SCALE + ");\n";
			else 
				dataToReturn += "\\draw [black] (" + p.getX()*TEX_SCALE + "," + p.getY()*TEX_SCALE + ") arc ("
					+ alpha*(180/Math.PI) + ":" + beta*(180/Math.PI) + ":" + radius*TEX_SCALE + ");\n";	
		
			// Draw label
			dataToReturn += "\\draw (" + midpoint[0]*TEX_SCALE + "," + midpoint[1]*TEX_SCALE + ") node [" + labelLocation + "] {$" + e.getTextField().getText() + "$};\n";
			
			// Draw forward head arrow
			if (e.getDirection() != EdgeDirection.NONE)
				dataToReturn += "\\fill [black] (" + e.getForward().xpoints[0]*TEX_SCALE + "," + (-1)*e.getForward().ypoints[0]*TEX_SCALE + ") -- ("
				+ e.getForward().xpoints[1]*TEX_SCALE + "," + (-1)*e.getForward().ypoints[1]*TEX_SCALE + ") -- (" + e.getForward().xpoints[2]*TEX_SCALE + ","
				+ (-1)*e.getForward().ypoints[2]*TEX_SCALE + ");\n";

			// Draw backward head arrow
			if (e.getDirection() == EdgeDirection.DOUBLE)
				dataToReturn += "\\fill [black] (" + e.getBackward().xpoints[0]*TEX_SCALE + "," + (-1)*e.getBackward().ypoints[0]*TEX_SCALE + ") -- ("
				+ e.getBackward().xpoints[1]*TEX_SCALE + "," + (-1)*e.getBackward().ypoints[1]*TEX_SCALE + ") -- (" + e.getBackward().xpoints[2]*TEX_SCALE + ","
				+ (-1)*e.getBackward().ypoints[2]*TEX_SCALE + ");\n";
		
		}
		
		return dataToReturn;
	}
}
