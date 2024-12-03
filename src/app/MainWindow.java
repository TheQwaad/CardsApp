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
        CollectionTypeSelector collectionTypeSelector = new CollectionTypeSelector(mainWindow);
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
    JSplitPane buttonsSplitPane;


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
        JPanel center = new JPanel( new GridBagLayout() );
        JLabel picLabel = new JLabel(new ImageIcon(ImageEditor.rescale(collection.getImage(), 1000, 900)));
        center.add(picLabel, new GridBagConstraints());
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(center);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    public MainWindow() {
        super("Генератор карточек");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setUndecorated(false);

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
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        buttonsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectPanel, generatePanel);
        contentPane.add(BorderLayout.NORTH, buttonsSplitPane);
        contentPane.add(BorderLayout.CENTER, imagePanel);

        this.setPreferredSize(new Dimension(1000, 1100));
        this.setMinimumSize(new Dimension(1000, 1100));
        this.setMaximumSize(new Dimension(1920, 1080));

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                setLocationRelativeTo(null);
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
