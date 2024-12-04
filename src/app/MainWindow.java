package app;

import collection.*;
import helpers.ImageHelper;
import helpers.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;


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
        try {
            mainWindow.setCollection(CollectionsIO.loadOneWayCollection(mainWindow.getSelectedCollection()));
            mainWindow.updateOneWayImage();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    mainWindow,
                    "Не удалось сгенерировать картинку. Попробуйте выбрать другую коллекцию Возможно, коллекция не была выбрана",
                    "Ошибка", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

public class MainWindow extends JFrame {
    Map<String, String> collectionTypes = Map.of(
            "one-way", "О/С",
            "two-way", "Д/С"
    );
    JButton generateButton;
    JButton createButton;
    JComboBox<String> collectionsComboBox;
    AbstractCollection collection;
    Pair<String, String>[] collections;
    JPanel imagePanel;
    JPanel selectPanel;
    JPanel generatePanel;
    JSplitPane buttonsSplitPane;


    public void updateCollectionsList() throws IOException {
        collections = CollectionsIO.loadCollectionsList();
        collectionsComboBox.removeAllItems();
        for (Pair<String, String> collection : collections) {
            String item = String.format("%s | %s", collection.first(), collectionTypes.get(collection.second()));
            collectionsComboBox.addItem(item);
        }
        collectionsComboBox.revalidate();
        collectionsComboBox.repaint();
        selectPanel.revalidate();
        selectPanel.repaint();
    }

    public void setCollection(AbstractCollection collection) {
        this.collection = collection;
    }

    public String getSelectedCollection() {
        return ((String) collectionsComboBox.getSelectedItem()).split("\\|")[0].strip();
    }

    public void updateImage(String path) {
        imagePanel.removeAll();

        JPanel loadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel loadingLabel = new JLabel("Карточка загружается...");
        loadingPanel.add(loadingLabel);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingPanel.add(progressBar);

        imagePanel.add(loadingPanel);
        imagePanel.revalidate();
        imagePanel.repaint();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                BufferedImage image = ImageHelper.loadImage(path);
                imagePanel.removeAll();

                JLabel picLabel = new JLabel(new ImageIcon(ImageHelper.rescale(image, 1000, 800)));
                JPanel center = new JPanel(new GridBagLayout());
                center.add(picLabel, new GridBagConstraints());

                imagePanel.setLayout(new GridBagLayout());
                imagePanel.add(center);
                return null;
            }

            @Override
            protected void done() {
                imagePanel.revalidate();
                imagePanel.repaint();
            }
        };

        worker.execute();
    }

    public void updateOneWayImage() throws IOException {
        String path = (String) collection.getItem().getContent();
        updateImage(path);
    }


    public MainWindow() throws IOException {
        super("Генератор карточек");
        setIconImage(ImageIO.read(new File("assets/icon.jpg")));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setUndecorated(false);

        generatePanel = new JPanel();

        generateButton = new JButton("Сгенерировать карточку");
        createButton = new JButton("Создать Коллекцию");
        collectionsComboBox = new JComboBox<>();

        generateButton.setSize(100, 100);

        generatePanel.add(generateButton);

        selectPanel = new JPanel();

        selectPanel.add(collectionsComboBox);
        selectPanel.add(createButton);

        createButton.addActionListener(new CreateButtonListener(this));
        generateButton.addActionListener(new GenerateButtonListener(this));

        imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        imagePanel.add(new JLabel("Здесь будет ваша картинка"));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        buttonsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectPanel, generatePanel);
        contentPane.add(BorderLayout.NORTH, buttonsSplitPane);
        contentPane.add(BorderLayout.CENTER, imagePanel);

        updateCollectionsList();

        this.setPreferredSize(new Dimension(900, 1100));
        this.setMinimumSize(new Dimension(900, 1100));
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
}
