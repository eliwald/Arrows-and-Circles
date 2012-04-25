/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontend;

import java.awt.Color;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author sstudent
 */
public class ImmobileInternalFrame extends JFrame {
    public ImmobileInternalFrame()
    {
        super();
        JInternalFrame i = new JInternalFrame("",false,false,false);
        this.add(i);
        JDesktopPane _dPane = new JDesktopPane();
        _dPane.setDesktopManager( new NoDragDesktopManager() );
        this.getContentPane().add( _dPane );

        setBackground(java.awt.Color.GRAY);
        setVisible( true );
        _dPane.add(i);
        this.pack();
        setBounds(this.getLocation().x, getLocation().y, 300, 300);
    }



    private class NoDragDesktopManager extends DefaultDesktopManager
    {
        @Override
        public void beginDraggingFrame(JComponent f)
        {
            
        }

        @Override
        public void dragFrame(JComponent f, int newX, int newY)
        {
            
        }

        @Override
        public void endDraggingFrame(JComponent f)
        {
            
        }
    }

}
