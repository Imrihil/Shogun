/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 *
 * @author Imrihil
 */
public class Picture extends Frame {
    
    private Image img;

    public Picture(String path) {
        URL myurl = this.getClass().getResource(path);
        Toolkit tk = this.getToolkit();
        img = tk.getImage(myurl);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getImg(), 0, 0, this);
    }
    
    public void paint(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, this);
    }

    /**
     * @return the img
     */
    public Image getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
