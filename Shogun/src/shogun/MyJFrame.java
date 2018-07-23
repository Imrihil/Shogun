/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;

/**
 *
 * @author Imrihil
 */
public class MyJFrame extends javax.swing.JFrame implements MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {

    private int startX = 0;
    private int startY = 0;

    private int mx;
    private int my;

    private int doubleClickX = 0;
    private int doubleClickY = 0;

    /**
     * Creates new form MyJFrame
     */
    public MyJFrame() {
        initComponents();
        addMouseListener((MouseListener) this);
        addMouseMotionListener((MouseMotionListener) this);
        addMouseWheelListener((MouseWheelListener) this);
        addKeyListener((KeyListener) this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Shogun.resolutionX, Shogun.resolutionY);
        this.setVisible(true);
        this.setResizable(true);
        this.setTitle("Shogun");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Dragged");
        Shogun.sem.acquireUninterruptibly();
        Shogun.shiftX += e.getX() - startX;
        Shogun.shiftY += e.getY() - startY;
        startX = e.getX();
        startY = e.getY();
        Shogun.sem.release();
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Shogun.sem.acquireUninterruptibly();
        //System.out.printf("Moved on %d, %d\n", e.getX(), e.getY());
        if (Shogun.step == 1) {
            GameStep1Welcome.mouseMoved(e);
        } else if (Shogun.step == 2) {
            GameStep2Options.mouseMoved(e);
        } else if (Shogun.step == 3) {
            GameStep3Game.mouseMoved(e);
        }
        mx = e.getX();
        my = e.getY();
        if (e.getX() != doubleClickX || e.getY() != doubleClickY) {
            doubleClickX = 0;
            doubleClickY = 0;
        }
        Shogun.sem.release();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Shogun.sem.acquireUninterruptibly();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getX() == doubleClickX && e.getY() == doubleClickY) {
                doubleClick(e);
                doubleClickX = 0;
                doubleClickY = 0;
            } else {
                doubleClickX = e.getX();
                doubleClickY = e.getY();
            }
        } else {
            doubleClickX = 0;
            doubleClickY = 0;
        }
        if (Shogun.step == 1) {
            GameStep1Welcome.mouseClicked(e);
        } else if (Shogun.step == 2) {
            GameStep2Options.mouseClicked(e);
        } else if (Shogun.step == 3) {
            GameStep3Game.mouseClicked(e);
        } else {
            Shogun.step = Shogun.step % 3 + 1;
        }
        Shogun.sem.release();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Shogun.sem.acquireUninterruptibly();
        //System.out.println("Pressed");
        startX = e.getX();
        startY = e.getY();
        Shogun.sem.release();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("Exited");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Shogun.sem.acquireUninterruptibly();
        //System.out.println("KeyTyped");
        if (Shogun.step == 2) {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                Shogun.step = 1;
            }
        } else if (Shogun.step == 3) {
            GameStep3Game.KeyTyped(e, getMx(), getMy());
        }
        Shogun.sem.release();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("KeyPressed");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("KeyReleased");
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Shogun.sem.acquireUninterruptibly();
        if (Shogun.step == 3) {
            GameStep3Game.mouseWheelMoved(e, this);
        }
        Shogun.sem.release();
    }

    public void doubleClick(MouseEvent e) {
        if (Shogun.step == 3) {
            GameStep3Game.mouseDoubleClicked(e);
        }
    }

    /**
     * @return the mx
     */
    public int getMx() {
        return mx;
    }

    /**
     * @return the my
     */
    public int getMy() {
        return my;
    }
}
