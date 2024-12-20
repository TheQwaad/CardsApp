package app;

import collection.CollectionsIO;
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
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class SelectButtonListener implements ActionListener {
    final FileFilter imageFilter = new FileNameExtensionFilter("Картинки", ImageIO.getReaderFileSuffixes());

    TwoWayCollectionCreator frame;
    SelectButtonListener(TwoWayCollectionCreator frame) {
        this.frame = frame;
    }
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(imageFilter);
        fileChooser.setMultiSelectionEnabled(true);
        int returnVal = fileChooser.showDialog(null, "Выбрать файлы");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
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
    TwoWayCollectionCreator(MainWindow mainWindow) {
        super(mainWindow);

        createCollectionButton.addActionListener(new TwoWayCreateButtonListener(this));
        selectFilesButton.addActionListener(new SelectButtonListener(this));
    }


    public List<String> getFiles() {
        updateFilesIfInt();
        return files;
    }

    private void updateFilesIfInt() {
        Pattern pattern = Pattern.compile(".*\\D(\\d+)\\.[^\\.]+$");

        boolean allMatch = files.stream().allMatch(file -> {
            Matcher matcher = pattern.matcher(file);
            return matcher.matches();
        });

        if (allMatch) {
            Collections.sort(files, new Comparator<String>() {
                @Override
                public int compare(String file1, String file2) {
                    Matcher matcher1 = pattern.matcher(file1);
                    Matcher matcher2 = pattern.matcher(file2);

                    if (matcher1.find() && matcher2.find()) {
                        int num1 = Integer.parseInt(matcher1.group(1));
                        int num2 = Integer.parseInt(matcher2.group(1));
                        return Integer.compare(num1, num2);
                    }
                    return 0;
                }
            });
        }
    }
}
