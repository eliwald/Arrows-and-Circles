/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drawing;

/**
 *
 * @author sstudent
 */
public abstract class DrawingShape {
/**
 * Welcome to the beginning of your very own graphics package!
 * This graphics package will be used in most of your assignments from
 * now on.
 *
 * This should look A LOT like the code you've seen in lecture (HINT HINT).
 * At first glance this class looks really dense, but don't worry- most of
 * the methods you have to fill in won't be very long.
 *
 * REMEMBER most of the code you will write here will be code you've seen
 * before (WINK WINK).
 *
 * All of the accessor methods return a dummy value so that the code is
 * compilable to begin with. All of the non-optional accessor methods
 * will need to be modified.
 *
 * Feel free to add other functionality, but keep in mind this is a shape
 * and shouldn't have capabilities that are more specific to say
 * bees or something.
 *
 * Some bells and whistles you might want to add:
 *  - set transparency (look at java.awt.Color in the docs)
 *  - anti aliasing (getting rid of jagged edges..)
 *  - changing border width
 *
 * @sstudent
 */

    /** Used to store some geometric data for this shape. */
    private java.awt.geom.RectangularShape _shape;

    /** Reference to containing subclass of JPanel. */
    private javax.swing.JPanel _panel;

    /** Border and Fill Colors. */
    private java.awt.Color _borderColor, _fillColor;

    /** Rotation (must be in radians). */
    private double _rotationAngle;

    /** Indicates whether or not the shape should wrap. */
    private boolean _wrapping;

    /** Whether or not the shape should paint itself. */
    private boolean _isVisible;

    /**
     * Initialize all instance variables here.  You'll need to store the
     * containing subclass of JPanel to deal with wrapping and some of the
     * extra credit stuff.
     */
    private int _borderWidth;

    public DrawingShape(javax.swing.JPanel container, java.awt.geom.RectangularShape s) {
    	_panel = container;
    	_shape = s;
    	_fillColor = new java.awt.Color (0, 0, 0);
    	_borderColor = new java.awt.Color (0, 0, 0);
    	_rotationAngle = 0;
    	_wrapping = true;
		_isVisible = true;
		_borderWidth = 0;
    }

    /** Should return the x location of the top left corner of
     * shape's bounding box. */
    public double getX() {
    	return _shape.getX();
    }

    /** Should return the y location of the top left corner of
     * shape's bounding box. */
    public double getY() {
    	return _shape.getY();
    }

    /** Should return height of shape's bounding box. */
    public double getHeight() {
    	return _shape.getHeight();
    }

    /** Should return width of shape's bounding box. */
    public double getWidth() {
    	return _shape.getWidth();
    }

    /** Should return the border color you are storing. */
    public java.awt.Color getBorderColor() {
    	return _borderColor;
    }

    /** Should return the fill color you are storing. */
    public java.awt.Color getFillColor() {
    	return _fillColor;
    }

    /** Should return the rotation you are storing. */
    public double getRotation() {
    	return _rotationAngle*180/Math.PI;
    }

    /** Optional.  Should return the width of the brush stroke for
     * the outline of your shape. */
    public int getBorderWidth() {
    	return _borderWidth;
    }

    /** Should return whether or not the shape is wrapping. */
    public boolean getWrapping() {
    	return _wrapping;
    }

    /** Should return whether or not the shape is visible. */
    public boolean getVisible() {
    	return _isVisible;
    }

    /**
     * Set the location of shape. Make sure to wrap if the wrap
     * boolean is true.
     */
    public void setLocation(double x, double y) {
	if (_wrapping) {
	    int containerWidth = _panel.getWidth();
	    int containerHeight = _panel.getHeight() ;
	    _shape.setFrame((x + containerWidth) % containerWidth, (y + containerHeight) % containerHeight, _shape.getWidth(), _shape.getHeight());
	}
	else {_shape.setFrame(x, y, _shape.getWidth(), _shape.getHeight());
        }
    }

    /** Set the size of shape. */
    public void setSize(double width, double height) {
    	_shape.setFrame(_shape.getX(), _shape.getY(), width, height);
    }

    /** Set the border color. */
    public void setBorderColor(java.awt.Color c) {
    	_borderColor = c;
    }

    /** Set the fill color. */
    public void setFillColor(java.awt.Color c) {
    	_fillColor = c;
    }

    /** Set the color of the whole shape. */
    public void setColor(java.awt.Color c) {
    	_fillColor = c;
    	_borderColor = c;
    }

    /** Set the rotation of the shape. Refer to the lecture to see how this
     *  should be done */
    public void setRotation(double degrees) {
    	_rotationAngle = degrees*Math.PI/180;
    }

    /** Optional: set how thick the shapes outline will be. */
    public void setBorderWidth(int width) {
    	_borderWidth = width;
    }

    /** Set whether or not the shape should wrap. */
    public void setWrapping(boolean wrap) {
    	_wrapping = wrap;
    }

    /** Set whether or not the shape should paint itself. */
    public void setVisible(boolean visible) {
    	_isVisible = visible;
    }

    /**
     * This method is best explained in pseudo code:
     *	If shape is visible
     *	    rotate graphics
     *	    set the brush stroke (width) of the graphics (optional)
     *	    set the color of the graphics to the fill color of the shape
     *	    fill the shape
     *	    set the color of the graphics to the border color of the shape
     *	    draw the shape
     *	    un-rotate the graphics
     */
    public void paint(java.awt.Graphics2D brush) {
	if (_isVisible) {
	    brush.rotate(_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
	    java.awt.BasicStroke s = new java.awt.BasicStroke(_borderWidth);
	    brush.setStroke(s);
	    brush.setColor(_borderColor);
	    brush.draw(_shape);
	    brush.setColor(_fillColor);
	    brush.fill(_shape);
	    brush.rotate(-_rotationAngle, _shape.getCenterX(), _shape.getCenterY());

	}
    }

    /**
     * Should return true if the point is within the shape.
     * There's a special case for when the shape is rotated which you will
     * hear about in lab.  This doesn't need to be done for Cartoon,
     * but it will be required for Swarm.
     */
    public boolean contains(java.awt.Point p) {
    	if (_shape.contains(p)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }



}
