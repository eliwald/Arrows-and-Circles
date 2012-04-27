/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontend;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Text area listener that gives focus back to the given container when enter is pressed.
 * @author sstudent
 */
public class EnterListener implements KeyListener{
        private DrawingPanel _container;

        public EnterListener(DrawingPanel container){
            _container = container;
        }
        
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n'){
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