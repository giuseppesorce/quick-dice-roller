package ohm.quickdice.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ohm.quickdice.R;
import ohm.quickdice.entity.DiceBagCollection;
import ohm.quickdice.entity.DiceCollection;
import ohm.quickdice.entity.ModifierCollection;
import ohm.quickdice.entity.RollResult;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class PersistenceManager {

	private static final String TAG = "PersistenceManager";
	
	// Object for intrinsic lock
	public static final Object dataAccessLock = new Object();

	public static final String FILE_NAME_DICEBAGS = "DiceBags";
	public static final String FILE_NAME_RESULTLIST = "ResultList";
	protected static final String OBSOLETE_FILE_NAME_DICEBAG = "DiceBag";	//No longer supported since version 8
	protected static final String OBSOLETE_FILE_NAME_BONUSBAG = "BonusBag";	//No longer supported since version 8
	
	protected static final int ACTION_SAVE = 0;
	protected static final int ACTION_EXPORT = 1;
	protected static final int ACTION_LOAD = 2;
	protected static final int ACTION_IMPORT = 3;

	private Context context;
	
	public PersistenceManager(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

//	/**
//	 * Load all the Dice Bags from the device internal memory, if found.
//	 * @return An {@code ArrayList<DiceBag>} representing all the dice bags, or {@code null} if an error occurred.
//	 */
//	@Deprecated
//	public ArrayList<DiceBag> loadDiceBags() {
//		return loadOrImportDiceBags(null, ACTION_LOAD);
//	}
//
//	/**
//	 * Load all the Dice Bags from the device external memory, if found.
//	 * @param path The path where to import from
//	 * @return An {@code ArrayList<DiceBag>} representing all the dice bags, or {@code null} if an error occurred.
//	 */
//	@Deprecated
//	public ArrayList<DiceBag> importDiceBags(String path) {
//		return loadOrImportDiceBags(path, ACTION_IMPORT);
//	}
//
//	/**
//	 * Load all the Dice Bags from the device internal memory, if found.
//	 * @param path The path where to import from (used only if {@code action} == {@link #ACTION_IMPORT})
//	 * @param action Either {@link #ACTION_LOAD} or {@link #ACTION_IMPORT}
//	 * @return An {@code ArrayList<DiceBag>} representing all the dice bags, or {@code null} if an error occurred.
//	 */
//	@Deprecated
//	protected ArrayList<DiceBag> loadOrImportDiceBags(String path, int action) {
//		ArrayList<DiceBag> retVal;
//		int errorMessageResId;
//		FileInputStream fis;
//		
//		retVal = null;
//		if (action == ACTION_LOAD) {
//			errorMessageResId = R.string.err_cannot_read;
//		} else if (action == ACTION_IMPORT) {
//			errorMessageResId = R.string.err_cannot_import;
//		} else {
//			throw new IllegalArgumentException();
//		}
//
//		try {
//			synchronized (dataAccessLock) {
//				if (action == ACTION_LOAD) {
//					//Internal storage
//					fis = context.openFileInput(FILE_NAME_DICEBAGS);
//				} else {
//					//External storage
//					fis = new FileInputStream(new File(path));
//				}
//
//				retVal = SerializationManager.DiceBags(fis);
//
//				fis.close();
//			}
//		} catch (FileNotFoundException e) {
//			Log.w(TAG, "loadOrImportDiceBags", e);
//			//Should show toast only if this is not the fist attempt
//			if (action == ACTION_IMPORT) {
//				//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//				showErrorMessage(context, errorMessageResId);
//			}
//		} catch (IOException e) {
//			Log.e(TAG, "loadOrImportDiceBags", e);
//			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, errorMessageResId);
//		} catch (Exception e) {
//			Log.e(TAG, "loadOrImportDiceBags", e);
//			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, errorMessageResId);
//		}
//		
//		if (retVal != null && retVal.size() == 0) {
//			retVal = null;
//		}
//
//		return retVal;
//	}
//
//	/**
//	 * Store all the Dice Bags in the device internal memory.
//	 * @param diceBags An {@code ArrayList<DiceBag>} representing all the Dice Bags.
//	 * @return {@code true} if data where saved, {@code false} otherwise
//	 */
//	@Deprecated
//	public boolean saveDiceBags(ArrayList<DiceBag> diceBags) {
//		return saveOrExportDiceBags(diceBags, null, ACTION_SAVE);
//	}
//	
//	/**
//	 * Store all the Dice Bags in the device external memory.
//	 * @param diceBags An {@code ArrayList<DiceBag>} representing all the Dice Bags.
//	 * @param path Path where to save the dice bags data.
//	 * @return {@code true} if data where saved, {@code false} otherwise
//	 */
//	@Deprecated
//	public boolean exportDiceBags(ArrayList<DiceBag> diceBags, String path) {
//		return saveOrExportDiceBags(diceBags, path, ACTION_EXPORT);
//	}
//
//	
//	/**
//	 * Store all the Dice Bags in the device internal or external memory.
//	 * @param diceBags An ArrayList<DiceBag> representing all the Dice Bags
//	 * @param path The path where to save (used only if {@code action} == {@link #ACTION_EXPORT})
//	 * @param action Either {@link #ACTION_SAVE} or {@link #ACTION_EXPORT}
//	 * @return {@code true} if data where saved, {@code false} otherwise
//	 */
//	@Deprecated
//	protected boolean saveOrExportDiceBags(ArrayList<DiceBag> diceBags, String path, int action) {
//		boolean retVal = false;
//		int errorMessageResId;
//		FileOutputStream fos;
//
//		if (action == ACTION_SAVE) {
//			errorMessageResId = R.string.err_cannot_update;
//		} else if (action == ACTION_EXPORT) {
//			errorMessageResId = R.string.err_cannot_export;
//		} else {
//			throw new IllegalArgumentException();
//		}
//
//		try {
//			synchronized (dataAccessLock) {
//				if (action == ACTION_SAVE) {
//					//Internal storage
//					fos = context.openFileOutput(FILE_NAME_DICEBAGS, Context.MODE_PRIVATE);
//				} else {
//					//External storage
//					fos = new FileOutputStream(new File(path));
//				}
//				SerializationManager.DiceBags(fos, diceBags);
//				fos.close();
//			}
//			retVal = true;
//		} catch (FileNotFoundException e) {
//			Log.e(TAG, "saveOrExportDiceBags", e);
//			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, errorMessageResId);
//		} catch (IOException e) {
//			Log.e(TAG, "saveOrExportDiceBags", e);
//			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, errorMessageResId);
//		} catch (Exception e) {
//			Log.e(TAG, "saveOrExportDiceBags", e);
//			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, errorMessageResId);
//		}
//		return retVal;
//	}

	/* **************************************** */
	
	/**
	 * Populate the specified collection of dice bags
	 * with the data stored on the device internal memory, if found.
	 * @param diceBagCollection Collection to populate.
	 * @return {@code true} if data where correctly loaded, {@code false} if an error occurred.
	 */
	public boolean loadDiceBagCollection(DiceBagCollection diceBagCollection) {
		return loadOrImportDiceBagCollection(diceBagCollection, null, ACTION_LOAD);
	}

	/**
	 * Populate the specified collection of dice bags
	 * with the data stored on the device external memory, if found.<br />
	 * If such data where not found, the collection will be populated
	 * with default data.
	 * @param diceBagCollection Collection to populate.
	 * @param path The path where to import from
	 * @return {@code true} if data where correctly loaded, {@code false} if an error occurred.
	 */
	public boolean importDiceBagCollection(DiceBagCollection diceBagCollection, String path) {
		return loadOrImportDiceBagCollection(diceBagCollection, path, ACTION_IMPORT);
	}

	/**
	 * Load all the Dice Bags from the device internal memory, if found.
	 * @param diceBagCollection Collection to populate.
	 * @param path The path where to import from (used only if {@code action} == {@link #ACTION_IMPORT})
	 * @param action Either {@link #ACTION_LOAD} or {@link #ACTION_IMPORT}
	 * @return {@code true} if data where correctly loaded, {@code false} if an error occurred.
	 */
	protected boolean loadOrImportDiceBagCollection(DiceBagCollection diceBagCollection, String path, int action) {
		boolean retVal = false;
		int errorMessageResId;
		FileInputStream fis;
		
		if (action == ACTION_LOAD) {
			errorMessageResId = R.string.err_cannot_read;
		} else if (action == ACTION_IMPORT) {
			errorMessageResId = R.string.err_cannot_import;
		} else {
			throw new IllegalArgumentException();
		}
		
		diceBagCollection.clear();

		try {
			synchronized (dataAccessLock) {
				if (action == ACTION_LOAD) {
					//Internal storage
					fis = context.openFileInput(FILE_NAME_DICEBAGS);
				} else {
					//External storage
					fis = new FileInputStream(new File(path));
				}

				SerializationManager.DiceBagCollection(fis, diceBagCollection);

				fis.close();
			}
			
			retVal = diceBagCollection.size() > 0;
		} catch (FileNotFoundException e) {
			Log.w(TAG, "loadOrImportDiceBags", e);
			//Should show toast only if this is not the fist attempt
			if (action == ACTION_IMPORT) {
				//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
				showErrorMessage(context, errorMessageResId);
			}
		} catch (IOException e) {
			Log.e(TAG, "loadOrImportDiceBags", e);
			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
			showErrorMessage(context, errorMessageResId);
		} catch (Exception e) {
			Log.e(TAG, "loadOrImportDiceBags", e);
			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
			showErrorMessage(context, errorMessageResId);
		}
		
		return retVal;
	}

	/**
	 * Store all the Dice Bags in the device internal memory.
	 * @param diceBagCollection The collection of dice bag to store.
	 * @return {@code true} if data where saved, {@code false} otherwise
	 */
	public boolean saveDiceBagCollection(DiceBagCollection diceBagCollection) {
		return saveOrExportDiceBagCollection(diceBagCollection, null, ACTION_SAVE);
	}
	
	/**
	 * Store all the Dice Bags in the device external memory.
	 * @param diceBags An {@code ArrayList<DiceBag>} representing all the Dice Bags.
	 * @param path Path where to save the dice bags data.
	 * @return {@code true} if data where saved, {@code false} otherwise
	 */
	public boolean exportDiceBagCollection(DiceBagCollection diceBagCollection, String path) {
		return saveOrExportDiceBagCollection(diceBagCollection, path, ACTION_EXPORT);
	}

	
	/**
	 * Store all the Dice Bags in the device internal or external memory.
	 * @param diceBags An ArrayList<DiceBag> representing all the Dice Bags
	 * @param path The path where to save (used only if {@code action} == {@link #ACTION_EXPORT})
	 * @param action Either {@link #ACTION_SAVE} or {@link #ACTION_EXPORT}
	 * @return {@code true} if data where saved, {@code false} otherwise
	 */
	protected boolean saveOrExportDiceBagCollection(DiceBagCollection diceBags, String path, int action) {
		boolean retVal = false;
		int errorMessageResId;
		FileOutputStream fos;

		if (action == ACTION_SAVE) {
			errorMessageResId = R.string.err_cannot_update;
		} else if (action == ACTION_EXPORT) {
			errorMessageResId = R.string.err_cannot_export;
		} else {
			throw new IllegalArgumentException();
		}

		try {
			synchronized (dataAccessLock) {
				if (action == ACTION_SAVE) {
					//Internal storage
					fos = context.openFileOutput(FILE_NAME_DICEBAGS, Context.MODE_PRIVATE);
				} else {
					//External storage
					fos = new FileOutputStream(new File(path));
				}
				SerializationManager.DiceBagCollection(fos, diceBags);
				fos.close();
			}
			retVal = true;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "saveOrExportDiceBags", e);
			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
			showErrorMessage(context, errorMessageResId);
		} catch (IOException e) {
			Log.e(TAG, "saveOrExportDiceBags", e);
			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
			showErrorMessage(context, errorMessageResId);
		} catch (Exception e) {
			Log.e(TAG, "saveOrExportDiceBags", e);
			//Toast.makeText(context, errorMessageResId, Toast.LENGTH_LONG).show();
			showErrorMessage(context, errorMessageResId);
		}
		return retVal;
	}
	
	/**
	 * Legacy method to load from old modifier file.
	 * @param collection Collection to populate
	 */
	protected boolean loadModifierCollection(ModifierCollection collection) {
		boolean retVal = false;
		FileInputStream fis;
		
		try {
			fis = context.openFileInput(OBSOLETE_FILE_NAME_BONUSBAG);

			//retVal = SerializationManager.BonusList(fis);
			SerializationManager.ModifierCollection(fis, collection);
			
			fis.close();
			
			retVal = collection.size() > 0;
		} catch (FileNotFoundException e) {
			//Should show toast only if this is not the fist attempt
			Log.w(TAG, "loadBonusBag", e);
			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
			//if (DEBUG) {Log.i(TAG, "loadDiceBag:FileNotFoundException", e);}
		} catch (IOException e) {
			Log.e(TAG, "loadBonusBag", e);
			//Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read_mod);
		} catch (Exception e) {
			Log.e(TAG, "loadBonusBag", e);
			//Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read_mod);
		}
		
		return retVal;
	}
	
	/**
	 * Load a dice bag from the old dice storage file.
	 * @param collection Collection to populate.
	 */
	protected boolean loadDiceCollection(DiceCollection collection) {
		boolean retVal = false;
		FileInputStream fis;
		
		try {
			fis = context.openFileInput(OBSOLETE_FILE_NAME_DICEBAG);

			//retVal = SerializationManager.DiceList(fis);
			SerializationManager.DiceCollection(fis, collection);
			
			fis.close();

			retVal = collection.size() > 0;
		} catch (FileNotFoundException e) {
			//Should show toast only if this is not the fist attempt
			Log.w(TAG, "loadDiceBag", e);
			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
		} catch (IOException e) {
			Log.e(TAG, "loadDiceBag", e);
			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read);
		} catch (Exception e) {
			Log.e(TAG, "loadDiceBag", e);
			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read);
		}
		
		return retVal;
	}
	
	/* **************************************** */

