#AgentWeb
-keep class com.just.library.** {
    *;
}
-dontwarn com.just.library.**
-keepclassmembers class com.just.library.agentweb.AndroidInterface{ *; }
