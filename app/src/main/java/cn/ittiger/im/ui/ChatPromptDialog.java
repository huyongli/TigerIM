package cn.ittiger.im.ui;

import cn.ittiger.im.R;
import cn.ittiger.im.bean.ContactEntity;
import cn.ittiger.im.util.IMUtil;
import cn.ittiger.im.util.IntentHelper;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * 联系人列表
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class ChatPromptDialog implements DialogInterface.OnClickListener {

    private AlertDialog mDialog;
    private Context mContext;
    private ContactEntity mContactEntity;

    public ChatPromptDialog(Context context) {

        mContext = context;
    }

    public void show(ContactEntity contactEntity) {

        mContactEntity = contactEntity;
        String message = String.format(mContext.getString(R.string.chat_dialog_message), contactEntity.getRosterEntry().getName());

        if(mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.chat_dialog_title))
                    .setCancelable(true)
                    .setNegativeButton(mContext.getString(R.string.cancel), this)
                    .setPositiveButton(mContext.getString(R.string.ok), this);
            mDialog = builder.create();
        }
        mDialog.setMessage(message);
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if(which == DialogInterface.BUTTON_POSITIVE) {
            dialog.dismiss();
            IMUtil.startChatActivity(mContext, mContactEntity.getRosterEntry());
            EventBus.getDefault().post(Integer.valueOf(IntentHelper.MESSAGE_TAB_INDEX));
            mContactEntity = null;
        } else if(which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }
}
