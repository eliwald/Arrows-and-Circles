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
public class HTMLParser implements DocumentListener{
        private JLabel _label;
        
        public HTMLParser(JLabel label){
            _label = label;
        }


		public void insertUpdate(DocumentEvent e) {
			try {
				_label.setText(setLabelText(e.getDocument().getText(0, e.getDocument().getLength())));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			try {
				_label.setText(setLabelText(e.getDocument().getText(0, e.getDocument().getLength())));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public void changedUpdate(DocumentEvent e) {
			try {
				_label.setText(setLabelText(e.getDocument().getText(0, e.getDocument().getLength())));
			} catch (BadLocationException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		/**
		 * This method converts a string into an html string that displays subscripted integers and select greek characters.
		 * @param e
		 * @throws BadLocationException
		 */
		
		public static String setLabelText(String s) throws BadLocationException{
			StringBuilder html = new StringBuilder("<html>");
			int i = 0;
			String[] _greekChars = {"beta", "alpha", "epsilon", "theta", "Theta"};
			String[] _greekCodes = {"&#x03B2;", "&#x03B1;","&#x03B5;", "&#x03B8;", "&#x0398;"};
			while (i < s.length()){
				if (s.charAt(i) != '_'){
					if (s.charAt(i)=='/'){
						//special case because html.
						html.append("&#x2F;");
					}
					else if (s.charAt(i)!= '\\'){
						html.append(s.charAt(i));
					}
					else {
						for (int j = 0; j < _greekChars.length; j++){
							String greekChar = _greekChars[j];
							if (i+greekChar.length() < s.length() && s.substring(i+1, i+greekChar.length()+1).equals(greekChar)){
								i+=greekChar.length();
								html.append(_greekCodes[j]);
							}
						}
					}
					i++;
				}
				else{
					i++;
					html.append("<sub>");
					while (i < s.length() && s.charAt(i) <= '9' && s.charAt(i) >= '0' ){
						html.append(s.charAt(i++));
					}
					html.append("</sub>");
				}
			}
			return html.toString();
		}
	}