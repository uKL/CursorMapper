/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cursor.mapper.log;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

/**
 * Helper methods that make logging more consistent throughout the app. Levels
 * ERROR and higher are sent to BugSense automatically if an application is
 * compiled in production mode.
 */
public final class LogUtils {

    public static final String DEFAULT_TAG = "detaultLogTag";
    private static final String WTF_TAG = "WTF: ";
    static final int MAX_LOG_TAG_LENGTH = 23;
    private static boolean LOG_ENABLED = true;

    public static String makeLogTag(String str) {
        if (str == null || str.length() == 0) {
            return DEFAULT_TAG;
        }

        if (str.length() >= MAX_LOG_TAG_LENGTH) {
            return str.substring(0, MAX_LOG_TAG_LENGTH);
        }

        return str;
    }

    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    @SuppressWarnings("rawtypes")
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) { // NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.d(tag, message, cause);
        }
    }

    public static boolean isLogEnabled() {
        return LOG_ENABLED;
    }

    public static void LOGV(final String tag, String message) {// NOSONAR

        if (LOG_ENABLED && message != null && tag != null) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {// NOSONAR

        if (LOG_ENABLED && message != null && tag != null) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.i(tag, message);
        }
    }

    public static void LOGI(final String tag, String message, Throwable cause) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.i(tag, message, cause);
        }
    }

    public static void LOGW(final String tag, Throwable throwable) {// NOSONAR
        if (LOG_ENABLED) {
            Log.w(tag, throwable);
        }
    }

    public static void LOGW(final String tag, String message) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.w(tag, message);
        }
    }

    public static void LOGW(final String tag, String message, Throwable cause) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.w(tag, message, cause);
        }
    }

    public static void LOGE(final String tag, String message) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {
            Log.e(tag, message);
        }
    }

    public static void LOGE(final String tag, String message, Throwable cause) {// NOSONAR
        if (LOG_ENABLED && message != null && tag != null) {

            Log.e(tag, message, cause);
        }
    }

    @SuppressLint("NewApi")
    public static void LOGWTF(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (LOG_ENABLED && message != null && tag != null) {
            if (hasFroyo()) {
                Log.wtf(tag, message);
            } else {
                Log.w(tag, WTF_TAG + message);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void LOGWTF(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (LOG_ENABLED) {
            if (hasFroyo()) {
                Log.wtf(tag, message);
            } else {
                Log.w(tag, WTF_TAG + message);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void LOGWTF(final String tag, Throwable throwable) {// NOSONAR
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (LOG_ENABLED) {
            if (hasFroyo()) {
                Log.wtf(tag, throwable);
            } else {
                Log.w(tag, throwable);
            }
        }
    }

    private LogUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
}
