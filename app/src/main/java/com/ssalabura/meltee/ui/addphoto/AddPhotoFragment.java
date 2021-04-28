package com.ssalabura.meltee.ui.addphoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.ssalabura.meltee.MainActivity;
import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.database.PhotoCard;
import com.ssalabura.meltee.database.RealmPhotoCard;
import com.ssalabura.meltee.util.BitmapTools;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddPhotoFragment extends Fragment
        implements AdditionalInfoDialogFragment.AdditionalInfoDialogListener, ReceiversDialogFragment.ReceiversDialogListener {
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private AddPhotoViewHolder holder;

    private CameraSelector cameraSelector;
    private ImageCapture imageCapture;
    private PhotoCard photoCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_photo, container, false);
        holder = new AddPhotoViewHolder(root);
        holder.changeState(AddPhotoViewHolder.State.PHOTO_NOT_TAKEN);

        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                    getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        startCamera();

        holder.button_flip_camera.setOnClickListener(v -> {
            if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
            startCamera();
        });

        holder.button_take_photo.setOnClickListener(v -> takePhoto());
        holder.button_back.setOnClickListener(v -> onClickButtonBack());
        holder.button_additional_info.setOnClickListener(v -> {
            DialogFragment dialogFragment = new AdditionalInfoDialogFragment();
            dialogFragment.setTargetFragment(this, 22);
            Bundle bundle = new Bundle();
            bundle.putString("message", holder.card_preview_holder.message.getText().toString());
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getParentFragmentManager(), "AdditionalInfoDialog");
        });
        holder.button_send.setOnClickListener(v -> {
            DialogFragment dialogFragment = new ReceiversDialogFragment();
            dialogFragment.setTargetFragment(this, 22);
            dialogFragment.show(getParentFragmentManager(), "ReceiversDialog");
        });

        photoCard = new PhotoCard();
        photoCard.sender = MainActivity.username;
        holder.card_preview_holder.sender.setText(MainActivity.username);
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

            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, imageCapture);
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
                photoCard.timestamp = System.currentTimeMillis();
                holder.card_preview_holder.timestamp.setText(
                        new SimpleDateFormat("KK:mm aa", Locale.ENGLISH).format(photoCard.timestamp));
                Bitmap bitmap = BitmapTools.fromImageProxy(image, cameraSelector);
                photoCard.bitmap = bitmap;
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                holder.card_preview_holder.photo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, width*4/3, true));
                holder.changeState(AddPhotoViewHolder.State.PHOTO_TAKEN);
                image.close();
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
    }

    private void sendPhoto() {
        photoCard.photo = BitmapTools.toByteArray(photoCard.bitmap);

        MelteeRealm.insertPhoto(new RealmPhotoCard(photoCard));

        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), "Photo successfully sent.", Toast.LENGTH_SHORT).show();
            System.out.println("Photo successfully sent.");
            ((BottomNavigationView)getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_dashboard);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            cameraProvider.unbindAll();
        }, ContextCompat.getMainExecutor(getContext()));
    }

    @Override
    public void onDialogPositiveClick(String message) {
        photoCard.message = message;
        holder.card_preview_holder.message.setText(message);
    }

    @Override
    public void onDialogPositiveClick(List<String> receivers) {
        photoCard.receiver = receivers.get(0);
        new Thread(this::sendPhoto).start();
    }
}