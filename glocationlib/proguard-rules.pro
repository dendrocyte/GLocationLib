# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
#保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* { *; }
# 不混淆R类里及其所有内部static类中的所有static变量字段，$是用来分割内嵌类与其母体的标志
-keep public class **.R$*{
   public static final int *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 若沒有{*;} 只會保留其類別 不會保留function
-keep public class com.example.googlelocation.listener.** { *; }

# 若要混淆的好，程式的抽象化要好 或是 在設計時就要做這層的考慮
# 保留我們自定義控件（繼承自View）的類別和特定成員不被混淆,
# 要在宣稱保留該類別時就應該在裡頭標記要保留誰; 若在之後宣稱,仍會被蓋掉
# 在引入時不會發現，但在build建立時才會發現少了這三個成員，會建不起來
-keep public class com.example.googlelocation.customView.*{
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public GpsUtil *();
}

-keep public class com.example.googlelocation.customView_abs.*{
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void *(AbsLocationListener);
    public *** *(int);
    public static final <fields>;
}
# 保留我們自定義控件（繼承自View）的類別不被混淆而已
#-keep public class com.example.googlelocation.customView.*
# 保留我們自定義控件（繼承自Activity, 繼承自Fragment）不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment

#-keep class com.google.android.gms.** { *; }
#-dontwarn com.google.android.gms.**


#混淆inner class
-keep public class com.example.googlelocation.util.GpsUtil$onGpsListener { *; }
# ****不在上面所贅述到的都會被混淆*****
-keep public class com.example.googlelocation.util.GpsUtil {
    public void *(android.app.Activity, com.example.googlelocation.util.GpsUtil$onGpsListener);
}

-keep public class com.example.googlelocation.util.LocationUpdateUtil { *; }