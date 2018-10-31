# Description
AspectJ for Android.

# Usage

In `build.gradle`:
```groovy
buildscript {
  dependencies {
    repositories {
      google()
      jcenter()
    }

    classpath 'org.solovyev.android.aspecta:plugin:1.0.0'
  }
}

apply plugin: 'com.android.application' // or 'com.android.library'
apply plugin: 'org.solovyev.android.aspecta'

aspecta {
  // enabled AspectA only in debug builds
  enabled { BaseVariant variant -> variant.buildType.debuggable}
}

dependencies {
  implementation 'org.aspectj:aspectjrt:1.9.1'
}
```

Define an annotation:
```java
package org.example.android;

// retention must be class
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface MyAnnotation {
}
```

Define your aspect:
```java
package org.example.android;

@Aspect
public class MyAspect {
    @Pointcut("execution(@org.example.android.MyAnnotation * *(..))")
    public void method() {
    }

    @Before("method()")
    public void execute(@NonNull JoinPoint jp) {
        // define action
    }
}
```

Use the annotation. The code will be automatically woven by the library:
```java
package org.example.android;

@Aspect
public class MainActivity extends Activity {
    
    @MyAnnotation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```
