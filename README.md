# MarqueeViewLibrary
一个竖滚同时横滚的跑马灯Library

效果图
---
![image](gif/marquee.gif)

## 使用

### Gradle:
步骤1:
```
allprojects {
        repositories {
                ...
                maven { url 'https://jitpack.io' }
        }
}
```  
步骤2:
```
dependencies {
        implementation 'com.github.pfsir:Marquee:V1.0.0'
}
```
### 属性
MarqueeView属性

| Attribute 属性          | Description 描述 |
|:---				     |:---|
| horizontal_text_speed         |横向文字的滚动速度    |
| horizontal_text_size          |横向文字的大小        |
| horizontal_text_width         |横向滚动控件的宽      |
| horizontal_text_height        |横向滚动控件的高      |
| horizontal_text_switch_time   |竖向滚动时间         |


TextHorizontalView属性

| Attribute 属性          | Description 描述 |
|:---				     |:---|
| text_color                     |    文字颜色       |
| text_size                      | 文字大小            |
| text_scroll_speed_time         |  每个字的滚动时间     |

### 常见用法：使用MarqueeView

#### XML
```
<com.xaf.marqueelib.MarqueeView
        android:id="@+id/marqueeView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/marquee_bg"
        android:paddingLeft="20dp"
        android:paddingRight="20dp"
        app:horizontal_text_height="40dp"
        app:horizontal_text_size="20sp"
        app:horizontal_text_width="300dp" />
```

#### 设置数据
```
MarqueeView marquee = findViewById(R.id.marqueeView);
List<String> contentList = new ArrayList<>();
List<Integer> colorList = new ArrayList<>();
colorList.add(R.color.colorAccent);
contentList.add("1.“网上95%的名人名言都是瞎掰，包括这句。”——鲁迅");

marquee.setDataAndScroll(contentList, colorList);
```

#### 设置监听事件
```
marquee.getCurrentView().setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View arg0) {
        Log.d("cccc","hhhh");
    }
});
```
