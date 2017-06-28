# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Duy\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class equation to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep class com.microblink.** { *; }
-keepclassmembers class com.microblink.** { *; }
-dontwarn android.hardware.**

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Duy\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class equation to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

######################################

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#-keep class com.google.android.gms.** { *; }

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

#############################################
#############################################

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#keep all classes that might be used in XML layouts
-keep public class * extends android.view.View
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.Fragment

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }

# appcombat
-keep public class * extends android.support.v7.app.ActionBarActivity { *; }

#keep all public and protected methods that could be used by java reflection
-keepclassmembernames class * {
    public protected <methods>;
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
	public static final android.os.Parcelable$Creator *;
}

##########################################
-dontwarn javax.annotation.**
-dontwarn org.apache.log4j.**
############################
-dontwarn com.x5.**
-keep class com.x5.template.** { *; }
-keep class com.x5.util.* { *; }
####################
-dontwarn java.beans.**
-dontwarn javax.swing.**
-dontwarn java.awt.**
-keep class org.** { *; }
-keep class edu.** { *; }
-keep class de.lab4inf.** { *; }
-keep class com.google.common** { *; }
-keep class cc.redberry.** { *; }
-keep class com.myscript.** { *; }
##################
-dontwarn rx.internal.util.**