package com.faceview;

import java.util.ArrayList;
import java.util.List;

import com.example.facepageactivity.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FaceView extends LinearLayout implements OnPageChangeListener,
		OnItemClickListener, OnClickListener {
	private static String imgTag = "";
	private List<View> m_arrFacePageView;
	private ViewPager m_vpFace;
	private List<ImageView> m_arrDotView;
	private LinearLayout m_dotBar;
	private Context m_context;
	private OnFaceSelectedListener m_listener;
	private EditText m_edit;
	private View m_btnView;

	private int m_ImgNum = 135;
	private int m_row;
	private int m_col;

	/**
	 * 实例化方法
	 * 
	 * @param context
	 *            the current context
	 * @param attrs
	 */
	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		m_context = context;
		LayoutInflater.from(context).inflate(R.layout.face_view, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FaceView);
		m_row = a.getInt(R.styleable.FaceView_row, 3);
		m_col = a.getInt(R.styleable.FaceView_col, 7);
		a.recycle();
		initView();
	}

	/**
	 * set the button to show or hide the FaceView
	 * 
	 * @param btnView
	 * @author saderos
	 * 
	 */
	public void setBtnView(View btnView) {
		m_btnView = btnView;
		m_btnView.setOnClickListener(this);
	}

	/**
	 * you just need to set the editText， and them ,you don't have to append
	 * text to the editText yourself
	 * 
	 * @author saderos
	 * @param editText
	 */
	public void setEdit(EditText editText) {
		m_edit = editText;
		m_edit.setOnClickListener(this);
	}

	public void setOnFaceSelectedListener(OnFaceSelectedListener listener) {
		m_listener = listener;
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		m_vpFace = (ViewPager) findViewById(R.id.chat_vpFace);
		m_dotBar = (LinearLayout) findViewById(R.id.chat_dotbar);
		initFaceBar();
	}

	private void initFaceBar() {
		m_arrFacePageView = new ArrayList<View>();
		int pageNum = m_ImgNum / m_col / m_row + 1;
		for (int i = 0; i < pageNum; i++) {
			GridView view = new GridView(m_context);
			FaceAdapter adapter = new FaceAdapter(m_context, i, m_row, m_col);
			view.setOnItemClickListener(this);
			view.setAdapter(adapter);
			view.setNumColumns(m_col);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			m_arrFacePageView.add(view);
		}

		ViewPagerAdapter adapter = new ViewPagerAdapter(m_arrFacePageView);
		if (!isInEditMode()) {
			m_vpFace.setAdapter(adapter);
			m_vpFace.setCurrentItem(0);
		}
		m_vpFace.setOnPageChangeListener(this);
		initDotBar();

	}

	private void initDotBar() {
		m_arrDotView = new ArrayList<ImageView>();

		for (int i = 0; i < m_arrFacePageView.size(); i++) {
			ImageView imgView = new ImageView(m_context);
			imgView.setBackgroundResource(R.drawable.common_indicator_nor);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			params.leftMargin = (int) getResources().getDimension(
					R.dimen.dotLeftMargin);
			params.rightMargin = (int) getResources().getDimension(
					R.dimen.dotRightMargin);

			m_dotBar.addView(imgView, params);
			m_arrDotView.add(imgView);
		}
		setSelDot(0);
	}

	private void setSelDot(int nSelIndex) {
		for (int i = 0; i < m_arrDotView.size(); i++) {
			if (nSelIndex == i) {
				m_arrDotView.get(i).setBackgroundResource(
						R.drawable.common_indicator_checked);
			} else {
				m_arrDotView.get(i).setBackgroundResource(
						R.drawable.common_indicator_nor);
			}
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		m_vpFace.setCurrentItem(arg0);
		setSelDot(arg0);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		View img = v.findViewById(R.id.facelistitem_imgFace);
		if (img.getTag() != null) {
			imgTag = img.getTag().toString();
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					Global.FACEMAP.get(imgTag));
			if (m_listener != null) {
				m_listener.OnFaceSelected(imgTag, bitmap);
			}
			if (m_edit != null) {
				ImageSpan imgSpan = new ImageSpan(m_context, bitmap);
				SpannableString spanString = new SpannableString(imgTag);
				spanString.setSpan(imgSpan, 0, spanString.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				m_edit.append(spanString);
			}
		}
	}

	public interface OnFaceSelectedListener {
		public void OnFaceSelected(String imgTag, Bitmap face);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		// show or hide the input Method
		if (v == m_btnView) {
			InputMethodManager imm = (InputMethodManager) m_context
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (this.getVisibility() == View.GONE) {
				imm.hideSoftInputFromWindow(((Activity) m_context)
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.setVisibility(View.VISIBLE);
			} else {
				imm.showSoftInput(m_edit, InputMethodManager.SHOW_IMPLICIT);
				this.setVisibility(View.GONE);

			}
		} else if (v == m_edit) {
			this.setVisibility(View.GONE);

		}
	}
}
