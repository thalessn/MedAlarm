package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utils {

    public static Calendar DataStringToCalendar(String data){
        SimpleDateFormat sdf = null;
        if(data.contains("/")){
            sdf =  new SimpleDateFormat("yyyy/MM/dd");
        }else{
            sdf =  new SimpleDateFormat("yyyy-MM-dd");
        }

        Calendar cal = Calendar.getInstance();
        try{

            cal.setTime(sdf.parse(data));
        }catch (ParseException e){
            e.printStackTrace();
        }
        return cal;
    }



    public static String CalendarToStringData(Calendar calendar){
        //Para formatar a data de acordo com o banco de dados
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Passa para String a data no calendário
        String data = sdf.format(calendar.getTime());

        return data;
    }

    public static String CalendarToStringHora(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horario = sdf.format(calendar.getTime());

        return horario;
    }

    public static Calendar StringHoraToCalendar(String hora){
        Calendar cal = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH : mm");
            cal.setTime(sdf.parse(hora));
            cal.set(Calendar.YEAR, c.get(Calendar.YEAR));
            cal.set(Calendar.MONTH, c.get(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return cal;

    }

    public static Calendar DataDiaMesAnoToCalendar(String data){
        Calendar cal = Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            cal.setTime(sdf.parse(data));
        }catch (ParseException e){
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * @return {@code true} if the device is {@link Build.VERSION_CODES#M} or later
     */
    public static boolean isMOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @return {@code true} if the device is {@link Build.VERSION_CODES#LOLLIPOP} or later
     */
    public static boolean isLOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String formataDataBrasil(int day, int month, int year){
        String dia = day < 10 ? ("0" + String.valueOf(day)) : String.valueOf(day);
        String mes = (month+1) < 10 ? ("0" + String.valueOf(month+1)) : String.valueOf(month+1);
        String ano = String.valueOf(year);
        String data = dia +"/"+ mes +"/"+ ano;
        return data;
    }

    public static String formataDataBrasil(String data){
        String []split = data.split("-");
        String dataFinal = split[2] + "/" +split[1] + "/" +split[0];
//        StringBuilder result = new StringBuilder(data);
//        result.reverse();
//        String dataFinal = result.toString();
//        dataFinal.replace("-","/");

        return dataFinal;
    }

    public static String formataDataUTC(String data){
        String a = "10/10/2018";
        String [] d = data.split("/");
        String result = d[2]+"-"+d[1]+"-"+d[0];
        return result;

    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(byte[] data, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static String CalendarToStringFormatada(Calendar cal){

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yy");


        SimpleDateFormat diaSemana = new SimpleDateFormat("EEE");
        String texto = diaSemana.format(cal.getTime()) +", "+ dia.format(cal.getTime()) +" de "+ mes.format(cal.getTime())+" "+ano.format(cal.getTime());


        return texto;
    }

}
