package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import backend.Edge;
import backend.Node;

public class ChangeDefaultTextListener implements ActionListener {
	private MainFrame _main;
	private boolean _node;

	public ChangeDefaultTextListener(MainFrame main, boolean node){
		_main = main;
		_node = node;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String title = "";
		String message = "Press 'OK' to set or enter '<default>' to set to default.";
		if (_node){
			title = "Change Default Node Text";
		}
		else{
			title = "Change Default Edge Text";

		}
		String[] op = {"Cancel", "Reset", "OK"};
		/**
		 * Component parentComponent,
                                     Object message,
                                     String title,
                                     int messageType,
                                     Icon icon,
                                     Object[] selectionValues,
                                     Object initialSelectionValue
		 */
		String input = (String) (JOptionPane.showInputDialog(_main, message, title, JOptionPane.PLAIN_MESSAGE, null, null, null));
		//do nothing if user cancels
		if (input == null){
			return;
		}
		//use default keyword to reset defaults; '0' for edges and q<numnodes> for nodes.
		if (input.equals("<default>")){
			if (_node){
				Node.DEFAULT_LABEL = null;
			}
			else{
				Edge.DEFAULT_STRING = "0";
			}
		}
		else{
			if (_node){
				Node.DEFAULT_LABEL = input;
			}
			else{
				Edge.DEFAULT_STRING = input;
			}
		}

	}

}
