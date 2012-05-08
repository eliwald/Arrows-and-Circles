/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontend;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

import backend.Diagram;

/**
 * Text area listener that gives focus back to the given container when enter is pressed.
 * @author sstudent
 */
public class EnterListener implements KeyListener{
        private DrawingPanel _container;
        private JTextField _field;

        public EnterListener(DrawingPanel container, JTextField field){
            _container = container;
            _field = field;
        }
        
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n'){
                _field.select(0, 0);
                _container.grabFocus();
            }
        }

        public void keyPressed(KeyEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void keyReleased(KeyEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

    }