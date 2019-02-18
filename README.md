# UI
- 需要状态页的Activity继承StateBaseActivity,Fragment继承StateBaseFragment(其中默认提供了5种状态的管理：showLoading()、showEmpty()、showError()、showSuccess()、showNetError()。默认使用showLoading(),其他状态在子类中直接调用即可)
- 如果不需要状态页的管理，直接继承BaseActivity或者BaseFragment即可
- 列表适配器统一用BaseRecyclerViewAdapterHelper（项目地址：https://github.com/CymChad/BaseRecyclerViewAdapterHelper ）
- 公共资源统一放在lib-resource中，包括公用的工具类、图片、drawable文件、第三方引用
- 项目中所有的xml文件的单位不再使用sp和dp，统一使用pt,UI图上是多少像素，就写多少pt。（项目地址：https://github.com/JessYanCoding/AndroidAutoSize ）    
- 建议项目全部使用Fragment来绘制页面，不仅性能更好，使用也更加灵活。默认集成了Fragmentation（项目地址：https://github.com/YoKeyword/Fragmentation ）

  
# 网络

项目统一使用Retrofit作为基础网络框架，基本使用如下：
```java
    Box.getRetrofit(GuardianshipApi.class)
                .getGuardianships()
                .compose(RxUtils.<List<GuardianshipBean>>httpResponseTransformer())
                .as(RxUtils.<List<GuardianshipBean>>autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<GuardianshipBean>>() {
                    @Override
                    public void onNext(List<GuardianshipBean> guardianshipBeans) {
                        mView.loadDataSuccess(guardianshipBeans);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        mView.loadDataError(ex.code, ex.message);
                    }

                    @Override
                    protected void onNetError() {
                        mView.onNetworkError();
                    }
                });
```     
      
      
# 用户系统

```java
//设置进去
Box.getSessionManager().setUser(objects[0]);
//全局任意位置获取
SessionUserInfo userInfo=Box.getSessionManager().getUser();
```
# 新建模块

- build.gradle统一改成如下代码,其他自动生成的代码全部删除：
```java
apply from: rootProject.file('auto-dependences.gradle')
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
}
```
- 如果想单独运行该模块，只需两步：（1）在local.properties中'模块名=true'；（2）在main文件夹下建立子目录debug/(java、res、assets、AndroidManifest.xml)(用法与CC一致。项目地址：https://github.com/luckybilly/CC)

# 路由
路由使用ARetrofit，项目地址：https://github.com/yifei8/ARetrofit 。因用法特别简单，请看官方文档。

# 工具类
- 项目中集成了MMKV，性能比原生的SharedPreferences更好，建议使用
- 日志统一使用Timber，禁止使用原生Log，便于统一管理日志的开启和关闭

# 其他
- 在Fragment中使用color、string等资源文件禁止直接使用getContext()获取到的上下文，而应该使用Box.class中提供的全局上下文，具体请查看Box.class
- 全局所有配置均在GlobalConfiguration.class这个文件中
- 所有第三方库的引用都应该放在lib-resource下的build.gradle中，方便统一管理，避免重复引用；如果需要在Application中初始化，请在AppStore中初始化
- 各模块中的资源文件命名应该遵循阿里标准，统一带上区别于其他模块的唯一标示字段
- 如果因为主线程耗时监听出现烦人的声音，可以先将lib-core下AndroidManifest.xml中的这句代码暂时注释掉：
```java
 <meta-data
      android:name="com.gzq.lib_core.base.quality.QualityBlockCanary"
      android:value="AppLifecycle" />
```
- 后面补充的一些工具类也应该直接放在lib-resource中，即使只在其中一个模块中有使用，这样做的好处是方便其他开发者一目了然的知道是否有自己需要使用的工具，避免同样功能的工具多次引入
- 建议每增加一位协作开发者，就增加一个对应的"update-xxx.md",不在一个文件中维护的目的是方便git的管理，避免每次合代码都出现很多的冲；同时也利于职责分离，方便后期管理人员的跟踪