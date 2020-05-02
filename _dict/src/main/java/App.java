import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private static final int w = 1280;
    private static final int h = 800;

    public static void main(String[] args) {
        JFrame frame= new JFrame();
        frame.setTitle("Dictionary");



Container container = frame.getContentPane();
container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
container.setLayout(new LayoutManager() {
    @Override
    public void addLayoutComponent(String s, Component component) {

    }

    @Override
    public void removeLayoutComponent(Component component) {

    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        return null;
    }

    @Override
    public void layoutContainer(Container container) {

    }
});

        JButton addButton = new JButton("Add word");
        addButton.setLocation(new Point(10, 10));
        addButton.setPreferredSize(new Dimension(w/10, h/10));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("dsfsdf");
            }
        });

        container.add(addButton);

        frame.pack();
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
