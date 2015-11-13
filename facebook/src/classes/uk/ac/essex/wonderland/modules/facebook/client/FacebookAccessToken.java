/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.logging.Logger;
import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import org.jdesktop.wonderland.client.hud.CompassLayout;
import org.jdesktop.wonderland.client.hud.HUD;
import org.jdesktop.wonderland.client.hud.HUDComponent;
import org.jdesktop.wonderland.client.hud.HUDManagerFactory;

/**
 *
 * @author Shaya
 */
public class FacebookAccessToken extends Observable
{
    private String accessToken = null;
    
    private static final Logger logger = Logger.getLogger(FacebookAccessToken.class.getName());
    
    public static boolean validAccessToken = false;
    
    private HUD requestAccessTokenHUD;
    private HUDComponent accessTokenHudComp;

    private JTextPane accessTokenTextPane;
    private JButton cancelButton;
    private JLabel insertAccessTokenLabel;
    private JScrollPane accessTokenRequestScrollPanel;
    private JButton submitButton;
    
    
    public FacebookAccessToken() {
        requestAccessTokenHUD = HUDManagerFactory.getHUDManager().getHUD("main");
        displayHud();
    }    

    
    public String getAccessToken() {
        return accessToken;
    }
    
    
     private void displayHud() {
        accessTokenRequestPanel("Please insert your Access Token", 18, 120);
        createHUDComponent("Please insert your Access Token", 18, 120);
        setHudComponentVisible(true);
    }
     
    /**
     * Creates the HUD Component, if it does not exist yet, and adds it to the
     * CENTER of the main HUD area (entire screen above the 3D scene).
     */
    private void createHUDComponent(final String title,final int fontSize,final int rgbColor) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (accessTokenHudComp == null) {
                    JPanel accessTokenPanel = accessTokenRequestPanel(title, fontSize, rgbColor);
                    accessTokenHudComp = requestAccessTokenHUD.createComponent(accessTokenPanel);
                    accessTokenHudComp.setDecoratable(true);
                    accessTokenHudComp.setName("Access Token");
                    accessTokenHudComp.setPreferredLocation(CompassLayout.Layout.CENTER);
                    requestAccessTokenHUD.addComponent(accessTokenHudComp);
                }
            }
        });
    }

    /**
     * Changes the visibility of the HUD according to the boolean passed.
     * @param show
     */
    public void setHudComponentVisible(final boolean show) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                accessTokenHudComp.setVisible(show);
            }
        });
    }
    
    private JPanel accessTokenRequestPanel(String title, int fontSize, int rgbColor){
        
        JPanel panel = new JPanel();
        
        insertAccessTokenLabel = new JLabel();
        accessTokenRequestScrollPanel = new JScrollPane();
        accessTokenTextPane = new JTextPane();
        submitButton = new JButton();
        cancelButton = new JButton();
        
        
        insertAccessTokenLabel.setFont(new Font("Times New Roman", 1, fontSize));
        insertAccessTokenLabel.setForeground(new Color(rgbColor));
        insertAccessTokenLabel.setText(title);

        accessTokenRequestScrollPanel.setViewportView(accessTokenTextPane);

        submitButton.setText("OK");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(insertAccessTokenLabel)
                        .addGap(0, 397, Short.MAX_VALUE))
                    .addComponent(accessTokenRequestScrollPanel)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(submitButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(insertAccessTokenLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accessTokenRequestScrollPanel, GroupLayout.PREFERRED_SIZE, 
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(cancelButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return panel;
    }
    
    private void submitButtonActionPerformed(ActionEvent e) {                                             

        logger.warning("The access token is: " + accessTokenTextPane.getText());
        accessToken = accessTokenTextPane.getText();
        setHudComponentVisible(false);

        //Check for validity ....
        // Just fake test here ...
        if(accessToken.length() > 1){ 
            validAccessToken = true;
            this.setChanged();
            this.notifyObservers();
        }

        if(!validAccessToken)
        {
            setHudComponentVisible(false);
            
            accessTokenRequestPanel("Invalid Access Token: Please insert your Access Token", 14, 20);
            createHUDComponent("Invalid Access Token: Please insert your Access Token", 14, 20);
            //setHudComponentVisible(true);
            
        }
    }   
    
    private void cancelButtonActionPerformed(ActionEvent e) {                                             
        setHudComponentVisible(false);
    }
}
