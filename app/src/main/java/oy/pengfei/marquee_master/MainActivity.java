package oy.pengfei.marquee_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xaf.marqueelib.MarqueeView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MarqueeView marquee = findViewById(R.id.marqueeView);
        List<String> contentList = new ArrayList<>();
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.colorAccent);
        contentList.add("1.“网上95%的名人名言都是瞎掰，包括这句。”——鲁迅");
        //开启滚动
        marquee.setDataAndScroll(contentList, colorList);

        //点击事件
        marquee.getCurrentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("cccc","hhhh");
            }

        });

        MarqueeView marquee2 = findViewById(R.id.marqueeView_2);
        List<String> contentList2 = new ArrayList<>();
        contentList2.add("1.“网上95%的名人名言都是瞎掰，包括这句。”——鲁迅");
        contentList2.add("2.“我即使是死了，钉在棺材里，也要在墓里，用这腐朽的声带喊出：我没说过这句话”——鲁迅");
        contentList2.add("3.“你们尽管编名言，说过一句，算我输。”——宫崎骏");
        colorList.add(R.color.colorPrimary);
        colorList.add(R.color.colorPrimaryDark);
        marquee2.setDataAndScroll(contentList2, colorList);



    }
}
