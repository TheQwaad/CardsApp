package app;

import collection.CollectionsIO;
import gui.PlaceholderTextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SelectFilesButtonListener implements ActionListener {
    final FileFilter imageFilter = new FileNameExtensionFilter("Картинки", ImageIO.getReaderFileSuffixes());

    CollectionCreator frame;
    SelectFilesButtonListener(CollectionCreator frame) {
        this.frame = frame;
    }
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(imageFilter);
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showDialog(null, "Выбрать файлы");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            for (File f : selectedFiles) {
                frame.addFile(f.getAbsolutePath());
            }
            frame.updateFilesCount();
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Не удалось добавить выбранные файлы",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

class CreateCollectionButtonListener implements ActionListener {
    CollectionCreator frame;
    CreateCollectionButtonListener(CollectionCreator frame) {
        this.frame = frame;
    }
    public void actionPerformed(ActionEvent e) {
        if (frame.getFiles().isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "В коллекции должна быть хотя бы одна картинка",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }
        if (frame.getEntryText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Нельзя создать безымянную колекцию",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        String[] files = new String[frame.getFiles().size()];
        frame.getFiles().toArray(files);
        try {
            CollectionsIO.saveCollection(frame.getEntryText(), files);
            frame.updateMainWindow();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Произошла ошибка при попытке сохранить коллекцию. Возможно, какие-то файлы были удалены",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

public class CollectionCreator extends javax.swing.JFrame {
    private final MainWindow mainWindow;
    private final List<String> files;
    private final PlaceholderTextField nameEntry;
    private final JButton selectFilesButton;
    private final JButton createCollectionButton;
    private final JPanel filesCountPanel;
    CollectionCreator(MainWindow mainWindow) {
        super("Создание коллекции");

        this.files = new ArrayList<>();
        this.mainWindow = mainWindow;

        nameEntry = new PlaceholderTextField("Введите название...");
        selectFilesButton = new JButton("Добавить файлы");
        createCollectionButton = new JButton("Создать коллекцию");

        createCollectionButton.addActionListener(new CreateCollectionButtonListener(this));
        selectFilesButton.addActionListener(new SelectFilesButtonListener(this));

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

    public void addFile(String path) {
        files.add(path);
    }

    public String getEntryText() {
        return nameEntry.getText();
    }

    public List<String> getFiles() {
        return files;
    }

    public void updateMainWindow() {
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
