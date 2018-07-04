package firstproject.cs496.merge;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;





public class CustomSwipeAdapter extends PagerAdapter {

    private List<Product> image_resources;
    private Context ctx;
    private LayoutInflater layoutInflater;
    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private TextView textResult;
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    public CustomSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }

    public List<Product> getProductList(){
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = ctx.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        image_resources = new ArrayList<>();
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            Toast.makeText(ctx,"No image", Toast.LENGTH_LONG);
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
                image_resources.add(product);
            }
            while(imageCursor.moveToNext());
        }

        else {
            Toast.makeText(ctx,"Image cursor is empty", Toast.LENGTH_LONG);
            // imageCursor가 비었습니다.
        }

        imageCursor.close();
        return image_resources;

    }

    @Override
    public int getCount() {
        getProductList();
        return image_resources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);

        TouchImageView imageView = (TouchImageView) item_view.findViewById(R.id.image_view);

        TextView textView = (TextView) item_view.findViewById(R.id.image_count);

        Product product =image_resources.get(position);

        Uri imageUri = product.getImageUri();
        Bitmap image = BitmapFactory.decodeFile(imageUri.getPath());
        imageView.setImageBitmap(image);
  //      imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(image);

        Bitmap classifyimage = Bitmap.createScaledBitmap(image, INPUT_SIZE, INPUT_SIZE, false);
        initTensorFlowAndLoadModel();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Classifier.Recognition> results;

        results = classifier.recognizeImage(classifyimage);

        textView.setText(results.toString());

        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            ctx.getAssets(),
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
