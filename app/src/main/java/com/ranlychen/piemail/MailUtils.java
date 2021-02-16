package com.ranlychen.piemail;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
    //发件人地址
    public static String senderAddress = "13651449532@163.com";
    //收件人地址
    public static String recipientAddress = "513446825@qq.com";
    //发件人账户名
    public static String senderAccount = "13651449532";
    //发件人账户密码
    public static String senderPassword = "THGHRQAXUNGEOBEK";

    public MailUtils() {

    }

    public void sendMessage() throws Exception {
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.163.com");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session);
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
        //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

        //5、关闭邮件连接
        transport.close();
    }

    public List<MailItem> getMessageFromServer() throws Exception{
        String recipientAddress = "13651449532@163.com";
        String recipientAccount = "13651449532";
        String recipientPassword = "THGHRQAXUNGEOBEK";


        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", "pop3.163.com");
        Session session = Session.getInstance(props);

        Store store = session.getStore("pop3");
        store.connect("pop3.163.com", recipientAccount, recipientPassword);
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);

        Log.d("folder.getMessageCount",String.valueOf(folder.getMessageCount()));
        Log.d("folder.getNewMessageCount",String.valueOf(folder.getNewMessageCount()));
        Message [] messages = folder.getMessages();

        List<MailItem> mailList = new ArrayList<>();

        for(int j=0;j<messages.length;j++){
            MailItem mailItem = new MailItem((MimeMessage)messages[j]);
            mailItem.setBodyText(resolveMessage(messages[j]));

            String from = messages[j].getFrom()[0].toString();
            if(from.contains("utf-8?B?")||from.contains("UTF-8?B?")){
                String fromName = new String(Base64.decode(from.split("<")[0]
                        .replace("utf-8?B?","")
                        .replace("UTF-8?B?","")
                        .replace("?=","")
                        .replace("=?",""),Base64.DEFAULT));
                String fromAccount = "<" + from.split("<")[1];
                mailItem.setFromName(fromName);
                mailItem.setFromAddress(fromAccount);

                
                if(mailItem.getFromAddress().contains("@qq.com")){
                    mailItem.setLogoResId(R.drawable.ic_mail_logo_qq);
                }
                if(mailItem.getFromAddress().contains("@gmail.com")){
                    mailItem.setLogoResId(R.drawable.ic_mail_logo_gmail);
                }
                if(mailItem.getFromAddress().contains("@163.com")||mailItem.getFromAddress().contains("netease.com")){
                    mailItem.setLogoResId(R.drawable.ic_mail_logo_netease);
                }

            }

            Log.d("item_mail_title",messages[j].getSubject());
            Log.d("item_mail_from",messages[j].getFrom()[0].toString());
            Log.d("item_mail_from_name&address_Base64_decoded",mailItem.getFromName() + mailItem.getFromAddress());
            Log.d("item_mail_time",messages[j].getSentDate().toString());
            Log.d("item_mail_contentType",messages[j].getContentType());

            mailList.add(mailItem);
        }

        folder.close();
        store.close();

        return mailList;
    }

    private String resolveMessage(Message message) throws IOException, MessagingException {
        Object o = message.getContent();
        String content = null;

        if (o instanceof Multipart) {
            Log.i("MailUtils","**This is a Multipart Message.  ");
            Multipart mp = (Multipart)o;
            Log.i("MailUtils","The Multipart message has " + mp.getCount() + " parts.");
            for (int j = 0; j < mp.getCount(); j++) {
                BodyPart b = mp.getBodyPart(j);
                if (b.getContentType().contains("multipart")) {
                    mp = (Multipart)b.getContent();
                    j = -1;
                    continue;
                }
                Log.i("MailUtils","This content type is " + b.getContentType());

                if(!b.getContentType().contains("text/html")) {
                    continue;
                }
                Object o2 = b.getContent();
                if (o2 instanceof String) {
                    Log.i("MailUtils_String",o2.toString());
                    content = o2.toString();
                }
            }
        }

        if(o instanceof String){
            Log.i("MailUtils","**This is a String Message.");
            content = o.toString();
        }

        return content;
    }
    
    public static MimeMessage getMimeMessage(Session session) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress));
        //设置邮件主题
        msg.setSubject("邮件主题","UTF-8");
        //设置邮件正文
        msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }

}