package JVE;

import JVE.Parsers.Video;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static JVE.Parsers.Macros.cleanMacroses;

public class FormTest extends JFrame {
    private JPanel bgPanel;
    private JButton loadButton;
    private JButton recompileButton;
    private JButton exitButton;
    private JSlider move;
    private JPanel frame;
    private JComboBox sceneSelect;
    private JProgressBar progress;
    private JButton render;
    private Video video;
    private String name;

    private void updateFrame() {
        Graphics2D g = (Graphics2D) frame.getGraphics();
        BufferedImage frameToDraw;
        try {
            float prop=frame.getWidth()*1f/Video.getW();
            if (sceneSelect.getSelectedIndex() == 0)
                frameToDraw = video.render(move.getValue() * 1f / move.getMaximum(), -1, prop);
            else
                frameToDraw = video.render(move.getValue() * 1f / move.getMaximum(),
                        sceneSelect.getSelectedIndex() - 1, prop);
            g.drawImage(frameToDraw, 0, 0, frame.getWidth(), (int)(prop*Video.getH()), null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void reloadVideo() {
        System.gc();
        try {
            cleanMacroses();
            video = new Video(name, state -> progress.setValue((int) (state * progress.getMaximum())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        sceneSelect.removeAllItems();
        sceneSelect.addItem("All");
        for (String s : Video.getSceneNames())
            sceneSelect.addItem(s);
        updateFrame();
    }

    private void lock() {
        progress.setValue(0);
        progress.setVisible(true);
        move.setVisible(false);
        loadButton.setVisible(false);
        recompileButton.setVisible(false);
        render.setVisible(false);
        sceneSelect.setSelectedIndex(0);
        sceneSelect.setVisible(false);
    }

    private void unlock() {
        progress.setVisible(false);
        move.setVisible(true);
        loadButton.setVisible(true);
        recompileButton.setVisible(true);
        render.setVisible(true);
        sceneSelect.setVisible(true);
    }

    public FormTest() {

        super("Title");

        setContentPane(bgPanel);

        loadButton.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Tex files", "tex", "tex");
            fileopen.setFileFilter(filter);
            int ret = fileopen.showDialog(null, "Select file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                name = file.getPath();
                new Thread(() -> {
                    lock();
                    reloadVideo();
                    unlock();
                }).start();
            }
        });

        recompileButton.addActionListener(e -> new Thread(() -> {
            lock();
            reloadVideo();
            unlock();
        }).start());

        render.addActionListener(e -> new Thread(() -> {
            lock();
            try {
                video.render(state -> {
                    progress.setValue((int) (state * progress.getMaximum()));
                    move.setValue((int) (state*move.getMaximum()));
                });
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
            progress.setValue(0);
            JOptionPane.showMessageDialog(null, "Rendered!");
            unlock();
        }).start());

        exitButton.addActionListener(e -> System.exit(0));

        move.addChangeListener(e -> updateFrame());

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        progress.setVisible(false);

        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        new FormTest();
    }
}
