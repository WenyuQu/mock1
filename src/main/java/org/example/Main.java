package org.example;

import java.util.ArrayList;
import javax.swing.*;              // Swing 定时器
import java.awt.GridLayout;
import java.awt.event.ActionListener;  // 定义时间到了要做什么
import java.awt.event.ActionEvent;     // 定时器触发的事件
import java.awt.Color;  // 使用 Color.RED 和 Color.WHITE
class Person{
    public String location;
    private ArrayList<String> waitinglist = new ArrayList<>();
    private int status = 0;
    private int finishhour = 0;
    public void addList(String x) {
        waitinglist.add(x);
    }
    public String takeNex() {
        if (waitinglist.isEmpty()){
            return null;
        }
        return waitinglist.remove(0);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getFinnishhour(){
        return finishhour;
    }

    public void setFinishhour(int finishhour) {
        this.finishhour = finishhour;
    }
    public int getWaitingCount() {
        return this.waitinglist.size();  // 返回这位巡检员还有几个展厅在排队
    }
}
class Lighting extends Person{
    public Lighting(String location){
        this.location = location;
    }
    private int duration = 2;
    public int getDuration(){
        return duration;
    }
}
class Climate extends Person{
    public Climate(String location){
        this.location = location;
    }
    private int duration = 3;

