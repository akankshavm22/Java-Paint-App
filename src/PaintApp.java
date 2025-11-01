import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintApp extends JFrame {
    private JPanel drawingPanel;
    private JPanel toolPanel;
    private JPanel controlPanel;
    private JButton clearButton;
    private JButton saveButton;
    private JButton colorButton;
    private JComboBox<String> sizeComboBox;
    private JToggleButton pencilButton;
    private JToggleButton eraserButton;
    private JToggleButton rectangleButton;
    private JToggleButton ovalButton;
    private JToggleButton lineButton;

    private Color currentColor = Color.BLACK;
    private int currentSize = 5;
    private String currentTool = "pencil";

    private Point startPoint;
    private Point previousPoint;
    private BufferedImage drawingImage;

    public PaintApp() {
        setTitle("Java Paint App");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        setMinimumSize(new Dimension(800, 600)); // Minimum size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize with default size image
        drawingImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = drawingImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1, 1);
        g2d.dispose();

        createUI();
    }

    private void createUI() {
        // Main layout
        setLayout(new BorderLayout());

        // Create tool panel for the top (responsive horizontal layout)
        toolPanel = new JPanel(new GridLayout(1, 0, 5, 5));
        toolPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolPanel.setBackground(new Color(240, 240, 240));

        // Create drawing panel that fills available space
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Scale image to fit current panel size
                g.drawImage(drawingImage, 0, 0, getWidth(), getHeight(), this);

                // Draw preview for shapes
                if (startPoint != null) {
                    Point currentPoint = getMousePosition();
                    if (currentPoint != null) {
                        g.setColor(currentColor);
                        ((Graphics2D) g).setStroke(new BasicStroke(currentSize));

                        int x = Math.min(startPoint.x, currentPoint.x);
                        int y = Math.min(startPoint.y, currentPoint.y);
                        int width = Math.abs(currentPoint.x - startPoint.x);
                        int height = Math.abs(currentPoint.y - startPoint.y);

                        switch (currentTool) {
                            case "rectangle":
                                g.drawRect(x, y, width, height);
                                break;
                            case "oval":
                                g.drawOval(x, y, width, height);
                                break;
                            case "line":
                                g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
                                break;
                        }
                    }
                }
            }
        };

        drawingPanel.setBackground(Color.WHITE);

        // Handle resizing of the drawing area
        drawingPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                BufferedImage newImage = new BufferedImage(
                        drawingPanel.getWidth(),
                        drawingPanel.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = newImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());

                // Scale existing drawing to new size
                if (drawingImage != null) {
                    g2d.drawImage(drawingImage, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
                }

                g2d.dispose();
                drawingImage = newImage;
            }
        });

        // Mouse listeners for drawing
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                previousPoint = e.getPoint();

                if (currentTool.equals("pencil") || currentTool.equals("eraser")) {
                    Graphics2D g2d = drawingImage.createGraphics();
                    g2d.setColor(currentTool.equals("eraser") ? Color.WHITE : currentColor);
                    g2d.setStroke(new BasicStroke(currentSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
                    g2d.dispose();
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (startPoint != null) {
                    Graphics2D g2d = drawingImage.createGraphics();
                    g2d.setColor(currentTool.equals("eraser") ? Color.WHITE : currentColor);
                    g2d.setStroke(new BasicStroke(currentSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    switch (currentTool) {
                        case "rectangle":
                            int x = Math.min(startPoint.x, e.getX());
                            int y = Math.min(startPoint.y, e.getY());
                            int width = Math.abs(e.getX() - startPoint.x);
                            int height = Math.abs(e.getY() - startPoint.y);
                            g2d.drawRect(x, y, width, height);
                            break;
                        case "oval":
                            x = Math.min(startPoint.x, e.getX());
                            y = Math.min(startPoint.y, e.getY());
                            width = Math.abs(e.getX() - startPoint.x);
                            height = Math.abs(e.getY() - startPoint.y);
                            g2d.drawOval(x, y, width, height);
                            break;
                        case "line":
                            g2d.drawLine(startPoint.x, startPoint.y, e.getX(), e.getY());
                            break;
                    }

                    g2d.dispose();
                }
                startPoint = null;
                previousPoint = null;
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentTool.equals("pencil") || currentTool.equals("eraser")) {
                    Graphics2D g2d = drawingImage.createGraphics();
                    g2d.setColor(currentTool.equals("eraser") ? Color.WHITE : currentColor);
                    g2d.setStroke(new BasicStroke(currentSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    if (previousPoint != null) {
                        g2d.drawLine(previousPoint.x, previousPoint.y, e.getX(), e.getY());
                    }

                    g2d.dispose();
                    previousPoint = e.getPoint();
                    drawingPanel.repaint();
                } else if (startPoint != null) {
                    drawingPanel.repaint();
                }
            }
        });

        // Create control panel for the bottom (responsive horizontal layout)
        controlPanel = new JPanel(new GridLayout(1, 0, 10, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPanel.setBackground(new Color(240, 240, 240));

        // Create tool buttons with responsive sizing
        ButtonGroup toolGroup = new ButtonGroup();

        pencilButton = createToolButton("Pencil", "pencil");
        eraserButton = createToolButton("Eraser", "eraser");
        rectangleButton = createToolButton("Rectangle", "rectangle");
        ovalButton = createToolButton("Oval", "oval");
        lineButton = createToolButton("Line", "line");

        toolGroup.add(pencilButton);
        toolGroup.add(eraserButton);
        toolGroup.add(rectangleButton);
        toolGroup.add(ovalButton);
        toolGroup.add(lineButton);

        pencilButton.setSelected(true);

        // Add tools to tool panel
        toolPanel.add(pencilButton);
        toolPanel.add(eraserButton);
        toolPanel.add(rectangleButton);
        toolPanel.add(ovalButton);
        toolPanel.add(lineButton);

        // Add color button
        colorButton = new JButton("Color");
        colorButton.setForeground(Color.BLACK);
        colorButton.setBackground(currentColor);
        colorButton.setForeground(getContrastColor(currentColor));
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(PaintApp.this, "Choose a color", currentColor);
            if (newColor != null) {
                currentColor = newColor;
                colorButton.setBackground(currentColor);
                colorButton.setForeground(getContrastColor(currentColor));
            }
        });

        // Add size combo box
        sizeComboBox = new JComboBox<>(new String[] { "1", "3", "5", "8", "10", "15", "20" });
        sizeComboBox.setSelectedIndex(2);
        sizeComboBox.addActionListener(e -> {
            currentSize = Integer.parseInt((String) sizeComboBox.getSelectedItem());
        });

        // Add action buttons
        clearButton = new JButton("Clear Canvas");
        clearButton.addActionListener(e -> {
            Graphics2D g2d = drawingImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, drawingImage.getWidth(), drawingImage.getHeight());
            g2d.dispose();
            drawingPanel.repaint();
        });

        saveButton = new JButton("Save Drawing");
        saveButton.addActionListener(e -> saveDrawing());

        // Add controls to control panel
        controlPanel.add(colorButton);
        controlPanel.add(new JLabel("Size:"));
        controlPanel.add(sizeComboBox);
        controlPanel.add(clearButton);
        controlPanel.add(saveButton);

        // Add components to frame
        add(toolPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JToggleButton createToolButton(String text, String tool) {
        JToggleButton button = new JToggleButton(text);
        button.addActionListener(e -> currentTool = tool);
        button.setFocusPainted(false);
        return button;
    }

    private void saveDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Drawing");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "PNG Images", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
            }

            try {
                ImageIO.write(drawingImage, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Drawing saved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving drawing: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            PaintApp app = new PaintApp();
            app.setVisible(true);
        });
    }
}