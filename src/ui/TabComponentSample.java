/*
 * JTattoo TabComponentSample (c) 2013 by MH Software-Entwicklung
 *
 * This sample shows a tabbed pane with custom tab components for adding and closing tabs.
 */
package ui;


import ui.icons.ImageHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author Michael Hagen
 */
public class TabComponentSample extends BaseSampleFrame {
    
    private static final int MAX_TABS = 20; // The maximum number of open tabs
    private static int tabNo = 0; // A static counter for numbering the tabs
    
    private JTabbedPane tabbedPane = null; // The sample tabbed pane
    
    public TabComponentSample() {
        super("TabComponentSample");

        // Create some controls to test different state of tabbed panes
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
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        // Create some tabs with different properties
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("DefaultTab", new JPanel());
        
        for (int i = 1; i < 7; i++) {
            String tabTitle = "Tab " + (++tabNo) + "  ";
            tabbedPane.add(tabTitle, new JPanel());
            tabbedPane.setTabComponentAt(i, new CloseableTabComponent(tabbedPane, tabTitle));
        }
        // adding the [+] tab with functionallity to add new tabs
        tabbedPane.addTab("+", new JPanel());
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new AddTabComponent(tabbedPane));
        // this tab must not be enabled because we don't want to select this tab
        tabbedPane.setEnabledAt(tabbedPane.getTabCount() - 1, false);
        
        // Put it all together and show it as content pane
        tabPanel.add(tabbedPane, BorderLayout.CENTER);
        tabPanel.add(optionPanel, BorderLayout.SOUTH);
        JScrollPane tabScrolPane = new JScrollPane(tabPanel);
        contentPanel.add(tabScrolPane, BorderLayout.CENTER);
        
    }

    private void updateTabbedPane() {
        // to make sure that all changes on the tabbed pane are done
        // we repaint the control after all events are dispatched
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                tabbedPane.validate();
                tabbedPane.repaint();
            }
        });
    }

    
//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    TabComponentSample app = new TabComponentSample();
                    app.setSize(800, 600);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main

    
