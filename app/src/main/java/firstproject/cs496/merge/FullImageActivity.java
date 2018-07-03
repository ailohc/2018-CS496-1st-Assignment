package firstproject.cs496.merge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FullImageActivity extends Activity {
    private List<Product> productList;

    public List<Product> getProductList(){

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        productList = new ArrayList<>();
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            Toast.makeText(this,"No image", Toast.LENGTH_LONG);
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
            Toast.makeText(this,"Image cursor is empty", Toast.LENGTH_LONG);
            // imageCursor가 비었습니다.
        }

        imageCursor.close();
        return productList;

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        getProductList();
        GridViewAdapter imageAdapter = new GridViewAdapter(this,R.layout.grid_item, productList);

        Product product = productList.get(position);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);

        Uri imageUri = product.getImageUri();

        Bitmap image = BitmapFactory.decodeFile(imageUri.getPath());

        imageView.setImageBitmap(image);


        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


    }

}
