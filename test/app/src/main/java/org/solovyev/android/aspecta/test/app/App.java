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
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;

public class App extends Application {

    public App() {
        AppAspect.setListener(new AppAspect.Listener() {
            @Override
            public void onExecuted(@NonNull JoinPoint jp) {
                Toast.makeText(App.this, jp.getSignature().getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
