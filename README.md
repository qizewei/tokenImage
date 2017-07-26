# TankImageView

<table><tr>
<td><img src="https://img.shields.io/badge/version%20-1.0.0-brightgreen.svg" alt="version 1.0.0" ／></td>
<td><img src="https://img.shields.io/badge/API-15%2B-brightgreen.svg" alt="API 15+"／></td>
<td><img src="https://img.shields.io/badge/QQ-779754469-red.svg" alt="QQ:779754469"／></td>
</tr></table>

可以发弹幕ImageView，可以自定义弹幕的方向，弹幕的速度，弹幕的行数等等。

## 效果展示
![示例](http://oqg7nynni.bkt.clouddn.com/GIF.gif) 

## 关于
视频上跑弹幕已经很常见了，前段时间在支付宝的口碑人气眼中看到的在图片上跑的弹幕，挺不错的，可惜应该是Web页面。<br/>
所以自己封装了一个用于播放弹幕的ImageView，封装起来。

## 使用
- **使用场景**

    多用于商品，店铺，景区等展示评价。
    
- **创建TankImageView，在XML布局文件中**	

```
<com.example.tankimage.tankImageView
        android:layout_marginTop="20dp"
        android:id="@+id/tank"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:image="@drawable/background"
        app:textColor="#fff"
        app:speed="3"
        app:rows="4"
        app:direction="right"
        app:space="300"
        app:textSize="50sp" />   
```

- **在代码中设置弹幕,TankImageView只接受String数组（如果你有自己的对象集合，可以便利提取String集合）**	

```
 tankImageView src = (tankImageView)findViewById(R.id.tank);
        List<String> main = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            main.add(i,"第"+i+"条数据");
        }
        src.setLists(main);
```

- **Finish！让它跑起来吧**

## 引用

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add the dependency

	dependencies {
	        compile 'com.github.QzwJuHao:ColorUtils:v1.0.0'
	}