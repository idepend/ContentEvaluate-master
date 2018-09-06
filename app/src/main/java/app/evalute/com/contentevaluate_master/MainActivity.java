package app.evalute.com.contentevaluate_master;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView evaluation1Iv;
    private ImageView evaluation2Iv;
    private ImageView evaluation3Iv;
    private ImageView evaluation4Iv;
    private ImageView evaluation5Iv;
    private boolean isEvaluation = false;
    private LinearLayout evaluateLL;
    private TextView satisfiedTv;

    private EditText contentEditText;
    private TextView evaluateNumTv;

    private RelativeLayout commitRll;
    private LinearLayout editLL;
    private TextView completionTv;
    private String value;
    private String[] parms;
    private EvaluationNegReasonsLayout mReasonsLayout;
    private MyAdapter mAdapter;
    private Set<String> mReasons = new HashSet<>();
    private String evaluate = "";
    private int mStarCount = 0;
    private String bid;
    private int _id;
    private int start = 0;//0-->未评价  1 已评价

    private LinearLayout negativeLL;
    private TextView negativeTv1;
    private TextView negativeTv2;
    private TextView negativeTv3;
    private TextView negativeTv4;
    private TextView negativeTv5;
    private TextView negativeTv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        evaluation1Iv = (ImageView) findViewById(R.id.evaluation_1);
        evaluation2Iv = (ImageView) findViewById(R.id.evaluation_2);
        evaluation3Iv = (ImageView) findViewById(R.id.evaluation_3);
        evaluation4Iv = (ImageView) findViewById(R.id.evaluation_4);
        evaluation5Iv = (ImageView) findViewById(R.id.evaluation_5);

        satisfiedTv = (TextView) findViewById(R.id.satisfied_tv);

        mReasonsLayout = (EvaluationNegReasonsLayout) findViewById(R.id.negative_layout);

        evaluateLL = (LinearLayout) findViewById(R.id.evaluate_ll);
        //已评价
        completionTv = (TextView) findViewById(R.id.evaluation_completion_tv);
        editLL = (LinearLayout) findViewById(R.id.content_ll);

        contentEditText = (EditText) findViewById(R.id.editText_content);
        evaluateNumTv = (TextView) findViewById(R.id.evaluate_num);
        commitRll = (RelativeLayout) findViewById(R.id.evaluation_commit_rll);

        negativeLL = (LinearLayout) findViewById(R.id.negative_ll);
        negativeTv1 = (TextView) findViewById(R.id.tv_negative_1);
        negativeTv2 = (TextView) findViewById(R.id.tv_negative_2);
        negativeTv3 = (TextView) findViewById(R.id.tv_negative_3);
        negativeTv4 = (TextView) findViewById(R.id.tv_negative_4);
        negativeTv5 = (TextView) findViewById(R.id.tv_negative_5);
        negativeTv6 = (TextView) findViewById(R.id.tv_negative_6);

        //有返回数据时直接替换掉就可以了
        reasonsData = new ArrayList<>();
        reasonsData.add("态度奇差");
        reasonsData.add("病情讲解不清");
        reasonsData.add("不专业");
        reasonsData.add("乱收费");
        reasonsData.add("毛没吹干");
        reasonsData.add("医嘱不全");


        initData();
        initReasonsLayout();
    }

    private int limit = 140; // 字数限制
    private CharSequence beforeSeq; // 保存修改前的值

    private int afterStart;
    private int afterCount;
    List<String> reasonsData;

    private void initData() {

        if (start == 0) {
            completionTv.setVisibility(View.GONE);
            editLL.setVisibility(View.VISIBLE);
            commitRll.setVisibility(View.VISIBLE);
        } else {
            mReasonsLayout.setVisibility(View.GONE);
            completionTv.setVisibility(View.VISIBLE);//评价内容
            editLL.setVisibility(View.GONE);
            commitRll.setVisibility(View.GONE);
            evaluation1Iv.setEnabled(false);
            evaluation2Iv.setEnabled(false);
            evaluation3Iv.setEnabled(false);
            evaluation4Iv.setEnabled(false);
            evaluation5Iv.setEnabled(false);


        }
        evaluation1Iv.setOnClickListener(this);
        evaluation2Iv.setOnClickListener(this);
        evaluation3Iv.setOnClickListener(this);
        evaluation4Iv.setOnClickListener(this);
        evaluation5Iv.setOnClickListener(this);
        commitRll.setOnClickListener(this);

        contentEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() + (after - count) > limit) {
                    beforeSeq = s.subSequence(start, start + count);

                    Toast.makeText(MainActivity.this, "不能超过" + limit + "字！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int before) {
//                if (count > before && s.length() > limit) { //如果字符数增加时，且当前字符数超过限制了, 保存原串用于还原
                if (s.length() >= limit) {
                    Toast.makeText(MainActivity.this, "不能超过" + limit + "字！", Toast.LENGTH_SHORT).show();
                    afterStart = start;
                    afterCount = count;
                }
                evaluateNumTv.setText(String.format("%s/" + limit, s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > limit) {
                    try {
                        s.replace(afterStart, afterStart + afterCount, beforeSeq);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evaluation_1:
                mStarCount = 1;
                getIconBg(mStarCount);
                break;
            case R.id.evaluation_2:
                mStarCount = 2;
                getIconBg(mStarCount);
                break;
            case R.id.evaluation_3:
                mStarCount = 3;
                getIconBg(mStarCount);
                break;
            case R.id.evaluation_4:
                mStarCount = 4;
                getIconBg(mStarCount);
                break;
            case R.id.evaluation_5:
                mStarCount = 5;
                getIconBg(mStarCount);
                break;

            case R.id.evaluation_commit_rll:
                String textTemp = contentEditText.getText().toString().trim();
                if (textTemp.isEmpty() && evaluate.equals("")) {
                    commitRll.setBackgroundResource(R.mipmap.evaluation_nor_grade);
                    Toast.makeText(MainActivity.this, "你必须选择一个评分或者评论", Toast.LENGTH_SHORT).show();
                } else {
//
                    Toast.makeText(MainActivity.this, "评分：" + mStarCount + "\n 评分选择：" + evaluate + "\n评论：" + textTemp, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getIconBg(int num) {
        isEvaluation = true;
        evaluate = "";
        commitRll.setBackgroundResource(R.mipmap.evaluation_seclect_grade);
        initReasonsLayout();
        if (num == 1) {
            evaluateLL.setVisibility(View.VISIBLE);
            mReasonsLayout.setVisibility(View.VISIBLE);
            satisfiedTv.setVisibility(View.GONE);
            contentEditText.setHint("您有哪些地方不满意，评价之后将会获得奖励哦～");
            evaluation1Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation2Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation3Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation4Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation5Iv.setImageResource(R.mipmap.evaluation_nor);
        } else if (num == 2) {
            evaluateLL.setVisibility(View.VISIBLE);
            mReasonsLayout.setVisibility(View.VISIBLE);
            satisfiedTv.setVisibility(View.GONE);
            contentEditText.setHint("您有哪些地方不满意，评价之后将会获得奖励哦～");
            evaluation1Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation2Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation3Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation4Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation5Iv.setImageResource(R.mipmap.evaluation_nor);
        } else if (num == 3) {
            evaluateLL.setVisibility(View.VISIBLE);
            mReasonsLayout.setVisibility(View.VISIBLE);
            satisfiedTv.setVisibility(View.GONE);
            contentEditText.setHint("您有哪些地方不满意，评价之后将会获得奖励哦～");
            evaluation1Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation2Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation3Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation4Iv.setImageResource(R.mipmap.evaluation_nor);
            evaluation5Iv.setImageResource(R.mipmap.evaluation_nor);
        } else if (num == 4) {
            evaluateLL.setVisibility(View.VISIBLE);
            mReasonsLayout.setVisibility(View.VISIBLE);
            satisfiedTv.setVisibility(View.GONE);
            contentEditText.setHint("您有哪些地方不满意，评价之后将会获得奖励哦～");
            evaluation1Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation2Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation3Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation4Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation5Iv.setImageResource(R.mipmap.evaluation_nor);
        } else if (num == 5) {
            satisfiedTv.setVisibility(View.VISIBLE);
            evaluateLL.setVisibility(View.VISIBLE);
            mReasonsLayout.setVisibility(View.GONE);
            evaluate = satisfiedTv.getText().toString();
            contentEditText.setHint("有好的建议，你可以提出，你的建议会让我们做的更好。");
            evaluation1Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation2Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation3Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation4Iv.setImageResource(R.mipmap.evaluation_seclect);
            evaluation5Iv.setImageResource(R.mipmap.evaluation_seclect);
        }
    }

    /**
     * 初始化理由布局
     */
    private void initReasonsLayout() {


        mAdapter = new MyAdapter(this);
        mReasonsLayout.setAdapter(mAdapter);
        mAdapter.setData(reasonsData);


        mReasonsLayout.setOnReasonSelectListener(new EvaluationNegReasonsLayout.OnReasonSelectListener() {
            @Override
            public void onItemSelect(EvaluationNegReasonsLayout parent, List<Integer> selectedList) {
                mReasons.clear();
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        mReasons.add(mAdapter.getItem(i));
                        StringBuilder sb = new StringBuilder();
                        for (String reason : mReasons) {
                            sb.append(" ").append(reason);
                            evaluate = sb.toString();
                        }

                    }
                } else {
                    mReasons.clear();
                }
            }
        });
    }

    static class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mData;

        MyAdapter(Context context) {
            mContext = context;
            mData = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_negative_reason, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_negative_reason);
            textView.setText(mData.get(position));
            return view;
        }

        void setData(List<String> data) {
            if (data == null) {
                return;
            }
            mData = data;
            notifyDataSetChanged();
        }
    }
}
