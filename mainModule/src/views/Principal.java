package views;

import customClass.JFrame;
import customClass.JPanel;

import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame {

    public Principal() {
        super("PointHelper", true,
                JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                            JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                                        new JLabel("inicio do expediente"),
                                        new JTextField(10)
                                    )
                        )
        );
    }
}
