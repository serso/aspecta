// Copyright 2018 Sergey Solovyev <se.solovyev@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.solovyev.android.aspecta.test.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MyAspect {

    public interface Listener {
        void onExecuted(@NonNull JoinPoint jp);
    }

    @Nullable
    private static Listener sListener;

    public static void setListener(@Nullable Listener listener) {
        sListener = listener;
    }

    @Pointcut("execution(@org.solovyev.android.aspecta.test.app.MyAnnotation * *(..))")
    public void method() {
    }

    @Pointcut("execution(@org.solovyev.android.aspecta.test.app.MyAnnotation *.new(..))")
    public void constructor() {
    }

    @Before("method() || constructor()")
    public void execute(@NonNull JoinPoint jp) {
        if (sListener != null) {
            sListener.onExecuted(jp);
        }
    }
}
