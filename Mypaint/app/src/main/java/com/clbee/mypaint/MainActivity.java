package com.clbee.mypaint;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import com.clbee.mypaint.Custompaintview;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Custompaintview custompv;

    private final int GALLERY_CODE=1112;
    public int currentDrawlineColor = Color.WHITE;
    public float currentDrawlineThick = 1f;
    LinearLayout linearLayout;
    Button color_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        custompv = new Custompaintview(this);
        //setContentView(mp);
        setContentView(R.layout.activity_main);

        custompv.setPaintInfo(currentDrawlineColor,currentDrawlineThick);

        init();
    }
    private void init(){
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.addView(custompv);

        color_btn = (Button)findViewById(R.id.color_btn);
        color_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.color_btn: /** Draw line color change button */
                Toast.makeText(this, "color change", Toast.LENGTH_SHORT).show();
                ColorPickerDialogBuilder
                        .with(this)
                        .setTitle(R.string.color_dialog_title)
                        .initialColor(currentDrawlineColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                // Handle on color change
                                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeDrawlineColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_bright))
                        .build()
                        .show();
                break;

        }
    }

    private void changeDrawlineColor(int selectedColor) {
        //currentDrawlineColor = selectedColor;
        custompv.setPaintInfo(selectedColor,currentDrawlineThick);
    }


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == R.id.action_search) {
            Toast.makeText(this, "갤러리 클릭", Toast.LENGTH_SHORT).show();
            selectGallery();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    break;

                default:
                    break;
            }

        }
    }
    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환

        //ivImage = new ImageView(this);
        //ivImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        //linearLayout.addView(ivImage);
        //linearLayout.removeView(mp);


        //ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

        Drawable drawble = new BitmapDrawable(rotate(bitmap, exifDegree));    // 백그라운드로 전체 화면 뿌리기 위해 Bitmap을 drable 로 변환
        custompv.setBackground(drawble);



    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
}
