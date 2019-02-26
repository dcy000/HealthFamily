package com.sjtu.yifei.VxNBehJBVj;

import com.gcml.module_sos_deal.MainSOSDealFragment;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$VxNBehJBVj$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/sosdeal/main", MainSOSDealFragment.class);
    return routMap;
  }
}
