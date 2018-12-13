package com.androidsrc.server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Function {
    public static final int FILE_DELETE = 2;
    public static final int FILE_LOAD = 0;
    public static final int FILE_SHERE = 1;
    public static final int MESSAGE_LOAD = 10;
    private static final String TAG = "Function";
    private static String fileName;
    private static AlertDialog m_Dlg;

    static /* synthetic */ String access$000() {
        return fileName;
    }

    static /* synthetic */ String access$002(String string2) {
        fileName = string2;
        return string2;
    }

    static /* synthetic */ AlertDialog access$100() {
        return m_Dlg;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static byte[] asToHex(String string2) {
        int n = string2.length();
        int n2 = (int) Math.ceil((double) ((double) n / 2.0));
        byte[] arrby = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            if (2 + n3 * 2 <= n) {
                int n4 = n3 * 2;
                int n5 = 2 + n3 * 2;

                BigInteger bigint = new BigInteger(string2.substring(n4, n5) + "", 16);
                Log.d((String) "TAG", bigint.intValue() + " BIGBYTE");
                arrby[n3] = (byte) Integer.parseInt((String) string2.substring(n4, n5), (int) 16);
//                arrby[n3] = (byte) Integer.parseInt(bigint + "");//Integer.parseInt((String) string2.substring(n4, n5), (int) 16);


            } else {
                int n6 = n3 * 2;
                int n7 = 1 + n3 * 2;
                try {
//                    BigInteger bigint = new BigInteger(string2.substring(n6, n7) + "", 16);
//                    arrby[n3] = (byte) Integer.parseInt(bigint + "");
                    arrby[n3] = (byte) Integer.parseInt((String) string2.substring(n6, n7), (int) 16);
                } catch (NumberFormatException var7_8) {
                    var7_8.printStackTrace();
                }
            }
            Log.d((String) "TAG", (String) ("HEX" + arrby[n3]));
            ++n3;
        }

        return arrby;
    }


    private static byte[] hexToBytes(char[] hex) {
        byte[] raw = new byte[hex.length / 2];
        for (int src = 0, dst = 0; dst < raw.length; ++dst) {
            int hi = Character.digit(hex[src++], 16);
            int lo = Character.digit(hex[src++], 16);
            if ((hi < 0) || (lo < 0))
                throw new IllegalArgumentException();
            raw[dst] = (byte) (hi << 4 | lo);
        }
        return raw;
    }

    public static byte[] decode(String input) {
        BigInteger bi = BigInteger.ZERO;
        final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        final BigInteger BASE = BigInteger.valueOf(ALPHABET.length());


        for (int i = input.length() - 1; i >= 0; i--) {
            int index = ALPHABET.indexOf(input.charAt(i));
            if (index == -1) {
                throw new IllegalArgumentException();
            }
            bi = bi.add(BASE.pow(input.length() - 1 - i).multiply(BigInteger.valueOf(index)));
        }
        byte[] bytes = bi.toByteArray();
        boolean stripSignByte = bytes.length > 1 && bytes[0] == 0 && bytes[1] < 0;
        int leadingZeros = 0;
        for (; leadingZeros < input.length() && input.charAt(leadingZeros) == ALPHABET.charAt(0); leadingZeros++)
            ;
        byte[] tmp = new byte[bytes.length - (stripSignByte ? 1 : 0) + leadingZeros];
        System.arraycopy(bytes, stripSignByte ? 1 : 0, tmp, leadingZeros, tmp.length - leadingZeros);
        return tmp;
    }


    public static final String[] toByteArray(String string2) {
        ArrayList<String> data = new ArrayList<>();
        int n = string2.length();
        int n2 = (int) Math.ceil((double) ((double) n / 2.0));
        byte[] arrby = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            if (2 + n3 * 2 <= n) {
                int n4 = n3 * 2;
                int n5 = 2 + n3 * 2;

                BigInteger bigint = new BigInteger(string2.substring(n4, n5) + "", 16);
                Log.d((String) "TAG", bigint.intValue() + " BIGBYTE");
                data.add(bigint + "");
                arrby[n3] = (byte) Integer.parseInt((String) string2.substring(n4, n5), (int) 16);
//                arrby[n3] = (byte) Integer.parseInt(bigint + "");//Integer.parseInt((String) string2.substring(n4, n5), (int) 16);


            } else {
                int n6 = n3 * 2;
                int n7 = 1 + n3 * 2;
                try {
                    BigInteger bigint = new BigInteger(string2.substring(n6, n7) + "", 16);
                    data.add(bigint + "");
//                    arrby[n3] = (byte) Integer.parseInt(bigint + "");
                    arrby[n3] = (byte) Integer.parseInt((String) string2.substring(n6, n7), (int) 16);
                } catch (NumberFormatException var7_8) {
                    var7_8.printStackTrace();
                }
            }
            Log.d((String) "TAG", (String) ("HEX" + arrby[n3]));
            ++n3;
        }

        String[] data1 = data.toArray(new String[data.size()]);

        return data1;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static String getHexString(byte[] bytes) {

        final char[] BYTE2HEX = (
                "000102030405060708090A0B0C0D0E0F" +
                        "101112131415161718191A1B1C1D1E1F" +
                        "202122232425262728292A2B2C2D2E2F" +
                        "303132333435363738393A3B3C3D3E3F" +
                        "404142434445464748494A4B4C4D4E4F" +
                        "505152535455565758595A5B5C5D5E5F" +
                        "606162636465666768696A6B6C6D6E6F" +
                        "707172737475767778797A7B7C7D7E7F" +
                        "808182838485868788898A8B8C8D8E8F" +
                        "909192939495969798999A9B9C9D9E9F" +
                        "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF" +
                        "B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF" +
                        "C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF" +
                        "D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF" +
                        "E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF" +
                        "F0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF").toCharArray();
        ;

        final int len = bytes.length;
        final char[] chars = new char[len << 1];
        int hexIndex;
        int idx = 0;
        int ofs = 0;
        while (ofs < len) {
            hexIndex = (bytes[ofs++] & 0xFF) << 1;
            chars[idx++] = BYTE2HEX[hexIndex++];
            chars[idx++] = BYTE2HEX[hexIndex];
        }
        return new String(chars);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    static byte[] toByteArray8(String s) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.asLongBuffer().put(Long.parseLong(s.substring(0, 16), 16))
                .put(Long.parseLong(s.substring(16), 16));
        return bb.array();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 8];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] HexStringToByteArray(String s) {
        byte data[] = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            data[i / 2] = (Integer.decode("0x" + s.charAt(i) + s.charAt(i + 1))).byteValue();
        }
        return data;
    }

    public static String hexToDec(String hex) {
        String HEX_DIGITS = "0123456789ABCDEF";

        char[] sources = hex.toCharArray();
        int dec = 0;
        for (int i = 0; i < sources.length; i++) {
            int digit = HEX_DIGITS.indexOf(Character.toUpperCase(sources[i]));
            dec += digit * Math.pow(16, (sources.length - (i + 1)));
        }
        return String.valueOf(dec);
    }

    public static String hexToAs(byte[] arrby, int n) {
        StringBuffer stringBuffer = new StringBuffer(n * 2);
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(Integer.toHexString((int) (255 & arrby[i])));
        }
        return stringBuffer.toString();
    }

    public static String getInputStremtoString(InputStream inputStream) throws Exception {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public static byte[] decodeUsingBigInteger(String hexString) {
        byte[] byteArray = new BigInteger(hexString, 16)
                .toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(
                    byteArray, 1, output,
                    0, output.length);
            return output;
        }

        return byteArray;
    }

    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }


    /*public static void fileControl(final Activity activity, final ArrayList<String> arrayList, final int n, final TcpClientActivty handler, String string2) {
        ArrayList arrayList2 = new ArrayList((Collection)Arrays.asList((Object[])activity.fileList()));
        ListView listView = new ListView((Context)activity);
        listView.setAdapter((ListAdapter)new ArrayAdapter((Context)activity, 17367043, (List)arrayList2));
        listView.setScrollingCacheEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            *//*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     *//*
            public void onItemClick(AdapterView<?> var1_1, View var2_3, int var3_4, long var4) {
                Function.access$002((String)((ListView)var1_1).getItemAtPosition(var3_4));
                switch (n) {
                    case 0: {
                        Function.fileLoad(activity, arrayList, Function.access$000());
                        handler.obtainMessage(10).sendToTarget();
                       break;
                    }
                    case 1: {
                        Function.fileshare(activity, arrayList, Function.access$000());
                    }
// lbl9:  3 sources:
                   *//* default: {
//                        ** GOTO lbl13
                    }
                    case 2: *//*
                }
                activity.deleteFile(Function.access$000());
lbl13: // 2 sources:
                Function.access$100().dismiss();
                Log.d((String)"Function", (String)("fileName" + Function.access$000()));
            }
        });
        m_Dlg = new AlertDialog.Builder((Context)activity).setTitle((CharSequence)("Select a " + string2 + "file")).setPositiveButton((CharSequence)"Cancel", null).setView((View)listView).create();
        m_Dlg.show();
    }
*/
    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    /*public static int fileLoad(Activity activity, ArrayList<String> arrayList, String string2) {
        BufferedReader bufferedReader;
        Log.d((String)"Function", (String)("fileSelect" + string2));
        FileInputStream fileInputStream = activity.openFileInput(string2);
        BufferedReader bufferedReader2 = bufferedReader = new BufferedReader((Reader)new InputStreamReader((InputStream)fileInputStream, "UTF-8"));
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return -1;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return -1;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            bufferedReader2 = null;
        }
        int n = 0;
        try {
            String string3;
            while ((string3 = bufferedReader2.readLine()) != null) {
                arrayList.add((Object)string3);
                ++n;
            }
        }
        catch (IOException var10_11) {
            var10_11.printStackTrace();
            return -1;
        }
        try {
            bufferedReader2.close();
            return n;
        }
        catch (IOException var13_12) {
            var13_12.printStackTrace();
            return n;
        }
    }
*/
  /*  public static boolean fileSave(final Activity activity, final ArrayList<String> arrayList, final int n, String string2) {
        final EditText editText = new EditText((Context)activity);
        new AlertDialog.Builder((Context)activity).setIcon(17301659).setTitle((CharSequence)("Input " + string2 + "File Name")).setView((View)editText).setPositiveButton((CharSequence)"OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialogInterface, int n2) {
                String string2 = editText.getText().toString();
                Function.saveFunction(activity, arrayList, n, string2);
            }
        }).setNegativeButton((CharSequence)"CANCEL", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialogInterface, int n) {
            }
        }).show();
        return true;
    }
*/
    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    /*public static int fileshare(Activity activity, ArrayList<String> arrayList, String string2) {
        BufferedReader bufferedReader;
        Log.d((String)"Function", (String)("fileSelect" + string2));
        FileInputStream fileInputStream = activity.openFileInput(string2);
        BufferedReader bufferedReader2 = bufferedReader = new BufferedReader((Reader)new InputStreamReader((InputStream)fileInputStream, "UTF-8"));
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return -1;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return -1;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            bufferedReader2 = null;
        }
        String string3 = null;
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from((Activity)activity);
        intentBuilder.startChooser();
        int n = 0;
        try {
            String string4;
            while ((string4 = bufferedReader2.readLine()) != null) {
                String string5;
                string3 = n == 0 ? string4 : (string5 = string3 + string4 + "\r\n");
                ++n;
            }
        }
        catch (IOException var12_14) {
            var12_14.printStackTrace();
            return -1;
        }
        try {
            bufferedReader2.close();
        }
        catch (IOException var14_16) {
            var14_16.printStackTrace();
        }
        intentBuilder.setChooserTitle((CharSequence)"Choose Share App");
        intentBuilder.setSubject(string2);
        Uri.fromFile((File)activity.getFileStreamPath(string2));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", string3);
        intent.setType("text/plain");
        activity.startActivity(intent);
        return n;
    }
*/


    /*
     * Enabled aggressive block sorting
     */
    static String replaceCode(String string2) {
        String string3 = "";
        int n = 0;
        while (n < string2.length()) {
            string3 = string2.charAt(n) == '\n' ? string3 + "<LF>" : (string2.charAt(n) == '\r' ? string3 + "<CR>" : string3 + string2.charAt(n));
            ++n;
        }
        return string3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean saveFunction(Activity activity, ArrayList<String> arrayList, int n, String string2) {
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        try {
            fileOutputStream = activity.openFileOutput(string2 + ".txt", 0);
        } catch (FileNotFoundException var4_11) {
            var4_11.printStackTrace();
            return false;
        }
        try {
            BufferedWriter bufferedWriter2;
            bufferedWriter = bufferedWriter2 = new BufferedWriter((Writer) new OutputStreamWriter((OutputStream) fileOutputStream, "UTF-8"));
        } catch (UnsupportedEncodingException var12_7) {
            var12_7.printStackTrace();
            bufferedWriter = null;
        }
        for (int i = 0; i < n; ++i) {
            try {
                bufferedWriter.write((String) arrayList.get(i));
            } catch (IOException var10_10) {
                var10_10.printStackTrace();
            }
            try {
                bufferedWriter.newLine();
                continue;
            } catch (IOException var11_9) {
                var11_9.printStackTrace();
            }
        }
        try {
            bufferedWriter.close();
            return true;
        } catch (IOException var9_12) {
            var9_12.printStackTrace();
            return true;
        }
    }

}