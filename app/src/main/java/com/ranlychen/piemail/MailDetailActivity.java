package com.ranlychen.piemail;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzhoujay.richtext.RichText;

public class MailDetailActivity extends WearableActivity {

    private MailItem mailItem;
    private TextView fromNameTv;
    private TextView fromAddressTv;
    private TextView subjectTv;
    private TextView timeTv;
    private TextView contentTv;
    private ImageView logoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);

        mailItem = (MailItem) getIntent().getSerializableExtra("MailItem");

        initView();
    }

    private void initView(){
        fromNameTv = findViewById(R.id.detail_from_name);
        fromAddressTv = findViewById(R.id.detail_from_address);
        subjectTv = findViewById(R.id.detail_subject);
        timeTv = findViewById(R.id.detail_time);
        contentTv = findViewById(R.id.detail_content);
        logoIv = findViewById(R.id.detail_logo);

        fromNameTv.setText(mailItem.getFromName());
        fromAddressTv.setText(mailItem.getFromAddress());
        subjectTv.setText("主题：" + mailItem.getSubject());
        timeTv.setText(mailItem.getSendTime());
        logoIv.setImageResource(mailItem.getLogoResId());

        RichText.initCacheDir(this);

        if(mailItem.getBodyText().contains("</head>")){
            mailItem.setBodyText(mailItem.getBodyText().split("</head>")[1]);
        }

        RichText.from(mailItem.getBodyText())
                .showBorder(false)
                .bind(this)
                //.noImage(true)
                .clickable(false)
                //.imageGetter()
                .into(contentTv);//也可改用markdown,即fromMarkdown()


    }

}
