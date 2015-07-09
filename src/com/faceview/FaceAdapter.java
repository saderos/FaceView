package com.faceview;

import com.example.facepageactivity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter {

	private Context m_Context;
	int face_page;
	int face_row;
	int face_col;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context
	 * @param face_page
	 *            The current page number of the ViewPager
	 * @param face_row
	 *            the number of columns in the grid
	 * @param face_col
	 *            the number of rows in the grid
	 */
	public FaceAdapter(Context context, int face_page, int face_row,
			int face_col) {
		m_Context = context;
		this.face_page = face_page;
		this.face_row = face_row;
		this.face_col = face_col;
	}

	@Override
	public int getCount() {
		return face_row * face_col;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(m_Context).inflate(
					R.layout.face_item, parent, false);

			holder = new ViewHolder();
			holder.m_imgFace = (ImageView) convertView
					.findViewById(R.id.facelistitem_imgFace);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imgTag = getImgTag(position);
		if (imgTag != null) {
			holder.m_imgFace.setImageResource(Global.FACEMAP.get(imgTag));
			holder.m_imgFace.setTag(imgTag);
		}
		return convertView;
	}

	/**
	 * get the imgTag according to the position
	 * 
	 * @param position
	 * @return
	 */
	private String getImgTag(int position) {
		String imgTag = "";
		int face_num = face_page * face_row * face_col + position;
		// the number of total image is 134,so when the image number larger than
		// 134 ,them return null
		if (face_num > 134)
			return null;
		imgTag = face_num + "";
		switch (imgTag.length()) {
		case 1:
			imgTag = "/e00" + imgTag;
			break;
		case 2:
			imgTag = "/e0" + imgTag;
			break;
		case 3:
			imgTag = "/e" + imgTag;
			break;
		default:
			break;
		}
		return imgTag;
	}

	class ViewHolder {
		public ImageView m_imgFace;
	}
}