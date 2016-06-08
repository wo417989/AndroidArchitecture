#### android architecture base on mvp+data binding demo

### 这是一个采用MVP + databinding 架构的一个demo。
MVP 是比较流行的android架构设计模式，google官方又推出了数据绑定的库，使得MVVM这种模式也流行起来。这个demo 是把这两种设计模式相结合所产生的。

##### 本demo所采用的技术实现如下：
1.databinding部分采用google 官方库，当然部分采用butterknife库相结合的模式
2.分层之间的通信解耦是通过RxJava实现的
3.网络通信是采用retrofit2 + OKhttp3

##### 后续会对此架构进一步的改善