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
 * 
 */
public class MyDocListener implements DocumentListener{
        private JLabel _label;
        
        public MyDocListener(JLabel label){
            _label = label;
        }


		public void insertUpdate(DocumentEvent e) {
			try {
				setLabelText(e);
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			try {
				setLabelText(e);
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void changedUpdate(DocumentEvent e) {
			try {
				setLabelText(e);
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		public void setLabelText(DocumentEvent e) throws BadLocationException{
			String s = e.getDocument().getText(0, e.getDocument().getLength());
			StringBuilder html = new StringBuilder("<html>");
			int i = 0;
			while (i < e.getDocument().getLength()){
				if (s.charAt(i) != '_'){
					html.append(s.charAt(i));
					i++;
				}
				else{
					i++;
					html.append("<sub>");
					while (i < e.getDocument().getLength() && s.charAt(i) <= '9' && s.charAt(i) >= '0' ){
						html.append(s.charAt(i++));
					}
					html.append("</sub>");
				}
			}
			_label.setText(html.toString());
		}
	}