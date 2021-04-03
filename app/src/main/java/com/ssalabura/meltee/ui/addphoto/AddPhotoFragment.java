package com.ssalabura.meltee.ui.addphoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.ssalabura.meltee.R;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPhotoFragment extends Fragment {
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private View root;
    private ExecutorService cameraExecutor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_photo, container, false);
        if(allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        Button camera_capture_button = root.findViewById(R.id.camera_capture_button);
        camera_capture_button.setOnClickListener(v -> takePhoto());
        cameraExecutor = Executors.newSingleThreadExecutor();
        return root;
    }

    private void takePhoto() {
        System.out.println("Took photo!");
    }

    private void startCamera() {
        PreviewView previewView = root.findViewById(R.id.viewFinder);
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(previewView.getSurfaceProvider());

            try {
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview);
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }
}