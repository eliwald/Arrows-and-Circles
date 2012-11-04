package manager;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import frontend.DrawingPanel;

import backend.*;

public class Export {
	private static final double TEX_SCALE = .1;
	private static final double SMALLER_RADIUS = 4;
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
		
		// For each edges
		for (Edge e : d.getEdges()) {
			
			// Getting center of the arc, and the tips of both ends
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
			
			// Obtain whether the edge is a straight line
			boolean isStraight = Math.abs(e.getHeight()) > 5000 && e.getStartNode() != e.getEndNode();
			
			// Find the radius of the arc
			double dx = e.getBackward().xpoints[2] - arcCenter.getX();
			double dy = (-1)*e.getBackward().ypoints[2] - arcCenter.getY();
			double radius = Math.sqrt(dx * dx + dy * dy);
			
			// Obtain the correct angle
			if(beta < alpha) {
				alpha -= Math.PI * 2;	
			}
			if (alpha < -Math.PI * 2) {
				alpha += Math.PI * 2;
				beta += Math.PI*2;
			}
			if (alpha > Math.PI * 2) {
				alpha -= Math.PI * 2;
				beta -= Math.PI * 2;
			}
			
			// Find the label location
			double[] midpoint = new double[2];
			String labelLocation = "";
			if(isStraight) { // In the case of straight line
				
				double[] halfpq = {
					(q.getX() - p.getX()) / 2,
					(q.getY() - p.getY()) / 2
				};
				double halfpqSize = Math.sqrt(halfpq[0] * halfpq[0] + halfpq[1] * halfpq[1]);
				
				// Find the exact point of the label
				midpoint[0] = (e.getTurn() ? -1 : 1) * (halfpq[1]) / halfpqSize * 25 + halfpq[0] + p.getX();
				midpoint[1] = (e.getTurn() ? 1 : -1) * (halfpq[0]) / halfpqSize * 25 + halfpq[1] + p.getY();
			}
			else { // In the case of the arc
				
				// Find the relative label position to the midpoint.
				double gamma = (alpha + beta) / 2;
				while(gamma >= Math.PI * 2)
					gamma -= Math.PI * 2;
				if(gamma >= Math.PI / 4 && gamma <= 3 * Math.PI / 4) {
					labelLocation = "above";
				} else if(gamma >= 3 * Math.PI / 4 && gamma <= 5 * Math.PI / 4) {
					labelLocation = "left";
				} else if(gamma >= 5 * Math.PI / 4 && gamma <= 7 * Math.PI / 4) {
					labelLocation = "below";
				} else {
					labelLocation = "right";
				}
				
				// Find the midpoint of the arc.
				midpoint[0] = arcCenter.getX() + (radius + 20) * Math.cos(gamma);
				midpoint[1] = arcCenter.getY() + (radius + 20) * Math.sin(gamma);
			}
			
			// Draw line and label
			if (isStraight) {
				dataToReturn += "\\draw [black] (" + p.getX()*TEX_SCALE + "," + p.getY()*TEX_SCALE + ") -- ("
					+ q.getX()*TEX_SCALE + "," + q.getY()*TEX_SCALE + ");\n";
				dataToReturn += "\\draw (" + midpoint[0]*TEX_SCALE + "," + midpoint[1]*TEX_SCALE + ") node {$" + e.getTextField().getText() + "$};\n";
			}
			else {
				dataToReturn += "\\draw [black] (" + p.getX()*TEX_SCALE + "," + p.getY()*TEX_SCALE + ") arc ("
					+ alpha*(180/Math.PI) + ":" + beta*(180/Math.PI) + ":" + radius*TEX_SCALE + ");\n";	// Draw label
				dataToReturn += "\\draw (" + midpoint[0]*TEX_SCALE + "," + midpoint[1]*TEX_SCALE + ") node [" + labelLocation + "] {$" + e.getTextField().getText() + "$};\n";
			}
			
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
		
		// For each node
		for (Node n : d.getNodes()) {
			Point2D.Double center = new Point2D.Double(n.getCenter().getX(), -n.getCenter().getY());
			double radius = n.getRadius();
			double minorRadius = n.getRadius() - SMALLER_RADIUS;
			
			// Draw main circle
			dataToReturn += "\\draw [black] (" + center.getX()*TEX_SCALE + "," + center.getY()*TEX_SCALE + ") circle (" + radius*TEX_SCALE + ");\n";
			
			// Draw smaller circle
			if(n.isEnd()) {
				dataToReturn += "\\draw [black] (" + center.getX()*TEX_SCALE + "," + center.getY()*TEX_SCALE + ") circle (" + minorRadius*TEX_SCALE + ");\n";
			}
			
			// Draw start triangle
			if(n.isStart()) {
				dataToReturn += "\\draw (" + n.getStartSymbol().xpoints[0]*TEX_SCALE + "," + (-1)*n.getStartSymbol().ypoints[0]*TEX_SCALE + ") -- ("
				+ n.getStartSymbol().xpoints[1]*TEX_SCALE + "," + (-1)*n.getStartSymbol().ypoints[1]*TEX_SCALE + ") -- (" + n.getStartSymbol().xpoints[2]*TEX_SCALE + ","
				+ (-1)*n.getStartSymbol().ypoints[2]*TEX_SCALE + ") -- cycle;\n";
			}
			
			// Draw label
			dataToReturn += "\\draw (" + center.getX()*TEX_SCALE + "," + center.getY()*TEX_SCALE + ") node {$" + n.getTextField().getText() + "$};\n";
		}
		
		return dataToReturn;
	}
	
	public static void writeImage(DrawingPanel toWrite, File file) {
		BufferedImage bi = new BufferedImage(toWrite.getSize().width, toWrite.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		toWrite.paint(g);
		g.dispose();
		try{
			ImageIO.write(bi, "png", file);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
