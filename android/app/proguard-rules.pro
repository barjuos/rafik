# Keep kotlinx.serialization generated serializers for our data model.
-keepattributes *Annotation*, InnerClasses, Signature

-keep,includedescriptorclasses class com.rafiq.adhkar.data.model.**$$serializer { *; }
-keepclassmembers class com.rafiq.adhkar.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.rafiq.adhkar.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.rafiq.adhkar.data.model.Dhikr { *; }

# Room entities are referenced from generated code.
-keep class com.rafiq.adhkar.data.db.** { *; }
