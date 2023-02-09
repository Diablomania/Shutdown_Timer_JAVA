import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SimpleGUI extends JFrame {
    public JButton button = new JButton("START");
    public JTextField input = new JTextField("",5);
    public JTextField input2 = new JTextField("",5);
    public JTextField input3 = new JTextField("",5);
    public JLabel label = new JLabel("Сhoose what you need:");
    public JLabel labelH = new JLabel("Hours:");
    public JLabel labelM = new JLabel("Minutes:");
    public JLabel labelS = new JLabel("Seconds:");
    public JLabel labelTimer = new JLabel("00:00:00");
    public JRadioButton radio1 = new JRadioButton("Shutdown");
    public JRadioButton radio2 = new JRadioButton("Restart");
    public JRadioButton radio3 = new JRadioButton("Log OUT(immediately)");
    public JRadioButton radio4 = new JRadioButton("Hibernate(immediately)");

    public boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }



    public SimpleGUI () {
        super("Shutdown Timer");
        this.setBounds(100,100, 650,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        JPanel container1 = new JPanel(new GridLayout(4, 1, 10,10));
        JPanel container2 = new JPanel(new GridLayout(4, 1,10,10));

        container1.add(label);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);
        group.add(radio3);
        group.add(radio4);
        container1.add(radio1);
        radio1.setSelected(true);
        container1.add(radio2);
        container1.add(radio3);
        container1.add(radio4);

        container2.add(labelH);
        container2.add(input);
        container2.add(labelM);
        container2.add(input2);
        container2.add(labelS);
        container2.add(input3);
        container2.add(labelTimer);

        button.addActionListener(new ButtonEventListener ());
        container2.add(button);

        container.add(container1, BorderLayout.WEST);
        container.add(container2, BorderLayout.CENTER);
    }

    int indexOfPressBtn = 0;
    boolean started = false;

    class ButtonEventListener implements ActionListener {

        public void actionPerformed (ActionEvent e) {

            String hoursText = "";
            String minutesText = "";
            String secondsText = "";


            if (input.getText().isEmpty()) {} else {hoursText = String.valueOf(input.getText());}
            if (input2.getText().isEmpty()) {} else {minutesText = String.valueOf(input2.getText());}
            if (input3.getText().isEmpty()) {} else {secondsText = String.valueOf(input3.getText());}


            int hours = 0;
            int minutes = 0;
            int seconds = 0;


            if (isNumber(hoursText)) {
                if (input.getText().isEmpty()) {hours = 0;} else {hours = Integer.parseInt(input.getText()) * 3600;
                }
            }

            if (isNumber(minutesText)) {
                if (input2.getText().isEmpty()) {minutes = 0;} else {minutes = Integer.parseInt(input2.getText()) * 60;
                }
            }

            if (isNumber(secondsText)) {
                if (input3.getText().isEmpty()) {seconds = 0;} else {seconds = Integer.parseInt(input3.getText());
                }
            }

            int timeInMSec = hours + minutes + seconds;
            int i = timeInMSec;
            timeInMSec *= 1000;



            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            final Runnable runnable = new Runnable() {
                int countdown = i * 1000;
                public void run() {

                    labelTimer.setText("Time left: " + String.format("%02d", countdown / 3600000) + ":" + String.format("%02d", (countdown / 60000) % 60) + ":" + String.format("%02d", (countdown / 1000) % 60));
                    countdown--;
                    if (button.getModel().isArmed()) {
                        scheduler.shutdown();
                    }
                }
            };


            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    if (radio1.isSelected()) {
                        try {
                            Runtime run = Runtime.getRuntime();
                            run.exec("shutdown /s");
                        } catch (IOException et) {
                            et.printStackTrace();
                        }
                    } else if (radio2.isSelected()){
                        try {
                            Runtime run = Runtime.getRuntime();
                            run.exec("shutdown /r");
                        } catch (IOException et) {
                            et.printStackTrace();
                        }
                    } else if(radio3.isSelected()) {
                        try {
                            Runtime run = Runtime.getRuntime();
                            run.exec("shutdown /l");
                        } catch (IOException et) {
                            et.printStackTrace();
                        }
                    } else if(radio4.isSelected()) {
                        try {
                            Runtime run = Runtime.getRuntime();
                            run.exec("shutdown /h");
                        } catch (IOException et) {
                            et.printStackTrace();
                        }
                    }
                }
            };


            final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
            final Runnable runnable2 = new Runnable() {
                int countdown2 = i*1000;
                public void run() {
                    countdown2--;
                    if (countdown2 == 0) {
                        timer.schedule(task, 1);
                    }
                    if (button.getModel().isArmed()) {
                        scheduler2.shutdown();
                    }
                }
            };


            if (e.getSource()==button) {
                if (started == false) {
                    button.setText("STOP");
                    started = true;
                    scheduler.scheduleAtFixedRate(runnable, 0, 1, MILLISECONDS);
                    scheduler2.scheduleAtFixedRate(runnable2, 0, 1, MILLISECONDS);
                } else {
                    button.setText("START");
                    started = false;
                }
            }

             }
        }



    }


