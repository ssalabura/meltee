package com.ssalabura.meltee.ui.addphoto;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.ssalabura.meltee.util.BitmapTools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LOCATION_SERVICE;

public class AddPhotoFragment extends Fragment
        implements AdditionalInfoDialogFragment.AdditionalInfoDialogListener, ReceiversDialogFragment.ReceiversDialogListener {
    private AddPhotoViewHolder holder;

    private CameraSelector cameraSelector;
    private ImageCapture imageCapture;
    private PhotoCard photoCard;
    private ArrayList<String> locationList = new ArrayList<>();

    private final Size resolution = new Size(600,800); // for faster queries, should be higher

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_photo, container, false);
        holder = new AddPhotoViewHolder(root);
        holder.changeState(AddPhotoViewHolder.State.PHOTO_NOT_TAKEN);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},0);
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        startCamera();
        updateLocationList();

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
            updateLocationList();
            DialogFragment dialogFragment = new AdditionalInfoDialogFragment();
            dialogFragment.setTargetFragment(this, 22);
            Bundle bundle = new Bundle();
            bundle.putString("message", holder.card_preview_holder.message.getText().toString());
            bundle.putStringArrayList("location_list", locationList);
            bundle.putString("location", holder.card_preview_holder.location.getText().toString());
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
        holder.card_preview_holder.profilePicture.setImageBitmap(MelteeRealm.getProfilePicture());
        holder.card_preview_holder.message.setVisibility(View.GONE);
        holder.card_preview_holder.location.setVisibility(View.GONE);
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
                    .setTargetResolution(resolution)
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build();

            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, imageCapture);
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void updateLocationList() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(location == null) {
                Log.e("Meltee", "location unavailable.");
                return;
            }
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                locationList.clear();
                locationList.add(getString(R.string.hint_empty));
                for(Address address : addressList) {
                    locationList.add(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void takePhoto() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Log.i("Meltee", "Photo capture succeeded.");
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
                Toast.makeText(getContext(), getString(R.string.error) + " " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Meltee", "Photo capture failed.", exception);
            }
        });
    }

    private void onClickButtonBack() {
        holder.changeState(AddPhotoViewHolder.State.PHOTO_NOT_TAKEN);
    }

    private void sendPhoto() {
        Activity activity = getActivity();
        activity.runOnUiThread(() -> {
            ((BottomNavigationView)activity.findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_dashboard);
        });

        photoCard.photo = BitmapTools.toByteArray(photoCard.bitmap);
        MelteeRealm.insertPhoto(photoCard);

        activity.runOnUiThread(() -> {
            Toast.makeText(activity, activity.getString(R.string.photo_success), Toast.LENGTH_SHORT).show();
            Log.i("Meltee", "Photo successfully sent.");
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
    public void onDialogPositiveClick(String message, String location) {
        photoCard.message = message;
        photoCard.location = location;
        holder.card_preview_holder.message.setText(message);
        if(message == null || message.isEmpty()) {
            holder.card_preview_holder.message.setVisibility(View.GONE);
        } else {
            holder.card_preview_holder.message.setVisibility(View.VISIBLE);
        }
        holder.card_preview_holder.location.setText(location);
        if(location == null || location.isEmpty()) {
            holder.card_preview_holder.location.setVisibility(View.GONE);
        } else {
            holder.card_preview_holder.location.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDialogPositiveClick(List<String> receivers) {
        if(receivers.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.no_receivers), Toast.LENGTH_SHORT).show();
        } else {
            photoCard.receivers = receivers;
            new Thread(this::sendPhoto).start();
        }
    }
}