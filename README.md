# 🎨 Java Paint App

A simple yet powerful **desktop drawing application** built entirely using **Java Swing**. This project lets users draw, erase, and create basic shapes with customizable colors and brush sizes — a mini version of **MS Paint** implemented from scratch.

## 🖌️ Features

✅ **Freehand Drawing (Pencil Tool)** – Draw smoothly with adjustable brush size  
✅ **Eraser Tool** – Erase parts of the drawing easily  
✅ **Shape Tools** – Draw rectangles, ovals, and straight lines  
✅ **Color Picker** – Choose any color using the system color chooser  
✅ **Brush Size Control** – Change line thickness from 1px up to 20px  
✅ **Canvas Management** – Clear the canvas or resize it dynamically  
✅ **Save Artwork** – Export your drawing as a `.png` image  
✅ **Responsive Design** – Canvas auto-adjusts when the window is resized  
✅ **Clean Modern UI** – Organized toolbar and control panel layout  

## 🧠 How It Works

The app uses:
- **`BufferedImage`** to store and render the drawing  
- **`Graphics2D`** for drawing lines, shapes, and applying brush strokes  
- **Mouse events** (`MouseListener`, `MouseMotionListener`) to track drawing actions  
- **Swing components** for UI: `JPanel`, `JButton`, `JToggleButton`, `JComboBox`, `JColorChooser`, and more

## 🪄 Tools Overview
<table>
  <tr><th>Tool</th><th>Description</th></tr>
  <tr><td>🖊️ <b>Pencil</b></td><td>Draw freely with the selected color</td></tr>
  <tr><td>🧽 <b>Eraser</b></td><td>Erase by drawing with white color</td></tr>
  <tr><td>⬛ <b>Rectangle</b></td><td>Draw outlined rectangles</td></tr>
  <tr><td>⚪ <b>Oval</b></td><td>Draw outlined circles/ellipses</td></tr>
  <tr><td>📏 <b>Line</b></td><td>Draw straight lines between two points</td></tr>
</table>

## ⚙️ Requirements
- **Java 8** or higher  
- **JDK** installed and added to your system `PATH`  
- Works on Windows, macOS, and Linux

## 🚀 How to Run
1. **Clone this repository**
   <pre> git clone https://github.com/yourusername/java-paint-app.git
   cd java-paint-app
   </pre>

2. Compile the Java file
   <pre> javac PaintApp.java </pre>
3. Run the application
   <pre> java PaintApp </pre>
   
## 🧩 Future Enhancements (Ideas)
- 🪣 Fill/Bucket tool
- 🅰️ Text tool
- 🌗 Dark mode
- 🖼️ Image import support
- 💾 Auto-save functionality
   
## 👩‍💻 Author
Akanksha Mane
🎓 B.E. Information Technology
