package com.ssalabura.meltee.ui.addphoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.ssalabura.meltee.MainActivity;
import com.ssalabura.meltee.R;
import com.ssalabura.meltee.ui.database.AppDatabase;
import com.ssalabura.meltee.ui.database.PhotoCard;
import com.ssalabura.meltee.ui.database.PhotoCardDao;
import com.ssalabura.meltee.util.BitmapTools;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPhotoFragment extends Fragment {
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private View root;
    private AddPhotoViewHolder holder;
    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_photo, container, false);
        holder = new AddPhotoViewHolder(root);
        holder.changeState(AddPhotoViewHolder.State.PHOTO_NOT_TAKEN);

        holder.button_back.setOnClickListener(v -> onClickButtonBack());
        holder.button_take_photo.setOnClickListener(v -> takePhoto());
        holder.button_send.setOnClickListener(v -> new Thread(this::onClickButtonSend).start());

        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                    getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        startCamera();

        cameraExecutor = Executors.newSingleThreadExecutor();
        return root;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(holder.previewView.getSurfaceProvider());

            imageCapture = new ImageCapture.Builder()
                    .setTargetResolution(new Size(1080,1440))
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build();

            CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

            try {
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, imageCapture);
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private boolean allPermissionsGranted() {
        return ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void takePhoto() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                System.out.println("Photo capture succeeded.");
                holder.setImageBitmap(BitmapTools.fromImageProxy(image));
                image.close();
                holder.changeState(AddPhotoViewHolder.State.PHOTO_TAKEN);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(getContext(), "Failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Photo capture failed: " + exception.getMessage());
            }
        });
    }

    private void onClickButtonBack() {
        holder.changeState(AddPhotoViewHolder.State.PHOTO_NOT_TAKEN);
        holder.imageBitmap.recycle();
    }

    private void onClickButtonSend() {
        //TODO: send to online database
        String text = "Photo taken: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH).format(System.currentTimeMillis());

        AppDatabase db = AppDatabase.getInstance(getContext());
        PhotoCardDao photoCardDao = db.photoCardDao();
        photoCardDao.insert(new PhotoCard(MainActivity.username, BitmapTools.toByteArray(holder.imageBitmap), text));
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), "Photo successfully saved.", Toast.LENGTH_SHORT).show();
            System.out.println("Photo successfully saved.");
            ((BottomNavigationView)getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_dashboard);
        });
        holder.imageBitmap.recycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }
}