/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontend;

import backend.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author egrystar
 */
public class MyDocListener implements DocumentListener{
        private JLabel _label;
        
        public MyDocListener(JLabel label){
            _label = label;
        }


		public void insertUpdate(DocumentEvent e) {
			try {
				_label.setText(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			try {
				_label.setText(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void changedUpdate(DocumentEvent e) {
			try {
				_label.setText(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}