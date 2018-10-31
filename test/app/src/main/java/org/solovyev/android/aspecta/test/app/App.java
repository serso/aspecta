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

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.solovyev.android.aspecta.test.lib.LibAspect;

public class App extends Application {

    public App() {
        AppAspect.setListener(new AppAspect.Listener() {
            @Override
            public void onExecuted(@NonNull JoinPoint jp) {
                handle(makeMessage("App", jp));
            }
        });
        LibAspect.setListener(new LibAspect.Listener() {
            @Override
            public void onExecuted(@NonNull JoinPoint jp) {
                handle(makeMessage("Lib", jp));
            }
        });
    }

    private void handle(@NonNull String msg) {
        Toast.makeText(App.this, msg, Toast.LENGTH_SHORT).show();
        Log.d("Aspecta", msg);
    }

    @NonNull
    private String makeMessage(@NonNull String tag, @NonNull JoinPoint jp) {
        final Signature signature = jp.getSignature();
        final Object obj = jp.getThis();
        return tag + "/" + signature.getName() + "@" + obj.getClass().getSimpleName();
    }
}
