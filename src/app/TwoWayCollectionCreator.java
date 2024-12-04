package app;

import collection.CollectionsIO;
import collection.OneWayCollection;
import collection.TwoWayCollection;
import helpers.Pair;

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

class TwoWaySelectButtonListener implements ActionListener {
    final FileFilter imageFilter = new FileNameExtensionFilter("Картинки", ImageIO.getReaderFileSuffixes());

    TwoWayCollectionCreator frame;
    TwoWaySelectButtonListener(TwoWayCollectionCreator frame) {
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

class TwoWayCreateButtonListener implements ActionListener {
    TwoWayCollectionCreator frame;
    TwoWayCreateButtonListener(TwoWayCollectionCreator frame) {
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
        if (frame.getFiles().size() % 2 != 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Требуется чётное количество файлов",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        Pair<String, String>[] files = new Pair[frame.getFiles().size() / 2];
        List <String> frameFiles = frame.getFiles();

        for (int i = 0; i < frame.getFiles().size() / 2; i++) {
            files[i] = new Pair<>(frameFiles.get(i), frameFiles.get(i + frame.getFiles().size() / 2));
        }

        try {
            CollectionsIO.saveTwoWayCollection(new TwoWayCollection(frame.getEntryText(), files));
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

public class TwoWayCollectionCreator extends AbstractCollectionCreator {
    private final List<String> files;
    TwoWayCollectionCreator(MainWindow mainWindow) {
        super(mainWindow);

        files = new ArrayList<>();

        createCollectionButton.addActionListener(new TwoWayCreateButtonListener(this));
        selectFilesButton.addActionListener(new TwoWaySelectButtonListener(this));
    }

    public void addFile(String path) {
        files.add(path);
    }

    public List<String> getFiles() {
        return files;
    }
}
