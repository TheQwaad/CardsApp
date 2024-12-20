package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

class OneWayCollectionButtonListener implements ActionListener {
    MainWindow mainWindow;
    CollectionTypeSelector collectionTypeSelector;

    OneWayCollectionButtonListener(MainWindow mainWindow, CollectionTypeSelector collectionTypeSelector) {
        super();
        this.mainWindow = mainWindow;
        this.collectionTypeSelector = collectionTypeSelector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OneWayCollectionCreator oneWayCollectionCreator = new OneWayCollectionCreator(mainWindow);
        collectionTypeSelector.dispatchEvent(new WindowEvent(collectionTypeSelector, WindowEvent.WINDOW_CLOSING));
    }
}

class TwoWayCollectionButtonListener implements ActionListener {
    MainWindow mainWindow;
    CollectionTypeSelector collectionTypeSelector;
    TwoWayCollectionButtonListener(MainWindow mainWindow, CollectionTypeSelector collectionTypeSelector) {
        super();
        this.mainWindow = mainWindow;
        this.collectionTypeSelector = collectionTypeSelector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TwoWayCollectionCreator twoWayCollectionCreator = new TwoWayCollectionCreator(mainWindow);
        collectionTypeSelector.dispatchEvent(new WindowEvent(collectionTypeSelector, WindowEvent.WINDOW_CLOSING));
    }
}


public class CollectionTypeSelector extends JFrame {
    JButton oneWayButton;
    JButton twoWayButton;
    JPanel buttonsPanel;
    MainWindow mainWindow;
    public CollectionTypeSelector(MainWindow mainWindow) {
        super("Выберите тип коллекции");
        this.mainWindow = mainWindow;

        oneWayButton = new JButton("Односторонние карточки");
        twoWayButton = new JButton("Двусторонние карточки");

        oneWayButton.addActionListener(new OneWayCollectionButtonListener(mainWindow, this));
        twoWayButton.addActionListener(new TwoWayCollectionButtonListener(mainWindow, this));


        oneWayButton.setPreferredSize(new Dimension(200, 30));
        twoWayButton.setPreferredSize(new Dimension(200, 30));

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(oneWayButton);
        buttonsPanel.add(twoWayButton);

        this.setSize(300, 200);
        this.getContentPane().add(buttonsPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }
}
