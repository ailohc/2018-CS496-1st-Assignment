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

public class CustomSwipeAdapter extends PagerAdapter {
  //  private List<Product> productList;
 //   private int[] image_resources = {R.drawable.china_flag, R.drawable.south_korea, R.drawable.japan_flag};
    private List<Product> image_resources;
    private Context ctx;
    private LayoutInflater layoutInflater;

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
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);
        TextView textView = (TextView) item_view.findViewById(R.id.image_count);

        Product product =image_resources.get(position);

        Uri imageUri = product.getImageUri();
        Bitmap image = BitmapFactory.decodeFile(imageUri.getPath());
        imageView.setImageBitmap(image);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //textView.setText("Image : " +position);
        textView.setText("hello world");
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
