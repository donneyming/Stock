/*
 * JTattoo ColoredTabbedPaneSample (c) 2013 by MH Software-Entwicklung
 *
 * This sample shows how to customize the different colors of tabbed panes.
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Michael Hagen
 */
public class ColoredTabbedPaneSample extends BaseSampleFrame {
    
    private JCheckBox opaqueCheck = null;
    private JCheckBox contentOpaqueCheck = null;
    private JCheckBox tabsOpaqueCheck = null;
    private JPanel tabPanel = null;
    private JTabbedPane tabbedPane = null;
    
    public ColoredTabbedPaneSample() {
        super("ColoredTabbedPaneSample");

        // Create some controls to test different state of tabbed panes
        opaqueCheck = new JCheckBox("TabbedPane.opaque", false);
        opaqueCheck.setOpaque(false);
        opaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setOpaque(!tabbedPane.isOpaque());
                contentOpaqueCheck.setEnabled(!opaqueCheck.isSelected());
                tabsOpaqueCheck.setEnabled(!opaqueCheck.isSelected());
                updateTabbedPane();
            }
        });
        contentOpaqueCheck = new JCheckBox("TabbedPane.contentOpaque", true);
        contentOpaqueCheck.setOpaque(false);
        contentOpaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Here we toggle the UIManager property "TabbedPane.contentOpaque"
                // This property, if set to false, in combination with the opaque flag, if also set to false, of the tabbed pane
                // causes the control to be transparent.
                boolean opaque = true;
                if (UIManager.get("TabbedPane.contentOpaque") != null) {
                    opaque = UIManager.getBoolean("TabbedPane.contentOpaque");
                }
                UIManager.put("TabbedPane.contentOpaque", !opaque);
                updateTabbedPane();
            }
        });
        tabsOpaqueCheck = new JCheckBox("TabbedPane.tabsOpaque", true);
        tabsOpaqueCheck.setOpaque(false);
        tabsOpaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Here we toggle the UIManager property "TabbedPane.tabsOpaque"
                // If this property is set to false the single tabs will be transparent.
                boolean opaque = true;
                if (UIManager.get("TabbedPane.tabsOpaque") != null) {
                    opaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
                }
                UIManager.put("TabbedPane.tabsOpaque", !opaque);
                updateTabbedPane();
            }
        });
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setOpaque(false);
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
        checkBoxPanel.add(opaqueCheck);
        checkBoxPanel.add(contentOpaqueCheck);
        checkBoxPanel.add(tabsOpaqueCheck);

        JPanel optionPanel = new JPanel();
        optionPanel.setOpaque(false);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        final JRadioButton topCheck = new JRadioButton("Top", true);
        topCheck.setOpaque(false);
        final JRadioButton leftCheck = new JRadioButton("Left", false);
        leftCheck.setOpaque(false);
        final JRadioButton bottomCheck = new JRadioButton("Bottom", false);
        bottomCheck.setOpaque(false);
        final JRadioButton rightCheck = new JRadioButton("Right", false);
        rightCheck.setOpaque(false);
        final JCheckBox scrollableCheck = new JCheckBox("ScrollableTabs", false);
        scrollableCheck.setOpaque(false);
        scrollableCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (scrollableCheck.isSelected()) {
                    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                } else {
                    tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                }
                updateTabbedPane();
            }
        });
        optionPanel.add(topCheck);
        optionPanel.add(leftCheck);
        optionPanel.add(bottomCheck);
        optionPanel.add(rightCheck);
        optionPanel.add(scrollableCheck);
        ButtonGroup orientatioGroup = new ButtonGroup();
        orientatioGroup.add(topCheck);
        orientatioGroup.add(leftCheck);
        orientatioGroup.add(bottomCheck);
        orientatioGroup.add(rightCheck);
        ActionListener orientationListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (topCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.TOP);
                } else if (leftCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.LEFT);
                } else if (bottomCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
                } else {
                    tabbedPane.setTabPlacement(JTabbedPane.RIGHT);
                }
                updateTabbedPane();
            }
        };
        topCheck.addActionListener(orientationListener);
        leftCheck.addActionListener(orientationListener);
        bottomCheck.addActionListener(orientationListener);
        rightCheck.addActionListener(orientationListener);

        // Create the sample tabbed pane
        tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        // Create some tabs with different properties
        tabbedPane = new JTabbedPane();
        //tabbedPane.setBackground(new Color(64, 196, 255));
        
        JPanel defaultPanel = new JPanel();
        tabbedPane.add("Default", defaultPanel);
        JPanel yellowPanel = new JPanel();
        yellowPanel.setBackground(Color.yellow);
        tabbedPane.add("Yellow", yellowPanel);
        tabbedPane.setBackgroundAt(1, Color.yellow);
        tabbedPane.setForegroundAt(1, Color.red);
        JPanel greenPanel = new JPanel();
        greenPanel.setBackground(Color.green);
        tabbedPane.add("Green", greenPanel);
        tabbedPane.setBackgroundAt(2, Color.green);
        tabbedPane.setForegroundAt(2, Color.blue);
        JPanel bluePanel = new JPanel();
        bluePanel.setBackground(Color.blue);
        tabbedPane.add("Blue", bluePanel);
        tabbedPane.setBackgroundAt(3, Color.blue);
        tabbedPane.setForegroundAt(3, Color.magenta);
        JPanel panelTransparent = new JPanel();
        panelTransparent.setOpaque(false);
        tabbedPane.add("Transparent", panelTransparent);

        // Put it all together and show it as content pane
        tabPanel.add(tabbedPane, BorderLayout.CENTER);

        contentPanel.setBackground(new Color(255, 196, 96));
        contentPanel.add(checkBoxPanel, BorderLayout.NORTH);
        contentPanel.add(tabPanel, BorderLayout.CENTER);
        contentPanel.add(optionPanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called from BaseSampleFrame.setLookAndFeel to prepare
     * the look and feel properties.
     * 
     * @return the look and feel properties
     */
    @Override
    public Properties getLAFProps() {
        Properties props = super.getLAFProps();
        // The dark look and feels needs special background otherwise it looks odd
        if ((selectedLaf == Constants.LAF_HIFI) || (selectedLaf == Constants.LAF_NOIRE)) {
            props.put("tabAreaBackgroundColor", "120 80 0");
        } else {
            props.put("tabAreaBackgroundColor", "240 150 0");
        }
        return props;
    }

    /**
     * Overriden to handle the background color of the background panel.
     */
    @Override
    public void setLookAndFeel() {
        // The dark look and feels needs special background otherwise it looks odd
        if ((selectedLaf == Constants.LAF_HIFI) || (selectedLaf == Constants.LAF_NOIRE)) {
            contentPanel.setBackground(new Color(128, 96, 48));
        } else {
            contentPanel.setBackground(new Color(255, 196, 96));
        }
        super.setLookAndFeel();
    }
    
    private void updateTabbedPane() {
        // to make sure that all changes on the tabbed pane are done
        // we repaint the control after all events are dispatched
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                tabPanel.validate();
                tabPanel.repaint();
            }
        });
    }

    
//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            // Create a property object 
            Properties props = new Properties();

            // This property sets the background color of tab areas
            props.put("tabAreaBackgroundColor", "240 150 0");
            
            // Set the theme
            com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);
            
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    ColoredTabbedPaneSample app = new ColoredTabbedPaneSample();
                    app.setSize(800, 600);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
    
}
