# citySelector
城市选择器(android)

这是一个城市选择实例项目，示例图：

![image](https://github.com/hxy/citySelector/blob/master/demonstration.jpg)

使用时可以将项目修改为module然后引用，修改为module的步骤：

1.修改名字app为citySelector

2.修改gradle中的apply plugin:为apply plugin: 'com.android.library'

3.删除defaultConfig中的applicationId

4.修改MainActivity为CitySelectActivity

5.修改布局文件activity_main为activity_cityselect

6.删除AndroidManifest中的activity定义和高德地图com.amap.api.v2.apikey的meta-data定义，将这两项放入主项目的AndroidManifest中

(1,4只是规范项目；5是因为module中的布局文件不能和主项目中重名)

修改完成之后正常引入即可
