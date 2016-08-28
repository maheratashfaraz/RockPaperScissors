package main.neural;

public class Background {
    JButton btn_browse;
    JButton btn_convert;

    public void createFrame() {
        JFrame frame = new JFrame("Gesture Recogniser");
        frame.setLayout(new GridLayout(2, 2));

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        btn_browse = new JButton("Browse");
        btn_convert = new JButton("Convert");

        JLabel lbl_first = new JLabel(new ImageIcon("frog.jpg"));
        JLabel lbl_two = new JLabel("hey pal");

        panel1.add(lbl_first);
        panel2.add(lbl_two);
        panel3.add(btn_browse);
        panel4.add(btn_convert);

        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel4);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ImageIcon resizeImageIcon(ImageIcon imageicon, int i, int b) {

        Image image = imageicon.getImage(); // transform it
        Image newimg = image.getScaledInstance(i, b,
                java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg); // transform it back

    }
}
