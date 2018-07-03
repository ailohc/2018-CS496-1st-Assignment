package firstproject.cs496.merge;

import android.net.Uri;

public class Product {
    private Uri imageUri;
    String fileName;

    public Product(Uri imageUri, String fileName) {
        this.imageUri = imageUri;
        this.fileName = fileName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}