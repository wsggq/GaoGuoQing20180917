package com.example.ggq.gaoguoqing20180917;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ggq.gaoguoqing20180917.adapter.ShopcartExpandableListViewAdapter;
import com.example.ggq.gaoguoqing20180917.entity.GroupInfo;
import com.example.ggq.gaoguoqing20180917.entity.ProductInfo;

public class MainActivity extends Activity implements ShopcartExpandableListViewAdapter.CheckInterface, ShopcartExpandableListViewAdapter.ModifyCountInterface, OnClickListener
{
	private ExpandableListView exListView;
	private CheckBox cb_check_all;
	private TextView tv_total_price;
	private TextView tv_delete;
	private Context context;

	private double totalPrice = 0.00;// 购买的商品总价
	private int totalCount = 0;// 购买的商品总数量

	private ShopcartExpandableListViewAdapter selva;
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();// 组元素数据列表
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();// 子元素数据列表

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initEvents();
	}

	private void initView()
	{
		context = this;
		virtualData();
		exListView = findViewById(R.id.exListView);
		cb_check_all = findViewById(R.id.all_chekbox);
		tv_total_price = findViewById(R.id.tv_total_price);
		tv_delete = findViewById(R.id.tv_delete);
	}

	private void initEvents()
	{
		selva = new ShopcartExpandableListViewAdapter(groups, children, this);
		selva.setCheckInterface(this);
		selva.setModifyCountInterface(this);
		exListView.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++)
		{
			exListView.expandGroup(i);
		}

		cb_check_all.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
	}
	private void virtualData()
	{
		for (int i = 0; i < 6; i++)
		{
			groups.add(new GroupInfo(i + "", "商家" + (i + 1) + "号店"));
			List<ProductInfo> products = new ArrayList<ProductInfo>();
			for (int j = 0; j <= i; j++)
			{
				products.add(new ProductInfo(j + "", "商品", "", groups.get(i).getName() + "的第" + (j + 1) + "个商品", 120.00 + i * j, 1));
			}
			children.put(groups.get(i).getId(), products);
		}
	}

	@Override
	public void onClick(View v)
	{
		AlertDialog alert;
		switch (v.getId())
		{
		case R.id.all_chekbox:
			doCheckAll();
			break;
		case R.id.tv_delete:
			if (totalCount == 0)
			{
				Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("您确定要将这些商品从购物车中移除吗？");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					doDelete();
				}
			});
			alert.show();
			break;
		}
	}

	protected void doDelete()
	{
		List<GroupInfo> toBeDeleteGroups = new ArrayList<GroupInfo>();
		for (int i = 0; i < groups.size(); i++)
		{
			GroupInfo group = groups.get(i);
			if (group.isChoosed())
			{

				toBeDeleteGroups.add(group);
			}
			List<ProductInfo> toBeDeleteProducts = new ArrayList<ProductInfo>();
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++)
			{
				if (childs.get(j).isChoosed())
				{
					toBeDeleteProducts.add(childs.get(j));
				}
			}
			childs.removeAll(toBeDeleteProducts);

		}
		groups.removeAll(toBeDeleteGroups);

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked)
	{

		ProductInfo product = (ProductInfo) selva.getChild(groupPosition, childPosition);
		int currentCount = product.getCount();
		currentCount++;
		product.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked)
	{

		ProductInfo product = (ProductInfo) selva.getChild(groupPosition, childPosition);
		int currentCount = product.getCount();
		if (currentCount == 1)
			return;
		currentCount--;

		product.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked)
	{
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++)
		{
			childs.get(i).setChoosed(isChecked);
		}
		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkChild(int groupPosition, int childPosiTion, boolean isChecked)
	{
		boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++)
		{
			if (childs.get(i).isChoosed() != isChecked)
			{
				allChildSameState = false;
				break;
			}
		}
		if (allChildSameState)
		{
			group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
		} else
		{
			group.setChoosed(false);// 否则，组元素一律设置为未选中状态
		}

		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	private boolean isAllCheck()
	{
		for (GroupInfo group : groups)
		{
			if (!group.isChoosed())
				return false;
		}
		return true;
	}

	/** 全选与反选 */
	private void doCheckAll()
	{
		for (int i = 0; i < groups.size(); i++)
		{
			groups.get(i).setChoosed(cb_check_all.isChecked());
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++)
			{
				childs.get(j).setChoosed(cb_check_all.isChecked());
			}
		}
		selva.notifyDataSetChanged();
	}

	private void calculate()
	{
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < groups.size(); i++)
		{
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++)
			{
				ProductInfo product = childs.get(j);
				if (product.isChoosed())
				{
					totalCount++;
					totalPrice += product.getPrice() * product.getCount();
				}
			}
		}
		tv_total_price.setText("￥" + totalPrice);
	}
}
