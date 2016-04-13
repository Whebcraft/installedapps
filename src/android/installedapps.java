package com.awaa.installedapps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class installedapps extends CordovaPlugin { 
	
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
      
        if (action.equals("getIcon")) {
        	final String packageName = args.getJSONObject(0).getString("package");
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    } 
                        
                    File iconDir = new File(appDir+"/icons");
                    if(!iconDir.exists()){
                        iconDir.mkdirs();
                    }
                    //end setup
                    
        			try {
        				ApplicationInfo choosenApp = pm.getApplicationInfo(packageName, 128);		
                        Drawable appIcon = choosenApp.loadIcon(pm);             
                        
                        File file = new File(iconDir, "/"+packageName+".png");

                        FileOutputStream foStream = null;

                        Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();

                        ByteArrayOutputStream oStream = new ByteArrayOutputStream();  
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //bm is the bitmap object   

                        byte[]	b = oStream.toByteArray();				
                        try {
                            foStream = new FileOutputStream(file);
                            oStream.write(b);
                            oStream.writeTo(foStream);
                            oStream.close();
                            foStream.close();					
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        appIcon = null;
                        file = null;
                        b = null;
                        foStream = null;
                        oStream = null;
                        bitmap = null;
        		        try {
							callbackContext.success(new JSONObject().put("returnVal", "file://"+appDir+"/icons/"+ packageName+".png"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
        			} catch (NameNotFoundException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}               	
                	
                }
            });
            return true;
        }        
        
        if (action.equals("getIcons")) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {

                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    } 
                        
                    File iconDir = new File(appDir+"/icons");
                    if(!iconDir.exists()){
                        iconDir.mkdirs();
                    }
                    //end setup
                    
                    JSONArray jArray = new JSONArray();
                    
                    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                    for (ApplicationInfo packageInfo : packages) {

                        Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);
                        try {
                        	
                            if(appActivity != null){
                                String pkgName = packageInfo.packageName;		
                                String appName = packageInfo.loadLabel(pm).toString();
                                
                                
                                //start icon
                                Drawable appIcon = packageInfo.loadIcon(pm);                                                      
                                File file = new File(iconDir, "/"+pkgName+".png");
                                FileOutputStream foStream = null;
                                Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();
                                ByteArrayOutputStream oStream = new ByteArrayOutputStream();  
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //bm is the bitmap object   
                                byte[]	b = oStream.toByteArray();				
                                try {
                                    foStream = new FileOutputStream(file);
                                    oStream.write(b);
                                    oStream.writeTo(foStream);
                                    oStream.close();
                                    foStream.close();					
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                appIcon = null;
                                file = null;
                                b = null;
                                foStream = null;
                                oStream = null;
                                bitmap = null;
                                //end icon
                                
                 
                                String[] appIntent = appActivity.toString().split("/");

                                String appIFormated = appIntent[1].substring(0, appIntent[1].length() - 2);

                                JSONObject json = new JSONObject();
								json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file://"+appDir+"/icons/"+ pkgName+".png");
								jArray.put(json);
                            }
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}							
                    }
                    packages = null;
                    System.gc();
                    String results = jArray.toString();
                    try {
						callbackContext.success(new JSONObject().put("returnVal", results));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                  	
                	
                	
                }
            });
            return true;
        }
            
        if (action.equals("getApps")) {         
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	
                	
                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    }    
                    //end setup            	
                	
                	 JSONArray jArray = new JSONArray();
                	 JSONArray jArray2 = new JSONArray();
					 JSONObject json = new JSONObject();

                     List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                     for (ApplicationInfo packageInfo : packages) {

                         try {
                             String pkgName = packageInfo.packageName;

                             Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);

                             

                             //Code to send package information to Eclipse Log.
                             //Log.d(id, "Name:" + appName);
                             //Log.d(id, "Package:" + pkgName);
                             //Log.d(id, "Activity:" + appActivity);
 
                             if(appActivity != null){
                                 String appName = packageInfo.loadLabel(pm).toString();
                                 String[] appIntent = appActivity.toString().split("/");

                                 String appIFormated = appIntent[1].substring(0, appIntent[1].length() - 2);

                                
								 
    if(pkgName == "com.chronicles.sb"){}
	else if(pkgName == "com.smartOS"){}
    else if(pkgName == "com.extwebtech.bibliography"){}
    else if(pkgName == "com.Phaedrus.EarwigFree"){}
    else if(pkgName == "com.funbox.englishidioms"){}
    else if(pkgName == "binh.app.englishidiom"){}
    else if(pkgName == "air.com.revisionbuddies.gcsemaths"){}
    else if(pkgName == "com.educationapps.gojimo"){}
    else if(pkgName == "com.solvus_lab.android.BibleEN_kjv"){}
    else if(pkgName == "org.khanacademy.android"){}
    else if(pkgName == "com.learnerscloud.igcsemaths.videos"){}
    else if(pkgName == "org.zezi.gb"){}
    else if(pkgName == "com.mobisystems.msdict.embedded.wireless.oxford.dictionaryofenglish"){}
    else if(pkgName == "com.aspartame.RemindMe"){}
    else if(pkgName == "com.zapp2learn.app"){}
    else if(pkgName == "com.kuntec.braincafe"){}
    else if(pkgName == "com.icenta.sudoku.ui"){}
    else if(pkgName == "com.underwater.geoquiz"){}
    else if(pkgName == "org.pondar.mathquizfree"){}
    else if(pkgName == "air.com.panapps.PeriodicTable"){}
    else if(pkgName == "com.microblink.photomath"){}
    else if(pkgName == "com.physics.quiz.game1"){}
    else if(pkgName == "com.codeansoftware.speakforme"){}
    else if(pkgName == "org.urbian.android.quiz.bible.demo"){}
    else if(pkgName == "com.socratica.mobile.words"){}
    else if(pkgName == "lammar.flags"){}
    else if(pkgName == "org.unver.derstakvimi"){}
    else if(pkgName == "com.learningmate.goclass.view"){}
    else if(pkgName == "org.edx.mobile"){}
    else if(pkgName == "de.rakuun.MyClassSchedule.free"){}
    else if(pkgName == "com.speedAnatomy.speedAnatomyLite"){}
    else if(pkgName == "com.studyblue"){}
    else if(pkgName == "com.class123.teacher"){}
    else if(pkgName == "com.udacity.android"){}
    else if(pkgName == "com.udemy.android"){}
    else if(pkgName == "bbc.mobile.news.ww"){}
    else if(pkgName == "com.google.android.apps.blogger"){}
    else if(pkgName == "com.cnn.mobile.android.phone"){}
    else if(pkgName == "com.google.android.apps.plus"){}
    else if(pkgName == "com.facebook.katana"){}
    else if(pkgName == "com.google.android.youtube"){}
    else if(pkgName == "com.skype.raider"){}
    else if(pkgName == "com.twitter.android"){}
    else if(pkgName == "com.whatsapp"){}
    else if(pkgName == "com.mybio.apps"){}
    else if(pkgName == "com.school.app"){}
    else if(pkgName == "com.flyersoft.moonreader"){}
    else if(pkgName == "com.hed.app"){}
    else if(pkgName == "com.app.dailymanna2015"){}
    else if(pkgName == "com.youth.life"){}	
    else if(pkgName == "com.google.android.apps.docs.editors.docs"){}
    else if(pkgName == "ng.com.efiko.quiz"){}
    else if(pkgName == "com.google.android.apps.classroom"){}
    else if(pkgName == "com.gidimo"){}
    else if(pkgName == "com.google.android.apps.docs.editors.sheets"){}
    else if(pkgName == "com.twitter.android"){}
    else if(pkgName == "com.whatsapp"){}
    else if(pkgName == "com.facebook.katana"){}
    else if(pkgName == "com.linkedin.android"){}
    else if(pkgName == "com.google.android.apps.plus"){}
    else if(pkgName == "com.bbm"){}
    else if(pkgName == "com.tencent.mm"){}
    else if(pkgName == "com.skype.raider"){}
    else if(pkgName == "com.sbreader"){}
	else {
		json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file://"+appDir+"/icons/"+ pkgName+".png");
        jArray.put(json);
	}
								 
                             }
							 
														 

                         } catch (Exception e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
                         }

                     }// Turns the JSON array into a string and returns the value. 

                     // String results = jArray.toString();				
                     String results = jArray.toString();				

                     try {
						callbackContext.success(new JSONObject().put("returnVal", results));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
                	
                }
            });
            return true;
        }      

        return true;       
	}  
}
