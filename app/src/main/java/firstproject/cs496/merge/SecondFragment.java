package firstproject.cs496.merge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ViewStub stubGrid;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private List<Product> productList;
    private ImageView imgPicture;
    Button button;


    public List<Product> getProductList(){

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        productList = new ArrayList<>();
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            Toast.makeText(getActivity(),"No image", Toast.LENGTH_LONG);
        }
        else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                Uri imageUri = Uri.parse(filePath);

                File file = new File(filePath);
                String fileName = file.getName();
                int fileIdx = fileName.lastIndexOf(".");
                String newFileName = fileName.substring(0, fileIdx);
                Product product = new Product(imageUri, newFileName);
                productList.add(product);
            }
            while(imageCursor.moveToNext());
        }

        else {
            Toast.makeText(getActivity(),"Image cursor is empty", Toast.LENGTH_LONG);
            // imageCursor가 비었습니다.
        }

        imageCursor.close();

        return productList;

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("hi5", "124344");
        final View rootview = inflater.inflate(R.layout.fragment_second, container, false);

        imgPicture = (ImageView) rootview.findViewById(R.id.imgPicture);

                stubGrid = (ViewStub) rootview.findViewById(R.id.stub_grid);
                stubGrid.inflate();
                gridView = (GridView) rootview.findViewById(R.id.mygridview);
                getProductList();

                gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, productList);
                gridView.setAdapter(gridViewAdapter);
                stubGrid.setVisibility(View.VISIBLE);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent i = new Intent(getActivity(), FullImageActivity.class);
                        i.putExtra("id", position);
                        startActivity(i);
                    }
                });



        return rootview;
    }

}


