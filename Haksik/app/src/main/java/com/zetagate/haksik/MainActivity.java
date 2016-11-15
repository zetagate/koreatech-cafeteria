package com.zetagate.haksik;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {   public static TextView textView;

    public static Button button;
    public static String[] title;
    public static String total = new String("");
    public static int a = 0;
    public static ArrayList<String> list = new ArrayList<>();
    public static Handler handler;

    public static Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {


                Document document =
                        Jsoup.connect("http://coop.koreatech.ac.kr/dining/menu.php?sday=1478530800&sdate=2")
                                .get();
                Elements elements = document.select("td.menu-listo");
                for (Element element : elements) {

                    list.add(element.getElementsByTag("td").text().replace(' ', '\n'));//리스트에 추가

                }

                elements = document.select("td.menu-list");
                for (Element element : elements) {

                    list.add(element.getElementsByTag("td").text().replace(' ', '\n'));//리스트에 추가


                }
                Message message = handler.obtainMessage();
                handler.sendMessage(message);// 헨들러를 통해서 메인 스레드에 신호 전달.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tempText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                textView.setText("");//텍스트 뷰 초기화
                for (int i = 0; i < list.size(); i++) {

                    title[i] = list.get(i);
                    String m;
                    switch (i % 8) {
                        case 0:
                            m = "한식\n";
                            break;
                        case 1:
                            m = "일품\n";
                            break;
                        case 2:
                            m = "특식\n";
                            break;
                        case 3:
                            m = "양식\n";
                            break;
                        case 4:
                            m = "능수관\n";
                            break;
                        case 5:
                            m = "수박여\n";
                            break;
                        case 6:
                            m = "Campus2\n";
                            break;
                        case 7:
                            m = "empty\n";
                            break;
                        default:
                            m = "";
                    }
                    total = total + "\n\n\n" + m + title[i].replace(' ', '\n');
                    textView.setText(total);
                }
            }
        };

        title = new String[24];

        if(!thread.isAlive()) {
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}