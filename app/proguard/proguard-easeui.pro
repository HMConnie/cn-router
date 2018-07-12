#easeui
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**

# BaiduMap
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

# google-play-services
-keep class com.google.android.gms.** {*;}
-dontwarn com.google.android.gms.**


# HMSSdkBase_2.5.1.300
-keep class aidl.com.huawei.hms.core.aidl.** {*;}
-keep class com.hianalytics.** {*;}
-dontwarn aidl.com.huawei.hms.core.aidl.**
-dontwarn com.hianalytics.**

# HMSSdkPush_2.5.1.300
-keep class com.huawei.hms.support.api.** {*;}
-dontwarn com.huawei.hms.**

# hyphenatechat_3.3.4
-keep class com.hyphenate.** {*;}
-keep class com.superrtc.** {*;}
-keep class internal.org.apache.http.entity.mime.** {*;}
-dontwarn com.superrtc.**
-dontwarn com.hyphenate.**
-dontwarn internal.org.apache.http.entity.mime.**

# MiPush_SDK_Client_2_2_19
-keep class com.google.protobuf.micro.** {*;}
-keep class com.xiaomi.** {*;}
-keep class org.apache.thrift.** {*;}
-dontwarn com.google.protobuf.micro.**
-dontwarn com.xiaomi.**
-dontwarn org.apache.thrift.**

# org.apache.http.legacy
-keep class android.net.** {*;}
-keep class com.android.internal.http.multipart.** {*;}
-keep class org.apache.** {*;}
-dontwarn android.net.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.**