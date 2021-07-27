package com.fbreader.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.king.zxing.util.CodeUtils;
import org.geometerplus.zlibrary.ui.android.R;

public class BookQrCodeDialog extends Dialog {

    ImageView ivQrcode;
    String qrCodeLink;

    private OnClickCancelListener onClickCancelListener;


    public BookQrCodeDialog(@NonNull Context context) {
        super(context);
    }

    public BookQrCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BookQrCodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BookQrCodeDialog(@NonNull Context context,String qrCodeLink) {
        super(context);
        this.qrCodeLink = qrCodeLink;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_book_qrcode);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params .width = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_qrcode_w);
        params .height = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_qrcode_h);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        ivQrcode = findViewById(R.id.iv_qrcode);
        Bitmap bitmap = CodeUtils.createQRCode(qrCodeLink,params.height,null);
        ivQrcode.setImageBitmap(bitmap);
    }

    public OnClickCancelListener getOnClickCancelListener() {
        return onClickCancelListener;
    }

    public BookQrCodeDialog setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
        return this;
    }

    public interface OnClickCancelListener {
        void onClick(View view);
    }
}
