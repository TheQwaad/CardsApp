package app;

import collection.*;
import helpers.ImageEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


class CreateButtonListener implements ActionListener {
    MainWindow mainWindow;

    CreateButtonListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        CollectionCreator collectionCreator = new CollectionCreator(mainWindow);
    }
}

class GenerateButtonListener implements ActionListener {
    MainWindow mainWindow;

    GenerateButtonListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        mainWindow.setCollection(CollectionsIO.loadCollection(mainWindow.getSelectedCollection()));
        try {
            mainWindow.updateImage();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    mainWindow,
                    "Не удалось сгенерировать картинку. Попробуйте выбрать другую коллекцию",
                    "Ошибка", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

public class MainWindow extends JFrame {
    JButton generateButton;
    JButton createButton;
    JComboBox<String> collectionsComboBox;
    Collection collection;
    String[] collections;
    JPanel imagePanel;
    JPanel selectPanel;
    JPanel generatePanel;


    public void updateCollectionsList() {
        collections = CollectionsIO.loadCollections();
        collectionsComboBox.removeAllItems();
        for (String collection : collections) {
            collectionsComboBox.addItem(collection);
        }
        collectionsComboBox.revalidate();
        collectionsComboBox.repaint();
        selectPanel.revalidate();
        selectPanel.repaint();
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getSelectedCollection() {
        return (String) collectionsComboBox.getSelectedItem();
    }

    public void updateImage() throws IOException {
        imagePanel.removeAll();
        JLabel picLabel = new JLabel(new ImageIcon(ImageEditor.rescaleToHeight(collection.getImage(), 900)));
        imagePanel.add(picLabel);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    public MainWindow() {
        super("Генератор карточек");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        String[] collections = CollectionsIO.loadCollections();

        generatePanel = new JPanel();


        generateButton = new JButton("Сгенерировать карточку");
        createButton = new JButton("Создать Коллекцию");
        collectionsComboBox = new JComboBox<>(collections);

        generateButton.setSize(100, 100);

        generatePanel.add(generateButton);

        selectPanel = new JPanel();

        selectPanel.add(collectionsComboBox);
        selectPanel.add(createButton);

        createButton.addActionListener(new CreateButtonListener(this));
        generateButton.addActionListener(new GenerateButtonListener(this));

        imagePanel = new JPanel();

        Container contentPane = getContentPane();
        contentPane.add(BorderLayout.NORTH, generatePanel);
        contentPane.add(BorderLayout.CENTER, selectPanel);
        contentPane.add(BorderLayout.SOUTH, imagePanel);

        this.setSize(1000, 1100);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
