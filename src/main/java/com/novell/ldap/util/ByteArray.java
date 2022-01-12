package com.novell.ldap.util;

import java.io.UnsupportedEncodingException;

public class ByteArray {
    byte[] value;

    public ByteArray(byte[] value) {
        this.value = value;
    }

    public ByteArray(String value) {
        try {
            this.value = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
           // can't happen
        }
    }

    public byte[] getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof ByteArray)) {
            return false;
        } else {
            ByteArray ba = (ByteArray) o;

            if (ba.value.length != value.length) {
                return false;
            }

            for (int i = 0;i < value.length;i++) {
                if (this.value[i] != ba.value[i]) {
                    return false;
                }
            }

            return true;
        }

    }

    @Override
    public String toString() {
        try {
            return new String(this.value,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            //can't happen
            return null;
        }
        
    }
}
