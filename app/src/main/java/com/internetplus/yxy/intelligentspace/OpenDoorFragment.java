package com.internetplus.yxy.intelligentspace;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.dalimao.corelibrary.VerificationCodeInput;

/**
 * Created by Y.X.Y on 2017/4/19 0019.
 */
public class OpenDoorFragment extends Fragment{

    private VerificationCodeInput key;

    private StringBuilder inputkey;

    private Button submit, crashhandler;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opendoor, container, false);
        key = (VerificationCodeInput) view.findViewById(R.id.opendoorkey);
        key.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String s) {
                inputkey = new StringBuilder();
                inputkey.append(s);
            }
        });
        submit = (Button) view.findViewById(R.id.opendoorsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getkey = "";
                if (inputkey != null) getkey = inputkey.toString();
                if ("123456".equals(getkey)) {
                    OpenDoor.opendoor(getActivity());
                } else {
                    Util.showToast(getActivity(), "密码错误");
                    key.setEnabled(true);
                }
            }
        });

        crashhandler = (Button) view.findViewById(R.id.crashhandler);
        crashhandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
        Util.deleteToast();
    }
}
