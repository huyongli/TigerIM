package cn.ittiger.im.ui;

import cn.ittiger.im.R;
import cn.ittiger.im.activity.ChatActivity;
import cn.ittiger.im.bean.ContactEntity;
import cn.ittiger.im.util.IntentHelper;
import cn.ittiger.util.ActivityUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * 联系人列表
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class ChatDialog {

    private AlertDialog mDialog;
    private Context mContext;

    public ChatDialog(Context context) {

        mContext = context;
    }

    public void show(final ContactEntity contactEntity) {

        String message = String.format(mContext.getString(R.string.chat_dialog_message), contactEntity.getRosterEntry().getName());

        if(mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.chat_dialog_title))
                    .setCancelable(true)
                    .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            intent.putExtra(IntentHelper.KEY_CHAT_NAME, contactEntity.getRosterEntry().getName());
                            intent.putExtra(IntentHelper.KEY_CHAT_USER, contactEntity.getRosterEntry().getUser());
                            ActivityUtil.startActivity(mContext, intent);
                        }
                    });
            mDialog = builder.create();
        }
        mDialog.setMessage(message);
        mDialog.show();
    }


}
