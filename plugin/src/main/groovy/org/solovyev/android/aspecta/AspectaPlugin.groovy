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

package org.solovyev.android.aspecta

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectaPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    def hasApp = project.plugins.withType(AppPlugin)
    def hasLib = project.plugins.withType(LibraryPlugin)
    if (!hasApp && !hasLib) {
      throw new IllegalStateException("'com.android.application' or 'com.android.library' plugin required.")
    }

    final def log = project.logger
    final def variants
    if (hasApp) {
      variants = project.android.applicationVariants
    } else {
      variants = project.android.libraryVariants
    }

    project.extensions.create('aspecta', AspectaExtension)

    variants.all { variant ->
      if (!project.aspecta.isEnabled(variant)) {
        log.debug("AspectA is disabled.")
        return
      } else {
        log.debug("AspectA is enabled and will weave the code.")
      }

      JavaCompile jc = variant.javaCompile
      jc.doLast {
        String[] args = [
                "-showWeaveInfo",
                "-1.5",
                "-inpath", jc.destinationDir.toString(),
                "-aspectpath", jc.classpath.asPath,
                "-d", jc.destinationDir.toString(),
                "-classpath", jc.classpath.asPath
        ]

        final boolean hasBootClasspath = !jc.options.bootstrapClasspath.isEmpty()
        if (hasBootClasspath) {
          args += ['-bootclasspath', jc.options.bootstrapClasspath.getAsPath()]
        }

        final MessageHandler handler = new MessageHandler(true)
        new Main().run(args, handler)
        for (IMessage message : handler.getMessages(null, true)) {
          switch (message.getKind()) {
            case IMessage.ABORT:
            case IMessage.ERROR:
            case IMessage.FAIL:
              log.error message.message, message.thrown
              break
            case IMessage.WARNING:
              log.warn message.message, message.thrown
              break
            case IMessage.INFO:
              log.info message.message, message.thrown
              break
            case IMessage.DEBUG:
              log.debug message.message, message.thrown
              break
          }
        }
      }
    }
  }
}