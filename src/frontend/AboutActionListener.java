package frontend;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class AboutActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame aboutFrame = new JFrame("About");
		aboutFrame.setVisible(true);
		JTabbedPane tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(400, 200));
		aboutFrame.add(tabs);
		JEditorPane edPane1 = new JEditorPane();
		edPane1.setEditable(false);
		edPane1.setContentType("text/html");
		edPane1.setText("<html> " +
				"<p><b>Note: many controls can be found immediately below the canvas while editing.</b></p><br>" +
				"<p><b>Key Controls</b><table> <tbody><tr><tr><td>Ctrl-A</td>" +
				"<td>Select all</tr></td></tr><tr> <td>Ctrl-S</td> <td>Save current tab</td> " +
				"</tr> <tr> <td>Ctrl-O</td> <td>Open tab from file</td> </tr><tr> <td>Ctrl-T</td> " +
				"<td>Open new blank tab</td>  </tr>  <tr>" +
				" <td>Ctrl-W</td> <td>Close current tab</td> </tr> <tr> <td>Ctrl-Q</td> " +
				"<td>Quit program</td>" +
				"<tr> <td>Ctrl-Z</td> <td>Undo</td>" +
				"<tr> <td>Ctrl-Y</td> <td>Redo</td> "  +
				"</tr></tbody></table></p>" + 

				"<p><b>Basic Mouse Controls (outside node/edge, no key modifiers)</b><table> <tbody><tr>" +
				"<tr> <td>Single-click</td> " +
				"<td> Deselect all</td>  </tr>" +
				"<tr> <td>Double-click</td> " +
				"<td> Create new node</td>  </tr>" +
				"<tr> <td>Drag</td> <td>Select within highlighted area</td>" +						
				"</tr></tbody></table></p>" +

				"<p><b>Basic Mouse Controls (on node, no key modifiers)</b><table> <tbody><tr>" +
				"<tr> <td>Single-click</td> " +
				"<td> Highlight this node only</td>  </tr>" +
				"<tr> <td>Double-click</td> " +
				"<td> Toggle node accept state</td>  </tr>" +
				"<tr> <td>Drag</td> <td>Move all selected nodes</td>" +						
				"</tr></tbody></table></p>" +

				"<p><b>Basic Mouse Controls (on edge, no key modifiers)</b><table> <tbody><tr>" +
				"<tr> <td>Single-click</td> " +
				"<td> Highlight this edge only</td>  </tr>" +
				"<tr> <td>Double-click</td> " +
				"<td> Create new node</td>  </tr>" +
				"<tr> <td>Drag</td> <td>Resize this edge only</td>" +						
				"</tr></tbody></table></p>" +


				"<p><b>Mouse Modifier Keys</b><table> <tbody><tr><tr> <td>Shift (hold)</td> " +
				"<td> Drag from a node to create new edge</td>  </tr>" +
				"<tr> <td>Ctrl (hold)</td> " +
				"<td> Click to select/deselect multiple nodes/edges</td>  </tr>" +
				"<tr> <td>S (press)</td> <td>Snap mouse to nearest node</td>" +						
				"</tr></tbody></table></p>"+

				"<p><b>Naming Nodes and Edges</b><table> <tbody><tr>" +
				"<tr> <td>_&#60;integer&#62;</td> " +
				"<td> Make &#60;integer&#62; subscript</td>  </tr>" +
				"<tr> <td>\\&#60;greek char name&#62;</td> " +
				"<td> Create greek character (alpha, beta, epsilon, theta only)</td>  </tr>" +
		"</tr></tbody></table></p>" );
		JScrollPane controls = new JScrollPane();
		controls.setViewportView(edPane1);
		edPane1.select(0, 0);
		controls.setName("Controls");
		edPane1.setVisible(true);
		edPane1.setSize(400,200);

		JEditorPane edPane2 = new JEditorPane();
		edPane2.setEditable(false);
		edPane2.setContentType("text/html");
		edPane2.setText("<html> <b>Developers</b><br> <br>Abhabongse (Plane) Janthong<br>Edward (Eddie) Grystar"+
		"<br>Elias (Eli) Wald <br>Sanford (Sandy) Student<br><br><b>For CS32 Spring 2012</b>");

		JScrollPane dev = new JScrollPane(edPane2);
		dev.setName("Development");
		tabs.add(dev);
		tabs.add(controls);
		aboutFrame.pack();
	}



}
