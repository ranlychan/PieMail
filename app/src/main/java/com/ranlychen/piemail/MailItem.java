package com.ranlychen.piemail;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.io.Serializable;
import java.util.List;

import javax.mail.internet.MimeMessage;

@Table(database = MailDataBase.class)
public class MailItem extends BaseModel implements Serializable {
    @PrimaryKey()
    @Column
    private String id;

    @Column
    public String belongToAccount;

    @Column
    private String fromName;
    @Column
    private String fromAddress;
    @Column
    private String sendTime;
    @Column
    private String toWho;//收件人
    @Column
    private String toCC;//抄送
    @Column
    private String toBCC;//密送
    @Column
    private String subject;
    @Column
    private String bodyText;
    @Column
    private int logoResId;

    public MailItem() {
        this.logoResId = R.drawable.ic_mail;
    }

    public MailItem(MimeMessage message) {
        this.logoResId = R.drawable.ic_mail;

        try {
            this.id = message.getMessageID();
            this.subject = message.getSubject();
            this.fromName = message.getFrom()[0].toString();
            this.sendTime = message.getSentDate().toString();

            //this.fromAddress = message.getSender().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public MailItem(String fromName, String fromAddress, String sendTime, String toWho, String subject, String bodyText) {
        this.fromName = fromName;
        this.fromAddress = fromAddress;
        this.sendTime = sendTime;
        this.toWho = toWho;
        this.subject = subject;
        this.bodyText = bodyText;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBelongToAccount() {
        return belongToAccount;
    }

    public void setBelongToAccount(String belongToAccount) {
        this.belongToAccount = belongToAccount;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getToCC() {
        return toCC;
    }

    public void setToCC(String toCC) {
        this.toCC = toCC;
    }

    public String getToBCC() {
        return toBCC;
    }

    public void setToBCC(String toBCC) {
        this.toBCC = toBCC;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void load(@NonNull DatabaseWrapper wrapper) {
        super.load(wrapper);
    }

    @Override
    public boolean save() {
        return super.save();
    }

    @Override
    public boolean save(@NonNull DatabaseWrapper databaseWrapper) {
        return super.save(databaseWrapper);
    }

    @Override
    public boolean delete() {
        return super.delete();
    }

    @Override
    public boolean delete(@NonNull DatabaseWrapper databaseWrapper) {
        return super.delete(databaseWrapper);
    }

    @Override
    public boolean update() {
        return super.update();
    }

    @Override
    public boolean update(@NonNull DatabaseWrapper databaseWrapper) {
        return super.update(databaseWrapper);
    }

    @Override
    public long insert() {
        return super.insert();
    }

    @Override
    public long insert(DatabaseWrapper databaseWrapper) {
        return super.insert(databaseWrapper);
    }

    @Override
    public boolean exists() {
        return super.exists();
    }

    @Override
    public boolean exists(@NonNull DatabaseWrapper databaseWrapper) {
        return super.exists(databaseWrapper);
    }

    @NonNull
    @Override
    public AsyncModel<? extends Model> async() {
        return super.async();
    }

    @Override
    public ModelAdapter getModelAdapter() {
        return super.getModelAdapter();
    }
}
