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
		
		
        //去掉Activity上面的状态栏
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
       
		// 得到设置属性的对象
		WebSettings settings = webView.getSettings();
		// 使能JavaScript
		settings.setJavaScriptEnabled(true);
		// 支持中文，否则页面中中文显示乱码
		settings.setDefaultTextEncodingName("GBK");
		
		//---定位-------------------------------------------------------------------------
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
//			//判断页面加载过程
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//            	
//            	 // 网页加载完成
//                if (newProgress == 100) {
//                   // 首次加载界面隐藏
//                	if(scbool){
//                		//延迟0.7秒后执行run方法中的页面跳转  
//                        new Handler().postDelayed(new Runnable() {            
//                            @Override
//                            public void run() {
//                            	//显示状态栏
//                                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                                lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                                getWindow().setAttributes(lp);
//                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//                                
//                                if(EmptyLayout.isConnectivity(MainActivity.this)){//网络连接问题
//                                	layout.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
//                                	webView.setVisibility(View.VISIBLE);//这一句即显示布局LinearLayout区域
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
//                    // 加载中
//                	//webView.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
//                	
//                	//layout.setVisibility(View.VISIBLE);//这一句即显示布局LinearLayout区域
//                }
//            }
//        });
		
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//监听点击手机号码则转到拨号界面
				if (url.startsWith("tel:")) { 
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url)); 
                    startActivity(intent); 
                    return true;
				}
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			
			// 加载中
			@Override  
		    public void onPageStarted(WebView view, String url, Bitmap favicon) {  
		        super.onPageStarted(view, url, favicon);  
		        //无网络连接
		        if(!EmptyLayout.isConnectivity(MainActivity.this)){
		        	mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		        	// 无网络信息给图片添加点击按钮
		        	mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
		    			@Override
		    			public void onClick(View v) {
		    			    virtulHttp();
		    			}
		    		});
		        	Toast.makeText(getApplicationContext(), "无网络信号",  Toast.LENGTH_SHORT).show();
		        }
		    }  
			
			// 加载完毕
		    @Override  
		    public void onPageFinished(WebView view, String url) {  
		        super.onPageFinished(view, url);
		        // 首次打开则执行
		        if(scbool){
		        	//显示状态栏
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getWindow().setAttributes(lp);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		        	
			        layout.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
			        webView.setVisibility(View.VISIBLE);//这一句即显示布局LinearLayout区域
			        scbool = false;
		        }
		    }
		});
		
		// 长按禁止复制黏贴
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
		
		//WebView加载web资源
		webView.loadUrl("http://api.gjtzw.net");
    }
	

	
	/*---------------------------------------------------------------------
	* 名称：手机按返回建监听
	* @param  {[type]} a [description]
	* @return {[type]}   [description]
	* 日期：2017-01-10 12:03:59
	* 功能：
	* 		如果返回到首页则判断是否需要退出按两次则退出程序
	*---------------------------------------------------------------------*/
	private long clickTime=0;
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
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
            Toast.makeText(getApplicationContext(), "在按一次退出程序",  Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();  
            Log.e(MainActivity.ACTIVITY_TAG, "exit 点击了退出程序");  
        } else {  
            Log.e(MainActivity.ACTIVITY_TAG, "exit 关闭了程序");  
            this.finish();
            System.exit(0);//退出程序
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

	//模拟网络请求数据
	private  void virtulHttp(){
		mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					if(EmptyLayout.isConnectivity(MainActivity.this)){//有没有网
						data = new String[]{"网络正常"};
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
			Toast.makeText(getApplicationContext(), "网络链接成功",  Toast.LENGTH_SHORT).show();
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else if (0==data.length){
			if(EmptyLayout.isConnectivity(this)){//网络连接问
				Toast.makeText(getApplicationContext(), "网络链接成功",  Toast.LENGTH_SHORT).show();
				mErrorLayout.setErrorType(EmptyLayout.NODATA);
				return;
			}
			Toast.makeText(getApplicationContext(), "无网络信号",  Toast.LENGTH_SHORT).show();
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		}
	}

}
