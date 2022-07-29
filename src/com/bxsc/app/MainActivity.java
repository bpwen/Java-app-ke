package com.bxsc.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends Activity {
	private EmptyLayout mErrorLayout;
	private ArrayAdapter<String> adapter;
	private String[] data ;
	
	private static final String ACTIVITY_TAG="LogDemo";
	private WebView webView;
	private FrameLayout layout;
	private boolean scbool = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
        //ȥ��Activity�����״̬��
		WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init(){
		layout = (FrameLayout)findViewById(R.id.FrameLayout1);
		
		webView = (WebView)findViewById(R.id.webView);
		webView.setVisibility(View.GONE);
		
		mErrorLayout = (EmptyLayout) findViewById(R.id.empty_view);
		mErrorLayout.setVisibility(View.GONE);
       
		// �õ��������ԵĶ���
		WebSettings settings = webView.getSettings();
		// ʹ��JavaScript
		settings.setJavaScriptEnabled(true);
		// ֧�����ģ�����ҳ����������ʾ����
		settings.setDefaultTextEncodingName("GBK");
		
		//---��λ-------------------------------------------------------------------------
		webView.setWebChromeClient(new WebChromeClient(){
		    @Override
		    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		        callback.invoke(origin, true, true);
		        super.onGeolocationPermissionsShowPrompt(origin, callback);
		    }
		});
		//----------------------------------------------------------------------------
		
//		webView.setWebChromeClient(new WebChromeClient()
//        {
//			//�ж�ҳ����ع���
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//            	
//            	 // ��ҳ�������
//                if (newProgress == 100) {
//                   // �״μ��ؽ�������
//                	if(scbool){
//                		//�ӳ�0.7���ִ��run�����е�ҳ����ת  
//                        new Handler().postDelayed(new Runnable() {            
//                            @Override
//                            public void run() {
//                            	//��ʾ״̬��
//                                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                                lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                                getWindow().setAttributes(lp);
//                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//                                
//                                if(EmptyLayout.isConnectivity(MainActivity.this)){//������������
//                                	layout.setVisibility(View.GONE);//��һ�伴���ز���LinearLayout����
//                                	webView.setVisibility(View.VISIBLE);//��һ�伴��ʾ����LinearLayout����
//                                	scbool = false;
//                                }else{
//                                	
//                                }
//                            	
//                            	//initView();
//                            	//virtulHttp();
//                            }  
//                        }, 2000);
//                	}
//                } else {
//                    // ������
//                	//webView.setVisibility(View.GONE);//��һ�伴���ز���LinearLayout����
//                	
//                	//layout.setVisibility(View.VISIBLE);//��һ�伴��ʾ����LinearLayout����
//                }
//            }
//        });
		
		//����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//��������ֻ�������ת�����Ž���
				if (url.startsWith("tel:")) { 
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url)); 
                    startActivity(intent); 
                    return true;
				}
				//����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
				view.loadUrl(url);
				return true;
			}
			
			// ������
			@Override  
		    public void onPageStarted(WebView view, String url, Bitmap favicon) {  
		        super.onPageStarted(view, url, favicon);  
		        //����������
		        if(!EmptyLayout.isConnectivity(MainActivity.this)){
		        	mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		        	// ��������Ϣ��ͼƬ��ӵ����ť
		        	mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
		    			@Override
		    			public void onClick(View v) {
		    			    virtulHttp();
		    			}
		    		});
		        	Toast.makeText(getApplicationContext(), "�������ź�",  Toast.LENGTH_SHORT).show();
		        }
		    }  
			
			// �������
		    @Override  
		    public void onPageFinished(WebView view, String url) {  
		        super.onPageFinished(view, url);
		        // �״δ���ִ��
		        if(scbool){
		        	//��ʾ״̬��
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getWindow().setAttributes(lp);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		        	
			        layout.setVisibility(View.GONE);//��һ�伴���ز���LinearLayout����
			        webView.setVisibility(View.VISIBLE);//��һ�伴��ʾ����LinearLayout����
			        scbool = false;
		        }
		    }
		});
		
		// ������ֹ�������
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
		
		//WebView����web��Դ
		webView.loadUrl("http://api.gjtzw.net");
    }
	

	
	/*---------------------------------------------------------------------
	* ���ƣ��ֻ������ؽ�����
	* @param  {[type]} a [description]
	* @return {[type]}   [description]
	* ���ڣ�2017-01-10 12:03:59
	* ���ܣ�
	* 		������ص���ҳ���ж��Ƿ���Ҫ�˳����������˳�����
	*---------------------------------------------------------------------*/
	private long clickTime=0;
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//������һҳ��
                return true;
            }
            else
            {
            	exit();
            	return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {  
        if ((System.currentTimeMillis() - clickTime) > 2000) {  
            Toast.makeText(getApplicationContext(), "�ڰ�һ���˳�����",  Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();  
            Log.e(MainActivity.ACTIVITY_TAG, "exit ������˳�����");  
        } else {  
            Log.e(MainActivity.ACTIVITY_TAG, "exit �ر��˳���");  
            this.finish();
            System.exit(0);//�˳�����
        }  
    }

	//------------------------------------

	private void initView() {
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    virtulHttp();
			}
		});
	}

	//ģ��������������
	private  void virtulHttp(){
		mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					if(EmptyLayout.isConnectivity(MainActivity.this)){//��û����
						data = new String[]{"��������"};
						adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1,data );
					}else{
						adapter = null;
						data = new String[]{};
					}
					mHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			initData();
		};
	};
	
	private void initData() {
		mErrorLayout.setVisibility(View.GONE);
		if(null!=adapter){
			Toast.makeText(getApplicationContext(), "�������ӳɹ�",  Toast.LENGTH_SHORT).show();
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else if (0==data.length){
			if(EmptyLayout.isConnectivity(this)){//����������
				Toast.makeText(getApplicationContext(), "�������ӳɹ�",  Toast.LENGTH_SHORT).show();
				mErrorLayout.setErrorType(EmptyLayout.NODATA);
				return;
			}
			Toast.makeText(getApplicationContext(), "�������ź�",  Toast.LENGTH_SHORT).show();
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		}
	}

}
