package com.example.xu.mmiphone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;

public class MMIPhone {

    private static final int MSG_CALI = 1;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            switch (msg.what){
                case MSG_CALI:
                    if (null == ar.exception) {
                        String[] receiveDate = (String[]) ar.result;
                        if(receiveDate!=null && receiveDate.length!=0 && receiveDate[0]!=null){
                            if (receiveDate[0].length() > 8){
                                //+EGMR: ""
                                String str = receiveDate[0].substring(8,receiveDate[0].length()-1);
                                if ("".equals(str)) {
                                    SystemProperties.set("gsm.mmi.cali", "SPACE");
                                }else {
                                    SystemProperties.set("gsm.mmi.cali", str);
                                }
                            }else {
                                SystemProperties.set("gsm.mmi.cali", receiveDate[0]);
                            }
                        }else{
                            SystemProperties.set("gsm.mmi.cali","NODATA");
                        }
                    }else{
                        SystemProperties.set("gsm.mmi.cali","EXC");
                    }
                    break;

                    default:
                        break;
            }
        }
    };

    public void getCalibrate(){
        Phone gsmPhone = null;
        String str[] = new String[2];
        str[0] = "AT+EGMR=0,5";
        str[1] = "+EGMR";
        Phone[] phones = PhoneFactory.getPhones();
        for (Phone phone : phones){
            if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_GSM){
                gsmPhone = phone;
                break;
            }
        }

        if (gsmPhone != null){
            gsmPhone.invokeOemRilRequestStrings(str, myHandler
                    .obtainMessage(MSG_CALI));
        }else{
            SystemProperties.set("gsm.mmi.cali","NOGSM");
        }

    }
}
