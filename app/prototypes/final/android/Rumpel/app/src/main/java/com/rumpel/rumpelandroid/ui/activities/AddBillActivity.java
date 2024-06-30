package com.rumpel.rumpelandroid.ui.activities;

import static com.rumpel.rumpelandroid.utils.Popups.toast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.common.InputImage;
import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.db.DAOBill;
import com.rumpel.rumpelandroid.db.DAOTags;
import com.rumpel.rumpelandroid.ui.screens.ComposeContent;
import com.rumpel.rumpelandroid.utils.textanalysis.TextAnalysis;
import com.szaumoor.rumple.model.entities.Bill;

import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import java.io.IOException;

import kotlin.Unit;

/**
 * Activity for the AddBill screen. Has camera recognition capabilities, but only partially implemented and not truly functional.
 */
public class AddBillActivity extends AppCompatActivity {
    private static final int CAMERA_START_CODE = 1;
    private static final int SELECT_IMAGE_CODE = 2;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DAOBill dao = new DAOBill(this);
        var intent = getIntent();
        var extras = intent.getExtras();
        Bill bill = null;
        if (extras != null) {
            var idStr = extras.getString("BILL_ID");
            if (idStr == null) {
                Log.e(getClass().getSimpleName(), "Failed to get intent extra String");
                finish();
            } else {
                var id = new BsonObjectId(new ObjectId(idStr));
                var op = dao.getById(id);
                if (op.isEmpty()) {
                    Log.e(getClass().getSimpleName(), "Failed to get the bill from db");
                    finish();
                } else {
                    bill = op.get();
                }
            }
        } else Log.e(getClass().getSimpleName(), "Failed to get intent data, but maybe none was sent?");
        ComposeContent.INSTANCE.setContentFromJavaActivity(this, this::takePicture,
                this::selectImage, new DAOTags(this).getAll(), bill);
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    private int getRotationCompensation(boolean isFrontFacing)
            throws CameraAccessException {
        int deviceRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        CameraManager cameraManager = (CameraManager) this.getSystemService(CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        System.out.println(cameraId);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (isFrontFacing) rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
        else rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
        return rotationCompensation;
    }


    /** @noinspection SameReturnValue*/
    private Unit takePicture() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            var startCamCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(startCamCapture, CAMERA_START_CODE);
        } else requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_START_CODE);
        return Unit.INSTANCE;
    }

    /** @noinspection SameReturnValue*/
    private Unit selectImage() {
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, ""), SELECT_IMAGE_CODE);
        } else requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, SELECT_IMAGE_CODE);
        return Unit.INSTANCE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_START_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    var bitmap =  data.getExtras().getParcelable("data", Bitmap.class);
                    if (bitmap != null) {
                        try {
                            TextAnalysis.extractText(InputImage.fromBitmap(bitmap, getRotationCompensation(false)),
                                    (text) -> {
                                        System.out.println("Success, here's the extracted text");
                                        System.out.println(text.getText());
                                    }, () -> System.out.println("Nothing was found!"));
                        } catch (CameraAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else if (requestCode == SELECT_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    var uri = data.getData();
                    if (uri != null) {
                        try {
                            TextAnalysis.extractText(InputImage.fromFilePath(this, uri),
                                    (text) -> {
                                        System.out.println("Success, here's the extracted text");
                                        System.out.println(text.getText());
                                    }, () -> System.out.println("Nothing was found!"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_START_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture();
            else toast(this, R.string.permission_cam_required);
        } else if (requestCode == SELECT_IMAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) selectImage();
            else toast(this, R.string.permission_storage_required);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityIfNeeded(new Intent(this, HomeActivity.class), 1);
        finish();
    }
}