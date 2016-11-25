package dk.unf.software.aar2013.gruppe7;

import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
	private int page;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		DBhelper dbhelper = new DBhelper(getApplicationContext());

		SQLiteDatabase db = dbhelper.getReadableDatabase();

		String[] projection = { FeedEntry._ID, FeedEntry.COLUM_NAME_TITLE };

		Cursor c = db.query(FeedEntry.TABLE_NAME, projection, null, null, null,
				null, null);

		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {
			// den skal lave en liste som indenholder alle overskrifterne og
			// disse skal skrives på skræmen.
			Button tmButton = new Button(this);
			tmButton.setId(i);
			tmButton.setText(c.getString(c
					.getColumnIndexOrThrow(FeedEntry.COLUM_NAME_TITLE)));
			tmButton.setOnClickListener(this);

			idMap.put(i, c.getInt(c.getColumnIndex(FeedEntry._ID)));
		}

		db.close();

	}

	public void ReloadFragments() {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	public SectionsPagerAdapter getSectionsAdapter() {
		return mSectionsPagerAdapter;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	// inder classe, definer alt igen...
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Fragment fragment = DummySectionFragment.newInstance();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			DBhelper dbhelper = new DBhelper(getApplicationContext());
			return dbhelper.getCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			if (position == 0) {
				return "Contents";
			} else {
				return "Page " + (position);
			}
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;

	}

	// * A dummy fragment representing a section of the app, but that simply
	// * displays dummy text.
	// */
	// classen under er en inder class det vil sige at man skal definerer sine
	// ting igen.
	public static class DummySectionFragment extends Fragment {
		DBhelper dbhelper;
		private HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
//					container, false);
//
//			dbhelper = new DBhelper(rootView.getContext());

			// ((FragmentActivity)
			// getActivity()).getActionBar().setDisplayOptions(R.menu.main);

			View rootView;
			
			
			if (getArguments().getInt(ARG_SECTION_NUMBER) == 0) {

				rootView = inflater.inflate(R.layout.layout_content,
						container, false);

				dbhelper = new DBhelper(rootView.getContext());
				
				LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layoutContentLin);
				layout.setOrientation(LinearLayout.VERTICAL);// Søren er Awesome

				int i = 0;
				for (String s : dbhelper.getTitles()) {
					i++;

					FrameLayout noobLayout = new FrameLayout(getActivity());
					layout.addView(noobLayout, LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);

					TextView tv = new TextView(getActivity());
					tv.setText(" " + s);
					tv.setTextSize(25);
					tv.setId(i);
					tv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							((MainActivity) getActivity()).mViewPager
									.setCurrentItem(v.getId());
						}
					});
					noobLayout.addView(tv);

					TextView tv2 = new TextView(getActivity());
					tv2.setText(i + "");
					tv2.setTextSize(25);
					tv2.setGravity(Gravity.RIGHT);
					noobLayout.addView(tv2);
				}

				// Indsæt oprettelse af TextViews(Clickable), baseret på
				// titler i DB.
			} else {
				
				rootView = inflater.inflate(R.layout.fragment_main_dummy,
						container, false);

				dbhelper = new DBhelper(rootView.getContext());

				// kalde metode som laver en entry fra db
				Entry entry = dbhelper.getEntry(getArguments().getInt(
						ARG_SECTION_NUMBER) - 1);
				// denne entry indeholder så den information som skal bruges til
				// view's

				// find view
				// dette er vores title
				TextView dummyTextView2 = (TextView) rootView
						.findViewById(R.id.textView2);
				// dette er vores text
				TextView dummyTextView1 = (TextView) rootView
						.findViewById(R.id.textView1);
				// dette er vores billed
				ImageView dummyImageView = (ImageView) rootView
						.findViewById(R.id.imageView1);

				// indsæt info i tesktview og foto view
				dummyTextView2.setText(entry.getText());
				dummyTextView1.setText(entry.getTitle());

				if (entry.getPhoto() == null) {
					dummyImageView.setVisibility(View.INVISIBLE);
				} else {

					Toast.makeText(getActivity(), entry.getPhoto().toString(),
							Toast.LENGTH_LONG).show();
					Options opts = new Options();
					opts.inJustDecodeBounds = true;

					Toast.makeText(getActivity(), getPath(entry.getPhoto()),
							Toast.LENGTH_LONG).show();
					BitmapFactory.decodeFile(getPath(entry.getPhoto()), opts);

					opts.inSampleSize = calculateInSampleSize(opts, 500, 500);

					opts.inJustDecodeBounds = false;

					dummyImageView.setImageBitmap(BitmapFactory
							.decodeFile(getPath(entry.getPhoto())));

					// dummyImageView.setImageBitmap(BitmapFactory.decodeFile(
					// "file:///" + entry.getPhoto().getPath(), opts));
					Log.d("logafuri", "Uri der skal vises er: "
							+ entry.getPhoto().toString());
				}

			}

			return rootView;
		}

		public String getPath(Uri uri) {
			Cursor cursor = getActivity().getContentResolver().query(uri, null,
					null, null, null);
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}

		public String getRealPathFromURI(Uri contentUri) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(
					contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.edit_page:
			int currentpage = ((MainActivity) getActivity()).mViewPager.getCurrentItem();
			Intent editintent = new Intent(getActivity(), EditPageActivity.class);
			editintent.putExtra("currentpage", currentpage);
			Log.d("logafos",currentpage+"");
			
			startActivityForResult(editintent, 200);
				break;
			case R.id.home:
				((MainActivity) getActivity()).mViewPager.setCurrentItem(0);
				break;
			case R.id.action_addd:
				Intent addpage = new Intent(getActivity(),
						AddPageActivity.class);
				startActivityForResult(addpage, 0);
				break;
			case R.id.action_delete:
				// åben anden activity
				// send intent
				int sidetal = getArguments().getInt(ARG_SECTION_NUMBER);
				Log.d("logafos", "" + sidetal);
				// her skal køres en metode som fjerne den med den ide siden er
				// på
				DBhelper dbhelper = new DBhelper(getActivity());
				// her slettes det i databassen som ligger på hvad id'et er.
				dbhelper.deleteEntry(sidetal - 1);
				Log.d("logafos", "Jaahhhaa");
				((MainActivity) getActivity()).ReloadFragments();

				break;
			}
			return true;
		}

		public static Fragment newInstance() {
			Fragment f = new DummySectionFragment();
			f.setHasOptionsMenu(true);
			return f;
		}
	}

	// denne ligger i mainActivity classen
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
		}
	}

	// ligger i main class.
	@Override
	public void onClick(View v) {

		// v.getId() // denne metode bruges til at få id'et for den knap som er
		// trykket på (her gør den ikke noget)
		// idMap.get(v.getId()) // dette giver id'et fra databasen
		// vi skal nu bruge en intent til at hive det information ud fra id'et
		// fra databasen.
	}

	// hvad der sker når man trykker på delete

	// getArguments().getInt(ARG_SECTION_NUMBER) // giver os nummeret for noten.
	//
	//
	// DBhelper dbhelper = new DBhelper(getApplicationContext());
	//
	// SQLiteDatabase db = dbhelper.getReadableDatabase();
	//
	// String[] projection = { FeedEntry._ID, FeedEntry.COLUM_NAME_TITLE };
	//
	// Cursor c = db.query(FeedEntry.TABLE_NAME, projection, null, null, null,
	// null, null);
	// String selection = FeedEntry.COLUM_NAME_TITLE

	// String selctionArgs = {String.valueof(
	// c.moveToFirst();

	// // Define 'where' part of query.
	// String selection = getArguments(ARG_SECTION_NUMBER) + " LIKE ?";
	// // Specify arguments in placeholder order.
	// String[] selectionArgs = { getArguments(ARG_SECTION_NUMBER)};
	// // Issue SQL statement.
	// db.delete(TABLE_NAME, selection, selectionArgs);

}
