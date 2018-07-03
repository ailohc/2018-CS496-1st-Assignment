package firstproject.cs496.merge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FullImageActivity extends Activity {
    private List<Product> productList;
    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private TextView textResult;
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();


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
        Log.d("hiiiiiii", "123244222");
        Bitmap classifyimage = Bitmap.createScaledBitmap(image, INPUT_SIZE, INPUT_SIZE, false);
        Log.d("hiiiiiii1", "123244222");
        textResult = findViewById(R.id.textResult);
        Log.d("hiiiiiiii2", "123244222");
        initTensorFlowAndLoadModel();
        Log.d("hiiiiiiii3", "123244222");
        List<Classifier.Recognition> results = classifier.recognizeImage(classifyimage);
        Log.d("hiiiiiiii4", "123244222");
        textResult.setText(results.toString());

        Log.d("hiiiiiiii5", "123244222");
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Log.d("hiiiiiiii6", "123244222");

    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }


}

