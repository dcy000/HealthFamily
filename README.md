# UI
- 需要状态页的Activity继承StateBaseActivity,Fragment继承StateBaseFragment(其中默认提供了5种状态的管理：showLoading()、showEmpty()、showError()、showSuccess()、showNetError()。默认使用showLoading(),其他状态在子类中直接调用即可)
- 列表适配器统一用BaseRecyclerViewAdapterHelper（项目地址：https://github.com/CymChad/BaseRecyclerViewAdapterHelper）
- 公共资源统一放在lib-resource中，包括公用的工具类、图片、drawable文件
- 项目中所有的xml文件的单位不再使用sp和dp，统一使用pt,UI图上是多少像素，就写多少pt。（项目地址：https://github.com/JessYanCoding/AndroidAutoSize）    
- 建议项目全部使用Fragment来绘制页面，不仅性能更好，使用也更加灵活。默认集成了Fragmentation（项目地址：https://github.com/YoKeyword/Fragmentation）

  
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
路由使用ARetrofit，项目地址：https://github.com/yifei8/ARetrofit。 因用法特别简单，请看官方文档。

# 工具类
- 项目中集成了MMKV，性能比原生的SharedPreferences更好，建议使用
- 日志统一使用Timber，禁止使用原生Log，便于统一管理日志的开启和关闭
