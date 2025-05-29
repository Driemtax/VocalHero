// Author: Jonas Rumpf

package utils;

import java.awt.*;
import java.awt.geom.*;

public class ScoreUtils {
    
    public void drawStaffLines(Graphics2D g2, int width, int startY, int lineSpacing) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        
        for (int i = 0; i < 5; i++) {
            int y = startY + i * lineSpacing;
            g2.drawLine(100, y, width - 100, y);
        }
    }

    public int calculateYPosition(int startY, int lineSpacing, int pitch) {
        // Assuming C4 is pitch 60 (MIDI standard)
        int C4 = startY + 5 * lineSpacing;
        return C4 - ((pitch - 60) * (lineSpacing / 2));
    }

    public void drawNote(Graphics2D g2, int x, int y, int panelHeight) {
        drawHelpLines(g2, x, y, panelHeight);

        // Note head
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        
        // Note stem
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x + 4, y, x + 4, y - 40);
    }

    private void drawHelpLines(Graphics2D g2, int x, int y, int panelHeight) {
        int lineSpacing = 20;
        int startY = panelHeight/2 - 2 * lineSpacing;
        int endY = startY + 4 * lineSpacing;
        
        if (y < startY || y > endY) {
            g2.setStroke(new BasicStroke(1f));
            
            // Help lines above staff
            if (y < startY) {
                int helpLineY = startY;
                while (helpLineY > y - lineSpacing/2) {
                    helpLineY -= lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }
            
            // Help lines below staff
            if (y > endY) {
                int helpLineY = endY;
                while (helpLineY < y + lineSpacing/2) {
                    helpLineY += lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }
        }
    }
}
