package com.example.rubbishcommunity.manager.base;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rubbishcommunity.LocationOutClass;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Base64;


public class Test {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {


        String str = "CexRuB6FY2pAEexRuB6FA2lAGgVDaGluYSIG5Zub5bedKgdDaGVuZ2R1Ognpq5jmlrDljLpAoI0G";
        //String str64 = new String(str.getBytes(), StandardCharsets.UTF_8);
        //Base64.getDecoder().decode(str)
        try {
            LocationOutClass.Location parseFrom = LocationOutClass.Location.parseFrom(
                    Base64.getDecoder().decode(str)
            );
            System.out.println(parseFrom.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


 /*   public void writeObj() throws Exception {
        LocationOuterClass.Location location = LocationOuterClass.Location.newBuilder().setCity("AAAAAA").setCountry("BBBB").build();
        location.writeTo(new FileOutputStream("prototbuf.data"));
    }


    public void readObj() throws Exception {
        LocationOuterClass.Location location = LocationOuterClass.Location.parseFrom(new FileInputStream("prototbuf.data"));
        System.out.println(String.format("city=%s  country=%s", location.getCity(), location.getCountry()));
    }

*/
}

