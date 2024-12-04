package app;

import gui.PlaceholderTextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCollectionCreator extends javax.swing.JFrame {
    protected final MainWindow mainWindow;
    protected final List<String> files;
    protected final PlaceholderTextField nameEntry;
    protected final JButton selectFilesButton;
    protected final JButton createCollectionButton;
    protected final JPanel filesCountPanel;
    AbstractCollectionCreator(MainWindow mainWindow) {
        super("Создание коллекции");

        this.files = new ArrayList<>();
        this.mainWindow = mainWindow;

        nameEntry = new PlaceholderTextField("Введите название...");
        selectFilesButton = new JButton("Добавить файлы");
        createCollectionButton = new JButton("Создать коллекцию");

        nameEntry.setPreferredSize(new Dimension(150, 30));

        JPanel parametersPanel = new JPanel();

        parametersPanel.setLayout(new GridLayout(1, 2));

        parametersPanel.add(nameEntry);
        parametersPanel.add(selectFilesButton);


        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new FlowLayout());

        buttonsPanel.add(createCollectionButton);

        filesCountPanel = new JPanel();

        Container contentPane = this.getContentPane();

        contentPane.setLayout(new BorderLayout());
        contentPane.add(BorderLayout.NORTH, parametersPanel);
        contentPane.add(BorderLayout.CENTER, buttonsPanel);
        contentPane.add(BorderLayout.SOUTH, filesCountPanel);


        this.setSize(300, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

    public String getEntryText() {
        return nameEntry.getText();
    }

    public void addFile(String path) {
        files.add(path);
    }

    public void updateMainWindow() throws IOException {
        mainWindow.updateCollectionsList();
    }

    public void updateFilesCount() {
        filesCountPanel.removeAll();
        JLabel countLabel = new JLabel(String.format("Выбрано %s файлов", files.size()));
        filesCountPanel.add(countLabel);
        filesCountPanel.revalidate();
        filesCountPanel.repaint();
    }
}
