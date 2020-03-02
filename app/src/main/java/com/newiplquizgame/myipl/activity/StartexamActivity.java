package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.pkg.QuestionArray;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class StartexamActivity extends AppCompatActivity implements APIcall.ApiCallListner {
    Toolbar toolbar;
    TextView txt_qustion;
    int QuePos = 1;
    GruopMaster queGruopMaster;
    List<GroupDatum> queGroupData;
    List<QuestionArray> pho = new ArrayList<>();
    private ProgressDialog dialog;

    TextView txt_que;
    TextView txt_ans_1;
    TextView txt_ans_2;
    TextView txt_ans_3;
    TextView txt_ans_4;

    RelativeLayout rl_ans_1;
    RelativeLayout rl_ans_2;
    RelativeLayout rl_ans_3;
    RelativeLayout rl_ans_4;

    LinearLayout rl_main;

    int start_pos = 0;
    CircleIndicator indicator;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexam);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findBindView();
        callAPIAllQuestion();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findBindView() {
        //    viewpager = findViewById(R.id.pager);

        txt_qustion = findViewById(R.id.txt_qustion);

        indicator = findViewById(R.id.indicator);


        txt_que = findViewById(R.id.txt_que);
        txt_ans_1 = findViewById(R.id.txt_ans_1);
        txt_ans_2 = findViewById(R.id.txt_ans_2);
        txt_ans_3 = findViewById(R.id.txt_ans_3);
        txt_ans_4 = findViewById(R.id.txt_ans_4);

        rl_ans_1 = findViewById(R.id.q_1);
        rl_ans_2 = findViewById(R.id.q_2);
        rl_ans_3 = findViewById(R.id.q_3);
        rl_ans_4 = findViewById(R.id.q_4);
        rl_main = findViewById(R.id.rl_main);
        rl_main.setVisibility(View.GONE);

        findViewById(R.id.bt_per_ans).setVisibility(View.GONE);

//        findViewById(R.id.bt_sub_ans).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (queGroupData.size() == start_pos) {
//                    findViewById(R.id.bt_sub_ans).setVisibility(View.GONE);
//                    Toast.makeText(StartexamActivity.this, "Over", Toast.LENGTH_SHORT).show();
//                } else {
//                    start_pos++;
//                    findViewById(R.id.bt_per_ans).setVisibility(View.VISIBLE);
//                    Showqustion_ans(start_pos);
//                    chgBacground();
//                    fillAutofill(start_pos);
//                }
//            }
//        });

        findViewById(R.id.bt_skip_ans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getrefereData();
            }
        });

        findViewById(R.id.bt_per_ans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_pos == 0) {
                    findViewById(R.id.bt_per_ans).setVisibility(View.GONE);
                    start_pos = 0;
                    QuePos = 1;
                    Toast.makeText(StartexamActivity.this, "Over", Toast.LENGTH_SHORT).show();
                } else {
                    start_pos--;
                    QuePos--;
                    findViewById(R.id.bt_skip_ans).setVisibility(View.VISIBLE);
                    Showqustion_ans(start_pos);
                    chgBacground();
                    fillAutofill(start_pos);
                    showTextBoxData();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getrefereData() {
        start_pos++;
        QuePos++;
        if (queGroupData.size() == start_pos) {
            findViewById(R.id.bt_skip_ans).setVisibility(View.GONE);
            Toast.makeText(StartexamActivity.this, "Over", Toast.LENGTH_SHORT).show();
        } else {
            findViewById(R.id.bt_per_ans).setVisibility(View.VISIBLE);
            Showqustion_ans(start_pos);
            chgBacground();
            fillAutofill(start_pos);
            showTextBoxData();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Showqustion_ans(final int sstart_pos) {
        txt_que.setText(Common.isempty(pho.get(sstart_pos).getQuestion()));
        String d = pho.get(sstart_pos).getAnswers();
        final String[] dummyTexts = d.split(",");

        txt_ans_1.setText(Common.isempty(dummyTexts[0].toString()));

        if (dummyTexts.length > 1) {
            rl_ans_2.setVisibility(View.VISIBLE);
            txt_ans_2.setText(Common.isempty(dummyTexts[1].toString()));
        } else {
            rl_ans_2.setVisibility(View.GONE);
            rl_ans_3.setVisibility(View.GONE);
            rl_ans_4.setVisibility(View.GONE);
        }
        if (dummyTexts.length > 2) {
            rl_ans_3.setVisibility(View.VISIBLE);
            rl_ans_4.setVisibility(View.VISIBLE);
            txt_ans_3.setText(Common.isempty(dummyTexts[2].toString()));
            txt_ans_4.setText(Common.isempty(dummyTexts[3].toString()));
        } else {
            rl_ans_3.setVisibility(View.GONE);
            rl_ans_4.setVisibility(View.GONE);
        }

        rl_ans_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionArray array = pho.get(sstart_pos);
                array.setAttempt(1);
                pho.set(sstart_pos, array);
                chgBacground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_2.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
                getrefereData();
            }
        });
        rl_ans_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionArray array = pho.get(sstart_pos);
                array.setAttempt(0);
                pho.set(sstart_pos, array);
                chgBacground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_1.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
                getrefereData();
            }
        });
        rl_ans_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionArray array = pho.get(sstart_pos);
                array.setAttempt(2);
                pho.set(sstart_pos, array);
                chgBacground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_3.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
                getrefereData();
            }
        });
        rl_ans_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QuestionArray array = pho.get(sstart_pos);
                array.setAttempt(3);
                pho.set(sstart_pos, array);
                chgBacground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_4.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
                getrefereData();
            }
        });
        if (queGroupData.size() == start_pos) {
            findViewById(R.id.bt_skip_ans).setVisibility(View.GONE);
            Toast.makeText(StartexamActivity.this, "Over", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fillAutofill(int mstart_pos) {
        if (pho.get(mstart_pos).getAttempt() != null) {
            if (pho.get(mstart_pos).getAttempt() == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_1.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
            }
            if (pho.get(mstart_pos).getAttempt() == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_2.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
            }
            if (pho.get(mstart_pos).getAttempt() == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_3.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
            }
            if (pho.get(mstart_pos).getAttempt() == 3) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rl_ans_4.setBackgroundColor(StartexamActivity.this.getColor(R.color.select_que));
                }
            }
        } else {
            chgBacground();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void chgBacground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rl_ans_1.setBackgroundColor(StartexamActivity.this.getColor(R.color.un_select_que));
            rl_ans_2.setBackgroundColor(StartexamActivity.this.getColor(R.color.un_select_que));
            rl_ans_3.setBackgroundColor(StartexamActivity.this.getColor(R.color.un_select_que));
            rl_ans_4.setBackgroundColor(StartexamActivity.this.getColor(R.color.un_select_que));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callAPIAllQuestion() {
        String url = AppConstant.GET_ALL_QUESTION;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_ALL_QUESTION, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ALL_QUESTION) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_ALL_QUESTION) {
            Gson gson = new Gson();
            queGruopMaster = gson.fromJson(response, GruopMaster.class);
            if (queGruopMaster.getStatus() == 0) {
                queGroupData = new ArrayList<>();
                queGroupData = queGruopMaster.getData();
                rl_main.setVisibility(View.VISIBLE);
                for (int i = 0; i < queGroupData.size(); i++) {
                    QuestionArray squestionArray = new QuestionArray();
                    squestionArray.setQueId(queGroupData.get(i).getQueId());
                    squestionArray.setAnswers(queGroupData.get(i).getAnswers());
                    squestionArray.setPoint(queGroupData.get(i).getPoint());
                    squestionArray.setQuestion(queGroupData.get(i).getQuestion());
                    pho.add(squestionArray);
                }
                Showqustion_ans(start_pos);
                showTextBoxData();

            } else {
                Common.displayToastMessageShort(this, "" + Common.isempty(queGruopMaster.getMsg()), true);
            }
        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    private void showTextBoxData() {
        indicator.createIndicators(pho.size(), 0);
        indicator.animatePageSelected(start_pos);
        txt_qustion.setText(String.valueOf(QuePos));
    }
}
