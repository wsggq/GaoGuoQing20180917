package com.example.ggq.gaoguoqing20180917.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ggq.gaoguoqing20180917.R;
import com.example.ggq.gaoguoqing20180917.entity.GroupInfo;
import com.example.ggq.gaoguoqing20180917.entity.ProductInfo;
import com.example.ggq.gaoguoqing20180917.utils.FrontViewToMove;

import java.util.List;
import java.util.Map;

public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter
{
	private List<GroupInfo> groups;
	private Map<String, List<ProductInfo>> children;
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;

	public ShopcartExpandableListViewAdapter(List<GroupInfo> groups, Map<String, List<ProductInfo>> children, Context context)
	{
		super();
		this.groups = groups;
		this.children = children;
		this.context = context;
	}

	public void setCheckInterface(CheckInterface checkInterface)
	{
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface)
	{
		this.modifyCountInterface = modifyCountInterface;
	}

	@Override
	public int getGroupCount()
	{
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		String groupId = groups.get(groupPosition).getId();
		return children.get(groupId).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		List<ProductInfo> childs = children.get(groups.get(groupPosition).getId());

		return childs.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{

		GroupHolder gholder;
		if (convertView == null)
		{
			gholder = new GroupHolder();
			convertView = View.inflate(context, R.layout.item_shopcart_group, null);
			gholder.cb_check = convertView.findViewById(R.id.determine_chekbox);
			gholder.tv_group_name = convertView.findViewById(R.id.tv_source_name);
			 convertView.setTag(gholder);
		} else
		{
			gholder = (GroupHolder) convertView.getTag();
		}
		final GroupInfo group = (GroupInfo) getGroup(groupPosition);
		if (group != null)
		{
			gholder.tv_group_name.setText(group.getName());
			gholder.cb_check.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)

				{
					group.setChoosed(((CheckBox) v).isChecked());
					checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
				}
			});
			gholder.cb_check.setChecked(group.isChoosed());
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		final ChildHolder cholder;
		if (convertView == null)
		{
			cholder = new ChildHolder();
			convertView = View.inflate(context, R.layout.item_shopcart_product, null);
			cholder.cb_check = convertView.findViewById(R.id.check_box);

			cholder.tv_product_desc = convertView.findViewById(R.id.tv_intro);
			cholder.tv_price = convertView.findViewById(R.id.tv_price);
			cholder.iv_increase = convertView.findViewById(R.id.tv_add);
			cholder.iv_decrease = convertView.findViewById(R.id.tv_reduce);
			cholder.tv_count = convertView.findViewById(R.id.tv_num);
			convertView.setTag(cholder);
		} else
		{
			cholder = (ChildHolder) convertView.getTag();
		}
		final ProductInfo product = (ProductInfo) getChild(groupPosition, childPosition);
		if (product != null)
		{
			cholder.tv_product_desc.setText(product.getDesc());
			cholder.tv_price.setText("￥" + product.getPrice() + "");
			cholder.tv_count.setText(product.getCount() + "");
			cholder.cb_check.setChecked(product.isChoosed());
			cholder.cb_check.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					product.setChoosed(((CheckBox) v).isChecked());
					cholder.cb_check.setChecked(((CheckBox) v).isChecked());
					checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
				}
			});
			cholder.iv_increase.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
				}
			});
			cholder.iv_decrease.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
				}
			});
		}
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}

	/**
	 * 组元素绑定器
	 * 
	 * 
	 */
	private class GroupHolder
	{
		CheckBox cb_check;
		TextView tv_group_name;
	}

	/**
	 * 子元素绑定器
	 * 
	 * 
	 */
	private class ChildHolder
	{
		CheckBox cb_check;
		TextView tv_product_name;
		TextView tv_product_desc;
		TextView tv_price;
		TextView iv_increase;
		TextView tv_count;
		TextView iv_decrease;
	}

	/**
	 * 复选框接口
	 * 
	 * 
	 */
	public interface CheckInterface
	{
		public void checkGroup(int groupPosition, boolean isChecked);
		public void checkChild(int groupPosition, int childPosition, boolean isChecked);
	}

	public interface ModifyCountInterface
	{
		public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
		public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
	}

}
