package com.kunlun.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kunlun.R;
import com.kunlun.douyindemo.com.activity.DouyinRecordActivity;
import com.kunlun.permission.PermissionUtils;

import java.util.*;


public class MainActivity extends BaseActivity {

    private Button multipleBtn;
    private Button btn_base_gldemo;
    private Button btn_cameraFilter;
    private Button btn_douyin;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;


        multipleBtn = findViewById(R.id.btn_multiply_video);
        btn_base_gldemo = findViewById(R.id.btn_base_gldemo);
        btn_cameraFilter = findViewById(R.id.btn_cameragl);
        btn_douyin =  findViewById(R.id.btn_douyin);

        btn_base_gldemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentBaseGlDemo = new Intent(mContext,BaseGLDemoActivity.class);
                startActivity(intentBaseGlDemo);
            }
        });

        btn_cameraFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,CameraOpenglActivity.class);
                startActivity(intent);
            }
        });

        multipleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(mContext,MultipleVideoActivity.class);
               startActivity(intent);
            }
        });

        btn_douyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DouyinRecordActivity.class);
                startActivity(intent);
            }
        });




        ArrayList<String> listArr = new ArrayList<String>();
        listArr.add(Manifest.permission.CAMERA);
        listArr.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listArr.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        listArr.add(Manifest.permission.RECORD_AUDIO);


        String[] listS = new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
        //listS = listArr.toArray(new String[listArr.size()]);
        PermissionUtils.checkPermissions2(listS);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
