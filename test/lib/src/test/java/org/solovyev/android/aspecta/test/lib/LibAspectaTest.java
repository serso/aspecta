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

package org.solovyev.android.aspecta.test.lib;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;

public class LibAspectaTest {

    @Test
    public void shouldInjectAspectInDebug() {
        final LibAspect.Listener listener = mock(LibAspect.Listener.class);
        LibAspect.setListener(listener);

        final LibObject obj = new LibObject();
        obj.wovenMethod();

        if (BuildConfig.DEBUG) {
            verify(listener).onExecuted(any(JoinPoint.class));
        } else {
            verify(listener, never()).onExecuted(any(JoinPoint.class));
        }
        LibAspect.setListener(null);
    }
}