//	/**
//	 * Load a bonus bag from the device internal memory.
//	 * @param context Context.
//	 * @return An ArrayList<RollModifier> representing a bonus bag, or null if an error occurred.
//	 * @deprecated This function is for backward compatibility only. Use {@link loadDiceBags} instead.
//	 */
//	public ArrayList<RollModifier> loadBonusBag() {
////		ArrayList<RollModifier> retVal;
////		FileInputStream fis;
////		InputStreamReader isr;
////		BufferedReader br;
////		StringBuilder text;
////		String line;
////		
////		retVal = null;
////		
////		try {
////			fis = context.openFileInput(OBSOLETE_FILE_NAME_BONUSBAG);
////			isr = new InputStreamReader(fis);
////			br = new BufferedReader(isr);
////			text = new StringBuilder();
////			
////			while ((line = br.readLine()) != null) {
////				if (text.length() > 0) {
////					text.append("\n");
////				}
////				text.append(line);
////			}
////			//retVal = convertBonusBagFromJSONString(text.toString());
////			retVal = SerializationManager.BonusList(text.toString());
////			fis.close();
////		} catch (FileNotFoundException e) {
////			//Should show toast only if this is not the fist attempt
////			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
////			//if (DEBUG) {Log.i(TAG, "loadDiceBag:FileNotFoundException", e);}
////		} catch (JSONException e) {
////			Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
////		} catch (IOException e) {
////			Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
////		}
////		
////		if (retVal != null && retVal.size() == 0) {
////			retVal = null;
////		}
////
////		return retVal;
//
//		ArrayList<RollModifier> retVal;
//		FileInputStream fis;
//		
//		retVal = null;
//		
//		try {
//			fis = context.openFileInput(OBSOLETE_FILE_NAME_BONUSBAG);
//
//			retVal = SerializationManager.BonusList(fis);
//			
//			fis.close();
//		} catch (FileNotFoundException e) {
//			//Should show toast only if this is not the fist attempt
//			Log.w(TAG, "loadBonusBag", e);
//			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
//			//if (DEBUG) {Log.i(TAG, "loadDiceBag:FileNotFoundException", e);}
//		} catch (IOException e) {
//			Log.e(TAG, "loadBonusBag", e);
//			//Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, R.string.err_cannot_read_mod);
//		} catch (Exception e) {
//			Log.e(TAG, "loadBonusBag", e);
//			//Toast.makeText(context, R.string.err_cannot_read_mod, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, R.string.err_cannot_read_mod);
//		}
//		
//		if (retVal != null && retVal.size() == 0) {
//			retVal = null;
//		}
//
//		return retVal;
//	}
	
