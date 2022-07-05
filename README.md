# MyCustomTicketApp

• 项目描述：此项目基于MVP架构。视图层包含首页、精选、特惠和搜索四大界面，每个界面有不同的显示风格和内容。用户可以浏览或搜索心仪的商品，通过点击商品或扫描二维码获取淘口令，并跳转到淘宝自动领券。

• 主要内容：1.M层使用GsonFormat创建数据封装类，P层使用Retrofit获取商品数据并解析，通过接口回调通知V层更新商品信息；
           2.采用单Activity多Fragment模式设计V层，用RecyclerView显示商品，外层嵌套TkRefreshLayout实现上拉加载更多功能；
           3.通过自定义View绘制搜索界面的主要布局，实现搜索功能的同时，编写缓存工具类扩展实现历史和推荐功能；
           4.编写淘口令界面，使用Zxing和RxTool实现扫码功能，并在V层设置接口回调，可跳转至淘口令界面，启动淘宝领券； 5.编写相关工具类，用于创建Retrofit实例、创建逻辑层实现类、数据缓存、拼接URL、保存常量、界面跳转等辅助功能。

• Tips: 1.将Activity嵌套Fragment再嵌套Fragment的模式抽取成统一的布局，从而实现复用；
        2.自定义AutoViewPager实现轮播图，自定义LoadingView实现加载动画，自定义TextFlowLayout实现搜索界面的主要布局；
        3.修改NestedScrollView源码解决嵌套滑动冲突、提前加载和复用问题，修改RxTool源码重绘扫码界面并识别领券二维码；
        4.编写JsonCacheUtils工具类，配合SharedPreferences和Gson辅助实现搜索、历史和推荐记录的缓存与显示
