>模仿华为的"天气"应用写的一个小Demo。部分功能、动画效果没有实现，也没有过多考虑性能、Bug等其它方面的因素。写这个Demo的初衷是想熟悉下目前网上常用的一些框架。

>Demo采用MVP模式，通过[Retrofit](https://github.com/square/retrofit "Retrofit")+[Rxjava](https://github.com/ReactiveX/RxJava "Rxjava")访问天气信息接口[和风天气](http://www.heweather.com/ "和风天气")获取数据。对于获取到的数据，采用内存+磁盘两级缓存。采用[LitePal](https://github.com/LitePalFramework/LitePal "LitePal")保存选择的城市到数据库中。采用[ButterKnife](https://github.com/JakeWharton/butterknife "ButterKnife")替换findViewById。用到了[EventBus](https://github.com/greenrobot/EventBus "EventBus")来作为组件间通信的工具。

界面展示如下:

>选择城市及城市天气信息加载:

![选择城市.gif](https://upload-images.jianshu.io/upload_images/3381990-7aaba9ba147d5898.gif?imageMogr2/auto-orient/strip)

![添加城市.gif](https://upload-images.jianshu.io/upload_images/3381990-7267dd0c673a657f.gif?imageMogr2/auto-orient/strip)

![刷新.gif](https://upload-images.jianshu.io/upload_images/3381990-c5a0367fb5686a3c.gif?imageMogr2/auto-orient/strip)

>设置及拖动排序:

![设置及拖动排序.gif](https://upload-images.jianshu.io/upload_images/3381990-df6dc856b2880a7b.gif?imageMogr2/auto-orient/strip)
>删除城市:

![删除城市.gif](https://upload-images.jianshu.io/upload_images/3381990-cf48ca809e2b4de8.gif?imageMogr2/auto-orient/strip)
>搜索城市:

![搜索城市.gif](https://upload-images.jianshu.io/upload_images/3381990-368c5dce5c88677d.gif?imageMogr2/auto-orient/strip)
