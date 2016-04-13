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
    if(jArray[i].package == 'com.chronicles.sb'){}
	else if(jArray[i].package == 'com.smartOS'){}
    else if(jArray[i].package == 'com.extwebtech.bibliography'){}
    else if(jArray[i].package == 'com.Phaedrus.EarwigFree'){}
    else if(jArray[i].package == 'com.funbox.englishidioms'){}
    else if(jArray[i].package == 'binh.app.englishidiom'){}
    else if(jArray[i].package == 'air.com.revisionbuddies.gcsemaths'){}
    else if(jArray[i].package == 'com.educationapps.gojimo'){}
    else if(jArray[i].package == 'com.solvus_lab.android.BibleEN_kjv'){}
    else if(jArray[i].package == 'org.khanacademy.android'){}
    else if(jArray[i].package == 'com.learnerscloud.igcsemaths.videos'){}
    else if(jArray[i].package == 'org.zezi.gb'){}
    else if(jArray[i].package == 'com.mobisystems.msdict.embedded.wireless.oxford.dictionaryofenglish'){}
    else if(jArray[i].package == 'com.aspartame.RemindMe'){}
    else if(jArray[i].package == 'com.zapp2learn.app'){}
    else if(jArray[i].package == 'com.kuntec.braincafe'){}
    else if(jArray[i].package == 'com.icenta.sudoku.ui'){}
    else if(jArray[i].package == 'com.underwater.geoquiz'){}
    else if(jArray[i].package == 'org.pondar.mathquizfree'){}
    else if(jArray[i].package == 'air.com.panapps.PeriodicTable'){}
    else if(jArray[i].package == 'com.microblink.photomath'){}
    else if(jArray[i].package == 'com.physics.quiz.game1'){}
    else if(jArray[i].package == 'com.codeansoftware.speakforme'){}
    else if(jArray[i].package == 'org.urbian.android.quiz.bible.demo'){}
    else if(jArray[i].package == 'com.socratica.mobile.words'){}
    else if(jArray[i].package == 'lammar.flags'){}
    else if(jArray[i].package == 'org.unver.derstakvimi'){}
    else if(jArray[i].package == 'com.learningmate.goclass.view'){}
    else if(jArray[i].package == 'org.edx.mobile'){}
    else if(jArray[i].package == 'de.rakuun.MyClassSchedule.free'){}
    else if(jArray[i].package == 'com.speedAnatomy.speedAnatomyLite'){}
    else if(jArray[i].package == 'com.studyblue'){}
    else if(jArray[i].package == 'com.class123.teacher'){}
    else if(jArray[i].package == 'com.udacity.android'){}
    else if(jArray[i].package == 'com.udemy.android'){}
    else if(jArray[i].package == 'bbc.mobile.news.ww'){}
    else if(jArray[i].package == 'com.google.android.apps.blogger'){}
    else if(jArray[i].package == 'com.cnn.mobile.android.phone'){}
    else if(jArray[i].package == 'com.google.android.apps.plus'){}
    else if(jArray[i].package == 'com.facebook.katana'){}
    else if(jArray[i].package == 'com.google.android.youtube'){}
    else if(jArray[i].package == 'com.skype.raider'){}
    else if(jArray[i].package == 'com.twitter.android'){}
    else if(jArray[i].package == 'com.whatsapp'){}
    else if(jArray[i].package == 'com.mybio.apps'){}
    else if(jArray[i].package == 'com.school.app'){}
    else if(jArray[i].package == 'com.flyersoft.moonreader'){}
    else if(jArray[i].package == 'com.hed.app'){}
    else if(jArray[i].package == 'com.app.dailymanna2015'){}
    else if(jArray[i].package == 'com.youth.life'){}	
    else if(jArray[i].package == 'com.google.android.apps.docs.editors.docs'){}
    else if(jArray[i].package == 'ng.com.efiko.quiz'){}
    else if(jArray[i].package == 'com.google.android.apps.classroom'){}
    else if(jArray[i].package == 'com.gidimo'){}
    else if(jArray[i].package == 'com.google.android.apps.docs.editors.sheets'){}
    else if(jArray[i].package == 'com.twitter.android'){}
    else if(jArray[i].package == 'com.whatsapp'){}
    else if(jArray[i].package == 'com.facebook.katana'){}
    else if(jArray[i].package == 'com.linkedin.android'){}
    else if(jArray[i].package == 'com.google.android.apps.plus'){}
    else if(jArray[i].package == 'com.bbm'){}
    else if(jArray[i].package == 'com.tencent.mm'){}
    else if(jArray[i].package == 'com.skype.raider'){}
    else if(jArray[i].package == 'com.sbreader'){}
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
