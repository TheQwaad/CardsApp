package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

class SingleCollectionButtonListener implements ActionListener {
    MainWindow mainWindow;
    CollectionTypeSelector collectionTypeSelector;

    SingleCollectionButtonListener(MainWindow mainWindow, CollectionTypeSelector collectionTypeSelector) {
        super();
        this.mainWindow = mainWindow;
        this.collectionTypeSelector = collectionTypeSelector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CollectionCreator collectionCreator = new CollectionCreator(mainWindow);
        collectionTypeSelector.dispatchEvent(new WindowEvent(collectionTypeSelector, WindowEvent.WINDOW_CLOSING));
    }
}


public class CollectionTypeSelector extends JFrame {
    JButton oneWayButton;
    JButton doubleWayButton;
    JPanel buttonsPanel;
    MainWindow mainWindow;
    public CollectionTypeSelector(MainWindow mainWindow) {
        super("Выберите тип коллекции");
        this.mainWindow = mainWindow;

        oneWayButton = new JButton("Односторонние карточки");
        oneWayButton.addActionListener(new SingleCollectionButtonListener(mainWindow, this));

        doubleWayButton = new JButton("Двусторонние карточки");


        oneWayButton.setPreferredSize(new Dimension(200, 30));
        doubleWayButton.setPreferredSize(new Dimension(200, 30));

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(oneWayButton);
        buttonsPanel.add(doubleWayButton);

        this.setSize(300, 200);
        this.getContentPane().add(buttonsPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }
}