//------------------------------------------------------------------------------    
// inner classes    
//------------------------------------------------------------------------------  
    
    // A closer button for the custom tab components
    private static class CloseButton extends JButton {
        
        private static final ImageIcon CLOSER_ICON = ImageHelper.loadImage("closer.gif");
        private static final ImageIcon CLOSER_ROLLOVER_ICON = ImageHelper.loadImage("closer_rollover.gif");
        private static final ImageIcon CLOSER_PRESSED_ICON = ImageHelper.loadImage("closer_pressed.gif");
        
        private static Dimension prefSize = new Dimension(16, 16);
        
        public CloseButton() {
            super("");
            // setup the button
            setIcon(CLOSER_ICON);
            setRolloverIcon(CLOSER_ROLLOVER_ICON);
            setPressedIcon(CLOSER_PRESSED_ICON);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusable(false);
            // the preferrd size of this button is the size of the closer image 
            prefSize = new Dimension(CLOSER_ICON.getIconWidth(), CLOSER_ICON.getIconHeight());
        }

        @Override
        public Dimension getPreferredSize() {
            return prefSize;
        }
        
    }

    // A add button for the last tab in the tabbed pane
    private static class AddButton extends JButton {
        
        private static final Dimension PREF_SIZE = new Dimension(16, 15);
        
        public AddButton() {
            super("+");
            // setup the button
            setFont(new Font("Dialog", Font.BOLD, 14));
            setForeground(Color.blue);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusable(false);
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    setContentAreaFilled(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setContentAreaFilled(false);
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return PREF_SIZE;
        }
        
    }
    
    // A component for the custom tabs with a closer button
    private static class CloseableTabComponent extends JPanel {
        
        private JTabbedPane tabbedPane = null; // the tabbed pane this component belongs to
        private JLabel titleLabel = null; // the title of the tab
        private JButton closeButton = null; // the closer button on the right side of the tab
        private Font defaultFont = null; // the default font of the title label
        private Font selectedFont = null; // the font of the title label if tab is selected
        private Color selectedColor = null; // the foreground color of the title lable if tab is selected
        
        public CloseableTabComponent(JTabbedPane aTabbedPane, String title) {
            super(new BorderLayout());
            tabbedPane = aTabbedPane;
            setOpaque(false);

            // setup the controls of this tab component
            titleLabel = new JLabel(title);
            titleLabel.setOpaque(false);
            // get the defaults for rendering the title label
            defaultFont = titleLabel.getFont().deriveFont(~Font.BOLD);
            selectedFont = titleLabel.getFont().deriveFont(Font.BOLD);
            selectedColor = UIManager.getColor("TabbedPane.selectedForeground");
            if (selectedColor == null) {
                selectedColor = tabbedPane.getForeground();
            }
            closeButton = new CloseButton();
            add(titleLabel, BorderLayout.CENTER);
            add(closeButton, BorderLayout.EAST);

            // add a action listener for closing tabs
            closeButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    // calculate the tab index of this tab component
                    int tabIndex = getTabIndex();
                    if (tabIndex >= 0) {
                        // remove the tab from the tabbed pane
                        tabbedPane.removeTabAt(tabIndex);
                    }
                    // the tab we removed maybe the selected tab so we have to select another one
                    if ((tabbedPane.getTabCount() > 1) && (tabbedPane.getSelectedIndex() == tabbedPane.getTabCount() - 1)) {
                        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
                    }
                }
            });
            
        }
        
        // calculate the tab index of this tab component
        private int getTabIndex() {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (this.equals(tabbedPane.getTabComponentAt(i))) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void updateUI() {
            super.updateUI();
            // if look and feel changes we have to set the new defaults for rendering the title label
            if (titleLabel != null) {
                defaultFont = titleLabel.getFont().deriveFont(~Font.BOLD);
                selectedFont = titleLabel.getFont().deriveFont(Font.BOLD);
                selectedColor = UIManager.getColor("TabbedPane.selectedForeground");
                if (selectedColor == null) {
                    selectedColor = tabbedPane.getForeground();
                }
            }
        }

        // We have to override paint to handle the rendering of the title label, because we want
        // the title to be painted different when tab is selected.
        @Override
        public void paint(Graphics g) {
            int tabIndex = getTabIndex();
            if (tabIndex >= 0) {
                if (tabIndex == tabbedPane.getSelectedIndex()) {
                    titleLabel.setFont(selectedFont);
                    if (tabbedPane.getForegroundAt(tabIndex) instanceof ColorUIResource) {
                        titleLabel.setForeground(selectedColor);
                    } else {
                        titleLabel.setForeground(tabbedPane.getForegroundAt(tabIndex));
                    }
                } else {
                    titleLabel.setFont(defaultFont);
                    titleLabel.setForeground(tabbedPane.getForegroundAt(tabIndex));
                }
            }
            
            super.paint(g);
        }
        
    }
    
    // A component for the last tab with an add button
    private static class AddTabComponent extends JPanel {
        
        private JButton addButton = null; 
        private JTabbedPane tabPane = null;
        
        public AddTabComponent(JTabbedPane aTabbedPane) {
            super(new BorderLayout());
            tabPane = aTabbedPane;
            
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
            
            addButton = new AddButton();
            
            addButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tabPane.getTabCount() < MAX_TABS) {
                        int tabIndex = tabPane.getTabCount() - 1;
                        String tabTitle = "Tab " + (++tabNo) + "  ";
                        tabPane.insertTab(tabTitle, null, new JPanel(), null, tabIndex);
                        tabPane.setTabComponentAt(tabIndex, new CloseableTabComponent(tabPane, tabTitle));
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                if (tabPane.getTabCount() > 1) {
                                    tabPane.setSelectedIndex(tabPane.getTabCount() - 2);
                                }
                            }
                        });
                    }
                }
            });
            add(addButton, BorderLayout.EAST);
        }
    }
    
}
