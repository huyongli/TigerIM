package cn.ittiger.im.adapter;

import cn.ittiger.im.R;
import cn.ittiger.im.adapter.viewholder.ChatRecordViewHolder;
import cn.ittiger.im.bean.ChatRecord;
import cn.ittiger.im.constant.EmotionType;
import cn.ittiger.im.ui.recyclerview.HeaderAndFooterAdapter;
import cn.ittiger.im.ui.recyclerview.ViewHolder;
import cn.ittiger.im.util.ChatTimeUtil;
import cn.ittiger.im.util.EmotionUtil;
import cn.ittiger.im.util.ImageLoaderHelper;
import cn.ittiger.util.ValueUtil;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.R.id.message;

/**
 * 聊天记录列表适配器
 * @author: laohu on 2017/1/22
 * @site: http://ittiger.cn
 */
public class ChatRecordAdapter extends HeaderAndFooterAdapter<ChatRecord> {
    private Context mContext;

    public ChatRecordAdapter(Context context, List<ChatRecord> list) {

        super(list);
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_record_item_layout, parent, false);
        return new ChatRecordViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, ChatRecord item) {

        ChatRecordViewHolder viewHolder = (ChatRecordViewHolder) holder;

        if(!ValueUtil.isEmpty(item.getFriendAvatar())) {
            ImageLoaderHelper.displayImage(viewHolder.avatar, item.getFriendAvatar());
        }
        viewHolder.nickName.setText(item.getFriendNickname());
        if(!ValueUtil.isEmpty(item.getLastMessage())) {
            if(viewHolder.message.getVisibility() == View.GONE) {
                viewHolder.message.setVisibility(View.VISIBLE);
            }
            SpannableString content = EmotionUtil.getInputEmotionContent(mContext, EmotionType.EMOTION_TYPE_CLASSIC, viewHolder.message, item.getLastMessage());
            viewHolder.message.setText(content);
        }
        viewHolder.chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(item.getChatTime()));
        String messageCount = item.getUnReadMessageCount() > 0 ? String.valueOf(item.getUnReadMessageCount()) : "";
        viewHolder.messageCount.setText(messageCount);
    }
}
