package org.kunze.diansh.salesTicket;


import javax.swing.*;
import java.awt.*;

/**
 * 内容换行问题
 */
public class SalesTicketDrawString {

    public static int drawString(Graphics2D g , Font font , String text , int x , int y , int maxWidth){
        JLabel label = new JLabel(text);
        label.setFont(font);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textH = metrics.getHeight();
        int textW = metrics.stringWidth(label.getText());
        maxWidth = maxWidth-5;
        String tempText = text;
        int row = textW / maxWidth;
        while (textW > maxWidth){
            int n = textW / maxWidth;
            int subPos = tempText.length() / n;
            String drawText = tempText.substring(0,subPos);
            int subTxtW = metrics.stringWidth(drawText);
            while (subTxtW > maxWidth){
                subPos--;
                drawText = tempText.substring(0,subPos);
                subTxtW = metrics.stringWidth(drawText);
            }
            g.drawString(drawText,x,y);
            y += textH;
            textW = textW - subTxtW;
            tempText = tempText.substring(subPos);
        }
        g.drawString(tempText,x,y);

        return row;
    }

}
