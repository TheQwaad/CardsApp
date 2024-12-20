package app;

import collection.*;
import helpers.ImageHelper;
import helpers.Pair;

import javax.imageio.ImageIO;
import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;



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
            if (mainWindow.getSelectedCollectionType() == CollectionTypes.ONE_WAY) {
                mainWindow.setCollection(CollectionsIO.loadOneWayCollection(mainWindow.getSelectedCollection()));
            } else {
                mainWindow.setCollection(CollectionsIO.loadTwoWayCollection(mainWindow.getSelectedCollection()));
            }
            mainWindow.regenerateImage();
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
    Map<String, String> collectionTypesSynonyms = Map.of(
            "one-way", "одност.",
            "two-way", "двухст. "
    );

    Map<String, CollectionTypes> collectionTypes = Map.of(
            "одност.", CollectionTypes.ONE_WAY,
            "двухст.", CollectionTypes.TWO_WAY
    );

    JButton generateButton;
    JButton createButton;
    JComboBox<String> collectionsComboBox;
    Collection collection;
    Pair<String, String>[] collections;
    JPanel imagePanel;
    JPanel selectPanel;
    JPanel generatePanel;
    JSplitPane buttonsSplitPane;
    MouseListener mouseListener;


    public void updateCollectionsList() throws IOException {
        collections = CollectionsIO.loadCollectionsList();
        collectionsComboBox.removeAllItems();
        for (Pair<String, String> collection : collections) {
            String item = String.format("%s | %s", collection.first(), collectionTypesSynonyms.get(collection.second()));
            collectionsComboBox.addItem(item);
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
        return ((String) collectionsComboBox.getSelectedItem()).split("\\|")[0].strip();
    }

    public CollectionTypes getSelectedCollectionType() {
        String name = ((String) collectionsComboBox.getSelectedItem()).split("\\|")[1].substring(1);
        return collectionTypes.get(name);
    }

    public void redrawImage(String path) {
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

    public void regenerateImage() throws IOException {
        Object content = collection.getItem().getContent();
        if (collection instanceof OneWayCollection) {
            String path = (String) content;
            redrawImage(path);
            if (mouseListener != null) {
                imagePanel.removeMouseListener(mouseListener);
                mouseListener = null;
            }
        } else {
            if (mouseListener != null) {
                imagePanel.removeMouseListener(mouseListener);
            }
            mouseListener = new MouseAdapter() {
                final Pair<String, String> paths = (Pair<String, String>) content;
                String currentPath = paths.first();
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Objects.equals(currentPath, paths.first())) {
                        currentPath = paths.second();
                    } else {
                        currentPath = paths.first();
                    }
                    redrawImage(currentPath);
                }
            };

            redrawImage(((Pair<String, String>) content).first());
            imagePanel.addMouseListener(mouseListener);
        }
    }


    public MainWindow() throws IOException {
        super("Генератор карточек");
        try {
            setIconImage(ImageIO.read(new File("assets/icon.jpg")));
        } catch (IIOException ignored) {
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setUndecorated(false);

        mouseListener = null;
        generatePanel = new JPanel();

        generateButton = new JButton("Сгенерировать карточку");
        createButton = new JButton("Создать Коллекцию");
        collectionsComboBox = new JComboBox<>();
        collectionsComboBox.setPreferredSize(new Dimension(200, 25));

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
