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

                                 JSONObject json = new JSONObject();
                                 json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file://"+appDir+"/icons/"+ pkgName+".png");				                            
		jArray.put(json);
								 
								 for(int i=0; i < jArray.length; i++){
    if(jArray[i][2] == 'com.chronicles.sb'){}
	else if(jArray[i][2] == 'com.smartOS'){}
    else if(jArray[i][2] == 'com.extwebtech.bibliography'){}
    else if(jArray[i][2] == 'com.Phaedrus.EarwigFree'){}
    else if(jArray[i][2] == 'com.funbox.englishidioms'){}
    else if(jArray[i][2] == 'binh.app.englishidiom'){}
    else if(jArray[i][2] == 'air.com.revisionbuddies.gcsemaths'){}
    else if(jArray[i][2] == 'com.educationapps.gojimo'){}
    else if(jArray[i][2] == 'com.solvus_lab.android.BibleEN_kjv'){}
    else if(jArray[i][2] == 'org.khanacademy.android'){}
    else if(jArray[i][2] == 'com.learnerscloud.igcsemaths.videos'){}
    else if(jArray[i][2] == 'org.zezi.gb'){}
    else if(jArray[i][2] == 'com.mobisystems.msdict.embedded.wireless.oxford.dictionaryofenglish'){}
    else if(jArray[i][2] == 'com.aspartame.RemindMe'){}
    else if(jArray[i][2] == 'com.zapp2learn.app'){}
    else if(jArray[i][2] == 'com.kuntec.braincafe'){}
    else if(jArray[i][2] == 'com.icenta.sudoku.ui'){}
    else if(jArray[i][2] == 'com.underwater.geoquiz'){}
    else if(jArray[i][2] == 'org.pondar.mathquizfree'){}
    else if(jArray[i][2] == 'air.com.panapps.PeriodicTable'){}
    else if(jArray[i][2] == 'com.microblink.photomath'){}
    else if(jArray[i][2] == 'com.physics.quiz.game1'){}
    else if(jArray[i][2] == 'com.codeansoftware.speakforme'){}
    else if(jArray[i][2] == 'org.urbian.android.quiz.bible.demo'){}
    else if(jArray[i][2] == 'com.socratica.mobile.words'){}
    else if(jArray[i][2] == 'lammar.flags'){}
    else if(jArray[i][2] == 'org.unver.derstakvimi'){}
    else if(jArray[i][2] == 'com.learningmate.goclass.view'){}
    else if(jArray[i][2] == 'org.edx.mobile'){}
    else if(jArray[i][2] == 'de.rakuun.MyClassSchedule.free'){}
    else if(jArray[i][2] == 'com.speedAnatomy.speedAnatomyLite'){}
    else if(jArray[i][2] == 'com.studyblue'){}
    else if(jArray[i][2] == 'com.class123.teacher'){}
    else if(jArray[i][2] == 'com.udacity.android'){}
    else if(jArray[i][2] == 'com.udemy.android'){}
    else if(jArray[i][2] == 'bbc.mobile.news.ww'){}
    else if(jArray[i][2] == 'com.google.android.apps.blogger'){}
    else if(jArray[i][2] == 'com.cnn.mobile.android.phone'){}
    else if(jArray[i][2] == 'com.google.android.apps.plus'){}
    else if(jArray[i][2] == 'com.facebook.katana'){}
    else if(jArray[i][2] == 'com.google.android.youtube'){}
    else if(jArray[i][2] == 'com.skype.raider'){}
    else if(jArray[i][2] == 'com.twitter.android'){}
    else if(jArray[i][2] == 'com.whatsapp'){}
    else if(jArray[i][2] == 'com.mybio.apps'){}
    else if(jArray[i][2] == 'com.school.app'){}
    else if(jArray[i][2] == 'com.flyersoft.moonreader'){}
    else if(jArray[i][2] == 'com.hed.app'){}
    else if(jArray[i][2] == 'com.app.dailymanna2015'){}
    else if(jArray[i][2] == 'com.youth.life'){}	
    else if(jArray[i][2] == 'com.google.android.apps.docs.editors.docs'){}
    else if(jArray[i][2] == 'ng.com.efiko.quiz'){}
    else if(jArray[i][2] == 'com.google.android.apps.classroom'){}
    else if(jArray[i][2] == 'com.gidimo'){}
    else if(jArray[i][2] == 'com.google.android.apps.docs.editors.sheets'){}
    else if(jArray[i][2] == 'com.twitter.android'){}
    else if(jArray[i][2] == 'com.whatsapp'){}
    else if(jArray[i][2] == 'com.facebook.katana'){}
    else if(jArray[i][2] == 'com.linkedin.android'){}
    else if(jArray[i][2] == 'com.google.android.apps.plus'){}
    else if(jArray[i][2] == 'com.bbm'){}
    else if(jArray[i][2] == 'com.tencent.mm'){}
    else if(jArray[i][2] == 'com.skype.raider'){}
    else if(jArray[i][2] == 'com.sbreader'){}
	else {
		//jArray2.put(jArray[i]);
		jArray2[jArray2.length] = jArray[i];
	}
								 }
                             }
							 
														 

                         } catch (Exception e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
                         }

                     }// Turns the JSON array into a string and returns the value. 

                     // String results = jArray.toString();				
                     String results = jArray2.toString();				

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
