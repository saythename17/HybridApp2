package com.icandothisallday2020.hybridapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText et;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);
        et=findViewById(R.id.et);
        wv=findViewById(R.id.wv);

        //웹뷰기본 설정들
        wv.setWebViewClient(new WebViewClient());//이전에는 이걸 안쓰면 새창으로 띄워짐
        wv.setWebChromeClient(new WebChromeClient());

        WebSettings settings=wv.getSettings();
        settings.setJavaScriptEnabled(true);//javascript 언어 문서 사용 허용

        //js 에서 Ajax 를 사용한경우
        //http:// 주소 에서만 가능 file://로 되는 곳에서 사용하려면
        settings.setAllowUniversalAccessFromFileURLs(true);

        //javascript 와 연결할 객체 추가
        wv.addJavascriptInterface(new WebViewConnector(),"Connector");//name : js 에서 이 객체를 지칭할 별명

        //웹뷰가 보여줄 웹문서 로딩
        wv.loadUrl("file:///android_asset/index.html");
    }

    //java -> javascript 의 function 사용
    public void send(View view) {
        //
        //WebView 가 보여주는 index.html 안에 있는
        //프로그래머가 작성한 특정 함수--setReceivedMSG() 호출

        //but 자바에서는 index.html 안에 있는 요소 직접 제어 불가
        String msg=et.getText().toString();
        wv.loadUrl("javascript:setReceivedMSG('"+msg+"')");
        et.setText("");
    }

    //javascript-> java method 사용을 위해 별도의 inner class 를 생성하여 javascript 와 연결
    class WebViewConnector{

        //javascript 에서 호출할 메소드 생성
        //※JavascriptInterface annotation 이 지정된 메소드만 javascript 와 통신가능
        @JavascriptInterface
        public void setText(String msg){
            tv.setText(msg);
        }

        //Gallery App 실행 작업 메소드 (javascript 와 연결할)
        @JavascriptInterface
        public void openGallery(){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent);
        }

    }



}//MainActivity class

