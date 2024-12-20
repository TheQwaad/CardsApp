package app;

import collection.CollectionsIO;
import collection.OneWayCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class OneWaySelectButtonListener implements ActionListener {
    final FileFilter imageFilter = new FileNameExtensionFilter("Картинки", ImageIO.getReaderFileSuffixes());

    OneWayCollectionCreator frame;
    OneWaySelectButtonListener(OneWayCollectionCreator frame) {
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

class OneWayCreateCollectionButtonListener implements ActionListener {
    OneWayCollectionCreator frame;
    OneWayCreateCollectionButtonListener(OneWayCollectionCreator frame) {
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
            CollectionsIO.saveOneWayCollection(new OneWayCollection(frame.getEntryText(), files));
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

public class OneWayCollectionCreator extends AbstractCollectionCreator {
    OneWayCollectionCreator(MainWindow mainWindow) {
        super(mainWindow);

        createCollectionButton.addActionListener(new OneWayCreateCollectionButtonListener(this));
        selectFilesButton.addActionListener(new OneWaySelectButtonListener(this));
    }

    public void addFile(String path) {
        files.add(path);
    }

    public List<String> getFiles() {
        return files;
    }
}