//	/**
//	 * Load a dice bag from the device internal memory.
//	 * @param context Context.
//	 * @return An {@code ArrayList<Dice>} representing a dice bag, or null if an error occurred.
//	 * @deprecated This function is for backward compatibility only. Use {@link loadDiceBags} instead.
//	 */
//	public ArrayList<Dice> loadDiceBag() {
////		ArrayList<Dice> retVal;
////		FileInputStream fis;
////		InputStreamReader isr;
////		BufferedReader br;
////		StringBuilder text;
////		String line;
////		
////		retVal = null;
////		
////		try {
////			fis = context.openFileInput(OBSOLETE_FILE_NAME_DICEBAG);
////			isr = new InputStreamReader(fis);
////			br = new BufferedReader(isr);
////			text = new StringBuilder();
////			
////			while ((line = br.readLine()) != null) {
////				if (text.length() > 0) {
////					text.append("\n");
////				}
////				text.append(line);
////			}
////			//retVal = convertDiceBagFromJSONString(text.toString());
////			retVal = SerializationManager.DiceList(text.toString());
////			fis.close();
////		} catch (FileNotFoundException e) {
////			//Should show toast only if this is not the fist attempt
////			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
////			//if (DEBUG) {Log.i(TAG, "loadDiceBag:FileNotFoundException", e);}
////		} catch (JSONException e) {
////			//if (DEBUG) {Log.e(TAG, "loadDiceBag:JSONException", e);}
////			Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
////		} catch (IOException e) {
////			//if (DEBUG) {Log.e(TAG, "loadDiceBag:IOException", e);}
////			Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
////		}
////		
////		if (retVal != null && retVal.size() == 0) {
////			//if (DEBUG) {Log.i(TAG, "loadDiceBag:noData");}
////			retVal = null;
////		}
////
////		return retVal;
//
//		ArrayList<Dice> retVal;
//		FileInputStream fis;
//		
//		retVal = null;
//		
//		try {
//			fis = context.openFileInput(OBSOLETE_FILE_NAME_DICEBAG);
//
//			retVal = SerializationManager.DiceList(fis);
//			
//			fis.close();
//		} catch (FileNotFoundException e) {
//			//Should show toast only if this is not the fist attempt
//			Log.w(TAG, "loadDiceBag", e);
//			//Toast.makeText(getBaseContext(), R.string.err_cannot_read, Toast.LENGTH_LONG);
//		} catch (IOException e) {
//			Log.e(TAG, "loadDiceBag", e);
//			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, R.string.err_cannot_read);
//		} catch (Exception e) {
//			Log.e(TAG, "loadDiceBag", e);
//			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
//			showErrorMessage(context, R.string.err_cannot_read);
//		}
//		
//		if (retVal != null && retVal.size() == 0) {
//			//if (DEBUG) {Log.i(TAG, "loadDiceBag:noData");}
//			retVal = null;
//		}
//
//		return retVal;
//	}

	public void saveResultList(RollResult[] lastResult, ArrayList<RollResult[]> resultList) {
		FileOutputStream fos;

		resultList.add(0, lastResult);

		try {
			synchronized (dataAccessLock) {
				fos = context.openFileOutput(FILE_NAME_RESULTLIST, Context.MODE_PRIVATE);
				
				SerializationManager.ResultList(fos, resultList);
				
				fos.close();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "saveResultList", e);
			//Toast.makeText(context, R.string.err_cannot_update, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_update);
		} catch (IOException e) {
			Log.e(TAG, "saveResultList", e);
			//Toast.makeText(context, R.string.err_cannot_update, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_update);
		} catch (Exception e) {
			Log.e(TAG, "saveResultList", e);
			//Toast.makeText(context, R.string.err_cannot_update, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_update);
		}
		
		resultList.remove(0);
		
		resultListCache = null;
	}

	public ArrayList<RollResult[]> loadResultList() {
		if (resultListCache == null) {
			preloadResultList();
		}
		//Since the first element is the "Last Roll", it is
		//usually removed (lastResult = loadResultList().remove(0)).
		//Thus, this function has to return a copy of the cache
		//ArrayList in order to preserve all it's elements.
		return resultListCache == null ? null : new ArrayList<RollResult[]>(resultListCache);
	}

	ArrayList<RollResult[]> resultListCache = null;
	
	public void preloadResultList() {
		ArrayList<RollResult[]> retVal;
		FileInputStream fis;
		
		retVal = null;
		
		try {
			fis = context.openFileInput(FILE_NAME_RESULTLIST);
			
			retVal = SerializationManager.ResultList(fis);

			fis.close();
		} catch (FileNotFoundException e) {
			//Should show toast only if this is not the fist attempt
			Log.w(TAG, "preloadResultList", e);
			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
			//showErrorMessage(context, R.string.err_cannot_read);
		} catch (IOException e) {
			Log.e(TAG, "preloadResultList", e);
			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read);
		} catch (Exception e) {
			Log.e(TAG, "preloadResultList", e);
			//Toast.makeText(context, R.string.err_cannot_read, Toast.LENGTH_LONG).show();
			showErrorMessage(context, R.string.err_cannot_read);
		}
		
		if (retVal != null && retVal.size() == 0) {
			Log.i(TAG, "preloadResultList: empty");
			retVal = null;
		}

		resultListCache = retVal;
	}
	
	private void showErrorMessage(final Context ctx, final int messageResId) {
		new android.os.Handler(ctx.getMainLooper()).post(new java.lang.Runnable() {
			@Override
			public void run() {
				Toast.makeText(ctx, messageResId, Toast.LENGTH_LONG).show();
			}
		});
	}
}
