#okhttp3.0
-dontwarn com.hitomi.tilibrary.**
-keep class com.hitomi.tilibrary.** { *;}
-keep class com.filippudak.ProgressPieView.ProgressPieView
# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# support design
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#-------------------------------------------------------------------------