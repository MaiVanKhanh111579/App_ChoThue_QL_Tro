package Fg_QLTinDang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Adapter.TinDang_DanhSachAnh;
import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.ENTITY.DanhSachAnh;

public class DangTinMoiFg_2Fg extends Fragment {
    private View view;
    private AppDatabase db;
    private RecyclerView ListAnh;
    private BottomNavigationView bottomNav;
    private Button btn_TaiAnh, btnTiepTuc, btnQuayLai;
    private TextView txtThoat;
    private int ma_TinDang;
    private List<String> imageUrls = new ArrayList<>();
    private TinDang_DanhSachAnh adapter;
    private static final int REQUEST_PERMISSION_CODE = 100;

    @SuppressLint("InlinedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dangtinmoi_2, container, false);
        anhXaView();

        // Lấy ma_TinDang từ Bundle
        if (getArguments() != null) {
            ma_TinDang = getArguments().getInt("ma_TinDang");
        }

        // Kiểm tra ma_TinDang
        if (ma_TinDang == 0) {
            Toast.makeText(requireContext(), "Không tìm thấy mã tin đăng", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return view;
        }

        // Thiết lập RecyclerView
        adapter = new TinDang_DanhSachAnh(imageUrls, position -> {
            // Xóa tệp ảnh khi xóa khỏi danh sách
            File file = new File(imageUrls.get(position));
            if (file.exists()) {
                file.delete();
            }
            imageUrls.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(requireContext(), "Đã xóa ảnh", Toast.LENGTH_SHORT).show();
        });
        ListAnh.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        ListAnh.setAdapter(adapter);

        // Tải ảnh đã lưu (nếu có)
        loadExistingImages();

        // Xử lý chọn ảnh
        btn_TaiAnh.setOnClickListener(v -> {
            if (imageUrls.size() >= 10) {
                Toast.makeText(requireContext(), "Bạn chỉ có thể thêm tối đa 10 ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    imagePickerLauncher.launch(intent);
                }
            } else {
                // Xử lý cho Android 12 trở xuống (nếu cần)
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imagePickerLauncher.launch(intent);
            }
        });

        // Lưu ảnh
        btnTiepTuc.setOnClickListener(v -> saveImages());

        // Quay lại
        btnQuayLai.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        // Thoát
        txtThoat.setOnClickListener(v -> Navigation.findNavController(view).popBackStack(R.id.navigation_qltindang, false));

        return view;
    }

    private void anhXaView() {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
        txtThoat = view.findViewById(R.id.txt_dangtinmoi_2_thoat);
        btn_TaiAnh = view.findViewById(R.id.btn_dangtinmoi_2_themanh);
        btnTiepTuc = view.findViewById(R.id.btn_dangtinmoi_2_tieptuc);
        btnQuayLai = view.findViewById(R.id.btn_dangtinmoi_quaylai);
        ListAnh = view.findViewById(R.id.recycleview_dangtinmoi_2_hinhanh);
        db = AppDatabase.getInstance(requireContext());
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    if (result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String filePath = copyImageToAppStorage(imageUri);
                        if (filePath != null) {
                            imageUrls.add(filePath);
                            adapter.notifyItemInserted(imageUrls.size() - 1);
                            Log.d("DangTinMoiFg_2Fg", "Saved image path: " + filePath);
                            Toast.makeText(requireContext(), "Đã thêm ảnh", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Không thể thêm ảnh", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Không có ảnh được chọn", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private String copyImageToAppStorage(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Log.e("DangTinMoiFg_2Fg", "Failed to open InputStream for URI: " + imageUri);
                return null;
            }
            File outputDir = requireContext().getFilesDir();
            File outputFile = new File(outputDir, "image_" + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            inputStream.close();
            return outputFile.getAbsolutePath();
        } catch (SecurityException e) {
            Log.e("DangTinMoiFg_2Fg", "SecurityException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e("DangTinMoiFg_2Fg", "Error copying image: " + e.getMessage());
            return null;
        }
    }

    private void saveImages() {
        if (imageUrls.size() < 4) {
            Toast.makeText(requireContext(), "Vui lòng thêm ít nhất 4 ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                danhSachAnhDao.deleteDanhSachAnh_by_TinDangId(ma_TinDang); // Xóa ảnh cũ
                for (String imagePath : imageUrls) {
                    DanhSachAnh image = new DanhSachAnh(ma_TinDang, imagePath);
                    danhSachAnhDao.insertDanhSachAnh(image);
                }
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Lưu ảnh thành công", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ma_TinDang", ma_TinDang);
                    Navigation.findNavController(view).navigate(R.id.navigation_dangtinmoi_3, bundle);
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Lỗi khi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadExistingImages() {
        new Thread(() -> {
            DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
            List<DanhSachAnh> images = danhSachAnhDao.getImagesByPostId(ma_TinDang);
            imageUrls.clear();
            for (DanhSachAnh image : images) {
                imageUrls.add(image.getImageUrl());
            }
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            } else {
                Toast.makeText(requireContext(), "Cần quyền truy cập để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
}