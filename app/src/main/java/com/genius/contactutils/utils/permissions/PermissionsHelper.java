package com.genius.contactutils.utils.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

/**
 * Created by geniuS on 7/10/2019.
 */
public class PermissionsHelper {

    private static final String TAG = "PermissionsHelper";
    private static int REQUEST_CODE = 999;

    private Activity activity;
    private Fragment fragment;
    private String[] permissions;
    private PermissionCallback mPermissionCallback;
    private boolean showRational;


    public PermissionsHelper(Activity activity) {
        this.activity = activity;
    }

    public PermissionsHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    public void initPermissions(String[] permissions) {
        this.permissions = permissions;
        checkIfPermissionPresentInAndroidManifest();
    }

    private void checkIfPermissionPresentInAndroidManifest() {
        for (String permission : permissions) {
            if (!hasPermissionInManifest(permission)) {
                throw new RuntimeException("Permission (" + permission + ") not declared in manifest");
            }
        }
    }

    public void requestPermission(PermissionCallback permissionCallback) {
        this.mPermissionCallback = permissionCallback;
        if (!checkPermission(permissions)) {
            showRational = shouldShowRationalDialog(permissions);
            if (activity != null)
                ActivityCompat.requestPermissions(activity, getUnGrantedPermissions(permissions), REQUEST_CODE);
            else
                fragment.requestPermissions(getUnGrantedPermissions(permissions), REQUEST_CODE);
        } else {
            if (mPermissionCallback != null)
                mPermissionCallback.onGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            boolean isDenied = false;
            int i = 0;
            ArrayList<String> grantedPermissions = new ArrayList<String>();
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isDenied = true;
                } else {
                    grantedPermissions.add(permissions[i]);
                }
                i++;
            }

            if (isDenied) {
                boolean currentShowRational = shouldShowRationalDialog(permissions);
                if (!showRational && !currentShowRational) {
                    if (mPermissionCallback != null)
                        mPermissionCallback.onDeniedCompletely();
                } else {
                    //Checking if any single individual permission is granted then show user that permission
                    if (!grantedPermissions.isEmpty()) {
                        if (mPermissionCallback != null)
                            mPermissionCallback.onSinglePermissionGranted(grantedPermissions.toArray(new String[grantedPermissions.size()]));
                    }
                    if (mPermissionCallback != null) {
                        mPermissionCallback.onDenied();
                    }
                }
            } else {
                if (mPermissionCallback != null)
                    mPermissionCallback.onGranted();
            }
        }
    }


    public interface PermissionCallback {
        void onGranted();

        void onSinglePermissionGranted(String[] grantedPermission);

        void onDenied();

        void onDeniedCompletely();
    }


    private <T extends Context> T getContext() {
        if (activity != null)
            return (T) activity;
        return (T) fragment.getContext();
    }

    //Return list that is not granted and we need to ask for permission
    private String[] getUnGrantedPermissions(String[] permissions) {
        List<String> notGrantedPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermission.add(permission);
            }
        }
        return notGrantedPermission.toArray(new String[notGrantedPermission.size()]);
    }

    //Check permission is there or not for group of permissions
    public boolean checkPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //Checking if there is need to show rational for group of permissions
    public boolean shouldShowRationalDialog(String[] permissions) {
        boolean currentShowRational = false;
        for (String permission : permissions) {

            if (activity != null) {
                if (shouldShowRequestPermissionRationale(activity, permission)) {
                    currentShowRational = true;
                    break;
                }
            } else {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
                    currentShowRational = true;
                    break;
                }
            }
        }
        return currentShowRational;
    }

    private boolean hasPermissionInManifest(String permission) {
        try {
            Context context = activity != null ? activity : fragment.getActivity();
            assert context != null;
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void askAndOpenSettings(String toShowMsg, String cancelMsg) {
        if (getContext() == null) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage(toShowMsg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            Toast.makeText(getContext(), "Grant permissions in settings", Toast.LENGTH_LONG).show();
            getContext().startActivity(new Intent()
                    .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .setData(Uri.parse("package:" + getContext().getPackageName()))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            );
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) ->
                Toast.makeText(getContext(), cancelMsg, Toast.LENGTH_LONG).show());
        if (!alertDialog.isShowing()) alertDialog.show();
    }

    public boolean isRTPermissionRequired() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public boolean isPermissionsAvailable() {
        return checkPermission(new String[]{Permissions.READ_CONTACTS,
                Permissions.READ_PHONE_STATE});
    }

    public void initPermissions() {
        initPermissions(new String[]{Permissions.READ_CONTACTS, Permissions.READ_PHONE_STATE});
    }
}
