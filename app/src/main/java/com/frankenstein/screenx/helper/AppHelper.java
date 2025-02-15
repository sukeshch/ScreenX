package com.frankenstein.screenx.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.frankenstein.screenx.Constants;
import com.frankenstein.screenx.models.Screenshot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppHelper {
    private static final Map<String, String> _packageToAppName = new HashMap<>();

    private static String getAppName(PackageManager _pm, String packageId) {
        if (!_packageToAppName.containsKey(packageId)) {
            ApplicationInfo ai;
            try {
                ai = _pm.getApplicationInfo( packageId, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            final String appName = (String) (ai != null ? _pm.getApplicationLabel(ai) : "");
            _packageToAppName.put(packageId, appName);
        }
        return _packageToAppName.get(packageId);
    }


    private static String getSourceApp(Context context, String filename) {
        final PackageManager _pm = context.getPackageManager();
        Pattern pattern = Pattern.compile(Constants.SCREENSHOT_PATTERN);
        Matcher matcher = pattern.matcher(filename);

        if (matcher.find()) {
            String matched = matcher.group();
            String packageId = matched.substring(1, matched.length() - 4);
            String appName = getAppName(_pm, packageId);
            appName = (appName == "") ? "Miscellaneous" : appName;
            return appName;
        }
        return "Miscellaneous";
    }


    public static Screenshot getScreenFromFile(Context context, File file) {
        String fileName = file.getName();
        String appName = getSourceApp(context, fileName);
        Screenshot screen = new Screenshot(fileName, file.getAbsolutePath(), appName);
        return screen;
    }
}
