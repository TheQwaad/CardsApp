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
    JButton singleCollectionButton;
    JButton doubleCollectionButton;
    JPanel buttonsPanel;
    MainWindow mainWindow;
    public CollectionTypeSelector(MainWindow mainWindow) {
        super("Выберите тип коллекции");
        this.mainWindow = mainWindow;

        singleCollectionButton = new JButton("Односторонние карточки");
        singleCollectionButton.addActionListener(new SingleCollectionButtonListener(mainWindow, this));

        doubleCollectionButton = new JButton("Двусторонние карточки");


        singleCollectionButton.setPreferredSize(new Dimension(200, 30));
        doubleCollectionButton.setPreferredSize(new Dimension(200, 30));

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(singleCollectionButton);
        buttonsPanel.add(doubleCollectionButton);

        this.setSize(300, 200);
        this.getContentPane().add(buttonsPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }
}
