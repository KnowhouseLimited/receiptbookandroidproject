package com.knowhouse.thereceiptbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageConverter {
    public static Bitmap convertFromStringToImg(String imageString){
        byte[] imageBytes = Base64.decode(imageString,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes,0,
                imageBytes.length);
    }
}
