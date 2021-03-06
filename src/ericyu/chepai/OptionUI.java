package ericyu.chepai;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by 麟 on 2016/4/4.
 */
public class OptionUI
{

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel practiceTab;
    private JTextField textField_start_hour;
    private JTextField textField_1st_add_second;
    private JTextField textField_1st_bid_second;
    private JTextField textField_2nd_add_second;
    private JTextField textField_start_minute;
    private JTextField textField_2nd_addMoney;
    private JTextField textField_2nd_bid_ready_second;
    private JTextField textField_2nd_bid_latest_second;
    private JButton runButton;
    private JCheckBox autoCheckBox;
    private JPanel inactionTab;
    private JLabel label_update_status;
    private JTextField textField_username;
    private JTextField textField_password;
    private boolean startButtonEnabled = true;

    public OptionUI() {

        // read strategies
        textField_username.setText(StrategyConfig.username);
        textField_password.setText(StrategyConfig.password);
        textField_start_hour.setText(String.valueOf(StrategyConfig.bidTimeHour));
        textField_start_minute.setText(String.valueOf(StrategyConfig.bidTimeMinute));
        textField_1st_add_second.setText(String.valueOf(StrategyConfig.firstBidSecond));
        textField_1st_bid_second.setText(String.valueOf(StrategyConfig.firstBidConfirmVCodeSecond));
        textField_2nd_add_second.setText(String.valueOf(StrategyConfig.addMoneySecond));
        textField_2nd_addMoney.setText(String.valueOf(StrategyConfig.addMoneyRange));
        textField_2nd_bid_ready_second.setText(String.valueOf(StrategyConfig.vCodeConfirmSecond));
        textField_2nd_bid_latest_second.setText(String.valueOf(StrategyConfig.latestBidTimeSecond));


        runButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!startButtonEnabled)
                    return;
//                label_update_status.setText("更新中...");
//                System.out.println("ready to update ");
//                Thread updateThread = new Thread(new Console(new String[]{CommandConstants.UPGRADE}));
//                updateThread.start();
//                try {
//                    updateThread.join();
//                    label_update_status.setText("更新完毕！");
//                    System.out.println("updated!");
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }

                // get strategies from ui
                StrategyConfig.username                      = textField_username.getText();
                StrategyConfig.password                      = textField_password.getText();
                StrategyConfig.bidTimeHour                   = Integer.parseInt(textField_start_hour.getText());
                StrategyConfig.exitTimeHour                  = StrategyConfig.bidTimeHour;
                StrategyConfig.bidTimeMinute                 = Integer.parseInt(textField_start_minute.getText());
                StrategyConfig.exitTimeMinute                = StrategyConfig.bidTimeMinute + 1;
                StrategyConfig.firstBidSecond                = Integer.parseInt(textField_1st_add_second.getText());
                StrategyConfig.firstBidConfirmVCodeSecond    = Integer.parseInt(textField_1st_bid_second.getText());
                StrategyConfig.addMoneySecond                = Integer.parseInt(textField_2nd_add_second.getText());
                StrategyConfig.addMoneyRange                 = Integer.parseInt(textField_2nd_addMoney.getText());
                StrategyConfig.vCodeConfirmSecond            = Integer.parseInt(textField_2nd_bid_ready_second.getText());
                StrategyConfig.latestBidTimeSecond           = Integer.parseInt(textField_2nd_bid_latest_second.getText());
//                System.out.println(StrategyConfig.bidTimeHour);
//                System.out.println(StrategyConfig.bidTimeMinute);
//                System.out.println(StrategyConfig.firstBidSecond);
//                System.out.println(StrategyConfig.firstBidConfirmVCodeSecond);
//                System.out.println(StrategyConfig.addMoneySecond);
//                System.out.println(StrategyConfig.addMoneyRange);
//                System.out.println(StrategyConfig.vCodeConfirmSecond);
//                System.out.println(StrategyConfig.latestBidTimeSecond);
                label_update_status.setEnabled(false);
                startButtonEnabled = false;
                runButton.setEnabled(false);
                // save current strategy configs
                System.out.println("write property File");
                StrategyConfig.writePropertyFile();

                Thread mainThread = new Thread(new Console(new String[]{}));
                mainThread.start();
            }
        });

    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("OptionUI");
        frame.setContentPane(new OptionUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