    public int getDuration() {
        return duration;
    }

}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] names = MuseumFeed.getGalleryNames();  // 拿到所有展厅名
        Lighting mike = new Lighting("Ceramics");
        Climate Jack = new Climate("Maps");
        ArrayList<Double> humihistory = new ArrayList<>();
        ArrayList<Integer> lighthistory = new ArrayList<>();
        int galleryCount = MuseumFeed.getGalleryNames().length;  // 展厅数量
        for (int i = 0; i < galleryCount; i++) {
            humihistory.add(50.0);  // 第 i 个展厅的湿度初始按正常处理
            lighthistory.add(100);  // 第 i 个展厅的光照初始按正常处理
        }
        JLabel[] labels = new JLabel[galleryCount];      // 创建数组，给四个展厅各留一个位置

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Museum");  // 创建标题为 Museum 的窗口，用变量 frame 保存它// 让 Swing 创建窗口，这段只执行一次
            frame.setLayout(new GridLayout(2, 2));       // 窗口排成两行两列

            for (int i = 0; i < names.length; i++) {
                labels[i] = new JLabel(names[i]);        // 用第 i 个展厅的名字创建标签
                labels[i].setOpaque(true);  // 让这张标签把背景色画出来
                frame.add(labels[i]);                    // 把这张标签放进窗口
            }

            frame.setSize(520, 160);                     // 设置窗口大小
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);                      // 显示窗口
        });

        Timer timer = new Timer(MuseumFeed.MILLIS_PER_HOUR, new ActionListener() {
            private int hour = 0;  // 从第 0 小时开始，保留这个计数

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Museum hour " + hour);  // 打印当前模拟小时
                String[] reportLines = new String[names.length];  // 给每个展厅留一行报告的位置
                String alarmText;


                for (int i = 0; i < MuseumFeed.getGalleryNames().length;i++){
                    //for (String galleries : MuseumFeed.getGalleryNames()){
                    double humid = MuseumFeed.getHumidity(i,hour);

                    int light = MuseumFeed.getLight(i,hour);
                    if (humid != -1) {
                        humihistory.set(i, humid);  // 更新这个展厅的湿度，缺测时保留旧值
                    }
                    if (light > 200 && lighthistory.get(i) <= 200){
                        mike.addList(MuseumFeed.getGalleryNames()[i]);
                    }
                    if (light != -1) {
                        lighthistory.set(i, light);  // 更新这个展厅的光照，缺测时保留旧值
                    }
                    String lightText;                 // 准备存放要显示的文字
                    if (light == -1) {                // 模拟器用 -1 表示这次没读到数据
                        lightText = "N/A";
                    } else {
                        lightText = light + " lux";   // 例如整数 220 拼上单位，得到字符串 "220 lux"
                    }
                    String humidText;
                    humidText = String.format("%.1f",humid) + "%";
                    labels[i].setText("<html>" + names[i] + "<br>" + lightText+"<br>"+ humidText+ "<html>");           // 更新这个展厅的标签
                    double lastHumid = humihistory.get(i);  // 这个展厅最近一次有效湿度
                    int lastLight = lighthistory.get(i);    // 这个展厅最近一次有效光照

                    if (lastHumid < 40 || lastHumid > 60 || lastLight > 200) {
                        labels[i].setBackground(Color.RED);// 任意一项超标，这一格变红
                        alarmText = "ALARM";
                    } else {
                        labels[i].setBackground(Color.WHITE);  // 两项都正常，这里选白色
                        alarmText = "normal";
                    }
                    if (hour >= Jack.getFinnishhour()){
                        Jack.setStatus(0);
                    }
                    if (hour >= mike.getFinnishhour()){
                        mike.setStatus(0);
                    }
                    if (humid != -1 && humid < 40 && 40 <= humihistory.get(i) && humihistory.get(i) <= 60){
                        //latehumid = humid;
                        Jack.addList(MuseumFeed.getGalleryNames()[i]);
                    }
                    if (humid > 60 && 40 <= humihistory.get(i) && humihistory.get(i) <= 60){
                        //latehumid = humid;
                        Jack.addList(MuseumFeed.getGalleryNames()[i]);
                    }

                    if (mike.getStatus() == 0 && hour >= mike.getFinnishhour()) {


                        String nextBay = mike.takeNex();  // 只取一次，因为取出来就会从队列删除
                        if (nextBay != null) {

                            for (int id = 0; id < names.length; id++) {
                                if (names[id].equals(nextBay)) {             // 找到队首展厅对应的编号
                                    MuseumFeed.requestVisit(hour, id, "Lighting");
                                    mike.setFinishhour( hour + mike.getDuration());
                                    mike.setStatus(1);
                                    break;                                  // 已经派工，结束这次查找
                                }
                            }
                        }

                    }
                    if (Jack.getStatus() == 0 && hour >= Jack.getFinnishhour()) {

                        String nextBay = Jack.takeNex();  // 只取一次，因为取出来就会从队列删除
                        if (nextBay != null) {
                            //String[] names = MuseumFeed.getGalleryNames();  // 拿到所有展厅名
                            for (int id = 0; id < names.length; id++) {
                                if (names[id].equals(nextBay)) {             // 找到队首展厅对应的编号
                                    MuseumFeed.requestVisit(hour, id, "Climate");
                                    Jack.setFinishhour( hour + Jack.getDuration());
                                    Jack.setStatus(1);
                                    break;                                  // 已经派工，结束这次查找
                                }
                            }
                        }

                    }
                    reportLines[i] = names[i] + " | " + humidText + " | " + lightText + " | " + alarmText;
                }
                if (hour > 0 && hour % 6 == 0) {
                    System.out.println("Report at hour " + hour);  // 第 6、12、18……小时打印标题

                    for (int i = 0; i < labels.length; i++) {


                        System.out.println(reportLines[i]);   // 取出第 i 个展厅的标签文字
                    }
                    System.out.println(Jack.getLocation() +" "+ Jack.getStatus() +" "+ Jack.getWaitingCount());
                    System.out.println(mike.getLocation() +" "+ mike.getStatus() +" "+ mike.getWaitingCount());
                }
                hour++;  // 本轮处理完，下次就进入下一小时
            }
        });
        timer.setInitialDelay(0);  // 第一次立即执行，显示第 0 小时
        timer.start();             // 启动计时器，只调用一次


        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //System.out.printf("Hello and welcome!");


    }
}