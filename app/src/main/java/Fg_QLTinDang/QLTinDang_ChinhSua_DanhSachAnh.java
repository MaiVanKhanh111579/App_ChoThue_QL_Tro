package Fg_QLTinDang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Adapter.TinDang_DanhSachAnh;
import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.ENTITY.DanhSachAnh;

public class QLTinDang_ChinhSua_DanhSachAnh extends Fragment {
    private View view;
    private RecyclerView recyclerViewAnh;
    private Button btnLuu, btnQuayLai, btnThemAnh;
    private AppDatabase db;
    private List<String> imageUrls = new ArrayList<>();
    private TinDang_DanhSachAnh adapter;
    private int ma_TinDang;
    private static final int REQUEST_PERMISSION_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_qltindang_chinhsua_danhsachanh, container, false);
        anhXaView();

        // Lấy ma_TinDang từ Bundle
        if (getArguments() != null) {
            ma_TinDang = getArguments().getInt("ma_TinDang");
        }
        if (ma_TinDang == 0) {
            Toast.makeText(requireContext(), "Không tìm thấy mã tin đăng", Toast.LENGTH_SHORT).show();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return view;
        }

        // Thiết lập RecyclerView
        adapter = new TinDang_DanhSachAnh(imageUrls, position -> {
            // Xóa ảnh khỏi danh sách và cơ sở dữ liệu
            String imagePath = imageUrls.get(position);
            imageUrls.remove(position);
            adapter.notifyItemRemoved(position);
            new Thread(() -> {
                DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                danhSachAnhDao.deleteByImageUrl(imagePath); // Xóa ảnh từ cơ sở dữ liệu
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Đã xóa ảnh", Toast.LENGTH_SHORT).show());
            }).start();
        });
        recyclerViewAnh.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewAnh.addItemDecoration(new ItemSpacingDecoration(15)); // Thêm khoảng cách 16dp giữa các item
        recyclerViewAnh.setAdapter(adapter);

        // Tải danh sách ảnh hiện có
        loadExistingImages();

        // Xử lý nút Thêm ảnh
        btnThemAnh.setOnClickListener(v -> {
            if (imageUrls.size() >= 10) {
                Toast.makeText(requireContext(), "Bạn chỉ có thể thêm tối đa 10 ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
                } else {
                    launchImagePicker();
                }
            } else {
                launchImagePicker();
            }
        });

        // Xử lý nút Lưu
        btnLuu.setOnClickListener(v -> saveImages());

        // Xử lý nút Quay lại
        btnQuayLai.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        return view;
    }

    private void anhXaView() {
        recyclerViewAnh = view.findViewById(R.id.recycleview_qltindang_chinhsua_danhsachanh);
        btnLuu = view.findViewById(R.id.btn_luu);
        btnQuayLai = view.findViewById(R.id.btn_thoat);
        btnThemAnh = view.findViewById(R.id.btn_themanh);
        db = AppDatabase.getInstance(requireContext());
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

    private void saveImages() {
        if (imageUrls.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng thêm ít nhất một ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                // Xóa danh sách ảnh cũ
                danhSachAnhDao.deleteDanhSachAnh_by_TinDangId(ma_TinDang);
                // Lưu danh sách ảnh mới
                for (String imagePath : imageUrls) {
                    DanhSachAnh image = new DanhSachAnh(ma_TinDang, imagePath);
                    danhSachAnhDao.insertDanhSachAnh(image);
                }
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Cập nhật danh sách ảnh thành công", Toast.LENGTH_SHORT).show();
//                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Lỗi khi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        String filePath = copyImageToAppStorage(imageUri);
                        if (filePath != null) {
                            imageUrls.add(filePath);
                            adapter.notifyItemInserted(imageUrls.size() - 1);
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
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                Toast.makeText(requireContext(), "Cần quyền truy cập để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lớp ItemSpacingDecoration để thêm khoảng cách giữa các item
    private static class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        public ItemSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.top = spacing;
            outRect.bottom = spacing;
        }
    }
}